package com.yasin.practice;

import com.yasin.utils.importers.FileImporter;
import com.yasin.utils.importers.Importer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aeneas' cryptographic disc (4th c. B.C.)
 * @author Yasin Zhang
 */
@Setter
@Slf4j
public class Solution1 {
    private double radius;
    private String finalString;
    private Map<Character, Double> degreeMap = new HashMap<>(26);

    public void assembleParams(List<List<Object>> params) {
        if (params.get(0).get(0) instanceof Integer) {
            radius = (Integer) params.get(0).get(0);
            params.remove(0);
        }

        if (params.get(params.size() - 1).get(0) instanceof String) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object subObj : params.get(params.size() - 1)) {
                stringBuilder.append((String)subObj);
            }
            finalString = stringBuilder.toString();
            params.remove(params.size() - 1);
        }

        for (List<Object> paramLine : params) {
            if (paramLine.get(0) instanceof String) {
                String letterS = (String) paramLine.get(0);
                degreeMap.put(letterS.charAt(0), (Double) paramLine.get(1));
            }
        }
    }

    public int getThreadLength() {
        double ret = radius;
        char[] chars = finalString.toUpperCase().toCharArray();
        Character currChar;
        Character prevChar = null;

        for (Character c : chars) {
            currChar = c;

            if (prevChar == null) {
                prevChar = c;
                continue;
            }

            ret += calcChordLength(getDegreeBetweenTwoLetters(prevChar, currChar));

            prevChar = currChar;
        }

        return (int)Math.ceil(ret);
    }

    private double getDegreeBetweenTwoLetters(Character l1, Character l2) {
        if (degreeMap.get(l1) == null || degreeMap.get(l2) == null) {
            log.debug("没有这个字母");
            return -1;
        }

        double d1 = degreeMap.get(l1);
        double d2 = degreeMap.get(l2);

        double originalDegree = Math.abs(d1 - d2);
        if (originalDegree > 180) {
            return 360.0 - originalDegree;
        } else {
            return originalDegree;
        }
    }

    private double calcChordLength(double degrees) {
        if (degrees == -1) {
            log.debug("度数值错误");
            return 0;
        }

        double radians = Math.toRadians(degrees / 2);
        double sinRatio = Math.sin(radians);
        return ((radius * sinRatio) * 2);
    }

    public static void main(String[] args) {
        Importer importer = new FileImporter("InputFiles/solution1.txt");
        long startTime = System.currentTimeMillis();
        Solution1 s = new Solution1();
        s.assembleParams(importer.getTotalData());
        log.info("绳子长" + s.getThreadLength());
        log.info("运行时间为" + (System.currentTimeMillis() - startTime) + "ms");
    }

}
