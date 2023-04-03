/// fix the bug in the following method
@Override
protected JavaType _narrow(Class<?> subclass) {
    if (_class == subclass) {
        return this;
    }
    return new SimpleType(subclass, _bindings, _superClass, _superInterfaces, // buggy line is here
     _valueHandler, _typeHandler, _asStatic);
}

/// Change the buggy line to fix the bug:
@Override
protected JavaType _narrow(Class<?> subclass) {
    if (_class == subclass) {
        return this;
    }
