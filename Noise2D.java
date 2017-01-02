package com.nationscape;

import java.util.Random;

/**
 * Creates a 2D coherent noise generator. Uses cubic interpolation. Once seed is set it cannot be modified (and hence is immutable.)
 * 
 * @author Daniel Eliasinski
 */
public class Noise2D {
	
	private final int seed;
	
	public Noise2D(int seed){
		this.seed = seed;
	}
	
	public Noise2D(){
		this(new Random().nextInt());
	}
	
	/**
	 * Outputs value at given coordinates using a coherent noise function.
	 * Method of interpolation is bicubic interpolation.
	 * {@link java.util.Random Random} is used to generate random points.
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param spread How spread apart are the generated sample points.
	 * @param amp Maximum possible range of values for white noise generator (i.e. -amp < value < amp)
	 * @return Value from noise function at given coordinates.
	 * <ul>
	 * <li>Will always be the same for same coordinates and same seed.</li>
	 * <li>This is <strong>NOT</strong> guaranteed to be between the range of <code>amp</code> due to cubic interpolation but is very likely.</li>
	 * </ul>
	 */
	public float getInterpValue(int x, int y, int spread, float amp){
		
		int floorX = x / spread;
		int floorY = y / spread;
		
		int x1 = (floorX - 1) * spread;
		int x2 = floorX * spread;
		int x3 = (floorX + 1) * spread;
		int x4 = (floorX + 2) * spread;
		
		int y1 = (floorY - 1) * spread;
		int y2 = floorY * spread;
		int y3 = (floorY + 1) * spread;
		int y4 = (floorY + 2) * spread;
		
		float p1 = getRandomValue(x1, y1, amp);
		float p2 = getRandomValue(x2, y1, amp);
		float p3 = getRandomValue(x3, y1, amp);
		float p4 = getRandomValue(x4, y1, amp);
		float p5 = getRandomValue(x1, y2, amp);
		float p6 = getRandomValue(x2, y2, amp);
		float p7 = getRandomValue(x3, y2, amp);
		float p8 = getRandomValue(x4, y2, amp);
		float p9 = getRandomValue(x1, y3, amp);
		float p10 = getRandomValue(x2, y3, amp);
		float p11 = getRandomValue(x3, y3, amp);
		float p12 = getRandomValue(x4, y3, amp);
		float p13 = getRandomValue(x1, y4, amp);
		float p14 = getRandomValue(x2, y4, amp);
		float p15 = getRandomValue(x3, y4, amp);
		float p16 = getRandomValue(x4, y4, amp);
		
		float fractionX = x % spread / (float)spread;
		float fractionY = y % spread / (float)spread;
		
		float a = cubicInterp(p1, p2, p3, p4, fractionX);
		float b = cubicInterp(p5, p6, p7, p8, fractionX);
		float c = cubicInterp(p9, p10, p11, p12, fractionX);
		float d = cubicInterp(p13, p14, p15, p16, fractionX);
		
		return cubicInterp(a, b, c, d, fractionY);
	}
	
	private float cubicInterp(float a, float b, float c, float d, float x){
		float p = (d - c) - (a - b);
		float q = a - b - p;
		float r = c - a;
		float s = b;
		
		return p * (float)Math.pow(x, 3) + q * (float)Math.pow(x, 2) + r * x + s;//p*x^3+q*x^2+r*x+s
	}
	
	private final Random random = new Random();
	
	public float getRandomValue(int x, int y, float amp){//Create lookup table for created values later?
		long seed = (((x + y) * (x + y + 1)) >> 1) + y;//http://math.stackexchange.com/a/23506
		seed = (((seed + this.seed) * (seed + this.seed + 1)) >> 1) + this.seed;
		// initial scramble
	    /*seed = (seed ^ multiplier) & mask;

	    // shuffle three times. This is like calling rng.nextInt() 3 times
	    seed = (seed * multiplier + addend) & mask;
	    seed = (seed * multiplier + addend) & mask;
	    seed = (seed * multiplier + addend) & mask;

	    // fit size
	    int integer = (int)(seed >>> 16);
	    return integer * amp / Integer.MAX_VALUE;*/
		random.setSeed(seed);
		return (random.nextFloat() * 2 - 1) * amp;
	}
	
	public int getSeed(){
		return seed;
	}
}
