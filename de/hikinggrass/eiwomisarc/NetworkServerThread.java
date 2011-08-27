package de.hikinggrass.eiwomisarc;

import java.io.*;
import java.net.*;

public class NetworkServerThread extends Thread {

	protected DatagramSocket socket = null;
	protected boolean run = true;
	private Core core;
	private SettingsNetwork settings;

	public NetworkServerThread(Core core) throws IOException {
		this("NetworkServerThread", core);
	}

	public NetworkServerThread(String name, Core core) throws IOException {
		super(name);
		this.core = core;
		this.settings = core.getSettingsNetwork();
		this.socket = new DatagramSocket(this.settings.getPort());
	}

	public void run() {

		while (this.run) {
			try {
				byte[] buf = new byte[6];

				// receive request
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				Core.debugMessage("[netthread] --- packet received ---");
				
				if (this.settings.getHosts().contains(packet.getAddress())) { //TODO PERFORMANCE????
					for (byte field : packet.getData()) {
						Core.debugMessage(Integer.toHexString(0x000000ff & field).toUpperCase() + "h - "
								+ Integer.toString(0x000000ff & field) + "d");
					}

					this.core.writeToSerialPort(packet.getData());
				} else {
					Core.debugMessage("[netthread] packet received from a host that is not in the acceptable range");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}

}
