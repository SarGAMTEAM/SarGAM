/// fix the bug in the following method
public JsonWriter value(double value) throws IOException {
    writeDeferredName();
    if (Double.isNaN(value) || Double.isInfinite(value)) { // buggy line is here
        throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
    }
    beforeValue();
    out.append(Double.toString(value));
    return this;
}

/// Change the buggy line to fix the bug:
public JsonWriter value(double value) throws IOException {
    writeDeferredName();
