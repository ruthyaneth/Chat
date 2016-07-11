package client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import server.ReceiveAudio;
import server.Server;

public class FrameVoiceCall extends JFrame {

	public FrameVoiceCall(String username, final PrintWriter clientOut) {

		DefaultListModel model = new DefaultListModel();
		JList online = new JList(model);

		final JFrame inputframe = new JFrame();
		JPanel selectedUser = new JPanel();
		JLabel req = new JLabel("Select User: ");
		model.removeElement(username);
		String[] onl = new String[model.getSize()];
		for (int i = 0; i < model.getSize(); i++) {
			onl[i] = model.getElementAt(i).toString();
		}
		final JComboBox listOnlUsers = new JComboBox(onl);
		selectedUser.add(req);
		JButton closeFrame = new JButton("OK");

		model.addElement(username);
		selectedUser.add(listOnlUsers);
		selectedUser.add(closeFrame);
		JButton cancelFrame = new JButton("Cancel");
		selectedUser.add(cancelFrame);
		cancelFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				inputframe.setVisible(false);
			}
		});
		selectedUser.add(cancelFrame);
		inputframe.setTitle("Audio Chat");
		inputframe.add(selectedUser);
		inputframe.setSize(400, 120);
		inputframe.setVisible(true);
		inputframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		closeFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				inputframe.setVisible(false);
				try {
					String usernameTextField = listOnlUsers.getSelectedItem().toString();
					try {
						final ServerSocket ss = new ServerSocket(0);
						String ip = ss.getInetAddress().getLocalHost().getHostAddress();
						int port = ss.getLocalPort();
						clientOut.println("VOICECHAT" + usernameTextField + ";" + ip + "*" + port);
						final Socket conn = ss.accept();
						ss.close();
						final JFrame callFr = new JFrame();
						JPanel callPn = new JPanel();
						JButton endCall = new JButton("End Call");
						callPn.add(endCall);
						callFr.add(callPn);
						callFr.setSize(400, 120);
						callFr.setVisible(true);
						callFr.setTitle("Server Audio Chat");
						callFr.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent evt) {
								System.out.println("shjt. close it for me!!!!!!!!");
								try {
									conn.close();
									ss.close();
								} catch (IOException ex) {
									Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						});

						endCall.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent ae) {
								callFr.setVisible(false);
								inputframe.setVisible(false);
								try {
									conn.close();
									ss.close();
									System.out.println("End call!");
								} catch (IOException ex) {
									Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						});

						System.out.println("Connected to client: " + conn.getRemoteSocketAddress());
						DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
						// Formatting audio data, start microphone
						// recording
						AudioFormat af = new AudioFormat(8000.0f, 8, 1, true, false);
						DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
						TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
						microphone.open(af);
						microphone.start();
						int bytesRead = 0;
						byte[] soundData = new byte[1];

						// Reading data in
						Thread inThread = new Thread(new ReceiveAudio(conn));
						inThread.start();
						// Sending data out
						while (bytesRead != -1) {
							bytesRead = microphone.read(soundData, 0, soundData.length);
							if (!conn.isClosed()) {
								if (bytesRead >= 0) {
									try {
										dos.write(soundData, 0, bytesRead);
									} catch (Exception e) {
										conn.close();
										ss.close();
										new FrameNotif("Data communication error! Exit!");
										break;
									}
								}
							} else {
								conn.close();
								ss.close();
								new FrameNotif("Client disconnected!");
								break;
							}
						}
						microphone.close();
						microphone.stop();
						System.out.println("IT IS DONE.");

					} catch (Exception ex) {
						new FrameNotif("Error!");
						Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
						ex.printStackTrace();
					}
				} catch (Exception e) {
					inputframe.setVisible(false);
					new FrameNotif("No usernameTextField was selected");
				}
			}
		});
	}

}
