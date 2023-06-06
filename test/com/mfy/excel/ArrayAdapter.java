package com.mfy.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


class ArrayAdapter implements IAdapter<Double> {
    double[][] data = {
            {1.0, 2.0},
            {2.0, -1.0},
            {4.0, 2.0}
    };
    String[][] ex = {
            {"0_1+0_0", ""}
    };

    @Override
    public Double getData(String cell) {
        String[] s = cell.split("_");
        int i = Integer.parseInt(s[0]);
        int j = Integer.parseInt(s[1]);
        return data[i][j];
    }

    @Override
    public void setData(String cell, Double value) {
        String[] s = cell.split("_");
        int i = Integer.parseInt(s[0]);
        int j = Integer.parseInt(s[1]);
        data[i][j] = value;
    }

    @Override
    public Map<String, String> getAllExMap() {
        Map<String, String> res = new HashMap<>();
        for (int i = 0; i < ex.length; i++) {
            for (int j = 0; j < ex[i].length; j++) {
                res.put(i + "_" + j, ex[i][j]);
            }
        }
        return res;
    }

    @Override
    public Pattern getCellPattern() {
        return Pattern.compile("\\d+_\\d+");
    }
}
