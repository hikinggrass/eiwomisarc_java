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
		// this.settings = core.getSettingsNetwork();
		this.socket = new DatagramSocket(1337);
	}

	public void run() {

		while (this.run) {
			try {
				byte[] buf = new byte[100];

				// receive request
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				System.out.println("[netthread] --- packet received ---");

				System.out.println("length:" + packet.getLength());
				byte field;
				byte[] buffer = packet.getData();
				for (int i = 0; i < packet.getLength(); i++) {
					field = buffer[i];
					System.out.println(Integer.toHexString(0x000000ff & field).toUpperCase() + "h - "
							+ Integer.toString(0x000000ff & field) + "d");

				}

				// this.core.writeToSerialPort(packet.getData());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}

}
