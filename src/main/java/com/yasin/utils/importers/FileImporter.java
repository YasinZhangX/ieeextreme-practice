package com.yasin.utils.importers;

import com.yasin.utils.processer.LineProcessor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasin Zhang
 */
@Setter
@Slf4j
public class FileImporter implements Importer {

    private LineProcessor lineProcessor = new LineProcessor();

    private String fileName;
    private FileReader input;

    public FileImporter(String fileName) {
        this.fileName = fileName;
        try {
            this.input = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            log.error("读取文件失败", e);
        }

    }

    @Override
    public List<List<Object>> getTotalData() {
        log.debug("开始读取文件 " + fileName);
        List<List<Object>> retList = new ArrayList<>();

        try {
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
                List<Object> objList = lineProcessor.parseLine(currentLine);
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

    @Override
    public List<Object> getOneLine() {
        log.debug("开始读取文件 " + fileName);

        try {
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
                return lineProcessor.parseLine(currentLine);

            }

            // 关闭缓存
            bufferedReader.close();
        } catch (IOException e) {
            log.error("读取文件失败", e);
        } catch (IllegalArgumentException e) {
            log.error("参数错误", e);
        }

        return null;
    }
}
