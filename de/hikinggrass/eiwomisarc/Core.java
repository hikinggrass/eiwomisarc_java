package de.hikinggrass.eiwomisarc;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

/**
 * This Class represents the Core of the Application, it handles input from the shell, the network interface and
 * produces output for the rs-232 interface. it also manages the application settings
 * 
 * @author Kai Hermann
 * @version 0.1
 */
public class Core {

	private static final int ERROR = 0;
	private static final int INFO = 1;
	private static final int DEBUG = 2;
	private static int messageLevel;

	private boolean networkServerMode;
	private boolean networkClientMode;

	private NetworkServer networkServer;
	private NetworkClient networkClient;
	private Serial serialPort;

	private Settings settings;

	private Timer fireTimer;

	/**
	 * Constructs a new Core
	 */
	public Core() {
		this.setup();
	}

	public Core(String serialPort, int baudRate, boolean networkServerMode) {
		Core.setMessageLevel(DEBUG);

		this.networkServerMode = networkServerMode;
		this.networkClientMode = false;
		if (networkServerMode) {
			this.networkServer = new NetworkServer(this); // todo port
		}

		try {
			this.serialPort = new Serial(serialPort, baudRate);
			Core.debugMessage("[core] connected to serial port");
			this.fireTimer = new Timer();
		} catch (IOException e) {
			Core.debugMessage("[core] could not connect to serial port");
		}
	}

	public Core(String serverAddress, int serverPort) {
		Core.setMessageLevel(DEBUG);

		try {
			this.networkClient = new NetworkClient(serverAddress, serverPort);
			this.networkClientMode = true;
			this.networkServerMode = false;
			this.networkServer = null;
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Reads the settings and configures all sub systems<br />
	 * 1. the network interface is initialized<br />
	 * 2. the serial port is initialized
	 */
	private void setup() {
		Core.setMessageLevel(DEBUG);

		settings = new Settings();
		for (String string : settings.getSettings()) {
			debugMessage(string);
		}

		this.networkServer = new NetworkServer(this);
		try {
			this.serialPort = new Serial(settings.getSerialPort(), settings.getBaudRate());
		} catch (IOException e) {
			errorMessage(e.getMessage());
		}
	}

	/**
	 * Returns the network settings
	 * 
	 * @return the network settings
	 */
	public SettingsNetwork getSettingsNetwork() {
		return this.settings.getSettingsNetwork();
	}

	/**
	 * Sets the message level
	 * 
	 * @param messageLevel
	 *            the message level
	 */
	public static void setMessageLevel(int messageLevel) {
		Core.messageLevel = messageLevel;
	}

	/**
	 * Displays an error message if the message level is set to ERROR, DEBUG or INFO
	 * 
	 * @param errorMessage
	 *            the error message that should be displayed
	 */
	public static void errorMessage(String errorMessage) {
		if (Core.messageLevel >= ERROR) {
			System.out.println("ERROR: " + errorMessage);
		}
	}

	/**
	 * Displays a debug message if the message level is set to DEBUG
	 * 
	 * @param debugMessage
	 *            the debug message that should be displayed
	 */
	public static void debugMessage(String debugMessage) {
		if (Core.messageLevel == DEBUG) {
			System.out.println("DEBUG: " + debugMessage);
		}
	}

	/**
	 * Displays a info message if the message level is set to INFO or DEBUG
	 * 
	 * 
	 * @param infoMessage
	 *            the info message that should be displayed
	 */
	public static void infoMessage(String infoMessage) {
		if (Core.messageLevel >= INFO) {
			System.out.println("INFO: " + infoMessage);
		}
	}

	/**
	 * Displays some debug information
	 */
	public void displayDebugInfo() {
		Core.debugMessage("[core] --- System Information ---");
		Core.debugMessage("[core] OS: " + System.getProperty("os.name"));
	}

	public boolean write(byte[] buffer) {
		if (networkClientMode) {
			return this.writeToNetwork(buffer);
		} else {
			return this.writeToSerialPort(buffer);
		}
	}

	/**
	 * Writes the given buffer to the serial port
	 * 
	 * @param buffer
	 *            the buffer to be written to the serial port
	 * @return true if the write was successful, false if not
	 */
	private boolean writeToSerialPort(byte[] buffer) {
		if (this.serialPort != null) {
			return this.serialPort.writeToSerialPort(buffer);
		} else {
			return false;
		}
	}

	private boolean writeToNetwork(byte[] buffer) {
		if (this.networkClient != null) {
			return this.networkClient.sendPacket(buffer);
		} else {
			return false;
		}
	}

	public void closeSerialPort() {
		if (this.serialPort != null) {
			this.serialPort.closeSerialPort();
			Core.debugMessage("[serial] Serial port " + serialPort.getSerialPort() + " successfully closed");
		}
	}

	public void startFireTimer(int time) {
		fireTimer = new Timer();
		fireTimer.schedule(new FireEffect(this), 0, time);
	}

	public void stopFireTimer() {
		fireTimer.cancel();
	}

	public void sendPacket(byte[] buffer) {
		this.networkClient.sendPacket(buffer);
	}

	public void stopServer() {
		if (this.networkServer != null) {
			networkServer.stopServerThreads();
		}
	}
}
