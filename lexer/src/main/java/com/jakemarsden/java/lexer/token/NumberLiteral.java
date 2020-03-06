package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

public final class NumberLiteral {

  public static NumberLiteral of(Number value, CharSequence rawValue) {
    return new NumberLiteral(value, rawValue);
  }

  private final Number value;
  private final CharSequence rawValue;

  private NumberLiteral(Number value, CharSequence rawValue) {
    this.value = requireNonNull(value);
    this.rawValue = requireNonNull(rawValue);
  }

  public Number value() {
    return this.value;
  }

  public CharSequence rawValue() {
    return this.rawValue;
  }

  @Override
  public String toString() {
    return this.rawValue.toString();
  }

  @Override
  public int hashCode() {
    return hash(this.value, this.rawValue);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof NumberLiteral)) return false;
    var obj = (NumberLiteral) o;
    return this.value.equals(obj.value) && strContentEquals(this.rawValue, obj.rawValue);
  }
}
