package java_programs;

/// fix the a bug in the following method
import java.util.*;
public class SUBSEQUENCES {
    public static ArrayList<ArrayList> subsequences(int a, int b, int k) {
        if (k == 0) {
            return new ArrayList(); // buggy line is here
        }
        ArrayList ret = new ArrayList(50); 
        for (int i=a; i<b+1-k; i++) {
            ArrayList base = new ArrayList(50);
            for (ArrayList rest : subsequences(i+1, b, k-1)) {
                rest.add(0,i);
                base.add(rest);
            }
            ret.addAll(base);
        }
        return ret;
    }
}

/// Change the buggy line to fix the bug:
package java_programs;

import java.util.*;
public class SUBSEQUENCES {
    public static ArrayList<ArrayList> subsequences(int a, int b, int k) {
        if (k == 0) {
