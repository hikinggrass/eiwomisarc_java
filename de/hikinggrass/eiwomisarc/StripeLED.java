package de.hikinggrass.eiwomisarc;

public class StripeLED {
	private byte stripe;
	private byte led;
	private byte r;
	private byte g;
	private byte b;

	/**
	 * @param stripe
	 * @param lED
	 */
	public StripeLED(byte stripe, byte led, byte r, byte g, byte b) {
		super();
		this.stripe = stripe;
		this.led = led;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * @return the stripe
	 */
	public byte getStripe() {
		return stripe;
	}

	/**
	 * @return the led
	 */
	public byte getLed() {
		return led;
	}

	

	/**
	 * @return the r
	 */
	public byte getR() {
		return r;
	}

	/**
	 * @return the g
	 */
	public byte getG() {
		return g;
	}

	/**
	 * @return the b
	 */
	public byte getB() {
		return b;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Leiste #" + stripe + " LED #" + led;
	}

}
