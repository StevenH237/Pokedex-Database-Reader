package net.nixill.pokemon.objects;

import lombok.Getter;

/**
 * Represents the function of a {@link GrowthRate}, up to a specific level.
 * <p>
 * The functions are of the format
 * <tt>(((ax+b)/c+d)x³/e+fx²/g+hx/i+j)*k/l</tt>, where x is the desired
 * level, a through k are the respective member fields of the class (of the
 * same name), and the output of the function is the total experience
 * required for the level.
 */
public class GrowthRateFunction {
  /** The {@link GrowthRate} with which this function is associated. */
  @Getter private final GrowthRate growthRate;
  /** The maximum level at which this function applies. */
  @Getter private final int maxLevel;
  
  // These ints are variables in the function
  @Getter private int a;
  @Getter private int b;
  @Getter private int c;
  @Getter private int d;
  @Getter private int e;
  @Getter private int f;
  @Getter private int g;
  @Getter private int h;
  @Getter private int i;
  @Getter private int j;
  @Getter private int k;
  @Getter private int l;
  
  /**
   * Creates a new GrowthRateFunction.
   * 
   * @param growthRate
   *   The {@link GrowthRate} with which this function should be
   *   associated. Can be null.
   * @param maxLevel
   *   The maximum level at which this function should apply.
   * @param a
   *   Variable <i>a</i> from the function: the fourth power factor.
   * @param b
   *   Variable <i>b</i> from the function: the fourth power addend.
   * @param c
   *   Variable <i>c</i> from the function: the fourth power divisor. Must
   *   not be 0.
   * @param d
   *   Variable <i>d</i> from the function: the third power addend.
   * @param e
   *   Variable <i>e</i> from the function: the third power divisor. Must
   *   not be 0.
   * @param f
   *   Variable <i>f</i> from the function: the second power factor.
   * @param g
   *   Variable <i>g</i> from the function: the second power divisor. Must
   *   not be 0.
   * @param h
   *   Variable <i>h</i> from the function: the first power factor.
   * @param i
   *   Variable <i>i</i> from the function: the first power divisor. Must
   *   not be 0.
   * @param j
   *   Variable <i>j</i> from the function: the static addend.
   * @param k
   *   Variable <i>k</i> from the function: the whole function factor. Must
   *   not be 0.
   * @param l
   *   Variable <i>l</i> from the function: the whole function divisor.
   *   Must not be 0.
   */
  public GrowthRateFunction(GrowthRate growthRate, int maxLevel, int a,
      int b, int c, int d, int e, int f, int g, int h, int i, int j, int k,
      int l) {
    if (c == 0 || e == 0 || g == 0 || i == 0 || l == 0) {
      throw new IllegalArgumentException(
          "Divisors of a GrowthRateFunction must not be zero.");
    }
    if (k == 0) {
      throw new IllegalArgumentException(
          "The full-function factor must not be 0, as that would cause all results to be 0.");
    }
    this.growthRate = growthRate;
    this.maxLevel = maxLevel;
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
    this.g = g;
    this.h = h;
    this.i = i;
    this.j = j;
    this.k = k;
    this.l = l;
  }
  
  /**
   * Calculates the experience this function requires to reach a specific
   * level.
   * 
   * @param x
   *   The level for which the calculation should be made.
   * @return The experience required for that level.
   */
  public int calculate(int x) {
    return (((a * x + b) / c + d) * x * x * x / e + f * x * x / g
        + h * x / i + j) * k / l;
  }
}
