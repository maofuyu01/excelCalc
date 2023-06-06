package com.mfy.excel;

import java.util.Map;
import java.util.regex.Pattern;

public interface IAdapter<T> {

    Pattern getCellPattern();

    Map<String, String> getAllExMap();

    T getData(String cell);

    void setData(String cell, T value);
}
