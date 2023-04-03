/// fix the bug in the following method
private boolean isPrototypePropertyAssign(Node assign) { // buggy line is here
private boolean isPrototypePropertyAssign(Node assign
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign) && n.getType() == Token.GETPROP) { // buggy line is here
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign) && n.getType() == Token.GETPROP
        boolean isChainedProperty = n.getFirstChild().getType() == Token.GETPROP;
        if (isChainedProperty) { // buggy line is here
        if (isChainedProperty
            Node child = n.getFirstChild().getFirstChild().getNext();
            if (child.getType() == Token.STRING && child.getString().equals("prototype")) { // buggy line is here
            if (child.getType() == Token.STRING && child.getString().equals("prototype")
                return true;
            }
        }
    }
    return false;
}

/// Change the buggy line to fix the bug:
