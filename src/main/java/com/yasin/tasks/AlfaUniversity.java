package com.yasin.tasks;

import javax.print.DocFlavor;
import javax.print.attribute.standard.NumberUp;
import java.util.*;

/**
 * @author Yasin Zhang
 */
public class AlfaUniversity {
    static VariableGraph graph;
    static List<Edge<Character, Character>> edgeList;
    static List<Character> vertexList;

    public static void main(String[] args) {
       Scanner in = new Scanner(System.in);
       int n = in.nextInt();
       vertexList = new ArrayList<>(n);
       for (int i = 0; i < n; i++) {
           vertexList.add((char) ('A'+i));
       }
       int m = in.nextInt();
       edgeList = new ArrayList<>(m);
       for (int i = 0; i < m; i++) {
           String pair = in.next();
           edgeList.add(new Edge<>(pair.charAt(0), pair.charAt(1)));
       }
       graph = new VariableGraph(edgeList, n);

       String result = graph.getLongestPath();
       if (result == null || result.equals("hasCycle")) {
           System.out.println(0);
       } else {
           System.out.println(1);
           System.out.println(result);
       }
    }
}

class Edge<TYPE1, TYPE2> {
    TYPE1 start;
    TYPE2 end;

    Edge(TYPE1 s, TYPE2 e) {
        start = s;
        end = e;
    }
}

class VariableGraph {
    /**
     * 顶点列表
     */
    private Set<Character> vertexSet = new HashSet<>();

    /**
     * 一个顶点的扇出顶点set
     */
    private Map<Character, Set<Character>> fanoutVertexesIndex = new HashMap<>();

    /**
     * 环路检测
     */
    private HashSet<Character> black = new HashSet<>();  // 已检索完成的节点
    private HashSet<Character> white = new HashSet<>();  // 未检索过的节点
    private HashSet<Character> gray = new HashSet<>();   // 正在检测的节点

    private int vertexNum;
    private boolean hasCycle = false;
    private List<Character> longestChar = null;

    public VariableGraph(List<Edge<Character, Character>> edgeList, Integer vertexNum) {
        this.vertexNum = vertexNum;

        for (Edge<Character, Character> edge : edgeList) {
            // 添加顶点
            vertexSet.add(edge.start);
            vertexSet.add(edge.end);

            // 填充邻接表
            addEdge(edge);

            // 初始化未访问节点列表
            white.addAll(vertexSet);
        }
    }

    private void addEdge(Edge<Character, Character> edge) {
        // 更新扇出列表
        Set<Character> fanoutVertexesSet = new HashSet<>();
        if (fanoutVertexesIndex.containsKey(edge.start)) {
            fanoutVertexesSet = fanoutVertexesIndex.get(edge.start);
        }
        fanoutVertexesSet.add(edge.end);
        fanoutVertexesIndex.put(edge.start, fanoutVertexesSet);
    }

    /**
     * 检测环路
     */
    public boolean detectingCycle() {
        for (Character v : vertexSet) {
            if(hasCycle(v)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycle(Character vertex) {
        white.remove(vertex);
        gray.add(vertex);  // 标记为已访问节点
        for (Character adj : fanoutVertexesIndex.getOrDefault(vertex, new HashSet<>())) {  // successors of current vertex
            if (white.contains(adj)) {
                if (hasCycle(adj))
                    return true;
            } else if (gray.contains(adj)) {
                return true;
            }
        }
        gray.remove(vertex);
        black.add(vertex);

        return false;
    }

    public String getLongestPath() {
        for (Character v : vertexSet) {
            black.clear();
            gray.clear();
            white.clear();
            white.addAll(vertexSet);
            Stack<Character> characters = new Stack<>();
            characters.add(v);
            dfs(v, characters);
            if (hasCycle) {
                hasCycle = false;
                return "hasCycle";
            }
            if (longestChar != null) {
                StringBuilder s = new StringBuilder();
                for (Character c : longestChar) {
                    s.append(c);
                }
                return s.toString();
            }
        }

        return null;
    }

    private void dfs(Character vertex, Stack<Character> stack) {
        white.remove(vertex);
        gray.add(vertex);  // 标记为已访问节点
        for (Character adj : fanoutVertexesIndex.getOrDefault(vertex, new HashSet<>())) {  // successors of current vertex
            if (white.contains(adj)) {
                stack.push(adj);
                if (stack.size() == vertexNum) {
                    longestChar = new ArrayList<>(stack);
                    return;
                }
                dfs(adj, stack);
                stack.pop();
            } else if (gray.contains(adj)) {
                hasCycle = true;
                return;
            }
        }
        gray.remove(vertex);
        black.add(vertex);
    }
}
