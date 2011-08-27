package de.hikinggrass.eiwomisarc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Properties;

import java.util.List;

/**
 * This class manages all configurations/settings that this application has
 */
public class Settings {

	private String serialPort;
	private int baudRate;
	private int port;
	private ArrayList<InetAddress> hosts; // IPv4/v6
	private int subnet;

	public Settings() {
		this.hosts = new ArrayList<InetAddress>();
		this.readConfigFile();
	}

	/**
	 * Parses the config file
	 */
	private void readConfigFile() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("config.cfg"));
			Core.debugMessage("[set] Loaded config.cfg");
		} catch (FileNotFoundException e) {
			Core.debugMessage("[set] config.cfg not found " + e.getMessage());
		} catch (IOException e) {
			Core.debugMessage("[set] " + e.getMessage());
		}

		this.setSerialPort(props.getProperty("serialport",
				"/Users/fakeman/Desktop/coding/macosx/eiwomisarc/eiwomisarc_java/test"));
		try {
			this.setBaudRate(Integer.parseInt(props.getProperty("baudrate")));
		} catch (NumberFormatException e) {
			this.setBaudRate(9600);
		}

		try {
			this.setPort(Integer.parseInt(props.getProperty("port")));
		} catch (NumberFormatException e) {
			this.setPort(1337);
		}

		this.setSubnet(24); // TODO

		String acceptedHosts = props.getProperty("acceptedhost", "");
		List<String> acceptedHostsList = Arrays.asList(acceptedHosts.split(","));
		for (String string : acceptedHostsList) {
			this.addHost(string);
		}
	}

	/**
	 * Returns the serial port used by this program
	 * 
	 * @return the serial port used by this program
	 */
	public String getSerialPort() {
		return this.serialPort;
	}

	/**
	 * Sets the serial port that this program will use
	 * 
	 * @param serialPort
	 *            the serial port that this program will use
	 */
	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * Returns the baud rate
	 * 
	 * @return the baud rate
	 */
	public int getBaudRate() {
		return this.baudRate;
	}

	/**
	 * Sets the baud rate of the serial port
	 * 
	 * @param baudRate
	 *            the baud rate of the serial port
	 */
	public void setBaudRate(int baudRate) {
		// TODO check value, if value is exotic, warn the user
		this.baudRate = baudRate;
	}

	/**
	 * Returns the port this program listens on
	 * 
	 * @return the port this program listens on
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Sets the port the program will listen on, if the port is not in the acceptable port range (0-65535) the default
	 * port (1337) will be used
	 * 
	 * @param port
	 *            the port the program will listen on
	 */
	public void setPort(int port) {
		if (port >= 0 && port <= 65535) {
			this.port = port;
		} else {
			this.port = 1337;
			Core.infoMessage("[set] " + port + " is not in the port range (0-65535)");
		}
	}

	/**
	 * Returns the list of acceptable source hosts
	 * 
	 * @return the list of acceptable source hosts
	 */
	public String[] getHosts() {
		String[] ips = new String[this.hosts.size()];
		this.hosts.toArray(ips);
		return ips;
	}

	/**
	 * Adds the given host to the list of acceptable source hosts
	 * 
	 * @param host
	 *            the host to add
	 */
	public void addHost(String host) {
		// ip check?
		try {
			InetAddress address = InetAddress.getByName(host);
			this.hosts.add(address);
		} catch (UnknownHostException e) {
			// TODO
			Core.debugMessage("[set] Unknown Host: " + e.getMessage());
		}
	}

	/**
	 * Removes the given host from the list of acceptable source hosts
	 * 
	 * @param host
	 *            the host that should be removed
	 */
	public void removeHosts(String host) {
		try {
			InetAddress address = InetAddress.getByName(host);
			int index = this.hosts.indexOf(address);
			if (index != -1) {
				this.hosts.remove(index);
			}
		} catch (UnknownHostException e) {
			// TODO
			Core.debugMessage("[set] Unknown Host: " + e.getMessage());
		}
	}

	/**
	 * Returns the acceptable source subnet
	 * 
	 * @return the acceptable source subnet
	 */
	public int getSubnet() {
		return this.subnet;
	}

	/**
	 * Sets the acceptable source subnet (ips that are not in the given subnet but should be allowed can be added via
	 * addIp()
	 * 
	 * @param subnet
	 *            the subnet to set
	 */
	public void setSubnet(int subnet) {
		this.subnet = subnet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Settings [serialPort=" + serialPort + ", baudRate=" + baudRate + ", port=" + port + ", ip=" + hosts
				+ ", subnet=" + subnet + "]";
	}

	/**
	 * Returns the readonly network settings
	 * 
	 * @return the network settings
	 */
	public SettingsNetwork getSettingsNetwork() {
		return new SettingsNetwork(this.port, this.hosts, this.subnet);
	}

	/**
	 * Returns the current settings as a string array
	 * 
	 * @return the current settings as a string array
	 */
	public String[] getSettings() {
		ArrayList<String> allSettings = new ArrayList<String>();

		allSettings.add("[set] --- Loaded Settings ---");
		allSettings.add("[set] Serial port: " + this.serialPort);
		allSettings.add("[set] Baud rate: " + this.baudRate);
		allSettings.add("[set] Port: " + this.port);
		for (InetAddress address : this.hosts) {
			allSettings.add("[set] Acceptable Host: " + address.getHostAddress());
		}
		allSettings.add("[set] Acceptable subnet: /" + this.subnet);

		String[] stringSettings = new String[allSettings.size()];
		allSettings.toArray(stringSettings);

		return stringSettings;
	}

}
