package de.hikinggrass.eiwomisarc;

import java.util.Random;
import java.util.TimerTask;

public class FireEffect extends TimerTask {

	private Core core;
	private Random randomGenerator;

	public FireEffect(Core core) {
		this.core = core;
		this.randomGenerator = new Random();
	}

	public void run() {
		int r = randomGenerator.nextInt(127);
		if (r <= 0) {
			r = 1;
		}
		int g = randomGenerator.nextInt(r);
		g = (g / 6);
		byte b = 0x00;
		System.out.println("R" + r + " G" + g);

		byte[] buffer = { 0x01, (byte) r, (byte) g, b };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}
}
