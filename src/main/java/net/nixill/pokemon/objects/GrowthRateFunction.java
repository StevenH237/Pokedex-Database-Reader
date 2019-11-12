package net.nixill.pokemon.objects;

/**
 * Represents the function of a {@link GrowthRate}, up to a specific level.
 */
public class GrowthRateFunction {
  private GrowthRate growthRate;
  private int        maxLevel;
  
  // These ints are variables in the function
  private int a;
  private int b;
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;
  private int i;
  private int j;
  private int k;
  private int l;
  
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
  
  public GrowthRate getGrowthRate() {
    return growthRate;
  }
  
  public int getMaxLevel() {
    return maxLevel;
  }
  
  public int getA() {
    return a;
  }
  
  public int getB() {
    return b;
  }
  
  public int getC() {
    return c;
  }
  
  public int getD() {
    return d;
  }
  
  public int getE() {
    return e;
  }
  
  public int getF() {
    return f;
  }
  
  public int getG() {
    return g;
  }
  
  public int getH() {
    return h;
  }
  
  public int getI() {
    return i;
  }
  
  public int getJ() {
    return j;
  }
  
  public int getK() {
    return k;
  }
  
  public int getL() {
    return l;
  }
}