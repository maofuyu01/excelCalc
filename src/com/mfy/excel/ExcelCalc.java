package com.mfy.excel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelCalc<T> {
    private final IAdapter<T> adapter;
    private final Map<String, String> expressionMap;
    private final Map<String, Set<String>> referencedMap = new HashMap<>();
    private final ScriptEngine js = new ScriptEngineManager().getEngineByName("js");
    private final Pattern cellPattern;


    public ExcelCalc(IAdapter<T> adapter) {
        this.adapter = adapter;
        expressionMap = adapter.getAllExMap();
        cellPattern = adapter.getCellPattern();
        expressionMap.forEach(this::updateExpressMap);
    }

    public void setCellExpression(String cell, String expression) {
        expressionMap.put(cell, expression);
        updateExpressMap(cell, expression);
        calcCell(cell);
    }

    private void updateExpressMap(String cell, String expression) {
        if (expression == null || expression.trim().equals("")) {
            return;
        }
        Matcher matcher = cellPattern.matcher(expression);
        while (matcher.find()) {
            String s = matcher.group();
            Set<String> refSet = referencedMap.getOrDefault(s, new HashSet<>());
            refSet.add(cell);
            referencedMap.put(s, refSet);
        }
    }

    public void calcCell(String calcCell) {
        Set<String> hadCalc = new HashSet<>();
        calcCell0(calcCell, hadCalc);
    }


    private void calcCell0(String calcCell, Set<String> hadCalc) {
        String script = expressionMap.get(calcCell);
        if (hadCalc.contains(calcCell) || script == null) {
            return;
        }
        Set<String> cells = new HashSet<>();
        Matcher matcher = cellPattern.matcher(script);
        while (matcher.find()) {
            cells.add(matcher.group());
        }
        for (String cell : cells) {
            Object data = adapter.getData(cell);
            script = script.replace(cell, data.toString());
        }
        try {
            adapter.setData(calcCell, (T) js.eval(script));
        } catch (ScriptException e) {
            throw new RuntimeException("解析单元格" + calcCell + "(" + expressionMap.get(calcCell) + ")失败，请检查。");
        }
        hadCalc.add(calcCell);
        if (referencedMap.get(calcCell) != null) {
            for (String refCell : referencedMap.get(calcCell)) {
                calcCell0(refCell, hadCalc);
            }
        }
    }
}

