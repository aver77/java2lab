package com.company;

/**
 * Интерфейс для пользовательских функций для ExpressionParser (sqrt)
 */
public interface IFunctionExpression {
    String getMetaname();
    double calculate(double value);
}
