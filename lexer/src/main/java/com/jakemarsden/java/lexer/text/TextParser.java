package com.jakemarsden.java.lexer.text;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.IntPredicate;

/** Useful for parsing some or all of the characters in a {@link CharIterator} */
public final class TextParser {

  private final CharIterator text;
  private TextPosition position = TextPosition.start();
  private Queue<Character> buffer = new ArrayDeque<>();

  public TextParser(CharIterator text) {
    this.text = requireNonNull(text);
  }

  /**
   * The position of the current character within the text (i.e. the position of the character which
   * would be returned by {@link #peek()}).
   *
   * @return the position of the current character within the text
   */
  public TextPosition getPosition() {
    return this.position;
  }

  /**
   * Returns {@code true} if there are more characters in the text, i.e. if it's safe to call {@link
   * #peek()}, {@link #consume()}, etc.
   *
   * @return {@code true} if there are more characters in the text
   */
  public boolean hasNext() {
    return !this.buffer.isEmpty() || this.text.hasNext();
  }

  /**
   * Returns the current character without advancing the position
   *
   * @return the current character
   * @throws NoSuchElementException if there are no more characters in the text
   */
  public char peek() {
    this.populateBuffer(1);
    return this.buffer.element();
  }

  /**
   * Returns the current character and then advances the position to the next character.
   *
   * @return the current character
   * @throws NoSuchElementException if there are no more characters in the text
   */
  public char consume() {
    var c = !this.buffer.isEmpty() ? this.buffer.remove() : this.text.nextChar();
    this.advancePosition(c);
    return c;
  }

  /**
   * Advances the position only if the current character matches the {@code expected} character
   *
   * @param expected the character to consume
   * @throws IllegalStateException if the current character doesn't match the {@code expected}
   *     character
   * @throws NoSuchElementException if there are no more characters in the text
   */
  public void consumeExact(char expected) {
    var actual = this.peek();
    if (actual != expected) {
      throw new IllegalStateException(format("Expected: '%c' but was: '%c'", expected, actual));
    }
    this.consume();
  }

  /**
   * Consumes characters from the text until either the {@code predicate} fails for the
   * <em>next</em> character, or until the text runs out of characters.
   *
   * @param predicate determines when to stop consuming characters; the first character to fail will
   *     not be consumed
   * @return the characters which were consumed while the {@code predicate} succeeded. doesn't
   *     include the character which failed the {@code predicate}
   */
  public String consumeWhile(IntPredicate predicate) {
    requireNonNull(predicate);
    var fragment = new StringBuilder();
    while (this.hasNext()) {
      var c = this.peek();
      if (!predicate.test(c)) break;
      fragment.append(c);
      this.consume();
    }
    return fragment.toString();
  }

  private void advancePosition(char c) {
    this.position = (c == '\n') ? this.position.nextLine() : this.position.nextColumn();
  }

  private void populateBuffer(int count) {
    while (this.buffer.size() < count) this.buffer.add(this.text.nextChar());
  }
}
