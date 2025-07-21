package com.company.data.util;

import java.util.List;
import java.util.Random;

public class CsvUtil {

    public static String getRandomRow(List<String> rows) {
        Random random = new Random();
        int index = random.nextInt(rows.size());
        return rows.get(index);
    }
}