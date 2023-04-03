/// fix the bug in the following method
public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    return solve(min, max); // buggy line is here
    
}

/// Change the buggy line to fix the bug:
public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
