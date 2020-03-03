package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenTestUtils.assertTokensEquals;
import static com.jakemarsden.java.lexer.TokenTestUtils.createToken;
import static com.jakemarsden.java.lexer.TokenType.*;

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
      createToken(KEYWORD, "package", 0, 0, 0),
      createToken(WHITESPACE, " ", 0, 7, 0),
      createToken(IDENTIFIER, "com", 0, 8, 0),
      createToken(DOT, ".", 0, 11, 0),
      createToken(IDENTIFIER, "my", 0, 12, 0),
      createToken(DOT, ".", 0, 14, 0),
      createToken(IDENTIFIER, "pkg", 0, 15, 0),
      createToken(SEMICOLON, ";", 0, 18, 0),
      createToken(WHITESPACE, "\n\n", 0, 19, 0),
      //
      createToken(KEYWORD, "public", 2, 0, 21),
      createToken(WHITESPACE, " ", 2, 6, 21),
      createToken(KEYWORD, "class", 2, 7, 21),
      createToken(WHITESPACE, " ", 2, 12, 21),
      createToken(IDENTIFIER, "SampleCode", 2, 13, 21),
      createToken(WHITESPACE, " ", 2, 23, 21),
      createToken(BRACE_LEFT, "{", 2, 24, 21),
      createToken(WHITESPACE, "\n\n  ", 2, 25, 21),
      //
      createToken(KEYWORD, "public", 4, 2, 48),
      createToken(WHITESPACE, " ", 4, 8, 48),
      createToken(KEYWORD, "static", 4, 9, 48),
      createToken(WHITESPACE, " ", 4, 15, 48),
      createToken(KEYWORD, "void", 4, 16, 48),
      createToken(WHITESPACE, " ", 4, 20, 48),
      createToken(IDENTIFIER, "main", 4, 21, 48),
      createToken(PAREN_LEFT, "(", 4, 25, 48),
      createToken(IDENTIFIER, "String", 4, 26, 48),
      createToken(BRACKET_LEFT, "[", 4, 32, 48),
      createToken(BRACKET_RIGHT, "]", 4, 33, 48),
      createToken(WHITESPACE, " ", 4, 34, 48),
      createToken(IDENTIFIER, "args", 4, 35, 48),
      createToken(PAREN_RIGHT, ")", 4, 39, 48),
      createToken(WHITESPACE, " ", 4, 40, 48),
      createToken(BRACE_LEFT, "{", 4, 41, 48),
      createToken(WHITESPACE, "\n    ", 4, 42, 48),
      //
      createToken(IDENTIFIER, "System", 5, 4, 91),
      createToken(DOT, ".", 5, 10, 91),
      createToken(IDENTIFIER, "out", 5, 11, 91),
      createToken(DOT, ".", 5, 14, 91),
      createToken(IDENTIFIER, "println", 5, 15, 91),
      createToken(PAREN_LEFT, "(", 5, 22, 91),
      createToken(LITERAL_STRING, "\"Hello, world!\"", 5, 23, 91),
      createToken(PAREN_RIGHT, ")", 5, 38, 91),
      createToken(SEMICOLON, ";", 5, 39, 91),
      createToken(WHITESPACE, "\n  ", 5, 40, 91),
      //
      createToken(BRACE_RIGHT, "}", 6, 2, 132),
      createToken(WHITESPACE, "\n", 6, 3, 132),
      //
      createToken(BRACE_RIGHT, "}", 7, 0, 136),
      createToken(WHITESPACE, "\n\n", 7, 1, 136)
    };

    var lexer = new Lexer();
    var tokens = lexer.lex(sourceCodeItr);
    assertTokensEquals(tokens, expectedTokens);
  }
}
