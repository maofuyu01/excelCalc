package com.mfy.excel;

import java.util.*;
import java.util.regex.Pattern;


class Adapter implements IAdapter<Integer> {
    List<List<String>> ex = Arrays.asList(
            Arrays.asList("A2", "A1", "", "B3+B4"),
            Arrays.asList("A1+A2", "", "B1+B2", "")
    );
    List<List<Integer>> data = Arrays.asList(
            new ArrayList<>(Arrays.asList(2, 4, 1, 0)),
            new ArrayList<>(Arrays.asList(0, 1, 0, 2))
    );

    @Override
    public Integer getData(String cell) {
        Integer[] axis = getAxis(cell);
        return data.get(axis[0]).get(axis[1]);
    }

    @Override
    public void setData(String cell, Integer value) {
        Integer[] axis = getAxis(cell);
        data.get(axis[0]).set(axis[1], value);
    }

    private Integer[] getAxis(String calcCell) {
        String rowName = calcCell.replaceAll("\\d+", "");
        String colName = calcCell.replaceAll(rowName, "");
        int i = rowName.charAt(0) - 'A';
        int j = Integer.parseInt(colName) - 1;
        return new Integer[]{i, j};
    }

    @Override
    public Map<String, String> getAllExMap() {
        Map<String, String> res = new HashMap<>();
        for (int i = 0; i < ex.size(); i++) {
            String rowName = String.valueOf((char) (i + 'A'));
            for (int j = 0; j < ex.get(i).size(); j++) {
                res.put(rowName + (j + 1), ex.get(i).get(j));
            }
        }
        return res;
    }

    @Override
    public Pattern getCellPattern() {
        return Pattern.compile("\\w+\\d+");
    }
}
