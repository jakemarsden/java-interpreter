package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenTestUtils.assertTokensEquals;
import static com.jakemarsden.java.lexer.token.CommentType.LINE;
import static com.jakemarsden.java.lexer.token.Keyword.*;
import static com.jakemarsden.java.lexer.token.Operator.*;
import static com.jakemarsden.java.lexer.token.Separator.*;

import com.jakemarsden.java.lexer.text.TextPosition;
import com.jakemarsden.java.lexer.token.Keyword;
import com.jakemarsden.java.lexer.token.NumberLiteral;
import com.jakemarsden.java.lexer.token.Token;
import org.junit.jupiter.api.Test;

class LexingTokenIteratorTest {

  @Test
  void invalid() {
    assertTokensEquals(initObjUnderTest("\uD83D\uDE00"), Token.invalid("\uD83D\uDE00", atStart()));
  }

  @Test
  void assignmentOperators() {
    assertTokensEquals(initObjUnderTest("="), Token.operator(ASSIGNMENT, atStart()));
  }

  @Test
  void numericalOperators() {
    assertTokensEquals(initObjUnderTest("+"), Token.operator(ADDITION, atStart()));
    assertTokensEquals(initObjUnderTest("/"), Token.operator(DIVISION, atStart()));
    assertTokensEquals(initObjUnderTest("*"), Token.operator(MULTIPLICATION, atStart()));
    assertTokensEquals(initObjUnderTest("%"), Token.operator(REMAINDER, atStart()));
    assertTokensEquals(initObjUnderTest("-"), Token.operator(SUBTRACTION, atStart()));
  }

  @Test
  void numericalAssignmentOperators() {
    assertTokensEquals(initObjUnderTest("+="), Token.operator(ADDITION_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("--"), Token.operator(DECREMENT, atStart()));
    assertTokensEquals(initObjUnderTest("/="), Token.operator(DIVISION_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("++"), Token.operator(INCREMENT, atStart()));
    assertTokensEquals(
        initObjUnderTest("*="), Token.operator(MULTIPLICATION_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("%="), Token.operator(REMAINDER_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("-="), Token.operator(SUBTRACTION_ASSIGNMENT, atStart()));
  }

  @Test
  void integralOperators() {
    assertTokensEquals(initObjUnderTest("&"), Token.operator(BITWISE_AND, atStart()));
    assertTokensEquals(initObjUnderTest("~"), Token.operator(BITWISE_COMPLEMENT, atStart()));
    assertTokensEquals(initObjUnderTest("|"), Token.operator(BITWISE_IOR, atStart()));
    assertTokensEquals(initObjUnderTest("^"), Token.operator(BITWISE_XOR, atStart()));
    assertTokensEquals(initObjUnderTest("<<"), Token.operator(LEFT_SHIFT, atStart()));
    assertTokensEquals(initObjUnderTest(">>"), Token.operator(RIGHT_SHIFT, atStart()));
    assertTokensEquals(initObjUnderTest(">>>"), Token.operator(UNSIGNED_RIGHT_SHIFT, atStart()));
  }

  @Test
  void integralAssignmentOperators() {
    assertTokensEquals(initObjUnderTest("&="), Token.operator(BITWISE_AND_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("|="), Token.operator(BITWISE_IOR_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("^="), Token.operator(BITWISE_XOR_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest("<<="), Token.operator(LEFT_SHIFT_ASSIGNMENT, atStart()));
    assertTokensEquals(initObjUnderTest(">>="), Token.operator(RIGHT_SHIFT_ASSIGNMENT, atStart()));
    assertTokensEquals(
        initObjUnderTest(">>>="), Token.operator(UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, atStart()));
  }

  @Test
  void comparisonOperators() {
    assertTokensEquals(initObjUnderTest("=="), Token.operator(EQUAL_TO, atStart()));
    assertTokensEquals(initObjUnderTest("&&"), Token.operator(LOGICAL_AND, atStart()));
    assertTokensEquals(initObjUnderTest("!"), Token.operator(LOGICAL_COMPLEMENT, atStart()));
    assertTokensEquals(initObjUnderTest("||"), Token.operator(LOGICAL_IOR, atStart()));
    assertTokensEquals(initObjUnderTest("!="), Token.operator(NOT_EQUAL_TO, atStart()));
    assertTokensEquals(initObjUnderTest("?"), Token.operator(TERNARY1, atStart()));
    assertTokensEquals(initObjUnderTest(":"), Token.operator(TERNARY2, atStart()));
  }

  @Test
  void numericalComparisonOperators() {
    assertTokensEquals(initObjUnderTest(">"), Token.operator(GREATER_THAN, atStart()));
    assertTokensEquals(initObjUnderTest(">="), Token.operator(GREATER_THAN_OR_EQUAL_TO, atStart()));
    assertTokensEquals(initObjUnderTest("<"), Token.operator(LESS_THAN, atStart()));
    assertTokensEquals(initObjUnderTest("<="), Token.operator(LESS_THAN_OR_EQUAL_TO, atStart()));
  }

  @Test
  void lambdaOperators() {
    assertTokensEquals(initObjUnderTest("->"), Token.operator(LAMBDA, atStart()));
  }

  @Test
  void operatorsStartingWithSameCharAreNotConfusedForEachOther() {
    // '>' was chosen as it has the most number of operators with the same first char. there is no
    // reason this test couldn't be extended for *all* operators starting with the same char, except
    // that there are a lot of them. instead, it is assumed that if the worst case behaves correctly
    // then all cases will behave correctly
    assertTokensEquals(
        initObjUnderTest(">;>=;>>;>>=;>>>;>>>="),
        Token.operator(GREATER_THAN, atStart()),
        Token.separator(SEMICOLON, atColumn(1)),
        Token.operator(GREATER_THAN_OR_EQUAL_TO, atColumn(2)),
        Token.separator(SEMICOLON, atColumn(4)),
        Token.operator(RIGHT_SHIFT, atColumn(5)),
        Token.separator(SEMICOLON, atColumn(7)),
        Token.operator(RIGHT_SHIFT_ASSIGNMENT, atColumn(8)),
        Token.separator(SEMICOLON, atColumn(11)),
        Token.operator(UNSIGNED_RIGHT_SHIFT, atColumn(12)),
        Token.separator(SEMICOLON, atColumn(15)),
        Token.operator(UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, atColumn(16)));

    assertTokensEquals(
        initObjUnderTest(">>>=;>>>;>>=;>>;>=;>"),
        Token.operator(UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, atStart()),
        Token.separator(SEMICOLON, atColumn(4)),
        Token.operator(UNSIGNED_RIGHT_SHIFT, atColumn(5)),
        Token.separator(SEMICOLON, atColumn(8)),
        Token.operator(RIGHT_SHIFT_ASSIGNMENT, atColumn(9)),
        Token.separator(SEMICOLON, atColumn(12)),
        Token.operator(RIGHT_SHIFT, atColumn(13)),
        Token.separator(SEMICOLON, atColumn(15)),
        Token.operator(GREATER_THAN_OR_EQUAL_TO, atColumn(16)),
        Token.separator(SEMICOLON, atColumn(18)),
        Token.operator(GREATER_THAN, atColumn(19)));
  }

  @Test
  void separators() {
    assertTokensEquals(initObjUnderTest("{"), Token.separator(OPENING_BRACE, atStart()));
    assertTokensEquals(initObjUnderTest("}"), Token.separator(CLOSING_BRACE, atStart()));
    assertTokensEquals(initObjUnderTest("["), Token.separator(OPENING_BRACKET, atStart()));
    assertTokensEquals(initObjUnderTest("]"), Token.separator(CLOSING_BRACKET, atStart()));
    assertTokensEquals(initObjUnderTest("("), Token.separator(OPENING_PAREN, atStart()));
    assertTokensEquals(initObjUnderTest(")"), Token.separator(CLOSING_PAREN, atStart()));

    assertTokensEquals(initObjUnderTest("@"), Token.separator(ANNOTATION, atStart()));
    assertTokensEquals(initObjUnderTest("."), Token.separator(DOT, atStart()));
    assertTokensEquals(initObjUnderTest(","), Token.separator(ELEMENT_DELIMITER, atStart()));
    assertTokensEquals(initObjUnderTest("::"), Token.separator(METHOD_REFERENCE, atStart()));
    assertTokensEquals(initObjUnderTest(";"), Token.separator(SEMICOLON, atStart()));
    assertTokensEquals(initObjUnderTest("..."), Token.separator(VARARG, atStart()));
  }

  @Test
  void varargSeparatorIsNotConfusedWithDotSeparator() {
    assertTokensEquals(
        initObjUnderTest(".;..."),
        Token.separator(DOT, atStart()),
        Token.separator(SEMICOLON, atColumn(1)),
        Token.separator(VARARG, atColumn(2)));
    assertTokensEquals(
        initObjUnderTest("...;."),
        Token.separator(VARARG, atStart()),
        Token.separator(SEMICOLON, atColumn(3)),
        Token.separator(DOT, atColumn(4)));
  }

  @Test
  void ternaryOperatorIsNotConfusedWithMethodReferenceSeparator() {
    assertTokensEquals(
        initObjUnderTest(":;::"),
        Token.operator(TERNARY2, atStart()),
        Token.separator(SEMICOLON, atColumn(1)),
        Token.separator(METHOD_REFERENCE, atColumn(2)));
    assertTokensEquals(
        initObjUnderTest("::;:"),
        Token.separator(METHOD_REFERENCE, atStart()),
        Token.separator(SEMICOLON, atColumn(2)),
        Token.operator(TERNARY2, atColumn(3)));
  }

  @Test
  void emptyLineCommentIsNotConfusedWithDivisionOperator() {
    assertTokensEquals(initObjUnderTest("///"), Token.comment(LINE, "///", atStart()));
    assertTokensEquals(
        initObjUnderTest("/;//"),
        Token.operator(DIVISION, atStart()),
        Token.separator(SEMICOLON, atColumn(1)),
        Token.comment(LINE, "//", atColumn(2)));
  }

  @Test
  void whitespace() {
    assertTokensEquals(initObjUnderTest(" "), Token.whitespace(" ", atStart()));
    assertTokensEquals(initObjUnderTest("\t"), Token.whitespace("\t", atStart()));
    assertTokensEquals(initObjUnderTest("\r"), Token.whitespace("\r", atStart()));
    assertTokensEquals(initObjUnderTest("\n"), Token.whitespace("\n", atStart()));
    assertTokensEquals(initObjUnderTest(" \t\r\n"), Token.whitespace(" \t\r\n", atStart()));
  }

  @Test
  void nullLiterals() {
    assertTokensEquals(initObjUnderTest("null"), Token.nullLiteral(atStart()));
    assertTokensEquals(
        initObjUnderTest(";null;"),
        Token.separator(SEMICOLON, atStart()),
        Token.nullLiteral(atColumn(1)),
        Token.separator(SEMICOLON, atColumn(5)));
  }

  @Test
  void booleanLiterals() {
    assertTokensEquals(initObjUnderTest("true"), Token.booleanLiteral(true, atStart()));
    assertTokensEquals(
        initObjUnderTest(";true;"),
        Token.separator(SEMICOLON, atStart()),
        Token.booleanLiteral(true, atColumn(1)),
        Token.separator(SEMICOLON, atColumn(5)));

    assertTokensEquals(initObjUnderTest("false"), Token.booleanLiteral(false, atStart()));
    assertTokensEquals(
        initObjUnderTest(";false;"),
        Token.separator(SEMICOLON, atStart()),
        Token.booleanLiteral(false, atColumn(1)),
        Token.separator(SEMICOLON, atColumn(6)));
  }

  @Test
  void characterLiterals() {
    assertTokensEquals(initObjUnderTest("'x'"), Token.characterLiteral("'x'", atStart()));
    assertTokensEquals(initObjUnderTest("'\u0000'"), Token.characterLiteral("'\u0000'", atStart()));

    assertTokensEquals(
        initObjUnderTest("'\\u0000'"), Token.characterLiteral("'\\u0000'", atStart()));

    assertTokensEquals(
        initObjUnderTest("'Hello, world!'"), Token.characterLiteral("'Hello, world!'", atStart()));

    assertTokensEquals(
        initObjUnderTest("'Hello, \"world\"!'"),
        Token.characterLiteral("'Hello, \"world\"!'", atStart()));
  }

  @Test
  void stringLiterals() {
    assertTokensEquals(initObjUnderTest("\"\""), Token.stringLiteral("\"\"", atStart()));

    assertTokensEquals(
        initObjUnderTest("\"\u0000\""), Token.stringLiteral("\"\u0000\"", atStart()));

    assertTokensEquals(
        initObjUnderTest("\"\\u0000\""), Token.stringLiteral("\"\\u0000\"", atStart()));

    assertTokensEquals(
        initObjUnderTest("\"Hello, world!\""), Token.stringLiteral("\"Hello, world!\"", atStart()));

    assertTokensEquals(
        initObjUnderTest("\"Hello, 'world'!\""),
        Token.stringLiteral("\"Hello, 'world'!\"", atStart()));
  }

  @Test
  void numberLiterals() {
    assertTokensEquals(
        initObjUnderTest("0"), Token.numberLiteral(NumberLiteral.of(0, "0"), atStart()));
    assertTokensEquals(
        initObjUnderTest("1_2"), Token.numberLiteral(NumberLiteral.of(12, "1_2"), atStart()));
    assertTokensEquals(
        initObjUnderTest("1_"),
        Token.numberLiteral(NumberLiteral.of(1, "1"), atStart()),
        Token.keyword(Keyword.UNDERSCORE, atColumn(1)));
  }

  @Test
  void keywords() {
    assertTokensEquals(initObjUnderTest("abstract"), Token.keyword(ABSTRACT, atStart()));
    assertTokensEquals(initObjUnderTest("assert"), Token.keyword(ASSERT, atStart()));
    assertTokensEquals(initObjUnderTest("boolean"), Token.keyword(BOOLEAN, atStart()));
    assertTokensEquals(initObjUnderTest("break"), Token.keyword(BREAK, atStart()));
    assertTokensEquals(initObjUnderTest("byte"), Token.keyword(BYTE, atStart()));
    assertTokensEquals(initObjUnderTest("case"), Token.keyword(CASE, atStart()));
    assertTokensEquals(initObjUnderTest("catch"), Token.keyword(CATCH, atStart()));
    assertTokensEquals(initObjUnderTest("char"), Token.keyword(CHAR, atStart()));
    assertTokensEquals(initObjUnderTest("class"), Token.keyword(CLASS, atStart()));
    assertTokensEquals(initObjUnderTest("const"), Token.keyword(CONST, atStart()));
    assertTokensEquals(initObjUnderTest("continue"), Token.keyword(CONTINUE, atStart()));
    assertTokensEquals(initObjUnderTest("default"), Token.keyword(DEFAULT, atStart()));
    assertTokensEquals(initObjUnderTest("do"), Token.keyword(DO, atStart()));
    assertTokensEquals(initObjUnderTest("double"), Token.keyword(DOUBLE, atStart()));
    assertTokensEquals(initObjUnderTest("else"), Token.keyword(ELSE, atStart()));
    assertTokensEquals(initObjUnderTest("enum"), Token.keyword(ENUM, atStart()));
    assertTokensEquals(initObjUnderTest("extends"), Token.keyword(EXTENDS, atStart()));
    assertTokensEquals(initObjUnderTest("final"), Token.keyword(FINAL, atStart()));
    assertTokensEquals(initObjUnderTest("finally"), Token.keyword(FINALLY, atStart()));
    assertTokensEquals(initObjUnderTest("float"), Token.keyword(FLOAT, atStart()));
    assertTokensEquals(initObjUnderTest("for"), Token.keyword(FOR, atStart()));
    assertTokensEquals(initObjUnderTest("goto"), Token.keyword(GOTO, atStart()));
    assertTokensEquals(initObjUnderTest("if"), Token.keyword(IF, atStart()));
    assertTokensEquals(initObjUnderTest("implements"), Token.keyword(IMPLEMENTS, atStart()));
    assertTokensEquals(initObjUnderTest("import"), Token.keyword(IMPORT, atStart()));
    assertTokensEquals(initObjUnderTest("instanceof"), Token.keyword(INSTANCEOF, atStart()));
    assertTokensEquals(initObjUnderTest("int"), Token.keyword(INT, atStart()));
    assertTokensEquals(initObjUnderTest("interface"), Token.keyword(INTERFACE, atStart()));
    assertTokensEquals(initObjUnderTest("long"), Token.keyword(LONG, atStart()));
    assertTokensEquals(initObjUnderTest("native"), Token.keyword(NATIVE, atStart()));
    assertTokensEquals(initObjUnderTest("new"), Token.keyword(NEW, atStart()));
    assertTokensEquals(initObjUnderTest("package"), Token.keyword(PACKAGE, atStart()));
    assertTokensEquals(initObjUnderTest("private"), Token.keyword(PRIVATE, atStart()));
    assertTokensEquals(initObjUnderTest("protected"), Token.keyword(PROTECTED, atStart()));
    assertTokensEquals(initObjUnderTest("public"), Token.keyword(PUBLIC, atStart()));
    assertTokensEquals(initObjUnderTest("return"), Token.keyword(RETURN, atStart()));
    assertTokensEquals(initObjUnderTest("short"), Token.keyword(SHORT, atStart()));
    assertTokensEquals(initObjUnderTest("static"), Token.keyword(STATIC, atStart()));
    assertTokensEquals(initObjUnderTest("strictfp"), Token.keyword(STRICTFP, atStart()));
    assertTokensEquals(initObjUnderTest("super"), Token.keyword(SUPER, atStart()));
    assertTokensEquals(initObjUnderTest("switch"), Token.keyword(SWITCH, atStart()));
    assertTokensEquals(initObjUnderTest("synchronized"), Token.keyword(SYNCHRONIZED, atStart()));
    assertTokensEquals(initObjUnderTest("this"), Token.keyword(THIS, atStart()));
    assertTokensEquals(initObjUnderTest("throw"), Token.keyword(THROW, atStart()));
    assertTokensEquals(initObjUnderTest("throws"), Token.keyword(THROWS, atStart()));
    assertTokensEquals(initObjUnderTest("transient"), Token.keyword(TRANSIENT, atStart()));
    assertTokensEquals(initObjUnderTest("try"), Token.keyword(TRY, atStart()));
    assertTokensEquals(initObjUnderTest("_"), Token.keyword(UNDERSCORE, atStart()));
    assertTokensEquals(initObjUnderTest("var"), Token.keyword(VAR, atStart()));
    assertTokensEquals(initObjUnderTest("void"), Token.keyword(VOID, atStart()));
    assertTokensEquals(initObjUnderTest("volatile"), Token.keyword(VOLATILE, atStart()));
    assertTokensEquals(initObjUnderTest("while"), Token.keyword(WHILE, atStart()));
  }

  @Test
  void identifiers() {
    assertTokensEquals(initObjUnderTest("String"), Token.identifier("String", atStart()));
    assertTokensEquals(initObjUnderTest("a$"), Token.identifier("a$", atStart()));
    assertTokensEquals(initObjUnderTest("a_"), Token.identifier("a_", atStart()));
    assertTokensEquals(initObjUnderTest("a1"), Token.identifier("a1", atStart()));
    assertTokensEquals(initObjUnderTest("$a"), Token.identifier("$a", atStart()));
    assertTokensEquals(initObjUnderTest("_a"), Token.identifier("_a", atStart()));
  }

  private static LexingTokenIterator initObjUnderTest(String sourceCode) {
    var sourceCodeItr = sourceCode.codePoints().iterator();
    return new LexingTokenIterator(sourceCodeItr);
  }

  private static TextPosition atStart() {
    return TextPosition.start();
  }

  private static TextPosition atColumn(int col) {
    return TextPosition.of(0, col, col);
  }
}
