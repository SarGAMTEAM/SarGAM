/// fix the bug in the following method
@Override
public Number read(JsonReader in) throws IOException {
    JsonToken jsonToken = in.peek();
    switch(jsonToken) {
        case NULL:
            in.nextNull();
            return null;
        case NUMBER:
        // buggy line is here
            return new LazilyParsedNumber(in.nextString());
        default:
            throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
    }
}

/// Change the buggy line to fix the bug:
@Override
public Number read(JsonReader in) throws IOException {
    JsonToken jsonToken = in.peek();
    switch(jsonToken) {
        case NULL:
            in.nextNull();
            return null;
        case NUMBER:
        