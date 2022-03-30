package com.company;

public class Main {
    public static void main(String[] args) {
        String expression = "5+7*10*(3+sqrt(4))-1";
//        String expression = "(4+sqrt(4))";
        var parser = new ExpressionsParser();
        try {
            System.out.println(parser.parse(expression));
        } catch (Exception e) {
            System.err.println("exception: " + e.getMessage());
        }
    }
}
