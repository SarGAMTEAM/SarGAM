package java_programs;

/// fix the a bug in the following method
import java.util.*;
public class TO_BASE {
    public static String to_base(int num, int b) {
        String result = "";
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int i;
        while (num > 0) {
            i = num % b;
            num = num / b; // floor division?
            result = result + String.valueOf(alphabet.charAt(i)); // buggy line is here
        }
        return result;
    }
}

/// Change the buggy line to fix the bug:
package java_programs;

import java.util.*;
public class TO_BASE {
    public static String to_base(int num, int b) {
        String result = "";
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int i;
        while (num > 0) {
            i = num % b;
            num = num / b; // floor division?
