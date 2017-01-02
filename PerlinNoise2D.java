package com.nationscape;

import java.util.Random;

/**
 * Creates perlin noise generator. Once seed is set it cannot be changed (and hence is immutable.)
 * 
 * @author Daniel Eliasinski
 */
public class PerlinNoise2D {
	
	private final Noise2D[] noises;
	
	public PerlinNoise2D(int octaves, int seed){
		if(octaves <= 0)throw new IllegalArgumentException("Octaves can not be less than or equal to 0!");
		noises = new Noise2D[octaves];
		for(int index = 0; index < noises.length; index++)
			noises[index] = new Noise2D(seed + index);
	}
	
	public PerlinNoise2D(int octaves){
		this(octaves, new Random().nextInt());
	}
	
	public PerlinNoise2D(){
		this(6);
	}
	
	/**
	 * Get value of perlin noise at given coordinates.
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
	public float getPerlinValue(int x, int y, int spread, float amp){
		float value = 0;
		amp /= 2;
		for(Noise2D noise : noises){
			value += noise.getInterpValue(x, y, spread, amp);
			spread /= 2;
			amp /= 2;
			if(spread < 1)break;
		}
		return value;
	}
	
	public int getSeed(){
		return noises[0].getSeed();
	}
}
