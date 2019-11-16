package net.nixill.pokemon.objects;

import lombok.Getter;

/**
 * Represents the function of a {@link GrowthRate}, up to a specific level.
 */
public class GrowthRateFunction {
  @Getter private final GrowthRate growthRate;
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
  
  public GrowthRateFunction(GrowthRate growthRate, int maxLevel, int a,
      int b, int c, int d, int e, int f, int g, int h, int i, int j, int k,
      int l) {
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
  
  public int calculate(int x) {
    return (((a * x + b) / c + d) * x * x * x / e + f * x * x / g
        + h * x / i + j) * k / l;
  }
}
