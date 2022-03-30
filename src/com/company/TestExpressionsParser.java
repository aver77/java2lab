package com.company;

import org.junit.Assert;
import org.junit.Test;

public class TestExpressionsParser {
    @Test
    public void expressionTest1() {
        String exp = "0/(2+3)";
        var parser = new ExpressionsParser();
        double expectedResult = 0;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest2() {
        String exp = "3+5";
        var parser = new ExpressionsParser();
        double expectedResult = 8;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest3() {
        String exp = "2/3";
        var parser = new ExpressionsParser();
        double expectedResult = 2/3.;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest4() {
        String exp = "4*(5+3)";
        var parser = new ExpressionsParser();
        double expectedResult = 32;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest5() {
        String exp = "4/(8+8)";
        var parser = new ExpressionsParser();
        double expectedResult = 0.25;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest6() {
        String exp = "sqrt(16)+3";
        var parser = new ExpressionsParser();
        double expectedResult = 7;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest7() {
        String exp = "sqrt(4)+sqrt(13+3)*3";
        var parser = new ExpressionsParser();
        double expectedResult = 14;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest8() {
        String exp = "-1+4/(2+2*3)";
        var parser = new ExpressionsParser();
        double expectedResult = -0.5;
        Assert.assertEquals(expectedResult, parser.parse(exp), 10e5);
    }

    @Test
    public void expressionTest9() {
        String exp = "-1+4/(2+2*3))";
        var parser = new ExpressionsParser();
        Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse(exp));
    }

    @Test
    public void expressionTest10() {
        String exp = "-1+4../(2+2*3)";
        var parser = new ExpressionsParser();
        Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse(exp));
    }

    @Test
    public void expressionTest11() {
        String exp = "-1+4.4.2/(2+2*3)";
        var parser = new ExpressionsParser();
        Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse(exp));
    }

    @Test
    public void expressionTest12() {
        String exp = "-1+4/(f(4)+2*3)";
        var parser = new ExpressionsParser();
        Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse(exp));
    }
}
