/// fix the bug in the following method
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, paint, stroke, alpha); // buggy line is here
    s
    this.value = value;
}

/// Change the buggy line to fix the bug:
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
