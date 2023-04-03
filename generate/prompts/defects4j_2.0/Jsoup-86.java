/// fix the bug in the following method
public XmlDeclaration asXmlDeclaration() {
    String data = getData();
    Document doc = Jsoup.parse("<" + data.substring(1, data.length() - 1) + ">", baseUri(), Parser.xmlParser());
    XmlDeclaration decl = null;
    if (doc.childNodeSize() > 0) { // buggy line is here
        Element el = doc.child(0);
        decl = new XmlDeclaration(NodeUtils.parser(doc).settings().normalizeTag(el.tagName()), data.startsWith("!"));
        decl.attributes().addAll(el.attributes());
    }
    return decl;
}

/// Change the buggy line to fix the bug:
public XmlDeclaration asXmlDeclaration() {
    String data = getData();
    Document doc = Jsoup.parse("<" + data.substring(1, data.length() - 1) + ">", baseUri(), Parser.xmlParser());
    XmlDeclaration decl = null;
