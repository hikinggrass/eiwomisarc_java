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
		Core.debugMessage("[netthread] new network server thread");
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
				Core.debugMessage("[netthread] --- packet received ---");

				Core.debugMessage("[netthread] packet length:" + packet.getLength());
				byte field;
				byte[] buffer = packet.getData();
				byte[] writebuffer = new byte[packet.getLength()];
				for (int i = 0; i < packet.getLength(); i++) {
					field = buffer[i];
					writebuffer[i] = buffer[i];
					Core.debugMessage("[netthread] " + Integer.toHexString(0x000000ff & field).toUpperCase() + "h - "
							+ Integer.toString(0x000000ff & field) + "d");

				}

				this.core.write(writebuffer);

			} catch (SocketException e) {
				Core.errorMessage("[netthread] socket exception");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!socket.isClosed()) {
			socket.close();
		}
	}

	public void stopServer() {
		if (!socket.isClosed()) {
			this.socket.close();
		}
		this.run = false;
	}

}
