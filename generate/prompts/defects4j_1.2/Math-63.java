/// fix the bug in the following method
public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || x == y; // buggy line is here
    
}

/// Change the buggy line to fix the bug:
public static boolean equals(double x, double y) {
