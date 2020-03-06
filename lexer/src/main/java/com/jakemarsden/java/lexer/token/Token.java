package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.INVALID;
import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public abstract class Token {

  public static BooleanLiteralToken booleanLiteral(boolean value, TextPosition position) {
    return new BooleanLiteralToken(value, position);
  }

  public static CharacterLiteralToken characterLiteral(CharSequence value, TextPosition position) {
    return new CharacterLiteralToken(value, position);
  }

  public static CommentToken comment(CommentType block, CharSequence value, TextPosition position) {
    return new CommentToken(block, value, position);
  }

  public static IdentifierToken identifier(CharSequence value, TextPosition position) {
    return new IdentifierToken(value, position);
  }

  public static KeywordToken keyword(Keyword type, TextPosition position) {
    return new KeywordToken(type, position);
  }

  public static NullLiteralToken nullLiteral(TextPosition position) {
    return new NullLiteralToken(position);
  }

  public static NumberLiteralToken numberLiteral(NumberLiteral value, TextPosition position) {
    return new NumberLiteralToken(value, position);
  }

  public static OperatorToken operator(Operator type, TextPosition position) {
    return new OperatorToken(type, position);
  }

  public static SeparatorToken separator(Separator type, TextPosition position) {
    return new SeparatorToken(type, position);
  }

  public static StringLiteralToken stringLiteral(CharSequence value, TextPosition position) {
    return new StringLiteralToken(value, position);
  }

  public static WhitespaceToken whitespace(CharSequence value, TextPosition position) {
    return new WhitespaceToken(value, position);
  }

  public static InvalidToken invalid(CharSequence value, TextPosition position) {
    return new InvalidToken(value, position);
  }

  private final TokenType type;
  private final TextPosition position;

  Token(TokenType type, TextPosition position) {
    this.type = requireNonNull(type);
    this.position = requireNonNull(position);
  }

  public final boolean isValid() {
    return this.type != INVALID;
  }

  public final TokenType type() {
    return this.type;
  }

  /**
   * Returns the position of the first character of the token within the source code.
   *
   * @return the position of the first character of the token within the source code
   */
  public final TextPosition position() {
    return this.position;
  }

  /**
   * Returns an implementation-specific substring for {@link #toString()} to use, or {@code null} if
   * the concrete class name conveys enough information on its own.
   *
   * @return an implementation-specific substring, or {@code null}
   */
  CharSequence valueAsString() {
    return null;
  }

  @Override
  public final String toString() {
    var value = this.valueAsString();
    return value == null
        ? format("%s[%s %s]", Token.class.getSimpleName(), this.position, this.type)
        : format("%s[%s %s %s]", Token.class.getSimpleName(), this.position, this.type, value);
  }

  @Override
  public int hashCode() {
    return hash(this.type, this.position);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (this.getClass() != o.getClass()) return false;
    var obj = (Token) o;
    return this.type == obj.type && this.position.equals(obj.position);
  }
}
