package de.hikinggrass.eiwomisarc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkClient {
	InetAddress address;
	int port;
	DatagramSocket socket;

	/**
	 * @param address
	 * @param packet
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public NetworkClient(String address, int port) throws UnknownHostException, SocketException {
		super();
		this.address = InetAddress.getByName(address);
		this.port = port;
		this.socket = new DatagramSocket();
	}

	public boolean sendPacket(byte[] buffer) {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
		try {
			socket.send(packet);
			return true;
		} catch (IOException e) {
			Core.errorMessage("[network client] could not send packet");
			return false;
		}
	}
}