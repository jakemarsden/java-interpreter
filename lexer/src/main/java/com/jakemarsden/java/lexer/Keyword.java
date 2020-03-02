package com.jakemarsden.java.lexer;

import java.util.Arrays;

public enum Keyword {
  ABSTRACT,
  ASSERT,
  BOOLEAN,
  BREAK,
  BYTE,
  CASE,
  CATCH,
  CHAR,
  CLASS,
  CONST,
  CONTINUE,
  DO,
  DOUBLE,
  ELSE,
  EXTENDS,
  FINAL,
  FINALLY,
  FLOAT,
  FOR,
  IF,
  GOTO,
  IMPLEMENTS,
  IMPORT,
  INSTANCEOF,
  INT,
  INTERFACE,
  LONG,
  NATIVE,
  NEW,
  PACKAGE,
  PRIVATE,
  PROTECTED,
  PUBLIC,
  RETURN,
  SHORT,
  STATIC,
  STRICTFP,
  SUPER,
  SWITCH,
  SYNCHRONIZED,
  THIS,
  THROW,
  THROWS,
  TRANSIENT,
  TRY,
  UNDERSCORE("_"),
  VAR,
  VOID,
  VOLATILE,
  WHILE;

  public static boolean isKeyword(String value) {
    return Arrays.stream(Keyword.values()) //
        .map(Keyword::value)
        .anyMatch(value::equals);
  }

  private final String value;

  Keyword() {
    this.value = String.valueOf(this).toLowerCase();
  }

  Keyword(String value) {
    this.value = value;
  }

  private String value() {
    return this.value;
  }
}
