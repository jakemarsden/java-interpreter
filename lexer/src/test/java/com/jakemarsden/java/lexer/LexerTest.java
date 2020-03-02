package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenType.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.jakemarsden.java.lexer.text.StringCharIterator;
import com.jakemarsden.java.lexer.text.TextPosition;
import java.util.List;
import org.junit.jupiter.api.Test;

class LexerTest {

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
            createToken(WHITESPACE, 0, 7, 7, " "),
            createToken(IDENTIFIER, 0, 8, 8, "com"),
            createToken(DOT, 0, 11, 11, "."),
            createToken(IDENTIFIER, 0, 12, 12, "my"),
            createToken(DOT, 0, 14, 14, "."),
            createToken(IDENTIFIER, 0, 15, 15, "pkg"),
            createToken(SEMICOLON, 0, 18, 18, ";"),
            createToken(WHITESPACE, 0, 19, 19, "\n\n"),
            //
            createToken(KEYWORD, 2, 0, 21, "public"),
            createToken(WHITESPACE, 2, 6, 21 + 6, " "),
            createToken(KEYWORD, 2, 7, 21 + 7, "class"),
            createToken(WHITESPACE, 2, 12, 21 + 12, " "),
            createToken(IDENTIFIER, 2, 13, 21 + 13, "SampleCode"),
            createToken(WHITESPACE, 2, 23, 21 + 23, " "),
            createToken(BRACE_LEFT, 2, 24, 21 + 24, "{"),
            createToken(WHITESPACE, 2, 25, 21 + 25, "\n\n  "),
            //
            createToken(KEYWORD, 4, 2, 48 + 2, "public"),
            createToken(WHITESPACE, 4, 8, 48 + 8, " "),
            createToken(KEYWORD, 4, 9, 48 + 9, "static"),
            createToken(WHITESPACE, 4, 15, 48 + 15, " "),
            createToken(KEYWORD, 4, 16, 48 + 16, "void"),
            createToken(WHITESPACE, 4, 20, 48 + 20, " "),
            createToken(IDENTIFIER, 4, 21, 48 + 21, "main"),
            createToken(PAREN_LEFT, 4, 25, 48 + 25, "("),
            createToken(IDENTIFIER, 4, 26, 48 + 26, "String"),
            createToken(BRACKET_LEFT, 4, 32, 48 + 32, "["),
            createToken(BRACKET_RIGHT, 4, 33, 48 + 33, "]"),
            createToken(WHITESPACE, 4, 34, 48 + 34, " "),
            createToken(IDENTIFIER, 4, 35, 48 + 35, "args"),
            createToken(PAREN_RIGHT, 4, 39, 48 + 39, ")"),
            createToken(WHITESPACE, 4, 40, 48 + 40, " "),
            createToken(BRACE_LEFT, 4, 41, 48 + 41, "{"),
            createToken(WHITESPACE, 4, 42, 48 + 42, "\n    "),
            //
            createToken(IDENTIFIER, 5, 4, 91 + 4, "System"),
            createToken(DOT, 5, 10, 91 + 10, "."),
            createToken(IDENTIFIER, 5, 11, 91 + 11, "out"),
            createToken(DOT, 5, 14, 91 + 14, "."),
            createToken(IDENTIFIER, 5, 15, 91 + 15, "println"),
            createToken(PAREN_LEFT, 5, 22, 91 + 22, "("),
            createToken(QUOTED_DOUBLE, 5, 23, 91 + 23, "\"Hello, world!\""),
            createToken(PAREN_RIGHT, 5, 38, 91 + 38, ")"),
            createToken(SEMICOLON, 5, 39, 91 + 39, ";"),
            createToken(WHITESPACE, 5, 40, 91 + 40, "\n  "),
            //
            createToken(BRACE_RIGHT, 6, 2, 132 + 2, "}"),
            createToken(WHITESPACE, 6, 3, 132 + 3, "\n"),
            //
            createToken(BRACE_RIGHT, 7, 0, 136, "}"),
            createToken(WHITESPACE, 7, 1, 136 + 1, "\n\n"));

    var lexer = new Lexer();
    var actualTokensItr = lexer.lex(new StringCharIterator(sampleCode));

    var expectedTokensItr = expectedTokens.iterator();
    int idx = 0;
    while (expectedTokensItr.hasNext() && actualTokensItr.hasNext()) {
      var expected = expectedTokensItr.next();
      var actual = actualTokensItr.next();
      assertEquals(expected, actual, "Token at index " + idx);
      idx++;
    }

    var finalIdx = idx;
    assertFalse(
        expectedTokensItr.hasNext(),
        () -> format("Missing token at index %d: %s", finalIdx, expectedTokensItr.next()));
    assertFalse(
        actualTokensItr.hasNext(),
        () -> format("Unexpected token at index %d: %s", finalIdx, actualTokensItr.next()));
  }

  private static Token createToken(
      TokenType type, int lineIdx, int colIdx, int charIdx, String value) {
    var position = TextPosition.of(lineIdx, colIdx, charIdx);
    return Token.of(type, position, value);
  }
}
