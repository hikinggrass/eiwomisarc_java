package de.hikinggrass.eiwomisarc;

import java.io.IOException;

/**
 * This Class handles all network functionalities of the program such as receiving UDP packets from clients (and
 * communication with other software?)
 * 
 * @author Kai Hermann
 * @version 0.1
 */
public class NetworkServer {
	private Core core;

	private NetworkServerThread networkServerThread;

	public NetworkServer(Core core) {
		this.core = core;
		Core.debugMessage("[network server] new server started");
		try {
			networkServerThread = new NetworkServerThread(this.core);
			networkServerThread.start();
			// Core.debugMessage("[net] Listening on port" + core.getSettingsNetwork().getPort());
		} catch (IOException e) {
			// Core.errorMessage("[net] Coudn't listen on port " + core.getSettingsNetwork().getPort() + ": "
			// + e.getMessage());
		}
	}

	public void stopServerThreads() {
		if (this.networkServerThread != null) {
			this.networkServerThread.stopServer();
		}
	}

}
