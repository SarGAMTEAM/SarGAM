/// fix the bug in the following method
protected XmlSerializerProvider(XmlSerializerProvider src) {
    super(src);
    _rootNameLookup = src._rootNameLookup; // buggy line is here
}

/// Change the buggy line to fix the bug:
protected XmlSerializerProvider(XmlSerializerProvider src) {
    super(src);
