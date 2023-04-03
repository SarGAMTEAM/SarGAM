package java_programs;

/// fix the a bug in the following method
import java.util.*;
public class MINIMUM_SPANNING_TREE {
    public static Set<WeightedEdge> minimum_spanning_tree(List<WeightedEdge> weightedEdges) {
        Map<Node,Set<Node>> groupByNode = new HashMap<>();
        Set<WeightedEdge> minSpanningTree = new HashSet<>();
        Collections.sort(weightedEdges);
        for (WeightedEdge edge : weightedEdges) {
            Node vertex_u = edge.node1;
            Node vertex_v = edge.node2;
            if (!groupByNode.containsKey(vertex_u)){
                groupByNode.put(vertex_u, new HashSet<>(Arrays.asList(vertex_u)));
            }
            if (!groupByNode.containsKey(vertex_v)){
                groupByNode.put(vertex_v, new HashSet<>(Arrays.asList(vertex_v)));
            }
            if (groupByNode.get(vertex_u) != groupByNode.get(vertex_v)) {
                minSpanningTree.add(edge);
                groupByNode = update(groupByNode, vertex_u, vertex_v);
                for (Node node : groupByNode.get(vertex_v)) {
                    groupByNode = update(groupByNode, node, vertex_u); // buggy line is here
                }
            }
        }
        return minSpanningTree;
    }
    public static Map<Node,Set<Node>> update(Map<Node,Set<Node>> groupByNode, Node vertex_u, Node vertex_v) {
        Set<Node> vertex_u_span = new HashSet<>(groupByNode.get(vertex_u));
        vertex_u_span.addAll(groupByNode.get(vertex_v));
        groupByNode.put(vertex_u, vertex_u_span);
        return groupByNode;
    }
}

/// Change the buggy line to fix the bug:
package java_programs;

import java.util.*;
public class MINIMUM_SPANNING_TREE {
    public static Set<WeightedEdge> minimum_spanning_tree(List<WeightedEdge> weightedEdges) {
        Map<Node,Set<Node>> groupByNode = new HashMap<>();
        Set<WeightedEdge> minSpanningTree = new HashSet<>();
        Collections.sort(weightedEdges);
        for (WeightedEdge edge : weightedEdges) {
            Node vertex_u = edge.node1;
            Node vertex_v = edge.node2;
            if (!groupByNode.containsKey(vertex_u)){
                groupByNode.put(vertex_u, new HashSet<>(Arrays.asList(vertex_u)));
            }
            if (!groupByNode.containsKey(vertex_v)){
                groupByNode.put(vertex_v, new HashSet<>(Arrays.asList(vertex_v)));
            }
            if (groupByNode.get(vertex_u) != groupByNode.get(vertex_v)) {
                minSpanningTree.add(edge);
                groupByNode = update(groupByNode, vertex_u, vertex_v);
                for (Node node : groupByNode.get(vertex_v)) {
