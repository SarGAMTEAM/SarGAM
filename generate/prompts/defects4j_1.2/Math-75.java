/// fix the bug in the following method
@Deprecated
public double getPct(Object v) {
    return getCumPct((Comparable<?>) v); // buggy line is here
    
}

/// Change the buggy line to fix the bug:
@Deprecated
public double getPct(Object v) {
