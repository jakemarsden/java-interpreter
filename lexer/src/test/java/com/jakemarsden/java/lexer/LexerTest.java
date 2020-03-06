package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenTestUtils.assertTokensEquals;
import static com.jakemarsden.java.lexer.token.Keyword.*;
import static com.jakemarsden.java.lexer.token.Separator.*;

import com.jakemarsden.java.lexer.text.TextPosition;
import com.jakemarsden.java.lexer.token.Token;
import org.junit.jupiter.api.Test;

class LexerTest {

  @Test
  void integration() {
    // Replace with Java 13 text block (eventually)
    String[] sourceCodeLines = {
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
    var sourceCode = String.join("\n", sourceCodeLines);
    var sourceCodeItr = sourceCode.codePoints().iterator();

    Token[] expectedTokens = {
      Token.keyword(PACKAGE, TextPosition.start()),
      Token.whitespace(" ", atColumn(7)),
      Token.identifier("com", atColumn(8)),
      Token.separator(DOT, atColumn(11)),
      Token.identifier("my", atColumn(12)),
      Token.separator(DOT, atColumn(14)),
      Token.identifier("pkg", atColumn(15)),
      Token.separator(SEMICOLON, atColumn(18)),
      Token.whitespace("\n\n", atColumn(19)),
      //
      Token.keyword(PUBLIC, atPosition(2, 0, 21)),
      Token.whitespace(" ", atPosition(2, 6, 21)),
      Token.keyword(CLASS, atPosition(2, 7, 21)),
      Token.whitespace(" ", atPosition(2, 12, 21)),
      Token.identifier("SampleCode", atPosition(2, 13, 21)),
      Token.whitespace(" ", atPosition(2, 23, 21)),
      Token.separator(OPENING_BRACE, atPosition(2, 24, 21)),
      Token.whitespace("\n\n  ", atPosition(2, 25, 21)),
      //
      Token.keyword(PUBLIC, atPosition(4, 2, 48)),
      Token.whitespace(" ", atPosition(4, 8, 48)),
      Token.keyword(STATIC, atPosition(4, 9, 48)),
      Token.whitespace(" ", atPosition(4, 15, 48)),
      Token.keyword(VOID, atPosition(4, 16, 48)),
      Token.whitespace(" ", atPosition(4, 20, 48)),
      Token.identifier("main", atPosition(4, 21, 48)),
      Token.separator(OPENING_PAREN, atPosition(4, 25, 48)),
      Token.identifier("String", atPosition(4, 26, 48)),
      Token.separator(OPENING_BRACKET, atPosition(4, 32, 48)),
      Token.separator(CLOSING_BRACKET, atPosition(4, 33, 48)),
      Token.whitespace(" ", atPosition(4, 34, 48)),
      Token.identifier("args", atPosition(4, 35, 48)),
      Token.separator(CLOSING_PAREN, atPosition(4, 39, 48)),
      Token.whitespace(" ", atPosition(4, 40, 48)),
      Token.separator(OPENING_BRACE, atPosition(4, 41, 48)),
      Token.whitespace("\n    ", atPosition(4, 42, 48)),
      //
      Token.identifier("System", atPosition(5, 4, 91)),
      Token.separator(DOT, atPosition(5, 10, 91)),
      Token.identifier("out", atPosition(5, 11, 91)),
      Token.separator(DOT, atPosition(5, 14, 91)),
      Token.identifier("println", atPosition(5, 15, 91)),
      Token.separator(OPENING_PAREN, atPosition(5, 22, 91)),
      Token.stringLiteral("\"Hello, world!\"", atPosition(5, 23, 91)),
      Token.separator(CLOSING_PAREN, atPosition(5, 38, 91)),
      Token.separator(SEMICOLON, atPosition(5, 39, 91)),
      Token.whitespace("\n  ", atPosition(5, 40, 91)),
      //
      Token.separator(CLOSING_BRACE, atPosition(6, 2, 132)),
      Token.whitespace("\n", atPosition(6, 3, 132)),
      //
      Token.separator(CLOSING_BRACE, atPosition(7, 0, 136)),
      Token.whitespace("\n\n", atPosition(7, 1, 136))
    };

    var lexer = new Lexer();
    var tokens = lexer.lex(sourceCodeItr);
    assertTokensEquals(tokens, expectedTokens);
  }

  private static TextPosition atColumn(int col) {
    return atPosition(0, col, 0);
  }

  private static TextPosition atPosition(int line, int col, int lineOffset) {
    return TextPosition.of(line, col, col + lineOffset);
  }
}
