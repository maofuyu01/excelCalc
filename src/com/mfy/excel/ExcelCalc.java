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
        expressionMap.forEach((k, v) -> {
            if (v.trim().equals("")) {
                return;
            }
            Matcher matcher = cellPattern.matcher(v);
            while (matcher.find()) {
                String s = matcher.group();
                Set<String> refSet = referencedMap.getOrDefault(s, new HashSet<>());
                refSet.add(k);
                referencedMap.put(s, refSet);
            }
        });
    }

    public void calcCell(String calcCell) throws ScriptException {
        Set<String> hadCalc = new HashSet<>();
        calcCell0(calcCell, hadCalc);
    }


    private void calcCell0(String calcCell, Set<String> hadCalc) throws ScriptException {
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
        adapter.setData(calcCell, (T) js.eval(script));
        hadCalc.add(calcCell);
        if (referencedMap.get(calcCell) != null) {
            for (String refCell : referencedMap.get(calcCell)) {
                calcCell0(refCell, hadCalc);
            }
        }
    }
}

