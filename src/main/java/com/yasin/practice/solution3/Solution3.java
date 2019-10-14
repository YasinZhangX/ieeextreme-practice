package com.yasin.practice.solution3;

import com.yasin.utils.importers.FileImporter;
import com.yasin.utils.importers.Importer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasin Zhang
 */
@Slf4j
public class Solution3 {
    private Graph graph = new Graph();
    private List<Pair<Integer, Integer>> demandList = new ArrayList<>();

    public void assembleParams(List<List<Object>> params) {
        graph.assembleParams(params);
        demandList = graph.getDemandList();
    }

    public List<Long> getRateList() {
        List<Long> rateList = new ArrayList<>();

        DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(graph);
        for (Pair<Integer, Integer> demand : demandList) {
            Vertex start = graph.getVertexById(demand.first());
            Vertex end = graph.getVertexById(demand.second());
            rateList.add(alg.getShortestPath(start, end).getRate());
        }

        return rateList;
    }

    public static void main(String[] args) {
        Importer importer = new FileImporter();
        long startTime = System.currentTimeMillis();
        Solution3 s = new Solution3();
        s.assembleParams(importer.importFromSource("InputFiles/solution3.txt"));
        log.info("结果为" + s.getRateList());
        log.info("运行时间为" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
