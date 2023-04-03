/// fix the bug in the following method
protected final boolean _add(Annotation ann) {
    if (_annotations == null) {
        _annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    }
    Annotation previous = _annotations.put(ann.annotationType(), ann);
    return (previous != null) && previous.equals(ann); // buggy line is here
}

/// Change the buggy line to fix the bug:
protected final boolean _add(Annotation ann) {
    if (_annotations == null) {
        _annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    }
    Annotation previous = _annotations.put(ann.annotationType(), ann);
