package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.COMMENT;
import static com.jakemarsden.java.lexer.util.StringUtils.codePointCount;
import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class CommentToken extends Token {

  private final CommentType commentType;
  private final CharSequence value;

  CommentToken(CommentType type, CharSequence value, TextPosition position) {
    super(COMMENT, position);
    this.commentType = requireNonNull(type);
    this.value = requireNonNull(value);
  }

  public CommentType commentType() {
    return this.commentType;
  }

  /**
   * Returns the value of the literal, including any opening/closing markers such as {@code "//"},
   * {@code "/*"}, <code>"&times;/"</code>. {@link CommentType#LINE} comments <em>do not</em>
   * contain a trailing newline.
   *
   * @return the value of the literal
   */
  public CharSequence value() {
    return this.value;
  }

  @Override
  public CharSequence valueAsString() {
    return format("%s %d", this.commentType, codePointCount(this.value));
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.commentType, this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (CommentToken) o;
    return this.commentType == obj.commentType && strContentEquals(this.value, obj.value);
  }
}
