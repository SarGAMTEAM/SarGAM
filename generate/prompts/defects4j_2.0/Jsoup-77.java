/// fix the bug in the following method
private void popStackToClose(Token.EndTag endTag) {
    String elName = endTag.name(); // buggy line is here
    Element firstFound = null;
    for (int pos = stack.size() - 1; pos >= 0; pos--) {
        Element next = stack.get(pos);
        if (next.nodeName().equals(elName)) {
            firstFound = next;
            break;
        }
    }
    if (firstFound == null)
        return;
    for (int pos = stack.size() - 1; pos >= 0; pos--) {
        Element next = stack.get(pos);
        stack.remove(pos);
        if (next == firstFound)
            break;
    }
}

/// Change the buggy line to fix the bug:
private void popStackToClose(Token.EndTag endTag) {
