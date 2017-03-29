package com.jack.jackassistant.bean;

/**
 * Created by xiaofeng on 2017/3/28.
 */

public class Function {

    private String functionName;
    private String functionImageString;

    public Function(String functionName, String functionImageString) {
        this.functionName = functionName;
        this.functionImageString = functionImageString;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionImageString() {
        return functionImageString;
    }

    public void setFunctionImageString(String functionImageString) {
        this.functionImageString = functionImageString;
    }
}
