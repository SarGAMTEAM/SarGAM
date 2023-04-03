/// fix the bug in the following method
public Complex reciprocal() {
    if (isNaN) {
        return NaN; // buggy line is here
        
    }
    if (real == 0.0 && imaginary == 0.0) {
        return NaN; // buggy line is here
        
    }
    if (isInfinite) {
        return ZERO;
    }
    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1. / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}

/// Change the buggy line to fix the bug:
public Complex reciprocal() {
    if (isNaN) {
