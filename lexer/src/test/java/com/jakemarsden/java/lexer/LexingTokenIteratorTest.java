package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenTestUtils.assertTokensEquals;
import static com.jakemarsden.java.lexer.TokenTestUtils.createToken;
import static com.jakemarsden.java.lexer.TokenType.*;

import org.junit.jupiter.api.Test;

class LexingTokenIteratorTest {

  @Test
  void invalid() {
    assertTokensEquals(initObjUnderTest("\uD83D\uDE00"), createToken(INVALID, "\uD83D\uDE00", 0));
  }

  @Test
  void singleChars() {
    assertTokensEquals(initObjUnderTest("<"), createToken(ANGLE_LEFT, "<", 0));
    assertTokensEquals(initObjUnderTest(">"), createToken(ANGLE_RIGHT, ">", 0));
    assertTokensEquals(initObjUnderTest("{"), createToken(BRACE_LEFT, "{", 0));
    assertTokensEquals(initObjUnderTest("}"), createToken(BRACE_RIGHT, "}", 0));
    assertTokensEquals(initObjUnderTest("["), createToken(BRACKET_LEFT, "[", 0));
    assertTokensEquals(initObjUnderTest("]"), createToken(BRACKET_RIGHT, "]", 0));
    assertTokensEquals(initObjUnderTest("."), createToken(DOT, ".", 0));
    assertTokensEquals(initObjUnderTest("("), createToken(PAREN_LEFT, "(", 0));
    assertTokensEquals(initObjUnderTest(")"), createToken(PAREN_RIGHT, ")", 0));
    assertTokensEquals(initObjUnderTest(";"), createToken(SEMICOLON, ";", 0));
  }

  @Test
  void whitespace() {
    assertTokensEquals(initObjUnderTest(" "), createToken(WHITESPACE, " ", 0));
    assertTokensEquals(initObjUnderTest("\t"), createToken(WHITESPACE, "\t", 0));
    assertTokensEquals(initObjUnderTest("\r"), createToken(WHITESPACE, "\r", 0));
    assertTokensEquals(initObjUnderTest("\n"), createToken(WHITESPACE, "\n", 0));
    assertTokensEquals(initObjUnderTest(" \t\r\n"), createToken(WHITESPACE, " \t\r\n", 0));
  }

  @Test
  void lineComments() {
    assertTokensEquals(
        initObjUnderTest("//\n"), // empty comment
        createToken(COMMENT_LINE, "//", 0),
        createToken(WHITESPACE, "\n", 2));

    assertTokensEquals(
        initObjUnderTest("// Line comment\n"),
        createToken(COMMENT_LINE, "// Line comment", 0),
        createToken(WHITESPACE, "\n", 15));

    assertTokensEquals(
        initObjUnderTest("//// Line comment\n"),
        createToken(COMMENT_LINE, "//// Line comment", 0),
        createToken(WHITESPACE, "\n", 17));

    assertTokensEquals(
        initObjUnderTest("// Line // comment\n"),
        createToken(COMMENT_LINE, "// Line // comment", 0),
        createToken(WHITESPACE, "\n", 18));

    assertTokensEquals(
        initObjUnderTest("// Line /* */ comment\n"),
        createToken(COMMENT_LINE, "// Line /* */ comment", 0),
        createToken(WHITESPACE, "\n", 21));

    // comments which are terminated by EOF:

    assertTokensEquals(
        initObjUnderTest("//"), // empty unterminated comment
        createToken(COMMENT_LINE, "//", 0));

    assertTokensEquals(
        initObjUnderTest("// Unterminated comment"),
        createToken(COMMENT_LINE, "// Unterminated comment", 0));
  }

  @Test
  void blockComments() {
    assertTokensEquals(
        initObjUnderTest("/**/"), // empty comment
        createToken(COMMENT_BLOCK, "/**/", 0));

    assertTokensEquals(
        initObjUnderTest("/* Block comment */"),
        createToken(COMMENT_BLOCK, "/* Block comment */", 0));

    assertTokensEquals(
        initObjUnderTest("/* Block comment */\n"),
        createToken(COMMENT_BLOCK, "/* Block comment */", 0),
        createToken(WHITESPACE, "\n", 19));

    assertTokensEquals(
        initObjUnderTest("/* Block /* comment */"),
        createToken(COMMENT_BLOCK, "/* Block /* comment */", 0));

    assertTokensEquals(
        initObjUnderTest("/* Block // comment */"),
        createToken(COMMENT_BLOCK, "/* Block // comment */", 0));

    assertTokensEquals(
        initObjUnderTest("/*/ Block comment */"),
        createToken(COMMENT_BLOCK, "/*/ Block comment */", 0));

    assertTokensEquals(
        initObjUnderTest("/**\n Javadoc\n * style\n * comment\n */"),
        createToken(COMMENT_BLOCK, "/**\n Javadoc\n * style\n * comment\n */", 0));

    // comments which are terminated by EOF:

    assertTokensEquals(
        initObjUnderTest("/*"), // empty unterminated comment
        createToken(INVALID, "/*", 0));

    assertTokensEquals(
        initObjUnderTest("/* Unterminated comment"),
        createToken(INVALID, "/* Unterminated comment", 0));

    assertTokensEquals(
        initObjUnderTest("/* Unterminated comment\n"),
        createToken(INVALID, "/* Unterminated comment\n", 0));

    assertTokensEquals(
        initObjUnderTest("/* Unterminated comment *"),
        createToken(INVALID, "/* Unterminated comment *", 0));
  }

  @Test
  void nullLiterals() {
    assertTokensEquals(initObjUnderTest("null"), createToken(LITERAL_NULL, "null", 0));

    assertTokensEquals(
        initObjUnderTest("null;"),
        createToken(LITERAL_NULL, "null", 0),
        createToken(SEMICOLON, ";", 4));

    assertTokensEquals(
        initObjUnderTest("null "),
        createToken(LITERAL_NULL, "null", 0),
        createToken(WHITESPACE, " ", 4));

    assertTokensEquals(
        initObjUnderTest("null\n"),
        createToken(LITERAL_NULL, "null", 0),
        createToken(WHITESPACE, "\n", 4));
  }

  @Test
  void booleanLiterals() {
    assertTokensEquals(initObjUnderTest("true"), createToken(LITERAL_BOOLEAN, "true", 0));
    assertTokensEquals(initObjUnderTest("false"), createToken(LITERAL_BOOLEAN, "false", 0));

    assertTokensEquals(
        initObjUnderTest("true;"),
        createToken(LITERAL_BOOLEAN, "true", 0),
        createToken(SEMICOLON, ";", 4));
    assertTokensEquals(
        initObjUnderTest("false;"),
        createToken(LITERAL_BOOLEAN, "false", 0),
        createToken(SEMICOLON, ";", 5));

    assertTokensEquals(
        initObjUnderTest("true "),
        createToken(LITERAL_BOOLEAN, "true", 0),
        createToken(WHITESPACE, " ", 4));
    assertTokensEquals(
        initObjUnderTest("false "),
        createToken(LITERAL_BOOLEAN, "false", 0),
        createToken(WHITESPACE, " ", 5));

    assertTokensEquals(
        initObjUnderTest("true\n"),
        createToken(LITERAL_BOOLEAN, "true", 0),
        createToken(WHITESPACE, "\n", 4));
    assertTokensEquals(
        initObjUnderTest("false\n"),
        createToken(LITERAL_BOOLEAN, "false", 0),
        createToken(WHITESPACE, "\n", 5));
  }

  @Test
  void characterLiterals() {
    assertTokensEquals(initObjUnderTest("'x'"), createToken(LITERAL_CHAR, "'x'", 0));
    assertTokensEquals(initObjUnderTest("'\u0000'"), createToken(LITERAL_CHAR, "'\u0000'", 0));
    assertTokensEquals(initObjUnderTest("'\\u0000'"), createToken(LITERAL_CHAR, "'\\u0000'", 0));

    assertTokensEquals(
        initObjUnderTest("'Hello, world!'"), //
        createToken(LITERAL_CHAR, "'Hello, world!'", 0));
    assertTokensEquals(
        initObjUnderTest("'Hello, \"world\"!'"),
        createToken(LITERAL_CHAR, "'Hello, \"world\"!'", 0));
  }

  @Test
  void stringLiterals() {
    assertTokensEquals(initObjUnderTest("\"\""), createToken(LITERAL_STRING, "\"\"", 0));
    assertTokensEquals(
        initObjUnderTest("\"\u0000\""), createToken(LITERAL_STRING, "\"\u0000\"", 0));
    assertTokensEquals(
        initObjUnderTest("\"\\u0000\""), createToken(LITERAL_STRING, "\"\\u0000\"", 0));

    assertTokensEquals(
        initObjUnderTest("\"Hello, world!\""), //
        createToken(LITERAL_STRING, "\"Hello, world!\"", 0));
    assertTokensEquals(
        initObjUnderTest("\"Hello, 'world'!\""),
        createToken(LITERAL_STRING, "\"Hello, 'world'!\"", 0));
  }

  @Test
  void identifiers() {
    assertTokensEquals(initObjUnderTest("String"), createToken(IDENTIFIER, "String", 0));
    assertTokensEquals(initObjUnderTest("a$"), createToken(IDENTIFIER, "a$", 0));
    assertTokensEquals(initObjUnderTest("a_"), createToken(IDENTIFIER, "a_", 0));
    assertTokensEquals(initObjUnderTest("a1"), createToken(IDENTIFIER, "a1", 0));
    assertTokensEquals(initObjUnderTest("$a"), createToken(IDENTIFIER, "$a", 0));
    assertTokensEquals(initObjUnderTest("_a"), createToken(IDENTIFIER, "_a", 0));
  }

  private static LexingTokenIterator initObjUnderTest(String sourceCode) {
    var sourceCodeItr = sourceCode.codePoints().iterator();
    return new LexingTokenIterator(sourceCodeItr);
  }
}
