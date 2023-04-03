/// fix the bug in the following method
public static int gcd(int u, int v) {
    if (u * v == 0) { // buggy line is here
    
        return (Math.abs(u) + Math.abs(v));
    }
    if (u > 0) {
        u = -u;
    }
    if (v > 0) {
        v = -v;
    }
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        u /= 2;
        v /= 2;
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }
    int t = ((u & 1) == 1) ? v : -(u / 2);
    do {
        while ((t & 1) == 0) {
            t /= 2;
        }
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        t = (v - u) / 2;
    } while (t != 0);
    return -u * (1 << k);
}

/// Change the buggy line to fix the bug:
public static int gcd(int u, int v) {
