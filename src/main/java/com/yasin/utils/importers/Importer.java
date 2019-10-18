package com.yasin.utils.importers;

import com.yasin.utils.processer.LineProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasin Zhang
 */
@Slf4j
public abstract class Importer {

    private LineProcessor lineProcessor = new LineProcessor();

    /**
     * 从源中获取数据
     * @param sourceName 源名称
     * @return 源数据列表
     */
    public abstract List<List<Object>> importByName(String sourceName);

    /**
     * 从源中获取数据
     * @param inputStream 源名称
     * @return 源数据列表
     */
    public List<List<Object>> importByStream(InputStream inputStream) {
        log.debug("开始读取流 " + inputStream.toString());
        List<List<Object>> retList = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

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

}
