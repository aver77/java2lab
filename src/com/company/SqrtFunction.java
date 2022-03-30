package com.company;

/**
 * Класс, перегружающий методы пользовательских функций для корневых выражений
 * */
public class SqrtFunction implements IFunctionExpression {
    @Override
    public String getMetaname() {
        return "sqrt";
    }

    @Override
    public double calculate(double value) {
        return Math.sqrt(value);
    }
}
