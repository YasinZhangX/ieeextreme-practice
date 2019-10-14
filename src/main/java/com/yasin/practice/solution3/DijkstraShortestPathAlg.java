package com.yasin.practice.solution3;

import java.util.*;

/**
 * @author Yasin Zhang
 */
public class DijkstraShortestPathAlg {

    private Graph graph;  // 算法将要计算的图

    private Set<Vertex> doneVertexes = new HashSet<>();   // 已处理顶点
    private PriorityQueue<Vertex> vertexCalculatedQueue = new PriorityQueue<>();  // 已计算的顶点
    private Map<Vertex, Long> costFromStartVertexIndex = new HashMap<>(); // 起点到当前顶点的代价值

    private Map<Vertex, Vertex> frontVertexIndex = new HashMap<>();  // 顶点 - 前一个最小代价点
    
    private static final Long EDGE_DISCONNECTED = -1L;
    private static final Long MOD_VALUE = 998244353L;

    /**
     * 构造器接收图数据
     */
    public DijkstraShortestPathAlg(Graph g) {
        this.graph = g;
    }

    /**
     * 获取最短路径
     */
    public Path getShortestPath(Vertex source, Vertex destination) {
        determineShortestPath(source, destination, true);

        List<Vertex> vertexList = new Vector<>();
        long rate = costFromStartVertexIndex.getOrDefault(destination, EDGE_DISCONNECTED);

        if (rate != EDGE_DISCONNECTED) {
            Vertex curVertex = destination;
            do {
                vertexList.add(curVertex);
                curVertex = frontVertexIndex.get(curVertex);
            } while (curVertex != null && curVertex != source);

            vertexList.add(source);
            Collections.reverse(vertexList);
        }

        return new Path(vertexList, rate);
    }

    /**
     * 确定图中最短路的树
     */
    private void determineShortestPath(Vertex source,
                                       Vertex destination, boolean isSource2Dest) {
        // 清除数据
        clear();

        // 初始化数据
        Vertex startVertex = isSource2Dest ? source : destination;
        Vertex endVertex = isSource2Dest ? destination : source;
        costFromStartVertexIndex.put(startVertex, 1L);
        vertexCalculatedQueue.add(startVertex);

        // 寻找最短路
        while (!vertexCalculatedQueue.isEmpty()) {
            Vertex cur_candidate = vertexCalculatedQueue.poll();
            if (cur_candidate.equals(endVertex)) break;
            doneVertexes.add(cur_candidate);
            extendVertex(cur_candidate, isSource2Dest);
        }
    }

    /**
     * 清除数据
     */
    private void clear() {
        doneVertexes.clear();
        vertexCalculatedQueue.clear();
        costFromStartVertexIndex.clear();
        frontVertexIndex.clear();
    }

    /**
     * 扩展节点到其邻接点,并计算代价
     */
    private void extendVertex(Vertex vertex, boolean isSource2Dest) {
        // 获取邻接顶点
        Set<Vertex> neighborVertexSet = isSource2Dest ? graph.getAdjacentVertices(vertex) :
            graph.getPrecedentVertices(vertex);

        //
        for (Vertex currentVertex : neighborVertexSet) {
            // 跳过已处理顶点
            if (doneVertexes.contains(currentVertex)) {
                continue;
            }

            // 计算该邻接点的代价
            long cost = costFromStartVertexIndex.getOrDefault(vertex, EDGE_DISCONNECTED);
            long nextRate = isSource2Dest ? graph.getEdgeRate(vertex, currentVertex)
                : graph.getEdgeRate(currentVertex, vertex);
            if (cost == EDGE_DISCONNECTED || nextRate == EDGE_DISCONNECTED) {
                continue;
            } else {
                cost = bigMultiMod(cost, nextRate);
            }

            // 更新参数,将邻接点数据保存
            if (!costFromStartVertexIndex.containsKey(currentVertex)
                || costFromStartVertexIndex.get(currentVertex) > cost) {
                costFromStartVertexIndex.put(currentVertex, cost);
                frontVertexIndex.put(currentVertex, vertex);
                vertexCalculatedQueue.add(currentVertex);
            }
        }
    }

    private long bigMultiMod(long A, long B) {
        long res = 0;
        while (B != 0) {
            if ((B & 1) != 0) {
                res = (res + A) % MOD_VALUE;
            }
            A = A << 1;
            A = A % MOD_VALUE;
            B = B >> 1;
        }
        return res;
    }

}
