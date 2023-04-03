/// fix the bug in the following method
@Override
protected String buildCanonicalName() {
    StringBuilder sb = new StringBuilder();
    sb.append(_class.getName());
    sb.append('<');
    sb.append(_referencedType.toCanonical());
    // buggy line is here
    return sb.toString();
}

/// Change the buggy line to fix the bug:
@Override
protected String buildCanonicalName() {
    StringBuilder sb = new StringBuilder();
    sb.append(_class.getName());
    sb.append('<');
    sb.append(_referencedType.toCanonical());
    