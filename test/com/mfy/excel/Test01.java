package com.mfy.excel;

import org.junit.Test;

import javax.script.ScriptException;
import java.util.Arrays;


public class Test01 {

    @Test
    public void test01() throws ScriptException {
        Adapter adapter = new Adapter();
        ExcelCalc<Integer> calc = new ExcelCalc<>(adapter);

        adapter.data.forEach(r -> {
            System.out.println(Arrays.toString(r.toArray()));
        });

        calc.calcCell("A1");

        System.out.println("=======================");
        adapter.data.forEach(r -> {
            System.out.println(Arrays.toString(r.toArray()));
        });

        calc.calcCell("A2");

        System.out.println("=======================");
        adapter.data.forEach(r -> {
            System.out.println(Arrays.toString(r.toArray()));
        });

        calc.setCellExpression("B1","B1==8?1:2");

        System.out.println("=======================");
        adapter.data.forEach(r -> {
            System.out.println(Arrays.toString(r.toArray()));
        });
    }

    @Test
    public void test02() throws ScriptException {
        ArrayAdapter adapter = new ArrayAdapter();
        ExcelCalc<Double> calc = new ExcelCalc<>(adapter);

        Arrays.stream(adapter.data).forEach(r -> {
            System.out.println(Arrays.toString(r));
        });

        calc.calcCell("0_0");

        System.out.println("=======================");
        Arrays.stream(adapter.data).forEach(r -> {
            System.out.println(Arrays.toString(r));
        });
    }
}
