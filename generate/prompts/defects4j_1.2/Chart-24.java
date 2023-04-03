/// fix the bug in the following method
public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((value - this.lowerBound) / (this.upperBound // buggy line is here
     - this.lowerBound) * 255.0);
    return new Color(g, g, g);
}

/// Change the buggy line to fix the bug:
public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
