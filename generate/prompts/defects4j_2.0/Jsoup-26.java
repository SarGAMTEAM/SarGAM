/// fix the bug in the following method
public Document clean(Document dirtyDocument) {
    Validate.notNull(dirtyDocument);
    Document clean = Document.createShell(dirtyDocument.baseUri());
    // buggy line is here
    copySafeNodes(dirtyDocument.body(), clean.body());
    return clean;
}

/// Change the buggy line to fix the bug:
public Document clean(Document dirtyDocument) {
    Validate.notNull(dirtyDocument);
    Document clean = Document.createShell(dirtyDocument.baseUri());
    