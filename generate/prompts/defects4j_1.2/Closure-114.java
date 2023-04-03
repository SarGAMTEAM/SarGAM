/// fix the bug in the following method
private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    if (ns != null) {
        if (parent.isFor() && !NodeUtil.isForIn(parent)) {
            if (parent.getFirstChild().getNext() != n) {
                recordDepScope(recordNode, ns);
            } else {
                recordDepScope(nameNode, ns);
            }
        } else { // buggy line is here
            recordDepScope(recordNode, ns);
        }
    }
}

/// Change the buggy line to fix the bug:
private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    if (ns != null) {
        if (parent.isFor() && !NodeUtil.isForIn(parent)) {
            if (parent.getFirstChild().getNext() != n) {
                recordDepScope(recordNode, ns);
            } else {
                recordDepScope(nameNode, ns);
            }
