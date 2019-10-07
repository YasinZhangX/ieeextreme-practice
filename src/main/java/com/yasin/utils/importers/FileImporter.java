package com.yasin.utils.importers;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Yasin Zhang
 */
@Setter
@Slf4j
public class FileImporter implements Importer {

    private static final String INTEGER_PATTERN = "^[0-9]+$";
    private static final String DOUBLE_PATTERN = "^[0-9]+(.[0-9]+)$";

    private String splitRegex = " ";

    @Override
    public List<List<Object>> importFromSource(String fileName) {
        log.debug("开始读取文件 " + fileName);
        List<List<Object>> retList = new ArrayList<>();

        try {
            FileReader input = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(input);

            String currentLine;

            currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                // 跳过空行和注释
                if (currentLine.trim().equals("") || currentLine.trim().startsWith("#")) {
                    currentLine = bufferedReader.readLine();
                    continue;
                }

                // 处理单行数据
                List<Object> objList = parseLine(currentLine);
                retList.add(objList);


                // 读下一行
                currentLine = bufferedReader.readLine();
            }

            // 关闭缓存
            bufferedReader.close();
        } catch (IOException e) {
            log.error("读取文件失败", e);
        } catch (IllegalArgumentException e) {
            log.error("参数错误", e);
        }

        return retList;
    }

    /**
     * 从行中读取数据,并存入list
     */
    private List<Object> parseLine(String line) {
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
