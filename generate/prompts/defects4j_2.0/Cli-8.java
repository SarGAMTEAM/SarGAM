/// fix the bug in the following method
protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);
    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }
    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    final String padding = createPadding(nextLineTabStop);
    while (true) {
        text = padding + text.substring(pos).trim();
        pos = findWrapPos(text, width, nextLineTabStop); // buggy line is here
        if (pos == -1) {
            sb.append(text);
            return sb;
        }
        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }
}

/// Change the buggy line to fix the bug:
protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);
    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }
    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    final String padding = createPadding(nextLineTabStop);
    while (true) {
        text = padding + text.substring(pos).trim();
        