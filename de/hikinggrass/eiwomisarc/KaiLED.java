package de.hikinggrass.eiwomisarc;

public class KaiLED {

	private byte[] buffer;
	private int bufferLen;

	public KaiLED(byte[] mode) {
		bufferLen = 18; // without data!

		if (mode != null && mode.length > 0) {
			if (mode[0] == 0x00) {
				// initialization
				if (mode.length == 2) {
					buffer = new byte[bufferLen + 2];
					buffer[17] = mode[0];
					buffer[18] = mode[1];
				}
			} else if (mode[0] == 0x01) {
				/**
				 * Globale Farbe <br />
				 * Data[0] 0x01 <br />
				 * Data[1] Rot (0..127) <br />
				 * Data[2] Grün (0..127)<br />
				 * Data[3] Blau (0..127)
				 */
				if (mode.length == 4) {
					for (int i = 1; i < mode.length; i++) {
						if (mode[i] < 0) {
							mode[i] = 0;
						} else if (mode[i] > 127) {
							mode[i] = 127;
						}
					}

					buffer = new byte[bufferLen + 4];
					buffer[17] = mode[0];
					buffer[18] = mode[1];
					buffer[19] = mode[2];
					buffer[20] = mode[3];
				}

			} else if (mode[0] == 0x02) {
				/**
				 * Lauflicht <br />
				 * Data[0] 0x02 <br />
				 * Data[1] Geschwindigkeit (1..255)
				 */
				if (mode.length == 2) {
					buffer = new byte[bufferLen + 2];
					buffer[17] = mode[0];
					buffer[18] = mode[1];
				}
			} else if (mode[0] == 0x03) {
				/**
				 * Farbübergänge <br />
				 * Data[0] 0x03 <br />
				 * Data[1] Geschwindigkeit (1..255)
				 */
				if (mode.length == 2) {
					buffer = new byte[bufferLen + 2];
					buffer[17] = mode[0];
					buffer[18] = mode[1];
				}
				if (mode.length == 2) {
					if (mode[1] < 1) {
						mode[1] = 1;
					} else if (mode[1] > 255) {
						mode[1] = (byte) 255;
					}
					buffer = new byte[bufferLen + 2];
					buffer[17] = mode[0];
					buffer[18] = mode[1];
				}
			} else if (mode[0] == 0x04) {
				/**
				 * Einzelfarben <br />
				 * Data[0] 0x04 <br />
				 * Data[1] Rot LED 1 (0..127) <br />
				 * Data[2] Grün LED 1 (0..127) <br />
				 * Data[3] Blau LED 1 (0..127) <br />
				 * Data[4] Rot LED 2 (0..127) <br />
				 * ... ... <br />
				 * Data[60] Blau LED 20 (0..127)
				 */
				buffer = new byte[bufferLen + mode.length];
				for (int i = 0; i < mode.length; i++) {
					buffer[i + 17] = mode[i];
				}
			} else if (mode[0] == 0x05) {
				/**
				 * LEDs AN/AUSschalten <br />
				 * Data[0] 0x05 <br />
				 * Data[1] Leiste 1 (0b00000..0b11111) <br />
				 * Data[2] Leiste 2 (0b00000..0b11111) <br />
				 * Data[3] Leiste 3 (0b00000..0b11111) <br />
				 * Data[4] Leiste 4 (0b00000..0b11111)
				 */
				buffer = new byte[bufferLen + mode.length];
				for (int i = 0; i < mode.length; i++) {
					buffer[i + 17] = mode[i];
				}
			} else if (mode[0] == 0x06) {
				/**
				 * Strobo anschalten <br />
				 * Data[0] 0x06 <br />
				 * Data[1] Anschaltdauer <br />
				 * Data[2] Ausschaltdauer
				 */
				if (mode.length == 3) {
					buffer = new byte[bufferLen + 3];
					buffer[17] = mode[0];
					buffer[18] = mode[1];
					buffer[19] = mode[2];
				}
			} else if (mode[0] == 0x12) {
				/**
				 * Lauflicht ausschalten <br />
				 * Data[0] 0x12
				 */
				buffer = new byte[bufferLen + 1];
				buffer[17] = mode[0];
			} else if (mode[0] == 0x13) {
				/**
				 * Farbübergänge ausschalten <br />
				 * Data[0] 0x13
				 */
				buffer = new byte[bufferLen + 1];
				buffer[17] = mode[0];
			} else if (mode[0] == 0x16) {
				/**
				 * Strobo ausschalten <br />
				 * Data[0] 0x16
				 */
				buffer = new byte[bufferLen + 1];
				buffer[17] = mode[0];
			} else if (mode[0] == (byte) 0xfd) {
				/**
				 * High Power einschalten <br />
				 * Data[0] 0xFD
				 */
				buffer = new byte[bufferLen + 1];
				buffer[17] = mode[0];
			} else if (mode[0] == (byte) 0xfe) {
				/**
				 * High Power ausschalten <br />
				 * Data[0] 0xFE
				 */
				buffer = new byte[bufferLen + 1];
				buffer[17] = mode[0];
			} else {
				/**
				 * Demo Modus <br />
				 * Data[0] 0xFF
				 */
				buffer = new byte[bufferLen + 1];
				buffer[17] = (byte) 0xff;
			}

			initializeBuffer();
			calculateLength();
			buffer[buffer.length - 1] = calculateChecksum();
		}
	}

	private void initializeBuffer() {
		if (buffer != null) {
			buffer[0] = 0x7e; // startbyte
			// buffer[1] = 0x00; //length MSB
			// buffer[2] = 0x10; //length LSB
			buffer[3] = 0x10; // frame type
			buffer[4] = 0x01; // frame id (can be set to any number)
			buffer[5] = 0x00; // address
			buffer[6] = 0x13; // address
			buffer[7] = (byte) 0xa2; // address
			buffer[8] = 0x00; // address
			buffer[9] = 0x40; // address
			buffer[10] = 0x69; // address
			buffer[11] = 0x6e; // address
			buffer[12] = (byte) 0xfd; // address
			buffer[13] = (byte) 0xff; // net address (unknown)
			buffer[14] = (byte) 0xfe; // net address (unknown)
			buffer[15] = 0x00; // range (0 = unlimited)
			buffer[16] = 0x00; // options (0 = no options)
		}
	}

	private void calculateLength() {
		short length = (short) (buffer.length - 4);
		buffer[1] = (byte) ((length & 0xff00) >> 8); // MSB
		buffer[2] = (byte) (length & 0xff); // least significant "byte"

	}

	private byte calculateChecksum() {
		byte sum = 0;
		for (int i = 3; i < buffer.length - 1; i++) {
			sum = (byte) (sum + buffer[i]);
		}

		return (byte) (((byte) 0xff) - sum);
	}

	public byte[] getBuffer() {
		return this.buffer;
	}

}
