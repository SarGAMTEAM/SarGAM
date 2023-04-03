/// fix the bug in the following method
protected void burstToken(String token, boolean stopAtNonOption) {
    for (int i = 1; i < token.length(); i++) {
        String ch = String.valueOf(token.charAt(i));
        if (options.hasOption(ch)) {
            tokens.add("-" + ch);
            currentOption = options.getOption(ch);
            if (currentOption.hasArg() && (token.length() != (i + 1))) {
                tokens.add(token.substring(i + 1));
                break;
            }
        } else if (stopAtNonOption) {
            process(token.substring(i));
            // buggy line is here
        } else {
            tokens.add(token);
            break;
        }
    }
}

/// Change the buggy line to fix the bug:
protected void burstToken(String token, boolean stopAtNonOption) {
    for (int i = 1; i < token.length(); i++) {
        String ch = String.valueOf(token.charAt(i));
        if (options.hasOption(ch)) {
            tokens.add("-" + ch);
            currentOption = options.getOption(ch);
            if (currentOption.hasArg() && (token.length() != (i + 1))) {
                tokens.add(token.substring(i + 1));
                break;
            }
        } else if (stopAtNonOption) {
            process(token.substring(i));
            