/// fix the bug in the following method
private boolean toStringEquals(Matcher m, Object arg) {
    return StringDescription.toString(m).equals(arg.toString()); // buggy line is here
    
}

/// Change the buggy line to fix the bug:
private boolean toStringEquals(Matcher m, Object arg) {
