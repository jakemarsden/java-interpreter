package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenType.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.jakemarsden.java.lexer.text.StringCharIterator;
import com.jakemarsden.java.lexer.text.TextPosition;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;

class LexerTest {

  @Test
  void lexSingleCharTokens() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(ANGLE_LEFT, 0, 0, 0, "<")), lexer.lex(new StringCharIterator("<")));
    assertTokensEquals(
        List.of(createToken(ANGLE_RIGHT, 0, 0, 0, ">")), lexer.lex(new StringCharIterator(">")));
    assertTokensEquals(
        List.of(createToken(BRACE_LEFT, 0, 0, 0, "{")), lexer.lex(new StringCharIterator("{")));
    assertTokensEquals(
        List.of(createToken(BRACE_RIGHT, 0, 0, 0, "}")), lexer.lex(new StringCharIterator("}")));
    assertTokensEquals(
        List.of(createToken(BRACKET_LEFT, 0, 0, 0, "[")), lexer.lex(new StringCharIterator("[")));
    assertTokensEquals(
        List.of(createToken(BRACKET_RIGHT, 0, 0, 0, "]")), lexer.lex(new StringCharIterator("]")));
    assertTokensEquals(
        List.of(createToken(DOT, 0, 0, 0, ".")), lexer.lex(new StringCharIterator(".")));
    assertTokensEquals(
        List.of(createToken(PAREN_LEFT, 0, 0, 0, "(")), lexer.lex(new StringCharIterator("(")));
    assertTokensEquals(
        List.of(createToken(PAREN_RIGHT, 0, 0, 0, ")")), lexer.lex(new StringCharIterator(")")));
    assertTokensEquals(
        List.of(createToken(SEMICOLON, 0, 0, 0, ";")), lexer.lex(new StringCharIterator(";")));
  }

  @Test
  void lexIdentifiers() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(IDENTIFIER, 0, 0, 0, "String")),
        lexer.lex(new StringCharIterator("String")));
    assertTokensEquals(
        List.of(createToken(IDENTIFIER, 0, 0, 0, "a$")), lexer.lex(new StringCharIterator("a$")));
    assertTokensEquals(
        List.of(createToken(IDENTIFIER, 0, 0, 0, "a_")), lexer.lex(new StringCharIterator("a_")));
    assertTokensEquals(
        List.of(createToken(IDENTIFIER, 0, 0, 0, "a1")), lexer.lex(new StringCharIterator("a1")));
    assertTokensEquals(
        List.of(createToken(IDENTIFIER, 0, 0, 0, "$a")), lexer.lex(new StringCharIterator("$a")));
    assertTokensEquals(
        List.of(createToken(IDENTIFIER, 0, 0, 0, "_a")), lexer.lex(new StringCharIterator("_a")));
  }

  @Test
  void lexNullLiteral() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(LITERAL_NULL, 0, 0, 0, "null")),
        lexer.lex(new StringCharIterator("null")));
  }

  @Test
  void lexBooleanLiterals() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(LITERAL_BOOLEAN, 0, 0, 0, "true")),
        lexer.lex(new StringCharIterator("true")));
    assertTokensEquals(
        List.of(createToken(LITERAL_BOOLEAN, 0, 0, 0, "false")),
        lexer.lex(new StringCharIterator("false")));
  }

  @Test
  void lexNumberLiterals() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1234")),
        lexer.lex(new StringCharIterator("1234")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "+1234")),
        lexer.lex(new StringCharIterator("+1234")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "-1234")),
        lexer.lex(new StringCharIterator("-1234")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1_234")),
        lexer.lex(new StringCharIterator("1_234")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1234.56")),
        lexer.lex(new StringCharIterator("1234.56")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1.234e3")),
        lexer.lex(new StringCharIterator("1.234e3")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1.234e+3")),
        lexer.lex(new StringCharIterator("1.234e+3")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1.234e-3")),
        lexer.lex(new StringCharIterator("1.234e-3")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1234.56f")),
        lexer.lex(new StringCharIterator("1234.56f")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1234.56F")),
        lexer.lex(new StringCharIterator("1234.56F")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1234l")),
        lexer.lex(new StringCharIterator("1234l")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "1234L")),
        lexer.lex(new StringCharIterator("1234L")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "0b10011001")),
        lexer.lex(new StringCharIterator("0b10011001")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "0231")),
        lexer.lex(new StringCharIterator("0231")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "0x99")),
        lexer.lex(new StringCharIterator("0x99")));
    assertTokensEquals(
        List.of(createToken(LITERAL_NUMBER, 0, 0, 0, "0xaAbBcCdDeEfF")),
        lexer.lex(new StringCharIterator("0xaAbBcCdDeEfF")));
  }

  @Test
  void lexCharacterLiterals() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(LITERAL_CHAR, 0, 0, 0, "'x'")),
        lexer.lex(new StringCharIterator("'x'")));
    assertTokensEquals(
        List.of(createToken(LITERAL_CHAR, 0, 0, 0, "'Hello, world!'")),
        lexer.lex(new StringCharIterator("'Hello, world!'")));
    assertTokensEquals(
        List.of(createToken(LITERAL_CHAR, 0, 0, 0, "'Hello, \"world\"!'")),
        lexer.lex(new StringCharIterator("'Hello, \"world\"!'")));
    assertTokensEquals(
        List.of(createToken(LITERAL_CHAR, 0, 0, 0, "'\u0000'")),
        lexer.lex(new StringCharIterator("'\u0000'")));
  }

  @Test
  void lexStringLiterals() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(LITERAL_STRING, 0, 0, 0, "\"\"")),
        lexer.lex(new StringCharIterator("\"\"")));
    assertTokensEquals(
        List.of(createToken(LITERAL_STRING, 0, 0, 0, "\"Hello, world!\"")),
        lexer.lex(new StringCharIterator("\"Hello, world!\"")));
    assertTokensEquals(
        List.of(createToken(LITERAL_STRING, 0, 0, 0, "\"Hello, 'world'!\"")),
        lexer.lex(new StringCharIterator("\"Hello, 'world'!\"")));
    assertTokensEquals(
        List.of(createToken(LITERAL_STRING, 0, 0, 0, "\"\u0000\"")),
        lexer.lex(new StringCharIterator("\"\u0000\"")));
  }

  @Test
  void lexWhiteSpace() {
    var lexer = new Lexer();

    assertTokensEquals(
        List.of(createToken(WHITESPACE, 0, 0, 0, " ")), lexer.lex(new StringCharIterator(" ")));
    assertTokensEquals(
        List.of(createToken(WHITESPACE, 0, 0, 0, "\t")), lexer.lex(new StringCharIterator("\t")));
    assertTokensEquals(
        List.of(createToken(WHITESPACE, 0, 0, 0, "\r")), lexer.lex(new StringCharIterator("\r")));
    assertTokensEquals(
        List.of(createToken(WHITESPACE, 0, 0, 0, "\n")), lexer.lex(new StringCharIterator("\n")));
    assertTokensEquals(
        List.of(createToken(WHITESPACE, 0, 0, 0, "    \t\r\n")),
        lexer.lex(new StringCharIterator("    \t\r\n")));
  }

  @Test
  void integration() {
    // Replace with Java 13 text block (eventually)
    String[] sampleCodeLines = {
      "package com.my.pkg;",
      "",
      "public class SampleCode {",
      "",
      "  public static void main(String[] args) {",
      "    System.out.println(\"Hello, world!\");",
      "  }",
      "}",
      "",
      ""
    };
    var sampleCode = String.join("\n", sampleCodeLines);

    var expectedTokens =
        List.of(
            createToken(KEYWORD, 0, 0, 0, "package"),
            createToken(WHITESPACE, 0, 7, 0, " "),
            createToken(IDENTIFIER, 0, 8, 0, "com"),
            createToken(DOT, 0, 11, 0, "."),
            createToken(IDENTIFIER, 0, 12, 0, "my"),
            createToken(DOT, 0, 14, 0, "."),
            createToken(IDENTIFIER, 0, 15, 0, "pkg"),
            createToken(SEMICOLON, 0, 18, 0, ";"),
            createToken(WHITESPACE, 0, 19, 0, "\n\n"),
            //
            createToken(KEYWORD, 2, 0, 21, "public"),
            createToken(WHITESPACE, 2, 6, 21, " "),
            createToken(KEYWORD, 2, 7, 21, "class"),
            createToken(WHITESPACE, 2, 12, 21, " "),
            createToken(IDENTIFIER, 2, 13, 21, "SampleCode"),
            createToken(WHITESPACE, 2, 23, 21, " "),
            createToken(BRACE_LEFT, 2, 24, 21, "{"),
            createToken(WHITESPACE, 2, 25, 21, "\n\n  "),
            //
            createToken(KEYWORD, 4, 2, 48, "public"),
            createToken(WHITESPACE, 4, 8, 48, " "),
            createToken(KEYWORD, 4, 9, 48, "static"),
            createToken(WHITESPACE, 4, 15, 48, " "),
            createToken(KEYWORD, 4, 16, 48, "void"),
            createToken(WHITESPACE, 4, 20, 48, " "),
            createToken(IDENTIFIER, 4, 21, 48, "main"),
            createToken(PAREN_LEFT, 4, 25, 48, "("),
            createToken(IDENTIFIER, 4, 26, 48, "String"),
            createToken(BRACKET_LEFT, 4, 32, 48, "["),
            createToken(BRACKET_RIGHT, 4, 33, 48, "]"),
            createToken(WHITESPACE, 4, 34, 48, " "),
            createToken(IDENTIFIER, 4, 35, 48, "args"),
            createToken(PAREN_RIGHT, 4, 39, 48, ")"),
            createToken(WHITESPACE, 4, 40, 48, " "),
            createToken(BRACE_LEFT, 4, 41, 48, "{"),
            createToken(WHITESPACE, 4, 42, 48, "\n    "),
            //
            createToken(IDENTIFIER, 5, 4, 91, "System"),
            createToken(DOT, 5, 10, 91, "."),
            createToken(IDENTIFIER, 5, 11, 91, "out"),
            createToken(DOT, 5, 14, 91, "."),
            createToken(IDENTIFIER, 5, 15, 91, "println"),
            createToken(PAREN_LEFT, 5, 22, 91, "("),
            createToken(LITERAL_STRING, 5, 23, 91, "\"Hello, world!\""),
            createToken(PAREN_RIGHT, 5, 38, 91, ")"),
            createToken(SEMICOLON, 5, 39, 91, ";"),
            createToken(WHITESPACE, 5, 40, 91, "\n  "),
            //
            createToken(BRACE_RIGHT, 6, 2, 132, "}"),
            createToken(WHITESPACE, 6, 3, 132, "\n"),
            //
            createToken(BRACE_RIGHT, 7, 0, 136, "}"),
            createToken(WHITESPACE, 7, 1, 136, "\n\n"));

    var lexer = new Lexer();
    var tokens = lexer.lex(new StringCharIterator(sampleCode));
    assertTokensEquals(expectedTokens, tokens);
  }

  private static void assertTokensEquals(Collection<Token> expected, Iterator<Token> actualItr) {
    var expectedItr = expected.iterator();
    int idx = 0;
    while (expectedItr.hasNext() && actualItr.hasNext()) {
      var expectedToken = expectedItr.next();
      var actualToken = actualItr.next();
      assertEquals(expectedToken, actualToken, "Token at index " + idx);
      idx++;
    }

    var finalIdx = idx;
    assertFalse(
        expectedItr.hasNext(),
        () -> format("Missing token at index %d: %s", finalIdx, expectedItr.next()));
    assertFalse(
        actualItr.hasNext(),
        () -> format("Unexpected token at index %d: %s", finalIdx, actualItr.next()));
  }

  private static Token createToken(
      TokenType type, int line, int col, int lineOffset, String value) {
    var position = TextPosition.of(line, col, lineOffset + col);
    return Token.of(type, position, value);
  }
}
