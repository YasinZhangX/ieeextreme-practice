package com.yasin.practice.solution3;

import java.util.Objects;

public class Pair<TYPE1, TYPE2>
{
    private TYPE1 o1;
    private TYPE2 o2;
    
    public Pair(TYPE1 o1, TYPE2 o2) {
    	this.o1 = o1;
    	this.o2 = o2;
    }

    public TYPE1 first() {
    	return o1;
    }
    
    public TYPE2 second() {
    	return o2;
    }

    public int hashCode() {
        int code = 0;
        if (o1 != null)
            code = o1.hashCode();
        if (o2 != null)
            code = code/2 + o2.hashCode()/2;
        return code;
    }

    private static boolean same(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

	public boolean equals(Object obj) 
    {
        if (!(obj instanceof Pair))
            return false;
        Pair<TYPE1, TYPE2> p = (Pair<TYPE1, TYPE2>)obj;
        return same(p.o1, this.o1) && same(p.o2, this.o2);
    }

    public String toString() {
        return "Pair{"+o1+", "+o2+"}";
    }
}
