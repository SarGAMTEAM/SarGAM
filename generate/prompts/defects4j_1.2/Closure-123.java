/// fix the bug in the following method
void add(Node n, Context context) {
    if (!cc.continueProcessing()) {
        return;
    }
    int type = n.getType();
    String opstr = NodeUtil.opToStr(type);
    int childCount = n.getChildCount();
    Node first = n.getFirstChild();
    Node last = n.getLastChild();
    if (opstr != null && first != last) {
        Preconditions.checkState(childCount == 2, "Bad binary operator \"%s\": expected 2 arguments but got %s", opstr, childCount);
        int p = NodeUtil.precedence(type);
        Context rhsContext = getContextForNoInOperator(context);
        if (last.getType() == type && NodeUtil.isAssociative(type)) {
            addExpr(first, p, context);
            cc.addOp(opstr, true);
            addExpr(last, p, rhsContext);
        } else if (NodeUtil.isAssignmentOp(n) && NodeUtil.isAssignmentOp(last)) {
            addExpr(first, p, context);
            cc.addOp(opstr, true);
            addExpr(last, p, rhsContext);
        } else {
            unrollBinaryOperator(n, type, opstr, context, rhsContext, p, p + 1);
        }
        return;
    }
    cc.startSourceMapping(n);
    switch(type) {
        case Token.TRY:
            {
                Preconditions.checkState(first.getNext().isBlock() && !first.getNext().hasMoreThanOneChild());
                Preconditions.checkState(childCount >= 2 && childCount <= 3);
                add("try");
                add(first, Context.PRESERVE_BLOCK);
                Node catchblock = first.getNext().getFirstChild();
                if (catchblock != null) {
                    add(catchblock);
                }
                if (childCount == 3) {
                    add("finally");
                    add(last, Context.PRESERVE_BLOCK);
                }
                break;
            }
        case Token.CATCH:
            Preconditions.checkState(childCount == 2);
            add("catch(");
            add(first);
            add(")");
            add(last, Context.PRESERVE_BLOCK);
            break;
        case Token.THROW:
            Preconditions.checkState(childCount == 1);
            add("throw");
            add(first);
            cc.endStatement(true);
            break;
        case Token.RETURN:
            add("return");
            if (childCount == 1) {
                add(first);
            } else {
                Preconditions.checkState(childCount == 0);
            }
            cc.endStatement();
            break;
        case Token.VAR:
            if (first != null) {
                add("var ");
                addList(first, false, getContextForNoInOperator(context));
            }
            break;
        case Token.LABEL_NAME:
            Preconditions.checkState(!n.getString().isEmpty());
            addIdentifier(n.getString());
            break;
        case Token.NAME:
            if (first == null || first.isEmpty()) {
                addIdentifier(n.getString());
            } else {
                Preconditions.checkState(childCount == 1);
                addIdentifier(n.getString());
                cc.addOp("=", true);
                if (first.isComma()) {
                    addExpr(first, NodeUtil.precedence(Token.ASSIGN), Context.OTHER);
                } else {
                    addExpr(first, 0, getContextForNoInOperator(context));
                }
            }
            break;
        case Token.ARRAYLIT:
            add("[");
            addArrayList(first);
            add("]");
            break;
        case Token.PARAM_LIST:
            add("(");
            addList(first);
            add(")");
            break;
        case Token.COMMA:
            Preconditions.checkState(childCount == 2);
            unrollBinaryOperator(n, Token.COMMA, ",", context, getContextForNoInOperator(context), 0, 0);
            break;
        case Token.NUMBER:
            Preconditions.checkState(childCount == 0);
            cc.addNumber(n.getDouble());
            break;
        case Token.TYPEOF:
        case Token.VOID:
        case Token.NOT:
        case Token.BITNOT:
        case Token.POS:
            {
                Preconditions.checkState(childCount == 1);
                cc.addOp(NodeUtil.opToStrNoFail(type), false);
                addExpr(first, NodeUtil.precedence(type), Context.OTHER);
                break;
            }
        case Token.NEG:
            {
                Preconditions.checkState(childCount == 1);
                if (n.getFirstChild().isNumber()) {
                    cc.addNumber(-n.getFirstChild().getDouble());
                } else {
                    cc.addOp(NodeUtil.opToStrNoFail(type), false);
                    addExpr(first, NodeUtil.precedence(type), Context.OTHER);
                }
                break;
            }
        case Token.HOOK:
            {
                Preconditions.checkState(childCount == 3);
                int p = NodeUtil.precedence(type);
                Context rhsContext = Context.OTHER; // buggy line is here
                
                addExpr(first, p + 1, context);
                cc.addOp("?", true);
                addExpr(first.getNext(), 1, rhsContext);
                cc.addOp(":", true);
                addExpr(last, 1, rhsContext);
                break;
            }
        case Token.REGEXP:
            if (!first.isString() || !last.isString()) {
                throw new Error("Expected children to be strings");
            }
            String regexp = regexpEscape(first.getString(), outputCharsetEncoder);
            if (childCount == 2) {
                add(regexp + last.getString());
            } else {
                Preconditions.checkState(childCount == 1);
                add(regexp);
            }
            break;
        case Token.FUNCTION:
            if (n.getClass() != Node.class) {
                throw new Error("Unexpected Node subclass.");
            }
            Preconditions.checkState(childCount == 3);
            boolean funcNeedsParens = (context == Context.START_OF_EXPR);
            if (funcNeedsParens) {
                add("(");
            }
            add("function");
            add(first);
            add(first.getNext());
            add(last, Context.PRESERVE_BLOCK);
            cc.endFunction(context == Context.STATEMENT);
            if (funcNeedsParens) {
                add(")");
            }
            break;
        case Token.GETTER_DEF:
        case Token.SETTER_DEF:
            Preconditions.checkState(n.getParent().isObjectLit());
            Preconditions.checkState(childCount == 1);
            Preconditions.checkState(first.isFunction());
            Preconditions.checkState(first.getFirstChild().getString().isEmpty());
            if (type == Token.GETTER_DEF) {
                Preconditions.checkState(!first.getChildAtIndex(1).hasChildren());
                add("get ");
            } else {
                Preconditions.checkState(first.getChildAtIndex(1).hasOneChild());
                add("set ");
            }
            String name = n.getString();
            Node fn = first;
            Node parameters = fn.getChildAtIndex(1);
            Node body = fn.getLastChild();
            if (!n.isQuotedString() && TokenStream.isJSIdentifier(name) && NodeUtil.isLatin(name)) {
                add(name);
            } else {
                double d = getSimpleNumber(name);
                if (!Double.isNaN(d)) {
                    cc.addNumber(d);
                } else {
                    addJsString(n);
                }
            }
            add(parameters);
            add(body, Context.PRESERVE_BLOCK);
            break;
        case Token.SCRIPT:
        case Token.BLOCK:
            {
                if (n.getClass() != Node.class) {
                    throw new Error("Unexpected Node subclass.");
                }
                boolean preserveBlock = context == Context.PRESERVE_BLOCK;
                if (preserveBlock) {
                    cc.beginBlock();
                }
                boolean preferLineBreaks = type == Token.SCRIPT || (type == Token.BLOCK && !preserveBlock && n.getParent() != null && n.getParent().isScript());
                for (Node c = first; c != null; c = c.getNext()) {
                    add(c, Context.STATEMENT);
                    if (c.isVar()) {
                        cc.endStatement();
                    }
                    if (c.isFunction()) {
                        cc.maybeLineBreak();
                    }
                    if (preferLineBreaks) {
                        cc.notePreferredLineBreak();
                    }
                }
                if (preserveBlock) {
                    cc.endBlock(cc.breakAfterBlockFor(n, context == Context.STATEMENT));
                }
                break;
            }
        case Token.FOR:
            if (childCount == 4) {
                add("for(");
                if (first.isVar()) {
                    add(first, Context.IN_FOR_INIT_CLAUSE);
                } else {
                    addExpr(first, 0, Context.IN_FOR_INIT_CLAUSE);
                }
                add(";");
                add(first.getNext());
                add(";");
                add(first.getNext().getNext());
                add(")");
                addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
            } else {
                Preconditions.checkState(childCount == 3);
                add("for(");
                add(first);
                add("in");
                add(first.getNext());
                add(")");
                addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
            }
            break;
        case Token.DO:
            Preconditions.checkState(childCount == 2);
            add("do");
            addNonEmptyStatement(first, Context.OTHER, false);
            add("while(");
            add(last);
            add(")");
            cc.endStatement();
            break;
        case Token.WHILE:
            Preconditions.checkState(childCount == 2);
            add("while(");
            add(first);
            add(")");
            addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
            break;
        case Token.EMPTY:
            Preconditions.checkState(childCount == 0);
            break;
        case Token.GETPROP:
            {
                Preconditions.checkState(childCount == 2, "Bad GETPROP: expected 2 children, but got %s", childCount);
                Preconditions.checkState(last.isString(), "Bad GETPROP: RHS should be STRING");
                boolean needsParens = (first.isNumber());
                if (needsParens) {
                    add("(");
                }
                addExpr(first, NodeUtil.precedence(type), context);
                if (needsParens) {
                    add(")");
                }
                if (this.languageMode == LanguageMode.ECMASCRIPT3 && TokenStream.isKeyword(last.getString())) {
                    add("[");
                    add(last);
                    add("]");
                } else {
                    add(".");
                    addIdentifier(last.getString());
                }
                break;
            }
        case Token.GETELEM:
            Preconditions.checkState(childCount == 2, "Bad GETELEM: expected 2 children but got %s", childCount);
            addExpr(first, NodeUtil.precedence(type), context);
            add("[");
            add(first.getNext());
            add("]");
            break;
        case Token.WITH:
            Preconditions.checkState(childCount == 2);
            add("with(");
            add(first);
            add(")");
            addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
            break;
        case Token.INC:
        case Token.DEC:
            {
                Preconditions.checkState(childCount == 1);
                String o = type == Token.INC ? "++" : "--";
                int postProp = n.getIntProp(Node.INCRDECR_PROP);
                if (postProp != 0) {
                    addExpr(first, NodeUtil.precedence(type), context);
                    cc.addOp(o, false);
                } else {
                    cc.addOp(o, false);
                    add(first);
                }
                break;
            }
        case Token.CALL:
            if (isIndirectEval(first) || n.getBooleanProp(Node.FREE_CALL) && NodeUtil.isGet(first)) {
                add("(0,");
                addExpr(first, NodeUtil.precedence(Token.COMMA), Context.OTHER);
                add(")");
            } else {
                addExpr(first, NodeUtil.precedence(type), context);
            }
            add("(");
            addList(first.getNext());
            add(")");
            break;
        case Token.IF:
            boolean hasElse = childCount == 3;
            boolean ambiguousElseClause = context == Context.BEFORE_DANGLING_ELSE && !hasElse;
            if (ambiguousElseClause) {
                cc.beginBlock();
            }
            add("if(");
            add(first);
            add(")");
            if (hasElse) {
                addNonEmptyStatement(first.getNext(), Context.BEFORE_DANGLING_ELSE, false);
                add("else");
                addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
            } else {
                addNonEmptyStatement(first.getNext(), Context.OTHER, false);
                Preconditions.checkState(childCount == 2);
            }
            if (ambiguousElseClause) {
                cc.endBlock();
            }
            break;
        case Token.NULL:
            Preconditions.checkState(childCount == 0);
            cc.addConstant("null");
            break;
        case Token.THIS:
            Preconditions.checkState(childCount == 0);
            add("this");
            break;
        case Token.FALSE:
            Preconditions.checkState(childCount == 0);
            cc.addConstant("false");
            break;
        case Token.TRUE:
            Preconditions.checkState(childCount == 0);
            cc.addConstant("true");
            break;
        case Token.CONTINUE:
            Preconditions.checkState(childCount <= 1);
            add("continue");
            if (childCount == 1) {
                if (!first.isLabelName()) {
                    throw new Error("Unexpected token type. Should be LABEL_NAME.");
                }
                add(" ");
                add(first);
            }
            cc.endStatement();
            break;
        case Token.DEBUGGER:
            Preconditions.checkState(childCount == 0);
            add("debugger");
            cc.endStatement();
            break;
        case Token.BREAK:
            Preconditions.checkState(childCount <= 1);
            add("break");
            if (childCount == 1) {
                if (!first.isLabelName()) {
                    throw new Error("Unexpected token type. Should be LABEL_NAME.");
                }
                add(" ");
                add(first);
            }
            cc.endStatement();
            break;
        case Token.EXPR_RESULT:
            Preconditions.checkState(childCount == 1);
            add(first, Context.START_OF_EXPR);
            cc.endStatement();
            break;
        case Token.NEW:
            add("new ");
            int precedence = NodeUtil.precedence(type);
            if (NodeUtil.containsType(first, Token.CALL, NodeUtil.MATCH_NOT_FUNCTION)) {
                precedence = NodeUtil.precedence(first.getType()) + 1;
            }
            addExpr(first, precedence, Context.OTHER);
            Node next = first.getNext();
            if (next != null) {
                add("(");
                addList(next);
                add(")");
            }
            break;
        case Token.STRING_KEY:
            Preconditions.checkState(childCount == 1, "Object lit key must have 1 child");
            addJsString(n);
            break;
        case Token.STRING:
            Preconditions.checkState(childCount == 0, "A string may not have children");
            addJsString(n);
            break;
        case Token.DELPROP:
            Preconditions.checkState(childCount == 1);
            add("delete ");
            add(first);
            break;
        case Token.OBJECTLIT:
            {
                boolean needsParens = (context == Context.START_OF_EXPR);
                if (needsParens) {
                    add("(");
                }
                add("{");
                for (Node c = first; c != null; c = c.getNext()) {
                    if (c != first) {
                        cc.listSeparator();
                    }
                    if (c.isGetterDef() || c.isSetterDef()) {
                        add(c);
                    } else {
                        Preconditions.checkState(c.isStringKey());
                        String key = c.getString();
                        if (!c.isQuotedString() && !(languageMode == LanguageMode.ECMASCRIPT3 && TokenStream.isKeyword(key)) && TokenStream.isJSIdentifier(key) && NodeUtil.isLatin(key)) {
                            add(key);
                        } else {
                            double d = getSimpleNumber(key);
                            if (!Double.isNaN(d)) {
                                cc.addNumber(d);
                            } else {
                                addExpr(c, 1, Context.OTHER);
                            }
                        }
                        add(":");
                        addExpr(c.getFirstChild(), 1, Context.OTHER);
                    }
                }
                add("}");
                if (needsParens) {
                    add(")");
                }
                break;
            }
        case Token.SWITCH:
            add("switch(");
            add(first);
            add(")");
            cc.beginBlock();
            addAllSiblings(first.getNext());
            cc.endBlock(context == Context.STATEMENT);
            break;
        case Token.CASE:
            Preconditions.checkState(childCount == 2);
            add("case ");
            add(first);
            addCaseBody(last);
            break;
        case Token.DEFAULT_CASE:
            Preconditions.checkState(childCount == 1);
            add("default");
            addCaseBody(first);
            break;
        case Token.LABEL:
            Preconditions.checkState(childCount == 2);
            if (!first.isLabelName()) {
                throw new Error("Unexpected token type. Should be LABEL_NAME.");
            }
            add(first);
            add(":");
            addNonEmptyStatement(last, getContextForNonEmptyExpression(context), true);
            break;
        case Token.CAST:
            add("(");
            add(first);
            add(")");
            break;
        default:
            throw new Error("Unknown type " + type + "\n" + n.toStringTree());
    }
    cc.endSourceMapping(n);
}

/// Change the buggy line to fix the bug:
void add(Node n, Context context) {
    if (!cc.continueProcessing()) {
        return;
    }
    int type = n.getType();
    String opstr = NodeUtil.opToStr(type);
    int childCount = n.getChildCount();
    Node first = n.getFirstChild();
    Node last = n.getLastChild();
    if (opstr != null && first != last) {
        Preconditions.checkState(childCount == 2, "Bad binary operator \"%s\": expected 2 arguments but got %s", opstr, childCount);
        int p = NodeUtil.precedence(type);
        Context rhsContext = getContextForNoInOperator(context);
        if (last.getType() == type && NodeUtil.isAssociative(type)) {
            addExpr(first, p, context);
            cc.addOp(opstr, true);
            addExpr(last, p, rhsContext);
        } else if (NodeUtil.isAssignmentOp(n) && NodeUtil.isAssignmentOp(last)) {
            addExpr(first, p, context);
            cc.addOp(opstr, true);
            addExpr(last, p, rhsContext);
        } else {
            unrollBinaryOperator(n, type, opstr, context, rhsContext, p, p + 1);
        }
        return;
    }
    cc.startSourceMapping(n);
    switch(type) {
        case Token.TRY:
            {
                Preconditions.checkState(first.getNext().isBlock() && !first.getNext().hasMoreThanOneChild());
                Preconditions.checkState(childCount >= 2 && childCount <= 3);
                add("try");
                add(first, Context.PRESERVE_BLOCK);
                Node catchblock = first.getNext().getFirstChild();
                if (catchblock != null) {
                    add(catchblock);
                }
                if (childCount == 3) {
                    add("finally");
                    add(last, Context.PRESERVE_BLOCK);
                }
                break;
            }
        case Token.CATCH:
            Preconditions.checkState(childCount == 2);
            add("catch(");
            add(first);
            add(")");
            add(last, Context.PRESERVE_BLOCK);
            break;
        case Token.THROW:
            Preconditions.checkState(childCount == 1);
            add("throw");
            add(first);
            cc.endStatement(true);
            break;
        case Token.RETURN:
            add("return");
            if (childCount == 1) {
                add(first);
            } else {
                Preconditions.checkState(childCount == 0);
            }
            cc.endStatement();
            break;
        case Token.VAR:
            if (first != null) {
                add("var ");
                addList(first, false, getContextForNoInOperator(context));
            }
            break;
        case Token.LABEL_NAME:
            Preconditions.checkState(!n.getString().isEmpty());
            addIdentifier(n.getString());
            break;
        case Token.NAME:
            if (first == null || first.isEmpty()) {
                addIdentifier(n.getString());
            } else {
                Preconditions.checkState(childCount == 1);
                addIdentifier(n.getString());
                cc.addOp("=", true);
                if (first.isComma()) {
                    addExpr(first, NodeUtil.precedence(Token.ASSIGN), Context.OTHER);
                } else {
                    addExpr(first, 0, getContextForNoInOperator(context));
                }
            }
            break;
        case Token.ARRAYLIT:
            add("[");
            addArrayList(first);
            add("]");
            break;
        case Token.PARAM_LIST:
            add("(");
            addList(first);
            add(")");
            break;
        case Token.COMMA:
            Preconditions.checkState(childCount == 2);
            unrollBinaryOperator(n, Token.COMMA, ",", context, getContextForNoInOperator(context), 0, 0);
            break;
        case Token.NUMBER:
            Preconditions.checkState(childCount == 0);
            cc.addNumber(n.getDouble());
            break;
        case Token.TYPEOF:
        case Token.VOID:
        case Token.NOT:
        case Token.BITNOT:
        case Token.POS:
            {
                Preconditions.checkState(childCount == 1);
                cc.addOp(NodeUtil.opToStrNoFail(type), false);
                addExpr(first, NodeUtil.precedence(type), Context.OTHER);
                break;
            }
        case Token.NEG:
            {
                Preconditions.checkState(childCount == 1);
                if (n.getFirstChild().isNumber()) {
                    cc.addNumber(-n.getFirstChild().getDouble());
                } else {
                    cc.addOp(NodeUtil.opToStrNoFail(type), false);
                    addExpr(first, NodeUtil.precedence(type), Context.OTHER);
                }
                break;
            }
        case Token.HOOK:
            {
                Preconditions.checkState(childCount == 3);
                int p = NodeUtil.precedence(type);
