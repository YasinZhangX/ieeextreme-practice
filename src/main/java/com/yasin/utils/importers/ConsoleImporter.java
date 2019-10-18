package com.yasin.utils.importers;

import com.yasin.utils.processer.LineProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Yasin Zhang
 */
@Slf4j
public class ConsoleImporter implements Importer {
    private LineProcessor lineProcessor = new LineProcessor();

    private Scanner sc;

    public ConsoleImporter(InputStream source) {
        this.sc = new Scanner(source);
    }

    /**
     * 与一行的处理一样
     */
    @Override
    public List<List<Object>> getTotalData() {
        List<List<Object>> retList = new ArrayList<>();
        retList.add(getOneLine());
        return retList;
    }

    @Override
    public List<Object> getOneLine() {
        while (true) {
            if (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                sc.close();
                return lineProcessor.parseLine(inputLine);
            }
        }
    }
}
