package com.jakemarsden.java.lexer;

import java.util.Arrays;

public enum Keyword {
  CLASS("class"),
  PACKAGE("package"),
  PUBLIC("public"),
  STATIC("static"),
  VOID("void");

  public static boolean isKeyword(String value) {
    return Arrays.stream(Keyword.values()) //
        .map(Keyword::value)
        .anyMatch(value::equals);
  }

  private final String value;

  Keyword(String value) {
    this.value = value;
  }

  private String value() {
    return this.value;
  }
}
