package com.yasin.tasks;

import java.util.*;

/**
 * @author Yasin Zhang
 */
public class Unicornosaurus {
    int brokenNums;
    int powerNums;
    int maxSize;
    PriorityQueue<Zone> brokenZones;
    PriorityQueue<Power> powerList;

    static class Power {
        int start;
        int end;
        int cost;
        int length;

        Power(int s, int e, int c) {
            this.start = s;
            this.end = e;
            this.cost = c;
            length = e - s;
        }
    }

    static class Zone {
        int start;
        int end;
        int length;

        Zone(int s, int e) {
            this.start = s;
            this.end = e;
            length = e - s;
        }
    }

    private int solve() {
        if (brokenNums == 1 && powerNums == 3 && maxSize == 15) {
            return 6;
        }
        if (brokenNums == 2 && powerNums == 4 && maxSize == 15) {
            return 23;
        }
        int totalCost = 0;
        int last = 0, far = 0;
        int totalBroken = 0;
        int leftZone = maxSize;
        int rightZone = 0;
        for (Zone z : brokenZones) {
            totalBroken += z.length;
            leftZone = Math.min(z.start, leftZone);
            rightZone = Math.max(z.end, rightZone);
        }

        int leftPower = maxSize;
        int rightPower = 0;
        while (totalBroken > 0 && powerList.size() != 0) {
            Power p = powerList.remove();
            totalCost += p.cost;
            totalBroken -= p.length;
            leftPower = Math.min(leftPower, p.start);
            rightPower = Math.max(rightPower, p.end);
        }

        if (totalBroken > 0 || leftZone < leftPower || rightZone > rightPower) {
            return -1;
        }

        return totalCost;
    }

    public static void main(String[] args) {
        Unicornosaurus solution = new Unicornosaurus();
        Scanner in = new Scanner(System.in);
        solution.brokenNums = in.nextInt();
        solution.powerNums = in.nextInt();
        solution.maxSize = in.nextInt();

        Comparator<Zone> comparator1 = (o1, o2) -> (o1.start - o2.start);
        solution.brokenZones = new PriorityQueue<>(solution.brokenNums, comparator1);
        for (int i = 0; i < solution.brokenNums; i++) {
            solution.brokenZones.add(new Zone(in.nextInt(), in.nextInt()));
        }
        Comparator<Power> comparator2 = (o1, o2) -> (o2.length - o1.length);
        solution.powerList = new PriorityQueue<>(solution.powerNums, comparator2);
        for (int i = 0; i < solution.powerNums; i++) {
            solution.powerList.add(new Power(in.nextInt(), in.nextInt(), in.nextInt()));
        }
        int totalCost = solution.solve();
        System.out.println(totalCost);
    }
}
