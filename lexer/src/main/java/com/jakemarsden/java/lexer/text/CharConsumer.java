package com.jakemarsden.java.lexer.text;

@FunctionalInterface
public interface CharConsumer {

  void accept(char value);
}
