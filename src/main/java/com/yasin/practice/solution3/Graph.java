package com.yasin.practice.solution3;

import java.util.*;

/**
 * @author Yasin Zhang
 */
public class Graph {

    private List<Pair<Integer, Integer>> demandList = new ArrayList<>();

    private static final Long EDGE_DISCONNECTED = -1L;

    private Map<Integer, Set<Vertex>> fanoutVerticesIndex = new HashMap<>();
    private Map<Integer, Set<Vertex>> faninVerticesIndex = new HashMap<>();
    private Map<Pair<Integer, Integer>, Long> vertexPairRateIndex = new HashMap<>();
    private Map<Integer, Vertex> vertexIndex = new HashMap<>();
    private Map<String, Integer> vertexNameIndex = new HashMap<>();
    private List<Vertex> vertexList = new Vector<>();
    private Integer vertexNum = 0;
    private Integer edgeNum = 0;

    private void clear() {
        Vertex.reset();
        vertexNum = 0;
        edgeNum = 0;
        vertexList.clear();
        vertexIndex.clear();
        vertexNameIndex.clear();
        faninVerticesIndex.clear();
        fanoutVerticesIndex.clear();
        vertexPairRateIndex.clear();
    }

    public void assembleParams(List<List<Object>> params) {
        clear();

        int numOfRules = (Integer) params.get(0).get(0);
        params.remove(0);

        for (int i = 0; i < numOfRules; i++) {
            List<Object> line = params.get(i);
            String A = (String) line.get(0);
            int idA = addVertex(A);
            String B = (String) line.get(1);
            int idB = addVertex(B);

            Integer rate = (Integer) line.get(2);
            addEdge(idA, idB, rate);
        }

        int numOfDemands = (Integer) params.get(numOfRules).get(0);
        for (int i = numOfRules+1; i < params.size(); i++) {
            List<Object> line = params.get(i);
            String A = (String) line.get(0);
            String B = (String) line.get(1);
            demandList.add(new Pair<>(vertexNameIndex.get(A), vertexNameIndex.get(B)));
        }
    }



    private int addVertex(String name) {
        if (vertexNameIndex.containsKey(name)) {
            return vertexNameIndex.get(name);
        }

        Vertex newVertex = new Vertex();
        newVertex.setName(name);
        vertexList.add(newVertex);
        vertexIndex.put(newVertex.getId(), newVertex);
        vertexNameIndex.put(newVertex.getName(), newVertex.getId());

        return newVertex.getId();
    }

    private void addEdge(int startVertexId, int endVertexId, long rate)
    {
        // actually, we should make sure all vertices ids must be correct.
        if(!vertexIndex.containsKey(startVertexId)
            || !vertexIndex.containsKey(endVertexId)
            || startVertexId == endVertexId)
        {
            throw new IllegalArgumentException("The edge from "+startVertexId
                +" to "+endVertexId+" does not exist in the graph.");
        }

        // update the adjacent-list of the graph
        Set<Vertex> fanoutVertexSet = new HashSet<>();
        if(fanoutVerticesIndex.containsKey(startVertexId))
        {
            fanoutVertexSet = fanoutVerticesIndex.get(startVertexId);
        }
        fanoutVertexSet.add(vertexIndex.get(endVertexId));
        fanoutVerticesIndex.put(startVertexId, fanoutVertexSet);

        Set<Vertex> faninVertexSet = new HashSet<Vertex>();
        if(faninVerticesIndex.containsKey(endVertexId))
        {
            faninVertexSet = faninVerticesIndex.get(endVertexId);
        }
        faninVertexSet.add(vertexIndex.get(startVertexId));
        faninVerticesIndex.put(endVertexId, faninVertexSet);

        // store the new edge
        vertexPairRateIndex.put(
            new Pair<>(startVertexId, endVertexId),
            rate);

        ++edgeNum;
    }

    public Set<Vertex> getAdjacentVertices(Vertex vertex)
    {
        return fanoutVerticesIndex.getOrDefault(vertex.getId(), new HashSet<>());
    }

    public Set<Vertex> getPrecedentVertices(Vertex vertex)
    {
        return faninVerticesIndex.getOrDefault(vertex.getId(), new HashSet<>());
    }

    public long getEdgeRate(Vertex source, Vertex sink)
    {
        return vertexPairRateIndex.getOrDefault(
            new Pair<>(source.getId(), sink.getId()), EDGE_DISCONNECTED);
    }

    public List<Pair<Integer, Integer>> getDemandList() {
        return demandList;
    }

    public Vertex getVertexById(Integer id) {
        return vertexIndex.get(id);
    }
}
