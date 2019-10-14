package com.yasin.practice.solution3;

/**
 * @author Yasin Zhang
 */
public class Vertex {
    private static int CURRENT_VERTEX_NUM = 0;
    private int _id = CURRENT_VERTEX_NUM++;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this._id;
    }

    public static void reset()
    {
        CURRENT_VERTEX_NUM = 0;
    }
}
