package com.example.mycalc.ui;

import androidx.annotation.NonNull;

import com.example.mycalc.tools.Calculator;
import com.example.mycalc.tools.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


enum Position {
    Value1,
    Value2,
    Operation,

}


public class CalculatorPresenter {
    private final int sizeFullString = 3;

    private final CalculatorView view;
    private final Calculator calculator;

    private String userStr = "";

    private final List<Symbol> operationSymbols = new ArrayList<Symbol>() {{
        add(Symbol.SUM);
        add(Symbol.SUB);
        add(Symbol.MULT);
        add(Symbol.DIV);
    }};

    public CalculatorPresenter(CalculatorView view, Calculator calculator) {
        this.view = view;
        this.calculator = calculator;

    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
        updateResult();
    }

    public void onKeyPressed(@NonNull Symbol symbol) {
        if (symbol == Symbol.BACKSPACE && !view.isWriting())
            return;

        if (!view.isWriting())
            clean();

        if (symbol == Symbol.BACKSPACE) {
            if (userStr.length() == 1) {
                clean();
            }
            else if (userStr.length() != 0) {
                userStr = (String) userStr.subSequence(0, userStr.length() - 1);
            }
        } else if (symbol == Symbol.CLEAN) {
            clean();
            return;
        } else if (operationSymbols.contains(symbol) && userStr.length() != 0) {
            for (Symbol operation : operationSymbols) {
                if (userStr.toString().contains(symbol.symbol))
                    return;
            }
            userStr += symbol.symbol;
        } else if (symbol == Symbol.EQUALS) {
            if (userStr.length() != 0)
                view.disableInput();
        } else if (symbol == Symbol.PERCENT) {
            userStr = applyPercent(userStr);
        } else if (symbol == Symbol.EXHIBITOR) {
            userStr = applyExhibitor(userStr);
        } else {
            userStr += symbol.symbol;
        }

        view.setUserData(userStr);
        updateResult();
    }

    private void updateResult() {
        String res;
        try {
            res = getResult();
        } catch (ArithmeticException e) {
            res = e.getMessage();
            view.setResult(res);
            view.disableInput();
            return;
        }

        if (res == null)
            return;
        view.setResult("=" + res);
    }

    private String getResult() {
        List<String> data = parse(userStr);

        if (data.size() == sizeFullString) {
            Number a, b;
            boolean isFloat = false;

            try {
                a = Integer.parseInt(data.get(Position.Value1.ordinal()));
            } catch (NumberFormatException e) {
                a = Float.parseFloat(data.get(Position.Value1.ordinal()));
                isFloat = true;
            }

            try {
                b = Integer.parseInt(data.get(Position.Value2.ordinal()));
            } catch (NumberFormatException e) {
                b = Float.parseFloat(data.get(Position.Value2.ordinal()));
                isFloat = true;
            }
            float res = calculator.performOperation(
                    (float) a.intValue(), (float) b.intValue(), data.get(Position.Operation.ordinal())
            );
            if (isFloat)
                return String.valueOf(res);
            else
                return String.valueOf((int) res);

        } else if (data.size() == 0) {
            return null;
        } else {
            try {
                return String.valueOf(Integer.parseInt(data.get(Position.Value1.ordinal())));
            } catch (NumberFormatException e) {
                return String.valueOf(Float.parseFloat(data.get(Position.Value1.ordinal())));
            }
        }
    }

    private List<String> parse(String text) {
        List<String> res = new ArrayList<>();
        for (Symbol s : operationSymbols) {
            if (text.contains(s.symbol)) {
                res.addAll(Arrays.asList(text.split(Pattern.quote(s.symbol))));
                res.add(s.symbol);
                return res;
            }
        }
        if (!text.equals(""))
            res.add(text);
        return res;
    }

    private String applyPercent(String text) {
        List<String> res = parse(text);

        if (res.size() == sizeFullString) {
            float a = Float.parseFloat(res.get(Position.Value1.ordinal()));
            float b = Float.parseFloat(res.get(Position.Value2.ordinal()));

            b /= 100;
            return a + res.get(2) + a * b;
        } else if (res.size() == 1) {
            return String.valueOf(Float.parseFloat(res.get(Position.Value1.ordinal())) / 100);
        }
        return text;
    }

    private String applyExhibitor(String text) {
        List<String> res = parse(text);
        if (res.size() == 2 || res.size() == 0)
            return text + Symbol.EXHIBITOR.symbol;
        return text;
    }

    private void clean(){
        userStr = "";
        view.clean();
    }
}
