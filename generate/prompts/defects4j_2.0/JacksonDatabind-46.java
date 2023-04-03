/// fix the bug in the following method
@Override
public StringBuilder getGenericSignature(StringBuilder sb) {
    _classSignature(_class, sb, false);
    sb.append('<');
    sb = _referencedType.getGenericSignature(sb);
    sb.append(';'); // buggy line is here
    return sb;
}

/// Change the buggy line to fix the bug:
@Override
public StringBuilder getGenericSignature(StringBuilder sb) {
    _classSignature(_class, sb, false);
    sb.append('<');
    sb = _referencedType.getGenericSignature(sb);
