package com.jakemarsden.java.lexer.text;

import static java.lang.String.format;

/** Information about the position of a character within text */
public final class TextPosition implements Comparable<TextPosition> {

  private static final TextPosition START = new TextPosition(0, 0, 0);

  public static TextPosition start() {
    return START;
  }

  public static TextPosition of(int lineIdx, int colIdx, int charIdx) {
    return new TextPosition(lineIdx, colIdx, charIdx);
  }

  private final int lineIdx;
  private final int colIdx;
  private final int charIdx;

  private TextPosition(int lineIdx, int colIdx, int charIdx) {
    if (lineIdx < 0) throw new IllegalArgumentException("Invalid line index: " + lineIdx);
    if (colIdx < 0) throw new IllegalArgumentException("Invalid column index: " + colIdx);
    if (charIdx < 0) throw new IllegalArgumentException("Invalid character index: " + charIdx);
    this.lineIdx = lineIdx;
    this.colIdx = colIdx;
    this.charIdx = charIdx;
  }

  /** @return {@code true} if this position represents the first character in the text */
  public boolean isStart() {
    return this.line() == 0 && this.column() == 0;
  }

  /** @return zero-based index of the relevant line within the text */
  public int line() {
    return this.lineIdx;
  }

  /** @return zero-based index of the relevant character within the line of text */
  public int column() {
    return this.colIdx;
  }

  /** @return zero-based index of the relevant character from the start of the text */
  public int charIndex() {
    return this.charIdx;
  }

  public TextPosition nextLine() {
    return TextPosition.of(this.line() + 1, 0, this.charIndex() + 1);
  }

  public TextPosition nextColumn() {
    return TextPosition.of(this.line(), this.column() + 1, this.charIndex() + 1);
  }

  @Override
  public String toString() {
    return format("[%d, %d, %d]", this.line(), this.column(), this.charIndex());
  }

  @Override
  public int hashCode() {
    // bottom two bytes of the line and column
    return (this.line() << 16) | (this.column() & 0xffff);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TextPosition)) return false;
    var obj = (TextPosition) o;
    return obj.line() == this.line()
        && obj.column() == this.column()
        && obj.charIndex() == this.charIndex();
  }

  @Override
  public int compareTo(TextPosition obj) {
    return this.charIndex() - obj.charIndex();
  }
}
