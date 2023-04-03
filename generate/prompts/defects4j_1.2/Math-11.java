/// fix the bug in the following method
public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }
    return FastMath.pow(2 * FastMath.PI, -dim / 2) * // buggy line is here
     FastMath.pow(covarianceMatrixDeterminant, -0.5) * getExponentTerm(vals);
}

/// Change the buggy line to fix the bug:
public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }
