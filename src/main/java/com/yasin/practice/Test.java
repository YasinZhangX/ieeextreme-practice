package com.yasin.practice;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Yasin Zhang
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            long startTime = System.currentTimeMillis();

            for (int j = 0; j < 100; j++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            log.info(i + ": time is " + (System.currentTimeMillis() - startTime));
        }
    }
}
