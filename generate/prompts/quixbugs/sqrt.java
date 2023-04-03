package java_programs;

/// fix the a bug in the following method
import java.util.*;
public class SQRT {
    public static double sqrt(double x, double epsilon) {
        double approx = x / 2f;
        while (Math.abs(x-approx) > epsilon) { // buggy line is here
            approx = 0.5f * (approx + x / approx);
        }
        return approx;
    }
}

/// Change the buggy line to fix the bug:
package java_programs;

import java.util.*;
public class SQRT {
    public static double sqrt(double x, double epsilon) {
        double approx = x / 2f;
