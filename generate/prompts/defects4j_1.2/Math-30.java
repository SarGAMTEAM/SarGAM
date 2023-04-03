/// fix the bug in the following method
private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2) throws ConvergenceException, MaxCountExceededException {
    final int n1n2prod = n1 * n2; // buggy line is here
    
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;
    final double z = (Umin - EU) / FastMath.sqrt(VarU);
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    return 2 * standardNormal.cumulativeProbability(z);
}

/// Change the buggy line to fix the bug:
private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2) throws ConvergenceException, MaxCountExceededException {
