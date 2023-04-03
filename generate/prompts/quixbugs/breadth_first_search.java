package java_programs;

/// fix the a bug in the following method
import java.util.*;
import java.util.ArrayDeque;
public class BREADTH_FIRST_SEARCH {
    public static Set<Node> nodesvisited = new HashSet<>();
    public static boolean breadth_first_search(Node startnode, Node goalnode) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.addLast(startnode);
        nodesvisited.add(startnode);
        while (true) { // buggy line is here
            Node node = queue.removeFirst();
            if (node == goalnode) {
                return true;
            } else {
                for (Node successor_node : node.getSuccessors()) {
                    if (!nodesvisited.contains(successor_node)) {
                        queue.addFirst(successor_node);
                        nodesvisited.add(successor_node);
                    }
                }
            }
        }
    }
}
/// Change the buggy line to fix the bug:
package java_programs;

import java.util.*;
import java.util.ArrayDeque;
public class BREADTH_FIRST_SEARCH {
    public static Set<Node> nodesvisited = new HashSet<>();
    public static boolean breadth_first_search(Node startnode, Node goalnode) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.addLast(startnode);
        nodesvisited.add(startnode);
