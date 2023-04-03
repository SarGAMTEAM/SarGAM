/// fix the bug in the following method
@Override
public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
    if (_isInt) {
        visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
    } else {
        Class<?> h = handledType();
        if (h == BigDecimal.class) {
            visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER); // buggy line is here
        } else {
            visitor.expectNumberFormat(typeHint);
        }
    }
}

/// Change the buggy line to fix the bug:
@Override
public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
    if (_isInt) {
        visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
    } else {
        Class<?> h = handledType();
        if (h == BigDecimal.class) {
