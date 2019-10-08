package com.yasin.practice;

import com.yasin.utils.importers.FileImporter;
import com.yasin.utils.importers.Importer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yasin Zhang
 */
@Slf4j
public class Test {
    private static Importer importer = new FileImporter();

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        Solution1 s = new Solution1();
        s.assembleParams(importer.importFromSource("InputFiles/solution1.txt"));
        log.info("运行时间为" + (System.currentTimeMillis() - startTime) + "ms");
        log.info("绳子长" + s.getThreadLength());
    }
}
