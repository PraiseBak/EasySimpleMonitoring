package com.monitoring.easysimplemonitering.utility;


import lombok.Getter;


@Getter
public enum MetricCalOperand {
    PLUS("+"),
    MINUS("-"),
    MULTIPLE("*"),
    DIVIDED("/"),

    BIGGER_THEN("biggerThen"),
    SMALLER("smaller");

    public String operand;

    MetricCalOperand(String operand) {
        this.operand = operand;
    }
}
