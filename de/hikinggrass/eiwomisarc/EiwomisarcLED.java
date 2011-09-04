package de.hikinggrass.eiwomisarc;

public class EiwomisarcLED {

	private byte[] buffer;

	public EiwomisarcLED(byte[] mode) {
		if (mode.length == 6 && mode[0] == 0xff) {
			buffer = new byte[6];
			initializeBuffer();
			if (checkbuffer(mode) == 0) {
				for (int i = 1; i < 6; i++) {
					buffer[i] = mode[i];
				}
			}
		}

	}

	private void initializeBuffer() {
		if (buffer != null) {
			buffer[0] = (byte) 0xff;
		}
	}

	/* check if buffer is valid */
	int checkbuffer(byte[] buffer) {
		int error = 0;

		/* byte0: startbyte = 255 */
		if (buffer[0] == 255) {
		} else {
			error = 1;
		}

		/* byte1: value part 1/2 */
		if (buffer[1] < 255) {
		} else {
			error = 1;
		}

		/* byte2: value part 2/2 */
		if (buffer[2] < 2) {
		} else {
			error = 1;
		}

		/* byte3: channel part 1/3 */
		if (buffer[3] < 255) {
		} else {
			error = 1;
		}

		/* byte4: channel part 2/3 */
		if (buffer[4] < 255) {
		} else {
			error = 1;
		}

		/* byte5: channel part 3/3 */
		if (buffer[5] < 5) {
		} else {
			error = 1;
		}

		return error;
	}

	public byte[] getBuffer() {
		return this.buffer;
	}

}
