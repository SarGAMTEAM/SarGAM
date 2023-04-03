/// fix the bug in the following method
private static void appendOption(final StringBuffer buff, final Option option, final boolean required) {
    if (!required) {
        buff.append("[");
    }
    if (option.getOpt() != null) {
        buff.append("-").append(option.getOpt());
    } else {
        buff.append("--").append(option.getLongOpt());
    }
    if (option.hasArg() && (option.getArgName() != null)) // buggy line is here
     {
        buff.append(" <").append(option.getArgName()).append(">");
    }
    if (!required) {
        buff.append("]");
    }
}

/// Change the buggy line to fix the bug:
private static void appendOption(final StringBuffer buff, final Option option, final boolean required) {
    if (!required) {
        buff.append("[");
    }
    if (option.getOpt() != null) {
        buff.append("-").append(option.getOpt());
    } else {
        buff.append("--").append(option.getLongOpt());
    }
    