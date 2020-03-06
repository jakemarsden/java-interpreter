package com.jakemarsden.java.lexer.text;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.PrimitiveIterator;
import java.util.function.Predicate;

/**
 * Useful for parsing some or all of the characters retrieved from a {@link
 * PrimitiveIterator.OfInt}.
 */
public final class TextParser implements UnmodifiableTextParser {

  private final PrimitiveIterator.OfInt text;
  private TextPosition position = TextPosition.start();
  private LinkedList<Integer> buffer = new LinkedList<>();

  public TextParser(CharSequence text) {
    this(text.codePoints().iterator());
  }

  public TextParser(PrimitiveIterator.OfInt text) {
    this.text = requireNonNull(text);
  }

  /** {@inheritDoc} */
  @Override
  public TextPosition getPosition() {
    return this.position;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasRemaining(int count) {
    if (count < 0) throw new IllegalArgumentException("Invalid count: " + count);
    return this.tryPopulateBuffer(count);
  }

  /** {@inheritDoc} */
  @Override
  public int peek(int offset) {
    if (offset < 0) throw new IllegalArgumentException("Invalid offset: " + offset);
    var exists = this.tryPopulateBuffer(offset + 1);
    return exists ? this.buffer.get(offset) : EOF;
  }

  /**
   * Advances the position by {@code count} characters.
   *
   * @param count the number of characters to advance the position by
   */
  public void skip(int count) {
    if (count < 0) throw new IllegalArgumentException("Invalid count: " + count);
    while (count > 0 && !this.buffer.isEmpty()) {
      var ch = this.buffer.removeFirst();
      this.advancePosition(ch);
      count--;
    }
    while (count > 0 && this.text.hasNext()) {
      var ch = this.text.nextInt();
      this.advancePosition(ch);
      count--;
    }
  }

  /**
   * Returns the current character, and then advances the position to the next character. If the end
   * of the text is reached before the current character {@link #EOF} is returned instead.
   *
   * <p>Functionally equivalent to:
   *
   * <pre><code>
   * var ch = this.{@link #peek(int) peek(0)};
   * this.{@link #skip(int) skip(1)};
   * return ch;
   * </code></pre>
   *
   * @return the current character before advancing the position, or {@link #EOF} if the end of the
   *     text is reached first
   */
  public int consume() {
    if (!this.buffer.isEmpty()) {
      var ch = this.buffer.removeFirst();
      this.advancePosition(ch);
      return ch;
    }
    if (this.text.hasNext()) {
      var ch = this.text.nextInt();
      this.advancePosition(ch);
      return ch;
    }
    return EOF;
  }

  /**
   * Advances the position if the current character matches the {@code expected} character;
   * otherwise, throws an {@code IllegalStateException}. The position is not advanced if an
   * exception is thrown.
   *
   * <p>An {@code IllegalStateException} will always be thrown if the end of the text is reached,
   * regardless of the {@code expected} character.
   *
   * <p>Functionally equivalent to:
   *
   * <pre><code>
   * var ch = this.{@link #peek(int) peek(0)};
   * if (ch == expected &amp;&amp; ch != {@link #EOF EOF}) {
   *   this.{@link #skip(int) skip(1)};
   * } else {
   *   throw new IllegalStateException(...);
   * }
   * </code></pre>
   *
   * @param expected the character to (maybe) consume
   * @throws IllegalStateException if the current character doesn't match the {@code expected}
   *     character, or if the end of the text is reached
   */
  public void consumeExact(int expected) {
    var ch = this.tryPopulateBuffer(1) ? this.buffer.getFirst() : EOF;
    if (ch == EOF) {
      throw new IllegalStateException(
          format(
              "Expected: '%c' U+%04X at position %s but was: EOF",
              expected, expected, this.getPosition()));
    }
    if (ch != expected) {
      throw new IllegalStateException(
          format(
              "Expected: '%c' U+%04X at position %s but was: '%c' U+%04X",
              expected, expected, this.getPosition(), ch, ch));
    }
    this.buffer.removeFirst();
    this.advancePosition(ch);
  }

  /**
   * Advances the position if the next characters all match the {@code expectedChars}; otherwise,
   * throws an {@code IllegalStateException}. The position is not advanced if an exception is
   * thrown.
   *
   * <p>An {@code IllegalStateException} will always be thrown if the end of the text is reached,
   * regardless of the {@code expectedChars}.
   *
   * <p><strong>Not</strong> functionally equivalent to:
   *
   * <pre><code>
   * expectedChars.codePoints()
   *     .forEach(this::consumeExact);
   * </code></pre>
   *
   * @param expectedChars the characters to (maybe) consume
   * @throws IllegalStateException the next characters don't match the {@code expectedChars}, or if
   *     the end of the text is reached
   */
  public void consumeExact(CharSequence expectedChars) {
    var expectedItr = expectedChars.codePoints().iterator();
    int offset = 0;
    while (expectedItr.hasNext()) {
      var expected = expectedItr.nextInt();
      var ch = this.peek(offset);
      if (ch == EOF) {
        throw new IllegalStateException(
            format(
                "Expected: '%c' U+%04X at position %s+%d but was: EOF",
                expected, expected, this.getPosition(), offset));
      }
      if (ch != expected) {
        throw new IllegalStateException(
            format(
                "Expected: '%c' U+%04X at position %s+%d but was: '%c' U+%04X",
                expected, expected, this.getPosition(), offset, ch, ch));
      }
      offset++;
    }
    this.skip(offset);
  }

  /**
   * Consumes characters and appends them to the output buffer, until the {@code predicate} fails
   * for the <em>next</em> character or until the end of the text is reached. The current character
   * when the {@code predicate} fails is <em>not</em> appended to the output buffer, but it
   * <em>will</em> be the current character after this method (and so can be accessed via {@code
   * peek(0)}).
   *
   * <p>Returns
   *
   * <ul>
   *   <li>{@code true} if the {@code predicate} failed before the end of the text
   *   <li>{@code false} if the end of the text was reached before the {@code predicate} failed
   * </ul>
   *
   * <p>Functionally equivalent to:
   *
   * <pre><code>
   * while (true) {
   *   var ch = this.{@link #peek(int) peek(0)};
   *   if (ch == {@link #EOF EOF}) return false;
   *   if (!predicate.test(this)) return true;
   *   outBuf.append(ch);
   *   this.{@link #skip(int) skip(1)};
   * }
   * </code></pre>
   *
   * @param outBuf the buffer to append consumed characters to
   * @param predicate the condition which should return {@code false} to stop consuming characters
   * @return {@code true} if the {@code predicate} failed before the end of the text was reached
   */
  public boolean consumeWhile(StringBuilder outBuf, Predicate<UnmodifiableTextParser> predicate) {
    requireNonNull(outBuf);
    requireNonNull(predicate);
    while (true) {
      var ch = this.peek(0);
      if (ch == EOF) return false;
      if (!predicate.test(this)) return true;
      outBuf.appendCodePoint(ch);
      this.skip(1);
    }
  }

  private void advancePosition(int ch) {
    if (ch == '\n') {
      this.position = this.position.nextLine();
    } else {
      this.position = this.position.nextColumn();
    }
  }

  /**
   * @return {@code true} if at least {@code count} characters were buffered before reaching the end
   *     of the text
   */
  private boolean tryPopulateBuffer(int count) {
    while (this.buffer.size() < count) {
      if (!this.text.hasNext()) return false;
      var ch = this.text.nextInt();
      this.buffer.addLast(ch);
    }
    return true;
  }
}
