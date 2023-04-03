package java_programs;

/// fix the a bug in the following method
public class BITCOUNT {
    public static int bitcount(int n) {
    int count = 0;
    while (n != 0) {
        n = (n ^ (n - 1)); // buggy line is here
        count++;
    }
    return count;
    }
}

/// Change the buggy line to fix the bug:

package java_programs;

public class BITCOUNT {
    public static int bitcount(int n) {
    int count = 0;
    while (n != 0) {
