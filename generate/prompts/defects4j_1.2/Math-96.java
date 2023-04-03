/// fix the bug in the following method
public boolean equals(Object other) {
    boolean ret;
    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else {
        try {
            Complex rhs = (Complex) other;
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
                ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) && (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary())); // buggy line is here
                
            }
        } catch (ClassCastException ex) {
            ret = false;
        }
    }
    return ret;
}

/// Change the buggy line to fix the bug:
public boolean equals(Object other) {
    boolean ret;
    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else {
        try {
            Complex rhs = (Complex) other;
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
