package de.hikinggrass.eiwomisarc;

import java.io.IOException;

/**
 * This class manages the serial port connections<br />
 * A new Serial Port can be opened be calling the constructor Serial() (then the default port will be opened). or the
 * constructor Serial(serialPort,baudRate) (then a new serial port with the given paramaters is opened.<br />
 * <br />
 * Caution! If you want to close the serial port you have to call this.closePort()!!!!!
 */
public class Serial {

	private String serialPort;
	private int baudRate;
	private boolean ready = false;

	/**
	 * Loads the Mac OS X specific serial port code <br />
	 * TODO linux & windows version!
	 */
	static {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			System.loadLibrary("eiwomisarc_serialOSX");
			System.out.println("[serial] Loaded eiwomisarc_serialOSX serial port library");
		} else if(System.getProperty("os.name").toLowerCase().equals("linux")) {
			System.loadLibrary("eiwomisarc_serialLinux");
			System.out.println("loaded linux lib");
		}
	}

	/**
	 * Opens a new serial port
	 */
	public Serial() throws IOException {
		this("/dev/master", 9600);
	}

	/**
	 * Opens a new serial port with the given values
	 * 
	 * @param serialPort
	 *            the serial port that will be opened
	 * @param baudRate
	 *            the baud rate that will be set
	 * @throws IOException
	 *             when then given serial port could not be opened
	 */
	public Serial(String serialPort, int baudRate) throws IOException {
		this.serialPort = serialPort;
		this.baudRate = baudRate;
		
		if (this.externOpenPort(serialPort, baudRate) == -1) {
			throw new IOException("[serial] Serial port " + serialPort + " could not be opened");
		} else {
			System.out.println("[serial] Serial port " + serialPort + " successfully opened with baud rate " + baudRate);
			this.ready = true;
		}
	}

	/**
	 * Returns the name of the serial port in use
	 * 
	 * @return the name of the serial port in use
	 */
	public String getSerialPort() {
		return this.serialPort;
	}

	/**
	 * Returns the currently used baud rate
	 * 
	 * @return the currently used baud rate
	 */
	public int getBaudRate() {
		return this.baudRate;
	}

	/**
	 * Writes the given buffer to the serial port
	 * 
	 * @param buffer
	 *            the buffer to be written to the serial port
	 * @return true if the write was successful, false if not
	 */
	public boolean writeToSerialPort(byte[] buffer) {
		if (this.ready) {
			this.externWrite(buffer);
			return true; // TODO
		} else {
			return false;
		}
	}

	/**
	 * Closes the serial port
	 */
	public void closeSerialPort() {
		this.externClosePort(); // TODO check?
		this.ready = false;
	}

	// external functions

	private native int externFuntion(String msg);

	private native void externTest(int msg);

	/**
	 * Opens a new serial port and keeps it open until closePort() is called
	 * 
	 * @param serialPort
	 *            the serial port that should be opened
	 * @param baudRate
	 *            the baud rate of the serial port
	 * @return -1 if the port couldn't be opened, 0 if the port was opened successfully
	 */
	private native int externOpenPort(String serialPort, int baudRate);

	/**
	 * Closes the serial port (if it has been opened)
	 */
	private native void externClosePort();

	/**
	 * Writes a given byte buffer to a serial port
	 * 
	 * @param buffer
	 *            a byte buffer that contains that data that will be written to the serial port
	 * @return -1 if the buffer could not be written (this is the case when no serial port is opened, 0 if the data was
	 *         written successfully
	 */
	private native int externWrite(byte[] buffer);

	public void test() {
		int port = this.externOpenPort("test", 9600);
		System.out.println("[serial] Opened Port with status: " + port);
		System.out.println("XXX:"+externFuntion("test"));
		byte[] buffer = { 0x01 };
		int wrt = this.externWrite(buffer);
		System.out.println("Wrote to port with status: " + wrt);
		this.externClosePort();
	}

}
