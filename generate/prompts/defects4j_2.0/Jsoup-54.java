/// fix the bug in the following method
private void copyAttributes(org.jsoup.nodes.Node source, Element el) {
    for (Attribute attribute : source.attributes()) {
        String key = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
        // buggy line is here
        el.setAttribute(key, attribute.getValue());
    }
}

/// Change the buggy line to fix the bug:
private void copyAttributes(org.jsoup.nodes.Node source, Element el) {
    for (Attribute attribute : source.attributes()) {
        String key = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
