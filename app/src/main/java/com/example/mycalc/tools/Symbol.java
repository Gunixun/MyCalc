package com.example.mycalc.tools;

public enum Symbol {
    DIV("/"),
    CLEAN("AC"),
    PERCENT("%"),
    EXHIBITOR(String.format("%.4f", Math.E).replace(",", ".")),
    BACKSPACE("<"),
    EQUALS("="),
    SUM("+"),
    SUB("-"),
    MULT("*"),
    DOT("."),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

    public final String symbol;

    private Symbol(String label) {
        this.symbol = label;
    }

}
