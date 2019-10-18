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
public interface Importer {

    /**
     * 从源中获取全数据
     * @return 源数据列表
     */
    List<List<Object>> getTotalData();

    /**
     * 获取一行数据
     * @return 一行数据列表
     */
    List<Object> getOneLine();

}
