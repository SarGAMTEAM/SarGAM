/// fix the bug in the following method
public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    return fit(new Gaussian.Parametric(), guess); // buggy line is here
    
}

/// Change the buggy line to fix the bug:
public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
