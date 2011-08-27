package de.hikinggrass.eiwomisarc;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * A simple wrapper? for network specific settings
 */
public class SettingsNetwork {
	private int port;
	private ArrayList<InetAddress> hosts;
	private int subnet;

	/**
	 * Constructs a new network settings object
	 * 
	 * @param port
	 *            the port this program listens on
	 * @param ips
	 *            the IPs this program accepts as source ips
	 * @param subnet
	 *            the subnet this program accepts as source subnet
	 */
	public SettingsNetwork(int port, ArrayList<InetAddress> hosts, int subnet) {
		super();
		this.port = port;
		this.hosts = hosts;
		this.subnet = subnet;
	}

	/**
	 * Returns the port
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns the hosts
	 * 
	 * @return the hosts
	 */
	public ArrayList<InetAddress> getHosts() {
		return hosts;
	}

	/**
	 * Returns the subnet
	 * 
	 * @return the subnet
	 */
	public int getSubnet() {
		return subnet;
	}

}
