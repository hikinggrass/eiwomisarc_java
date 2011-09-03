package de.hikinggrass.eiwomisarc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI {

	private JFrame frame;

	private static Core core;
	private static JTextField textSerial;
	private static JTextField textBaud;
	private static JSlider sliderR;
	private static JSlider sliderG;
	private static JSlider sliderB;
	private static JSlider sliderSpeed;
	private static JTextField textNumberOfLEDStripes;

	private static void init() {
		if (core == null) {
			core = new Core(textSerial.getText(), Integer.parseInt(textBaud.getText()));
		}
		byte count = (byte) Integer.parseInt(textNumberOfLEDStripes.getText());
		byte[] buffer = { 0x00, count };

		core.writeToSerialPort(new KaiLED(buffer).getBuffer());
	}

	private static void globalColor() {
		byte r = (byte) sliderR.getValue();
		byte g = (byte) sliderG.getValue();
		byte b = (byte) sliderB.getValue();

		byte[] buffer = { 0x01, r, g, b };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void lauflicht() {
		byte s = (byte) sliderSpeed.getValue();

		s = (byte) sliderSpeed.getValue();
		byte[] buffer = { 0x02, s };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

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
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
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
		sliderR.setBounds(146, 78, 190, 29);
		sliderR.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}
		});
		frame.getContentPane().add(sliderR);

		sliderG = new JSlider();
		sliderG.setMaximum(127);
		sliderG.setValue(0);
		sliderG.setBounds(146, 119, 190, 29);
		sliderG.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}
		});
		frame.getContentPane().add(sliderG);

		sliderB = new JSlider();
		sliderB.setValue(0);
		sliderB.setMaximum(127);
		sliderB.setBounds(146, 160, 190, 29);
		sliderB.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}
		});
		frame.getContentPane().add(sliderB);

		JLabel lblR = new JLabel("R");
		lblR.setBounds(136, 83, 15, 16);
		frame.getContentPane().add(lblR);

		JLabel lblG = new JLabel("G");
		lblG.setBounds(136, 119, 15, 16);
		frame.getContentPane().add(lblG);

		JLabel lblB = new JLabel("B");
		lblB.setBounds(136, 158, 15, 16);
		frame.getContentPane().add(lblB);

		sliderSpeed = new JSlider();
		sliderSpeed.setMaximum(255);
		sliderSpeed.setMinimum(1);
		sliderSpeed.setValue(1);
		sliderSpeed.setBounds(146, 197, 190, 29);
		sliderSpeed.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				lauflicht();
			}
		});
		frame.getContentPane().add(sliderSpeed);

		JLabel lblSpeed = new JLabel("Speed");
		lblSpeed.setBounds(111, 201, 48, 16);
		frame.getContentPane().add(lblSpeed);

		textSerial = new JTextField("/dev/ttyUSB0");
		textSerial.setBounds(153, 36, 110, 28);
		frame.getContentPane().add(textSerial);
		textSerial.setColumns(10);

		textBaud = new JTextField("9600");
		textBaud.setBounds(275, 36, 61, 28);
		frame.getContentPane().add(textBaud);
		textBaud.setColumns(10);

		JLabel lblNewLabel = new JLabel("Serial Port");
		lblNewLabel.setBounds(174, 8, 77, 16);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblBaudrate = new JLabel("Baudrate");
		lblBaudrate.setBounds(275, 8, 61, 16);
		frame.getContentPane().add(lblBaudrate);

		textNumberOfLEDStripes = new JTextField();
		textNumberOfLEDStripes.setText("1");
		textNumberOfLEDStripes.setBounds(354, 36, 61, 28);
		frame.getContentPane().add(textNumberOfLEDStripes);
		textNumberOfLEDStripes.setColumns(10);

		JLabel lblAnzahlLeisten = new JLabel("Anzahl Leisten");
		lblAnzahlLeisten.setBounds(344, 8, 100, 16);
		frame.getContentPane().add(lblAnzahlLeisten);
		
		JLabel lblGlobaleFarbe = new JLabel("Globale Farbe:");
		lblGlobaleFarbe.setBounds(18, 119, 95, 16);
		frame.getContentPane().add(lblGlobaleFarbe);
		
		JLabel lblLauflicht = new JLabel("Lauflicht:");
		lblLauflicht.setBounds(18, 197, 61, 16);
		frame.getContentPane().add(lblLauflicht);
	}
}
