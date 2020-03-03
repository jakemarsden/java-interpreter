package com.jakemarsden.java.lexer.text;

/** Read-only functionality of a {@link TextParser}. */
public interface UnmodifiableTextParser {

  /**
   * Typically returned by methods such as {@link #peek()}, {@link TextParser#consume()} to indicate
   * the end of the text (or <em>past</em> the end of the text).
   */
  int EOF = -1;

  /**
   * The position of the current character within the text (i.e. the position of the character which
   * would be returned by {@link #peek()}).
   *
   * @return the position of the current character within the text
   */
  TextPosition getPosition();

  /**
   * Returns {@code true} if there is at least {@code 1} character left in the text. If {@code true}
   * is returned, {@code peek(0)} would return a non-null character.
   *
   * @return {@code true} if there is at least {@code 1} character left in the text
   */
  default boolean hasRemaining() {
    return this.hasRemaining(1);
  }

  /**
   * Returns {@code true} if there are at least {@code count} characters left in the text. If {@code
   * true} is returned, {@code peek(count - 1)} would return a non-null character.
   *
   * @param count how many characters to check for
   * @return {@code true} if there are at least {@code count} characters left in the text
   */
  boolean hasRemaining(int count);

  /**
   * Returns the current character, without advancing the position. If the end of the text is
   * reached before the current character, {@link #EOF} is returned instead.
   *
   * @return the current character, or {@link #EOF} if the end of the text is reached first
   */
  default int peek() {
    return this.peek(0);
  }

  /**
   * Returns the character which is {@code offset} characters ahead of the current character,
   * without advancing the position. If the end of the text is reached before the target character,
   * {@link #EOF} is returned instead.
   *
   * <p>For example
   *
   * <ul>
   *   <li>use {@code peek(0)} to get the current character
   *   <li>use {@code peek(1)} to get the next character
   * </ul>
   *
   * @param offset the index from the current character to peek at
   * @return the character at the given {@code offset} beyond the current character, or {@link #EOF}
   *     if the end of the text is reached first
   */
  int peek(int offset);
}
