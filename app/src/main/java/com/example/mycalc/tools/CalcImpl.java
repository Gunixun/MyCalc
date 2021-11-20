package com.example.mycalc.tools;

public class CalcImpl implements Calculator {
    @Override
    public float performOperation(float value1, float value2, String operation) {
        if (Symbol.MULT.symbol.equals(operation))
            return value1 * value2;
        else if (Symbol.SUB.symbol.equals(operation))
            return value1 - value2;
        else
            return value1 + value2;
    }
}
