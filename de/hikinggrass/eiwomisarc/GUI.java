package de.hikinggrass.eiwomisarc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI {

	private JFrame frame;

	private static Core core;
	private static JTextField textSerial;
	private static JTextField textBaud;
	private static JLabel lblSerialServer;
	private static JLabel lblBaudratePort;
	private static JSlider sliderR;
	private static JSlider sliderG;
	private static JSlider sliderB;
	private static JSlider sliderSpeed;
	private static JTextField textNumberOfLEDStripes;
	private static JSlider sliderRSingle;
	private static JSlider sliderGSingle;
	private static JSlider sliderBSingle;
	private static JComboBox singleColorComboBox;
	private static JButton btnFading;
	private static JSlider sliderFading;
	private static JButton btnDeactivateLED;
	private static JButton btnFireEffect;
	private static boolean fireEffectEnabled;
	private static JTextField textStroboDelay;
	private static JTextField textStroboDuration;
	private static JButton btnStrobo;
	private static boolean stroboEnabled;
	private static JButton btnHighPower;
	private static boolean highPowerEnabled;
	private static JTextField textServerAddress;
	private static JTextField textPort;
	private static JComboBox networkComboBox;
	private static JCheckBox chckbxNetzwerkmodus;

	private static void init() {
		byte count = (byte) Integer.parseInt(textNumberOfLEDStripes.getText());

		if (core != null) {
			core.closeSerialPort();
			core.stopServer();
			if (singleColorComboBox != null) {
				singleColorComboBox.removeAllItems();
			}
		}
		if (chckbxNetzwerkmodus.isSelected()) {
			if (networkComboBox.getSelectedItem().equals("Client")) {
				core = new Core(textServerAddress.getText(), Integer.parseInt(textPort.getText()));
				lblBaudratePort.setText("Port");
				lblSerialServer.setText("Serveradresse");
				textBaud.setVisible(false);
				textSerial.setVisible(false);
				textPort.setVisible(true);
				textServerAddress.setVisible(true);
				Core.debugMessage("[gui] client mode");
			} else {
				core = new Core(textSerial.getText(), Integer.parseInt(textBaud.getText()), true);
				lblBaudratePort.setText("Baudrate");
				lblSerialServer.setText("Serial Port");
				textBaud.setVisible(true);
				textSerial.setVisible(true);
				textPort.setVisible(false);
				textServerAddress.setVisible(false);
				Core.debugMessage("[gui] server mode");
			}
		} else {
			core = new Core(textSerial.getText(), Integer.parseInt(textBaud.getText()), false);
			lblBaudratePort.setText("Baudrate");
			lblSerialServer.setText("Serial Port");
			textBaud.setVisible(true);
			textSerial.setVisible(true);
			textPort.setVisible(false);
			textServerAddress.setVisible(false);
			Core.debugMessage("[gui] network mode disabled");
		}
		for (int i = 0; i < count * 5; i++) {
			singleColorComboBox.addItem(new StripeLED((byte) ((i / 5) + 1), (byte) (5 - (i % 5)), (byte) 0, (byte) 0,
					(byte) 0, true));
		}
		singleColorComboBox.setEnabled(true);
		sliderRSingle.setEnabled(true);
		sliderGSingle.setEnabled(true);
		sliderBSingle.setEnabled(true);
		btnDeactivateLED.setEnabled(true);
		sliderFading.setEnabled(true);

		byte[] buffer = { 0x00, count };

		core.write(new KaiLED(buffer).getBuffer());
	}

	private static void networkStateChange() {
		if (core != null) {
			core.closeSerialPort();
			core.stopServer();
		}
		if (chckbxNetzwerkmodus.isSelected()) {
			if (networkComboBox.getSelectedItem().equals("Client")) {
				lblBaudratePort.setText("Port");
				lblSerialServer.setText("Serveradresse");
				textBaud.setVisible(false);
				textSerial.setVisible(false);
				textPort.setVisible(true);
				textServerAddress.setVisible(true);
				Core.debugMessage("[gui] client mode");
			} else {
				core = new Core(textSerial.getText(), Integer.parseInt(textBaud.getText()), true);
				lblBaudratePort.setText("Baudrate");
				lblSerialServer.setText("Serial Port");
				textBaud.setVisible(true);
				textSerial.setVisible(true);
				textPort.setVisible(false);
				textServerAddress.setVisible(false);
				Core.debugMessage("[gui] server mode");
			}
		} else {
			core = new Core(textSerial.getText(), Integer.parseInt(textBaud.getText()), false);
			lblBaudratePort.setText("Baudrate");
			lblSerialServer.setText("Serial Port");
			textBaud.setVisible(true);
			textSerial.setVisible(true);
			textPort.setVisible(false);
			textServerAddress.setVisible(false);
			Core.debugMessage("[gui] network mode disabled");
		}

	}

	private static void demo() {
		byte[] buffer = { (byte) 0xff };

		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void fading() {
		byte speed = (byte) sliderFading.getValue();

		byte[] buffer = { 0x03, speed };

		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void deactivateFading() {
		// deactivate fading
		byte[] buffer = { 0x13 };

		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void globalColor() {
		byte r = (byte) sliderR.getValue();
		byte g = (byte) sliderG.getValue();
		byte b = (byte) sliderB.getValue();

		byte[] buffer = { 0x01, r, g, b };

		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void ledSelected() {
		StripeLED stripeLED = (StripeLED) singleColorComboBox.getSelectedItem();
		if (stripeLED != null) {
			String btnText = stripeLED.toString();
			if (stripeLED.isActivated()) {
				btnDeactivateLED.setText(btnText + " deaktivieren");
			} else {
				btnDeactivateLED.setText(btnText + " aktivieren");
			}
		}
	}

	private static void changeLEDState() {
		StripeLED stripeLED = (StripeLED) singleColorComboBox.getSelectedItem();
		if (stripeLED.isActivated()) {
			// disable LED
			stripeLED.setActivated(false);

		} else {
			// enable LED
			stripeLED.setActivated(true);

		}
		if (singleColorComboBox.getItemCount() >= 5) {
			String[] states = new String[singleColorComboBox.getItemCount() / 5];
			for (int i = 0; i < states.length; i++) {
				states[i] = "";
			}
			for (int i = 0; i < singleColorComboBox.getItemCount(); i++) {
				stripeLED = (StripeLED) singleColorComboBox.getItemAt(i);
				if (stripeLED.isActivated()) {
					states[i / 5] += "1";
				} else {
					states[i / 5] += "0";
				}
			}
			byte[] buffer = new byte[states.length + 1];
			buffer[0] = 0x05;
			for (int i = 0; i < states.length; i++) {
				buffer[i + 1] = (byte) Integer.parseInt(states[i], 2);
			}
			if (core != null) {
				core.write(new KaiLED(buffer).getBuffer());
			}
		}

		ledSelected();
	}

	private static void singleColor() {
		byte r = (byte) sliderRSingle.getValue();
		byte g = (byte) sliderGSingle.getValue();
		byte b = (byte) sliderBSingle.getValue();

		byte[] buffer = new byte[singleColorComboBox.getItemCount() * 3 + 1];
		buffer[0] = 0x04;
		StripeLED stripeLED = (StripeLED) singleColorComboBox.getSelectedItem();
		stripeLED.setColor(r, g, b);
		for (int i = 0; i < singleColorComboBox.getItemCount() * 3; i++) {
			stripeLED = (StripeLED) singleColorComboBox.getItemAt(i / 3);
			switch (i % 3) {
			case 0:
				buffer[i + 1] = stripeLED.getR();
				break;
			case 1:
				buffer[i + 1] = stripeLED.getG();
				break;
			case 2:
				buffer[i + 1] = stripeLED.getB();
				break;
			}

		}

		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void lauflicht() {
		byte s = (byte) sliderSpeed.getValue();

		s = (byte) sliderSpeed.getValue();
		byte[] buffer = { 0x02, s };

		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void deactivateLEDSequencer() {
		byte[] buffer = { 0x12 };
		if (core != null) {
			core.write(new KaiLED(buffer).getBuffer());
		}
	}

	private static void fireEffect() {
		if (core != null) {
			if (fireEffectEnabled) {
				core.stopFireTimer();
				btnFireEffect.setText("Feuereffekt aktivieren");
				fireEffectEnabled = false;
			} else {
				core.startFireTimer(100);
				btnFireEffect.setText("Feuereffekt deaktivieren");
				fireEffectEnabled = true;
			}
		}
	}

	private static void strobo() {
		if (core != null) {
			if (stroboEnabled) {
				byte[] buffer = { 0x16 };
				core.write(new KaiLED(buffer).getBuffer());

				btnStrobo.setText("Strobo aktivieren");
				stroboEnabled = false;
			} else {
				byte duration = (byte) Integer.parseInt(textStroboDuration.getText());
				byte delay = (byte) Integer.parseInt(textStroboDelay.getText());
				byte[] buffer = { 0x06, duration, delay };
				core.write(new KaiLED(buffer).getBuffer());

				btnStrobo.setText("Strobo deaktivieren");
				stroboEnabled = true;
			}
		}
	}

	private static void highPower() {
		if (core != null) {
			if (highPowerEnabled) {
				byte[] buffer = { (byte) 0xfe };
				core.write(new KaiLED(buffer).getBuffer());

				btnHighPower.setText("High Power Modus aktivieren");
				highPowerEnabled = false;
			} else {
				byte[] buffer = { (byte) 0xfd };
				core.write(new KaiLED(buffer).getBuffer());

				btnHighPower.setText("High Power Modus deaktivieren");
				highPowerEnabled = true;
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "eiwomisarc");
		fireEffectEnabled = false;
		stroboEnabled = false;
		highPowerEnabled = false;
		
		// Get the native look and feel class name
		String nativeLF = UIManager.getSystemLookAndFeelClassName();

		// Install the look and feel
		try {
		    UIManager.setLookAndFeel(nativeLF);
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		} catch (UnsupportedLookAndFeelException e) {
		} catch (IllegalAccessException e) {
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("eiwomisarc");
		frame.setBounds(100, 100, 480, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnInitialisierung = new JButton("Initialisierung");
		btnInitialisierung.setBounds(6, 37, 145, 29);
		btnInitialisierung.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				init();
			}
		});
		frame.getContentPane().add(btnInitialisierung);

		sliderR = new JSlider();
		sliderR.setValue(0);
		sliderR.setMaximum(127);
		sliderR.setBounds(146, 95, 190, 29);
		sliderR.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderR);

		sliderG = new JSlider();
		sliderG.setMaximum(127);
		sliderG.setValue(0);
		sliderG.setBounds(146, 136, 190, 29);
		sliderG.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderG);

		sliderB = new JSlider();
		sliderB.setValue(0);
		sliderB.setMaximum(127);
		sliderB.setBounds(146, 177, 190, 29);
		sliderB.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderB);

		JLabel lblR = new JLabel("R");
		lblR.setBounds(136, 100, 15, 16);
		frame.getContentPane().add(lblR);

		JLabel lblG = new JLabel("G");
		lblG.setBounds(136, 136, 15, 16);
		frame.getContentPane().add(lblG);

		JLabel lblB = new JLabel("B");
		lblB.setBounds(136, 175, 15, 16);
		frame.getContentPane().add(lblB);

		sliderSpeed = new JSlider();
		sliderSpeed.setMaximum(255);
		sliderSpeed.setMinimum(1);
		sliderSpeed.setValue(1);
		sliderSpeed.setBounds(146, 214, 190, 29);
		sliderSpeed.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				lauflicht();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderSpeed);

		JLabel lblSpeed = new JLabel("Speed");
		lblSpeed.setBounds(111, 218, 48, 16);
		frame.getContentPane().add(lblSpeed);

		textSerial = new JTextField("/dev/ttyUSB0");
		textSerial.setBounds(153, 36, 110, 28);
		frame.getContentPane().add(textSerial);
		textSerial.setColumns(10);

		textBaud = new JTextField("9600");
		textBaud.setBounds(275, 36, 61, 28);
		frame.getContentPane().add(textBaud);
		textBaud.setColumns(10);

		lblSerialServer = new JLabel("Serial Port");
		lblSerialServer.setBounds(153, 8, 110, 16);
		frame.getContentPane().add(lblSerialServer);

		lblBaudratePort = new JLabel("Baudrate");
		lblBaudratePort.setBounds(275, 8, 61, 16);
		frame.getContentPane().add(lblBaudratePort);

		textNumberOfLEDStripes = new JTextField();
		textNumberOfLEDStripes.setText("1");
		textNumberOfLEDStripes.setBounds(354, 36, 61, 28);
		frame.getContentPane().add(textNumberOfLEDStripes);
		textNumberOfLEDStripes.setColumns(10);

		JLabel lblAnzahlLeisten = new JLabel("Anzahl Leisten");
		lblAnzahlLeisten.setBounds(344, 8, 100, 16);
		frame.getContentPane().add(lblAnzahlLeisten);

		JLabel lblGlobaleFarbe = new JLabel("Globale Farbe:");
		lblGlobaleFarbe.setBounds(18, 136, 95, 16);
		frame.getContentPane().add(lblGlobaleFarbe);

		JLabel lblLauflicht = new JLabel("Lauflicht:");
		lblLauflicht.setBounds(18, 214, 61, 16);
		frame.getContentPane().add(lblLauflicht);

		JButton btnLauflichtDeaktivieren = new JButton("Lauflicht deaktivieren");
		btnLauflichtDeaktivieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deactivateLEDSequencer();
			}
		});
		btnLauflichtDeaktivieren.setBounds(6, 246, 164, 29);
		frame.getContentPane().add(btnLauflichtDeaktivieren);

		singleColorComboBox = new JComboBox();
		singleColorComboBox.setEnabled(false);
		singleColorComboBox.setBounds(102, 387, 164, 27);
		singleColorComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				ledSelected();
			}
		});
		frame.getContentPane().add(singleColorComboBox);

		JLabel lblNewLabel_1 = new JLabel("Einzelfarbe:");
		lblNewLabel_1.setBounds(18, 391, 84, 16);
		frame.getContentPane().add(lblNewLabel_1);

		sliderRSingle = new JSlider();
		sliderRSingle.setEnabled(false);
		sliderRSingle.setValue(0);
		sliderRSingle.setMaximum(127);
		sliderRSingle.setBounds(28, 411, 190, 29);
		sliderRSingle.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				singleColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderRSingle);

		sliderGSingle = new JSlider();
		sliderGSingle.setEnabled(false);
		sliderGSingle.setValue(0);
		sliderGSingle.setMaximum(127);
		sliderGSingle.setBounds(28, 452, 190, 29);
		sliderGSingle.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				singleColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderGSingle);

		sliderBSingle = new JSlider();
		sliderBSingle.setEnabled(false);
		sliderBSingle.setValue(0);
		sliderBSingle.setMaximum(127);
		sliderBSingle.setBounds(28, 493, 190, 29);
		sliderBSingle.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				singleColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderBSingle);

		JLabel label = new JLabel("R");
		label.setBounds(18, 416, 15, 16);
		frame.getContentPane().add(label);

		JLabel label_1 = new JLabel("G");
		label_1.setBounds(18, 452, 15, 16);
		frame.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("B");
		label_2.setBounds(18, 491, 15, 16);
		frame.getContentPane().add(label_2);

		btnFading = new JButton("Farb\u00FCberg\u00E4nge ausschalten");
		btnFading.setBounds(6, 346, 208, 29);
		btnFading.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deactivateFading();
			}
		});
		frame.getContentPane().add(btnFading);

		sliderFading = new JSlider();
		sliderFading.setMinimum(1);
		sliderFading.setValue(1);
		sliderFading.setMaximum(255);
		sliderFading.setEnabled(false);
		sliderFading.setBounds(225, 320, 190, 29);
		sliderFading.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				fading();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderFading);

		JLabel lblNewLabel_2 = new JLabel("Farb\u00FCberg\u00E4nge Geschwindigkeit:");
		lblNewLabel_2.setBounds(16, 320, 208, 16);
		frame.getContentPane().add(lblNewLabel_2);

		JButton btnDemoModus = new JButton("Demo Modus");
		btnDemoModus.setBounds(364, 246, 110, 29);
		btnDemoModus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				demo();
			}
		});
		frame.getContentPane().add(btnDemoModus);

		btnDeactivateLED = new JButton("LED deaktivieren");
		btnDeactivateLED.setEnabled(false);
		btnDeactivateLED.setBounds(212, 411, 226, 29);
		btnDeactivateLED.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				changeLEDState();
			}
		});
		frame.getContentPane().add(btnDeactivateLED);

		btnFireEffect = new JButton("Feuer Effekt aktivieren");
		btnFireEffect.setBounds(166, 246, 200, 29);
		btnFireEffect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fireEffect();
			}
		});
		frame.getContentPane().add(btnFireEffect);

		btnStrobo = new JButton("Strobo aktivieren");
		btnStrobo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				strobo();
			}
		});
		btnStrobo.setBounds(6, 279, 153, 29);
		frame.getContentPane().add(btnStrobo);

		JLabel lblStroboDelay = new JLabel("Strobo Delay:");
		lblStroboDelay.setBounds(176, 284, 84, 16);
		frame.getContentPane().add(lblStroboDelay);

		JLabel lblStroboDauer = new JLabel("Strobo Dauer:");
		lblStroboDauer.setBounds(325, 284, 91, 16);
		frame.getContentPane().add(lblStroboDauer);

		JLabel lblMs = new JLabel("ms");
		lblMs.setBounds(294, 284, 19, 16);
		frame.getContentPane().add(lblMs);

		JLabel label_3 = new JLabel("ms");
		label_3.setBounds(445, 284, 19, 16);
		frame.getContentPane().add(label_3);

		textStroboDelay = new JTextField();
		textStroboDelay.setHorizontalAlignment(SwingConstants.RIGHT);
		textStroboDelay.setText("5");
		textStroboDelay.setBounds(263, 279, 33, 28);
		frame.getContentPane().add(textStroboDelay);
		textStroboDelay.setColumns(10);

		textStroboDuration = new JTextField();
		textStroboDuration.setText("5");
		textStroboDuration.setHorizontalAlignment(SwingConstants.RIGHT);
		textStroboDuration.setBounds(413, 279, 33, 28);
		frame.getContentPane().add(textStroboDuration);
		textStroboDuration.setColumns(10);

		btnHighPower = new JButton("High Power Modus aktivieren");
		btnHighPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highPower();
			}
		});
		btnHighPower.setBounds(6, 65, 226, 29);
		frame.getContentPane().add(btnHighPower);

		chckbxNetzwerkmodus = new JCheckBox("Netzwerkmodus");
		chckbxNetzwerkmodus.setBounds(232, 66, 134, 23);
		chckbxNetzwerkmodus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				networkStateChange();
			}
		});
		frame.getContentPane().add(chckbxNetzwerkmodus);

		networkComboBox = new JComboBox();
		networkComboBox.setModel(new DefaultComboBoxModel(new String[] { "Server", "Client" }));
		networkComboBox.setBounds(379, 66, 95, 27);
		networkComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				networkStateChange();
			}
		});
		frame.getContentPane().add(networkComboBox);

		textServerAddress = new JTextField("localhost");
		textServerAddress.setColumns(10);
		textServerAddress.setBounds(153, 36, 110, 28);
		textServerAddress.setVisible(false);
		frame.getContentPane().add(textServerAddress);

		textPort = new JTextField("1337");
		textPort.setColumns(10);
		textPort.setBounds(275, 36, 61, 28);
		textPort.setVisible(false);
		frame.getContentPane().add(textPort);
	}
}
