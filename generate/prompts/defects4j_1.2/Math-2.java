/// fix the bug in the following method
public double getNumericalMean() {
    return (double) (getSampleSize() * getNumberOfSuccesses()) / (double) getPopulationSize(); // buggy line is here
    
}

/// Change the buggy line to fix the bug:
public double getNumericalMean() {
