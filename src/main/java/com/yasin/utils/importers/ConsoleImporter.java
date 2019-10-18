package com.yasin.utils.importers;

import com.yasin.utils.processer.LineProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * @author Yasin Zhang
 */
@Slf4j
public class ConsoleImporter extends Importer {
    private LineProcessor lineProcessor = new LineProcessor();

    private Scanner sc;

    public ConsoleImporter(InputStream source) {
        this.sc = new Scanner(source);
    }

    public List<Object> getOneLine() {
        while (true) {
            if (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                sc.close();
                return lineProcessor.parseLine(inputLine);
            }
        }
    }

    @Override
    public List<List<Object>> importByName(String sourceName) {
        if (sourceName.equals("System.in")) {
            return importByStream(System.in);
        }

        return null;
    }
}
