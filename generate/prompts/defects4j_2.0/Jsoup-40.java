/// fix the bug in the following method
public DocumentType(String name, String publicId, String systemId, String baseUri) {
    super(baseUri);
    Validate.notEmpty(name); // buggy line is here
    attr("name", name);
    attr("publicId", publicId);
    attr("systemId", systemId);
}

/// Change the buggy line to fix the bug:
public DocumentType(String name, String publicId, String systemId, String baseUri) {
    super(baseUri);
