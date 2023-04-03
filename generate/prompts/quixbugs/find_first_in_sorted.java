package java_programs;

/// fix the a bug in the following method
import java.util.*;
public class FIND_FIRST_IN_SORTED {
    public static int find_first_in_sorted(int[] arr, int x) {
        int lo = 0;
        int hi = arr.length;
        while (lo <= hi) { // buggy line is here
            int mid = (lo + hi) / 2; // check if this is floor division
            if (x == arr[mid] && (mid == 0 || x != arr[mid-1])) {
                return mid;
            } else if (x <= arr[mid]) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return -1;
    }
}

/// Change the buggy line to fix the bug:
package java_programs;

import java.util.*;
public class FIND_FIRST_IN_SORTED {
    public static int find_first_in_sorted(int[] arr, int x) {
        int lo = 0;
        int hi = arr.length;
