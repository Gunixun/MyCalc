package com.example.mycalc.ui;

public interface CalculatorView {

    boolean isWriting();

    void setResult(String value);

    void setUserData(String value);

    void disableInput();

    void clean();
}
