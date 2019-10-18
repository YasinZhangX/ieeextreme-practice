package com.yasin.practice;

import com.yasin.utils.importers.FileImporter;
import com.yasin.utils.importers.Importer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Yasin Zhang
 */
@Slf4j
public class Solution4 {
    private int n;
    private List<Integer> numList = new ArrayList<>();

    public void assembleParams(List<List<Object>> params) {
        n = (Integer) params.get(0).get(0);
        for (Object obj : params.get(1)) {
            numList.add((Integer) obj);
        }
    }

    public long divisiblePairs() {
        long d0 =  0, d1 = 0, d2 = 0;

        for (Integer i : numList) {
            if (i % 3 == 0) {
                d0++;
            }
            if (i % 3 == 1) {
                d1++;
            }
            if (i % 3 == 2) {
                d2++;
            }
        }

        return d0*(d0-1)/2 + d1*d2;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Importer importer = new FileImporter("InputFiles/solution4.txt");
        Solution4 s = new Solution4();
        s.assembleParams(importer.getTotalData());
        log.info("数目为 " + s.divisiblePairs());
        log.info("运行时间为" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
