package com.yasin.practice.solution3;

import java.util.List;
import java.util.Vector;

/**
 * @author Yasin Zhang
 */
public class Path {

    private List<Vertex> vertexList = new Vector<>();
    private Long rate = -1L;

    public Path(List<Vertex> vertexList, long rate) {
        this.vertexList = vertexList;
        this.rate = rate;
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object right) {
        if(right instanceof Path)
        {
            Path r_path = (Path) right;
            return vertexList.equals(r_path.vertexList);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return vertexList.hashCode();
    }

    public String toString() {
        return vertexList.toString() + ":" + rate;
    }
}
