/// fix the bug in the following method
static Document parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) {
    String docData;
    Document doc = null;
    if (charsetName == null) {
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        doc = parser.parseInput(docData, baseUri);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset;
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
                if (foundCharset == null && meta.hasAttr("charset")) {
                    try {
                        if (Charset.isSupported(meta.attr("charset"))) {
                            foundCharset = meta.attr("charset");
                        }
                    } catch (IllegalCharsetNameException e) {
                        foundCharset = null;
                    }
                }
            } else {
                foundCharset = meta.attr("charset");
            }
            if (foundCharset != null && foundCharset.length() != 0 && !foundCharset.equals(defaultCharset)) {
                foundCharset = foundCharset.trim().replaceAll("[\"']", "");
                charsetName = foundCharset;
                byteData.rewind();
                docData = Charset.forName(foundCharset).decode(byteData).toString();
                doc = null;
            }
        }
    } else {
        Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        docData = Charset.forName(charsetName).decode(byteData).toString();
    }
    if (docData.length() > 0 && docData.charAt(0) == 65279) {
        byteData.rewind();
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        docData = docData.substring(1);
        charsetName = defaultCharset;
        // buggy line is here
    }
    if (doc == null) {
        doc = parser.parseInput(docData, baseUri);
        doc.outputSettings().charset(charsetName);
    }
    return doc;
}

/// Change the buggy line to fix the bug:
static Document parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) {
    String docData;
    Document doc = null;
    if (charsetName == null) {
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        doc = parser.parseInput(docData, baseUri);
        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        if (meta != null) {
            String foundCharset;
            if (meta.hasAttr("http-equiv")) {
                foundCharset = getCharsetFromContentType(meta.attr("content"));
                if (foundCharset == null && meta.hasAttr("charset")) {
                    try {
                        if (Charset.isSupported(meta.attr("charset"))) {
                            foundCharset = meta.attr("charset");
                        }
                    } catch (IllegalCharsetNameException e) {
                        foundCharset = null;
                    }
                }
            } else {
                foundCharset = meta.attr("charset");
            }
            if (foundCharset != null && foundCharset.length() != 0 && !foundCharset.equals(defaultCharset)) {
                foundCharset = foundCharset.trim().replaceAll("[\"']", "");
                charsetName = foundCharset;
                byteData.rewind();
                docData = Charset.forName(foundCharset).decode(byteData).toString();
                doc = null;
            }
        }
    } else {
        Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        docData = Charset.forName(charsetName).decode(byteData).toString();
    }
    if (docData.length() > 0 && docData.charAt(0) == 65279) {
        byteData.rewind();
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        docData = docData.substring(1);
        charsetName = defaultCharset;
        