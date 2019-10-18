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
public class FileImporter extends Importer {

    private LineProcessor lineProcessor = new LineProcessor();

    @Override
    public List<List<Object>> importByName(String fileName) {
        log.debug("开始读取文件 " + fileName);
        List<List<Object>> retList = new ArrayList<>();

        try {
            FileInputStream input = new FileInputStream(fileName);
            return importByStream(input);
        } catch (IOException e) {
            log.error("读取文件失败", e);
        } catch (IllegalArgumentException e) {
            log.error("参数错误", e);
        }

        return retList;
    }
}
