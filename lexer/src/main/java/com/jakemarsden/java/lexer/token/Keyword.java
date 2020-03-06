package com.jakemarsden.java.lexer.token;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Optional;

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
  DEFAULT,
  DO,
  DOUBLE,
  ELSE,
  ENUM,
  EXTENDS,
  FINAL,
  FINALLY,
  FLOAT,
  FOR,
  GOTO,
  IF,
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
  /** The value "<code>_</code>". */
  UNDERSCORE,
  /**
   * According to the language spec:
   *
   * <blockquote>
   *
   * {@code var} is not a keyword, but rather an identifier with special meaning as the type of a
   * local variable declaration <em>[...]</em> and the type of a lambda formal parameter.
   * <footer>-<cite><a
   * href="https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.9">The Java&reg;
   * Language Specification: Java SE 11 Edition - Chapter 3. Lexical Structure</a></cite></footer>
   *
   * </blockquote>
   *
   * For the sake of simplicity however, it makes sense to treat it as a keyword in this context.
   */
  VAR,
  VOID,
  VOLATILE,
  WHILE;

  public static Optional<Keyword> of(String value) {
    requireNonNull(value);
    return Arrays.stream(Keyword.values())
        .filter(keyword -> keyword.value().equals(value))
        .findFirst();
  }

  private String value() {
    return this == Keyword.UNDERSCORE ? "_" : this.name().toLowerCase();
  }

  @Override
  public String toString() {
    return this.value();
  }
}
