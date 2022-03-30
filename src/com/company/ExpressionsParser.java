package com.company;
/**
 * Структура данных список
 */
import java.util.ArrayList;
/**
 * Структура данных Map,пары "ключ"-значение
 */
import java.util.HashMap;
/**
 * Класс для считывания данных
 */
import java.util.Scanner;

//parse операции (+ -) -> (* /) -> (скобки) -> либо переводим ответ в число, либо дополнительно переводим sqrt и переводим ответ в число

/**
 * Класс, позволяющий парсить математические выражения (пр: 2*10-sqrt(9))
 * @author Никита Аверочкин
 * @version 1.0
 * */
public class ExpressionsParser {
    private final HashMap<String, Double> vars;
    private final ArrayList<IFunctionExpression> customFunctions;

    public ExpressionsParser() {
        vars = new HashMap<>();
        customFunctions = new ArrayList<>();
        customFunctions.add(new SqrtFunction());
    }

    /**
     * Добавление пользовательской функции в парсер (анализатор)
     * @param func пользовательская функция
     */
    public void addCustomFunction(IFunctionExpression func) {
        customFunctions.add(func);
    }

    /**
     * Взятие переменной пользователя из хранилища (hashMap)
     * @param variableName имя переменной
     * @return значение переменной
     */
    private Double getVariable(String variableName) {
        if (!vars.containsKey(variableName)) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter a value of variable: " + variableName);
            var varValue = in.nextDouble();
            vars.put(variableName, varValue);
        }

        return vars.get(variableName);
    }

    /**
     * парсинг строки выражения
     * @param s выражение строка
     * @return значение выражения, после парсинга выражения строки
     * @throws IllegalArgumentException если выражение не верное
     */
    public double parse(String s) throws IllegalArgumentException {
        ExpressionsResult result = processPlusMinus(s);
        if (!result.rest.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression " + s + ", supposed problem symbol at position " + (s.length() - result.rest.length() + 1));
        }
        return result.value;
    }

    /**
     * выполнение операций сложения и вычитания
     * @param s выражение строка
     * @return результат выражения после парсинга (вычисленное значение + остальная часть выражения)
     * @throws IllegalArgumentException если выражение не верное
     */
    private ExpressionsResult processPlusMinus(String s) throws IllegalArgumentException {
        ExpressionsResult current = processMulDiv(s);
        double leftValue = current.value;

        while (current.rest.length() > 0) {
            if (!(current.rest.charAt(0) == '+' || current.rest.charAt(0) == '-')) break;

            char operation = current.rest.charAt(0);
            String next = current.rest.substring(1);

            current = processMulDiv(next);
            if (operation == '+') {
                leftValue += current.value;
            } else {
                leftValue -= current.value;
            }
        }
        return new ExpressionsResult(leftValue, current.rest);
    }

    /**
     * выполнение операций в скобках
     * @param s выражение строка
     * @return результат выражения после парсинга (вычисленное значение + остальная часть выражения)
     * @throws IllegalArgumentException если выражение не верное
     */
    private ExpressionsResult processBracket(String s) throws IllegalArgumentException {
        char zeroChar = s.charAt(0);
        if (zeroChar == '(') {
            ExpressionsResult r = processPlusMinus(s.substring(1));
            if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new IllegalArgumentException("Invalid bracket expression");
            }
            return r;
        }
        return processFunctionOrVariable(s);
    }

    /**
     * вычисление значения функции или пользовательской переменной
     * @param s выражение строка
     * @return результат выражения после парсинга (вычисленное значение + остальная часть выражения)
     * @throws IllegalArgumentException если выражение не верное
     */
    private ExpressionsResult processFunctionOrVariable(String s) throws IllegalArgumentException {
        StringBuilder f = new StringBuilder();
        int i = 0;

        while (i < s.length() && (Character.isLetter(s.charAt(i)) || ( Character.isDigit(s.charAt(i)) && i > 0 ) )) {
            f.append(s.charAt(i));
            i++;
        }
        if (f.length() > 0) {
            if (s.length() > i && s.charAt( i ) == '(') { // start of func arg
                ExpressionsResult r = processBracket(s.substring(f.length()));
                return processFunction(f.toString(), r);
            } else { // user variable
                return new ExpressionsResult(getVariable(f.toString()), s.substring(f.length()));
            }
        }
        return toNumber(s);
    }

    /**
     * выполнение операций умножения или деления
     * @param s выражение строка
     * @return результат выражения после парсинга (вычисленное значение + остальная часть выражения)
     * @throws IllegalArgumentException если выражение не верное
     */
    private ExpressionsResult processMulDiv(String s) throws IllegalArgumentException {
        ExpressionsResult current = processBracket(s);

        double leftValue = current.value;
        while (true) {
            if (current.rest.length() == 0) {
                return current;
            }
            char operation = current.rest.charAt(0);
            if ((operation != '*' && operation != '/')) return current;

            String next = current.rest.substring(1);
            ExpressionsResult right = processBracket(next);

            if (operation == '*') {
                leftValue *= right.value;
            } else {
                leftValue /= right.value;
            }

            current = new ExpressionsResult(leftValue, right.rest);
        }
    }

    /**
     * перевод выражения строки в число
     * @param s выражение строка
     * @return значение функции
     * @throws IllegalArgumentException если выражения нету
     */
    private ExpressionsResult toNumber(String s) throws IllegalArgumentException {
        int i = 0;
        int dots = 0;
        boolean negative = false;

        if( s.charAt(0) == '-' ) {
            negative = true;
            s = s.substring(1);
        }

        while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            if (s.charAt(i) == '.' && ++dots > 1) {
                throw new IllegalArgumentException("Invalid number " + s.substring(0, i + 1));
            }
            i++;
        }

        if (i == 0) {
            throw new IllegalArgumentException("Invalid number " + s);
        }

        double dPart = Double.parseDouble(s.substring(0, i));
        if (negative)
            dPart = -dPart;
        String restPart = s.substring(i);

        return new ExpressionsResult(dPart, restPart);
    }

    /**
     * Добавляет значение в пользовательскую функцию
     * @param funcMetaname название функции
     * @param r значение которое будет передано в функцию
     * @return значение функции
     * @throws IllegalArgumentException если функции или названия нету
     */
    private ExpressionsResult processFunction(String funcMetaname, ExpressionsResult r) throws IllegalArgumentException {
        var func = this.customFunctions.stream().filter(f -> funcMetaname.equals(f.getMetaname())).findFirst().orElse(null);

        if (func == null) {
            throw new IllegalArgumentException("Invalid function name " + funcMetaname);
        }

        return new ExpressionsResult(func.calculate(r.value), r.rest);
    }


}
