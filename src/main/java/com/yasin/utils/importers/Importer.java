package com.yasin.utils.importers;

import java.util.List;

/**
 * @author Yasin Zhang
 */
public interface Importer {

    /**
     * 从源中获取数据
     * @param sourceName 源名称
     * @return 源数据列表
     */
    List<List<Object>> importFromSource(String sourceName);

}
