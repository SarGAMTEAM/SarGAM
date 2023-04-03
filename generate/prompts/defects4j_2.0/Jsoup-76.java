/// fix the bug in the following method
boolean process(Token t, HtmlTreeBuilder tb) {
    switch(t.type) {
        case Character:
            {
                Token.Character c = t.asCharacter();
                if (c.getData().equals(nullString)) {
                    tb.error(this);
                    return false;
                } else if (tb.framesetOk() && isWhitespace(c)) {
                    tb.reconstructFormattingElements();
                    tb.insert(c);
                } else {
                    tb.reconstructFormattingElements();
                    tb.insert(c);
                    tb.framesetOk(false);
                }
                break;
            }
        case Comment:
            {
                tb.insert(t.asComment());
                break;
            }
        case Doctype:
            {
                tb.error(this);
                return false;
            }
        case StartTag:
            Token.StartTag startTag = t.asStartTag();
            String name = startTag.normalName();
            if (name.equals("a")) {
                if (tb.getActiveFormattingElement("a") != null) {
                    tb.error(this);
                    tb.processEndTag("a");
                    Element remainingA = tb.getFromStack("a");
                    if (remainingA != null) {
                        tb.removeFromActiveFormattingElements(remainingA);
                        tb.removeFromStack(remainingA);
                    }
                }
                tb.reconstructFormattingElements();
                Element a = tb.insert(startTag);
                tb.pushActiveFormattingElements(a);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartEmptyFormatters)) {
                tb.reconstructFormattingElements();
                tb.insertEmpty(startTag);
                tb.framesetOk(false);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartPClosers)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
            } else if (name.equals("span")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            } else if (name.equals("li")) {
                tb.framesetOk(false);
                ArrayList<Element> stack = tb.getStack();
                for (int i = stack.size() - 1; i > 0; i--) {
                    Element el = stack.get(i);
                    if (el.nodeName().equals("li")) {
                        tb.processEndTag("li");
                        break;
                    }
                    if (tb.isSpecial(el) && !StringUtil.inSorted(el.nodeName(), Constants.InBodyStartLiBreakers))
                        break;
                }
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
            } else if (name.equals("html")) {
                tb.error(this);
                Element html = tb.getStack().get(0);
                for (Attribute attribute : startTag.getAttributes()) {
                    if (!html.hasAttr(attribute.getKey()))
                        html.attributes().put(attribute);
                }
            } else if (StringUtil.inSorted(name, Constants.InBodyStartToHead)) {
                return tb.process(t, InHead);
            } else if (name.equals("body")) {
                tb.error(this);
                ArrayList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    return false;
                } else {
                    tb.framesetOk(false);
                    Element body = stack.get(1);
                    for (Attribute attribute : startTag.getAttributes()) {
                        if (!body.hasAttr(attribute.getKey()))
                            body.attributes().put(attribute);
                    }
                }
            } else if (name.equals("frameset")) {
                tb.error(this);
                ArrayList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    return false;
                } else if (!tb.framesetOk()) {
                    return false;
                } else {
                    Element second = stack.get(1);
                    if (second.parent() != null)
                        second.remove();
                    while (stack.size() > 1) stack.remove(stack.size() - 1);
                    tb.insert(startTag);
                    tb.transition(InFrameset);
                }
            } else if (StringUtil.inSorted(name, Constants.Headings)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                if (StringUtil.inSorted(tb.currentElement().nodeName(), Constants.Headings)) {
                    tb.error(this);
                    tb.pop();
                }
                tb.insert(startTag);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartPreListing)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
                // buggy line is here
                tb.framesetOk(false);
            } else if (name.equals("form")) {
                if (tb.getFormElement() != null) {
                    tb.error(this);
                    return false;
                }
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insertForm(startTag, true);
            } else if (StringUtil.inSorted(name, Constants.DdDt)) {
                tb.framesetOk(false);
                ArrayList<Element> stack = tb.getStack();
                for (int i = stack.size() - 1; i > 0; i--) {
                    Element el = stack.get(i);
                    if (StringUtil.inSorted(el.nodeName(), Constants.DdDt)) {
                        tb.processEndTag(el.nodeName());
                        break;
                    }
                    if (tb.isSpecial(el) && !StringUtil.inSorted(el.nodeName(), Constants.InBodyStartLiBreakers))
                        break;
                }
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
            } else if (name.equals("plaintext")) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
                tb.tokeniser.transition(TokeniserState.PLAINTEXT);
            } else if (name.equals("button")) {
                if (tb.inButtonScope("button")) {
                    tb.error(this);
                    tb.processEndTag("button");
                    tb.process(startTag);
                } else {
                    tb.reconstructFormattingElements();
                    tb.insert(startTag);
                    tb.framesetOk(false);
                }
            } else if (StringUtil.inSorted(name, Constants.Formatters)) {
                tb.reconstructFormattingElements();
                Element el = tb.insert(startTag);
                tb.pushActiveFormattingElements(el);
            } else if (name.equals("nobr")) {
                tb.reconstructFormattingElements();
                if (tb.inScope("nobr")) {
                    tb.error(this);
                    tb.processEndTag("nobr");
                    tb.reconstructFormattingElements();
                }
                Element el = tb.insert(startTag);
                tb.pushActiveFormattingElements(el);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartApplets)) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
                tb.insertMarkerToFormattingElements();
                tb.framesetOk(false);
            } else if (name.equals("table")) {
                if (tb.getDocument().quirksMode() != Document.QuirksMode.quirks && tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
                tb.framesetOk(false);
                tb.transition(InTable);
            } else if (name.equals("input")) {
                tb.reconstructFormattingElements();
                Element el = tb.insertEmpty(startTag);
                if (!el.attr("type").equalsIgnoreCase("hidden"))
                    tb.framesetOk(false);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartMedia)) {
                tb.insertEmpty(startTag);
            } else if (name.equals("hr")) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insertEmpty(startTag);
                tb.framesetOk(false);
            } else if (name.equals("image")) {
                if (tb.getFromStack("svg") == null)
                    return tb.process(startTag.name("img"));
                else
                    tb.insert(startTag);
            } else if (name.equals("isindex")) {
                tb.error(this);
                if (tb.getFormElement() != null)
                    return false;
                tb.processStartTag("form");
                if (startTag.attributes.hasKey("action")) {
                    Element form = tb.getFormElement();
                    form.attr("action", startTag.attributes.get("action"));
                }
                tb.processStartTag("hr");
                tb.processStartTag("label");
                String prompt = startTag.attributes.hasKey("prompt") ? startTag.attributes.get("prompt") : "This is a searchable index. Enter search keywords: ";
                tb.process(new Token.Character().data(prompt));
                Attributes inputAttribs = new Attributes();
                for (Attribute attr : startTag.attributes) {
                    if (!StringUtil.inSorted(attr.getKey(), Constants.InBodyStartInputAttribs))
                        inputAttribs.put(attr);
                }
                inputAttribs.put("name", "isindex");
                tb.processStartTag("input", inputAttribs);
                tb.processEndTag("label");
                tb.processStartTag("hr");
                tb.processEndTag("form");
            } else if (name.equals("textarea")) {
                tb.insert(startTag);
                tb.tokeniser.transition(TokeniserState.Rcdata);
                tb.markInsertionMode();
                tb.framesetOk(false);
                tb.transition(Text);
            } else if (name.equals("xmp")) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.reconstructFormattingElements();
                tb.framesetOk(false);
                handleRawtext(startTag, tb);
            } else if (name.equals("iframe")) {
                tb.framesetOk(false);
                handleRawtext(startTag, tb);
            } else if (name.equals("noembed")) {
                handleRawtext(startTag, tb);
            } else if (name.equals("select")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
                tb.framesetOk(false);
                HtmlTreeBuilderState state = tb.state();
                if (state.equals(InTable) || state.equals(InCaption) || state.equals(InTableBody) || state.equals(InRow) || state.equals(InCell))
                    tb.transition(InSelectInTable);
                else
                    tb.transition(InSelect);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartOptions)) {
                if (tb.currentElement().nodeName().equals("option"))
                    tb.processEndTag("option");
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartRuby)) {
                if (tb.inScope("ruby")) {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals("ruby")) {
                        tb.error(this);
                        tb.popStackToBefore("ruby");
                    }
                    tb.insert(startTag);
                }
            } else if (name.equals("math")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            } else if (name.equals("svg")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartDrop)) {
                tb.error(this);
                return false;
            } else {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            }
            break;
        case EndTag:
            Token.EndTag endTag = t.asEndTag();
            name = endTag.normalName();
            if (StringUtil.inSorted(name, Constants.InBodyEndAdoptionFormatters)) {
                for (int i = 0; i < 8; i++) {
                    Element formatEl = tb.getActiveFormattingElement(name);
                    if (formatEl == null)
                        return anyOtherEndTag(t, tb);
                    else if (!tb.onStack(formatEl)) {
                        tb.error(this);
                        tb.removeFromActiveFormattingElements(formatEl);
                        return true;
                    } else if (!tb.inScope(formatEl.nodeName())) {
                        tb.error(this);
                        return false;
                    } else if (tb.currentElement() != formatEl)
                        tb.error(this);
                    Element furthestBlock = null;
                    Element commonAncestor = null;
                    boolean seenFormattingElement = false;
                    ArrayList<Element> stack = tb.getStack();
                    final int stackSize = stack.size();
                    for (int si = 0; si < stackSize && si < 64; si++) {
                        Element el = stack.get(si);
                        if (el == formatEl) {
                            commonAncestor = stack.get(si - 1);
                            seenFormattingElement = true;
                        } else if (seenFormattingElement && tb.isSpecial(el)) {
                            furthestBlock = el;
                            break;
                        }
                    }
                    if (furthestBlock == null) {
                        tb.popStackToClose(formatEl.nodeName());
                        tb.removeFromActiveFormattingElements(formatEl);
                        return true;
                    }
                    Element node = furthestBlock;
                    Element lastNode = furthestBlock;
                    for (int j = 0; j < 3; j++) {
                        if (tb.onStack(node))
                            node = tb.aboveOnStack(node);
                        if (!tb.isInActiveFormattingElements(node)) {
                            tb.removeFromStack(node);
                            continue;
                        } else if (node == formatEl)
                            break;
                        Element replacement = new Element(Tag.valueOf(node.nodeName(), ParseSettings.preserveCase), tb.getBaseUri());
                        tb.replaceActiveFormattingElement(node, replacement);
                        tb.replaceOnStack(node, replacement);
                        node = replacement;
                        if (lastNode == furthestBlock) {
                        }
                        if (lastNode.parent() != null)
                            lastNode.remove();
                        node.appendChild(lastNode);
                        lastNode = node;
                    }
                    if (StringUtil.inSorted(commonAncestor.nodeName(), Constants.InBodyEndTableFosters)) {
                        if (lastNode.parent() != null)
                            lastNode.remove();
                        tb.insertInFosterParent(lastNode);
                    } else {
                        if (lastNode.parent() != null)
                            lastNode.remove();
                        commonAncestor.appendChild(lastNode);
                    }
                    Element adopter = new Element(formatEl.tag(), tb.getBaseUri());
                    adopter.attributes().addAll(formatEl.attributes());
                    Node[] childNodes = furthestBlock.childNodes().toArray(new Node[furthestBlock.childNodeSize()]);
                    for (Node childNode : childNodes) {
                        adopter.appendChild(childNode);
                    }
                    furthestBlock.appendChild(adopter);
                    tb.removeFromActiveFormattingElements(formatEl);
                    tb.removeFromStack(formatEl);
                    tb.insertOnStackAfter(furthestBlock, adopter);
                }
            } else if (StringUtil.inSorted(name, Constants.InBodyEndClosers)) {
                if (!tb.inScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (name.equals("span")) {
                return anyOtherEndTag(t, tb);
            } else if (name.equals("li")) {
                if (!tb.inListItemScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (name.equals("body")) {
                if (!tb.inScope("body")) {
                    tb.error(this);
                    return false;
                } else {
                    tb.transition(AfterBody);
                }
            } else if (name.equals("html")) {
                boolean notIgnored = tb.processEndTag("body");
                if (notIgnored)
                    return tb.process(endTag);
            } else if (name.equals("form")) {
                Element currentForm = tb.getFormElement();
                tb.setFormElement(null);
                if (currentForm == null || !tb.inScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.removeFromStack(currentForm);
                }
            } else if (name.equals("p")) {
                if (!tb.inButtonScope(name)) {
                    tb.error(this);
                    tb.processStartTag(name);
                    return tb.process(endTag);
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (StringUtil.inSorted(name, Constants.DdDt)) {
                if (!tb.inScope(name)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                }
            } else if (StringUtil.inSorted(name, Constants.Headings)) {
                if (!tb.inScope(Constants.Headings)) {
                    tb.error(this);
                    return false;
                } else {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(Constants.Headings);
                }
            } else if (name.equals("sarcasm")) {
                return anyOtherEndTag(t, tb);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartApplets)) {
                if (!tb.inScope("name")) {
                    if (!tb.inScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name))
                        tb.error(this);
                    tb.popStackToClose(name);
                    tb.clearFormattingElementsToLastMarker();
                }
            } else if (name.equals("br")) {
                tb.error(this);
                tb.processStartTag("br");
                return false;
            } else {
                return anyOtherEndTag(t, tb);
            }
            break;
        case EOF:
            break;
    }
    return true;
}

/// Change the buggy line to fix the bug:
boolean process(Token t, HtmlTreeBuilder tb) {
    switch(t.type) {
        case Character:
            {
                Token.Character c = t.asCharacter();
                if (c.getData().equals(nullString)) {
                    tb.error(this);
                    return false;
                } else if (tb.framesetOk() && isWhitespace(c)) {
                    tb.reconstructFormattingElements();
                    tb.insert(c);
                } else {
                    tb.reconstructFormattingElements();
                    tb.insert(c);
                    tb.framesetOk(false);
                }
                break;
            }
        case Comment:
            {
                tb.insert(t.asComment());
                break;
            }
        case Doctype:
            {
                tb.error(this);
                return false;
            }
        case StartTag:
            Token.StartTag startTag = t.asStartTag();
            String name = startTag.normalName();
            if (name.equals("a")) {
                if (tb.getActiveFormattingElement("a") != null) {
                    tb.error(this);
                    tb.processEndTag("a");
                    Element remainingA = tb.getFromStack("a");
                    if (remainingA != null) {
                        tb.removeFromActiveFormattingElements(remainingA);
                        tb.removeFromStack(remainingA);
                    }
                }
                tb.reconstructFormattingElements();
                Element a = tb.insert(startTag);
                tb.pushActiveFormattingElements(a);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartEmptyFormatters)) {
                tb.reconstructFormattingElements();
                tb.insertEmpty(startTag);
                tb.framesetOk(false);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartPClosers)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
            } else if (name.equals("span")) {
                tb.reconstructFormattingElements();
                tb.insert(startTag);
            } else if (name.equals("li")) {
                tb.framesetOk(false);
                ArrayList<Element> stack = tb.getStack();
                for (int i = stack.size() - 1; i > 0; i--) {
                    Element el = stack.get(i);
                    if (el.nodeName().equals("li")) {
                        tb.processEndTag("li");
                        break;
                    }
                    if (tb.isSpecial(el) && !StringUtil.inSorted(el.nodeName(), Constants.InBodyStartLiBreakers))
                        break;
                }
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);
            } else if (name.equals("html")) {
                tb.error(this);
                Element html = tb.getStack().get(0);
                for (Attribute attribute : startTag.getAttributes()) {
                    if (!html.hasAttr(attribute.getKey()))
                        html.attributes().put(attribute);
                }
            } else if (StringUtil.inSorted(name, Constants.InBodyStartToHead)) {
                return tb.process(t, InHead);
            } else if (name.equals("body")) {
                tb.error(this);
                ArrayList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    return false;
                } else {
                    tb.framesetOk(false);
                    Element body = stack.get(1);
                    for (Attribute attribute : startTag.getAttributes()) {
                        if (!body.hasAttr(attribute.getKey()))
                            body.attributes().put(attribute);
                    }
                }
            } else if (name.equals("frameset")) {
                tb.error(this);
                ArrayList<Element> stack = tb.getStack();
                if (stack.size() == 1 || (stack.size() > 2 && !stack.get(1).nodeName().equals("body"))) {
                    return false;
                } else if (!tb.framesetOk()) {
                    return false;
                } else {
                    Element second = stack.get(1);
                    if (second.parent() != null)
                        second.remove();
                    while (stack.size() > 1) stack.remove(stack.size() - 1);
                    tb.insert(startTag);
                    tb.transition(InFrameset);
                }
            } else if (StringUtil.inSorted(name, Constants.Headings)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                if (StringUtil.inSorted(tb.currentElement().nodeName(), Constants.Headings)) {
                    tb.error(this);
                    tb.pop();
                }
                tb.insert(startTag);
            } else if (StringUtil.inSorted(name, Constants.InBodyStartPreListing)) {
                if (tb.inButtonScope("p")) {
                    tb.processEndTag("p");
                }
                tb.insert(startTag);