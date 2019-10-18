package com.yasin.utils.processer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Yasin Zhang
 */
public class LineProcessor {
    private static final String INTEGER_PATTERN = "^[0-9]+$";
    private static final String DOUBLE_PATTERN = "^[0-9]+(.[0-9]+)$";

    private String splitRegex = " ";

    /**
     * 从行中读取数据,并存入list
     */
    public List<Object> parseLine(String line) {
        List<Object> paramList = new ArrayList<>();

        String[] strList = line.trim().split(splitRegex);

        for (String s : strList) {
            paramList.add(stringParser(s));
        }

        return paramList;
    }

    private Object stringParser(String s) {
        if (isMatchInteger(s)) {
            return Integer.parseInt(s);
        } else if (isMatchDouble(s)) {
            return Double.parseDouble(s);
        } else {
            return s;
        }
    }

    private boolean isMatchInteger(String s) {
        return Pattern.matches(INTEGER_PATTERN, s);
    }

    private boolean isMatchDouble(String s) {
        return Pattern.matches(DOUBLE_PATTERN, s);
    }
}
