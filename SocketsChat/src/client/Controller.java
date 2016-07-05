package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import client.constants.ConstantsListener;
import client.constants.ConstantsLogic;
import client.view.FirstPanel;
import client.view.FrameChatPrivate;
import client.view.FrameChatRoom;
import client.view.FrameLogin;
import client.view.FrameNotif;
import client.view.FrameRegister;
import client.view.GroupChatFrame;
import client.view.PanelLogin;
import client.view.PanelRegister;
import client.view.SignupPanel;
import client.view.WindowClient;
import config.HandlerLanguage;
import server.ReceiveAudio;
import server.Server;

public class Controller implements ActionListener {

	private HandlerLanguage handlerLanguage;
	private WindowClient windowClient;
	private FrameLogin frameLogin;
	private FirstPanel firstPanel;
	private SignupPanel signupPanel;
	private FrameChatPrivate frameChatPrivate;
	private GroupChatFrame groupChatFrame;
	boolean isMasterGroupChat, isMemberGroupChat = false;
	private JTextArea privateTextArea = new JTextArea();
	private JTextField privateChatTextField = new JTextField();
	private FrameRegister frameRegister;

	boolean isPrivateChat = false;
	boolean isGroupChat = false;
	String privatePartner = null;
	String fileName = null;
	String masterGroupName = null;
	JTextPane chatArea = new JTextPane();
	String username = null;

	String userName;
	Socket clientSocket = null;
	ServerSocket fileSocket;
	ArrayList listBlocked = new ArrayList();
	PrintWriter clientOut = null; // outToServer
	BufferedReader clientIn = null;

	StyledDocument doc = null;
	Style def = null;
	Style notification = null;
	StyledDocument docGroup = null;
	Style defGroup = null;
	Style notificationGroup = null;
	Style regular = null;

	DefaultListModel model = new DefaultListModel();
	JList online = new JList(model);
	File fileFile = null;

	public Controller() {
		loadConfiguration();
		frameRegister = new FrameRegister(this);
		signupPanel = new SignupPanel();
	}

	public void loadConfiguration() {
		if (handlerLanguage == null) {
			handlerLanguage = new HandlerLanguage("language/config.ini");
			try {
				handlerLanguage.loadLanguage();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(HandlerLanguage.language);
		}
	}

	public void writeFile() throws Exception {
		String filename = frameRegister.getPanelRegister().getUsernameRegister().getText() + "ChatRecord.txt";
		FileWriter writer = new FileWriter(new File(filename));
		writer.write(chatArea.getText());
		System.out.println(chatArea.getText());
		writer.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		
		case ConstantsListener.A_LOGIN_BUTTON:
			if (frameLogin.getPanelLogin().getUsernameTextFieldLogin().getText().equals("")
					|| String.valueOf(frameLogin.getPanelLogin().getUsernameTextFieldLogin()).equals("")) {
				new FrameNotif("Fields are empty.");
				frameLogin.getPanelLogin().getUsernameTextFieldLogin().setText("");
				frameLogin.getPanelLogin().getPasswordLogin().setText("");
			} else if (ConstantsLogic.SERVER_NO_OK == true) {
				username = frameLogin.getPanelLogin().getUsernameTextFieldLogin().getText().trim();
				clientOut.println("CHECKUSER" + username + "|"
						+ String.valueOf(frameLogin.getPanelLogin().getUsernameTextFieldLogin().getText().trim()).trim());
//				frameLogin.getPanelLogin().setVisible(false);
//				signupPanel.setVisible(true);
				new FrameChatRoom();
			}
			break;
		case ConstantsListener.A_REGISTER_BUTTON:
			frameRegister.setVisible(true);
			break;
		case ConstantsListener.A_BUTTON_REGISTER_OK:

			if (String.valueOf(frameRegister.getPanelRegister().getPasswordRegister().getPassword()).equals(
					String.valueOf(frameRegister.getPanelRegister().getPasswordRetypeRegister().getPassword()))) {
				if (frameRegister.getPanelRegister().getUsernameRegister().getText().equals("") || String
						.valueOf(frameRegister.getPanelRegister().getPasswordRegister().getPassword()).equals("")) {
					new FrameNotif("Fields are empty.");
					frameRegister.getPanelRegister().getUsernameRegister().setText("");
					frameRegister.getPanelRegister().getPasswordRegister().setText("");
					frameRegister.getPanelRegister().getPasswordRetypeRegister().setText("");
				} else
					clientOut.println(
							"REGISTER" + frameRegister.getPanelRegister().getUsernameRegister().getText().trim() + "|"
									+ String.valueOf(
											frameRegister.getPanelRegister().getPasswordRegister().getPassword())
											.trim());
			} else {
				new FrameNotif("Passwords are not the same.");
				frameRegister.getPanelRegister().getUsernameRegister().setText("");
				frameRegister.getPanelRegister().getPasswordRegister().setText("");
				frameRegister.getPanelRegister().getPasswordRetypeRegister().setText("");
			}
			break;
		case ConstantsListener.A_BUTTON_BACK:
			frameRegister.setVisible(false);
			break;
		}
	}

	public void run() throws Exception {
		while (!ConstantsLogic.SERVER_NO_OK) {
			int port;
			frameLogin = new FrameLogin(this);
			String ipAdd = JOptionPane.showInputDialog(frameLogin, "Please enter the server IP address.", "localhost");
			if (ipAdd == null) {
				System.exit(0);
			} else if (ipAdd.isEmpty()) {
				JOptionPane.showInputDialog(frameLogin, "Please enter the server IP address", "localhost");
			} else {
				try {
					clientSocket = new Socket(ipAdd, 60000);
					clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
					ConstantsLogic.SERVER_NO_OK = true;
				} catch (Exception e1) {
					new FrameNotif("Connect error! Please check the server IP.");
					ConstantsLogic.SERVER_NO_OK = false;
				}
			}
		}
		while (true) {
			try {
				String input = clientIn.readLine().trim();
				byte[] file = new byte[8000000];
				if (input == null) {
					return;
				} else if (input.startsWith("ADMINBLOCKED")) {
					if (input.substring(12).equals(userName)) {
						JOptionPane.showMessageDialog(null, "You has been blocked by admin!");
						System.exit(0);
					} else {
						doc.insertString(doc.getLength(), "* " + input.substring(12) + " just left our chat room * \n",
								doc.getStyle("notification"));
						clientOut.println("REFRESHONL");
						model.clear();
					}
				} else if (input.startsWith("INCORRECT")) {
					new FrameNotif("Wrong id or password.");
//					panelLogin.getUsernameTextFieldLogin().setText("");
//					panelLogin.getPasswordLogin().setText("");
				} else if (input.startsWith("ISONLINE")) {
					new FrameNotif("User is already online.");
//					panelLogin.getUsernameTextFieldLogin().setText("");
//					panelLogin.getPasswordLogin().setText("");
				} else if (input.startsWith("ISBLOCKED")) {
					new FrameNotif("This user has been blocked! Please contact admin.");
//					panelLogin.getUsernameTextFieldLogin().setText("");
//					panelLogin.getPasswordLogin().setText("");
					frameLogin.sendVisible(false);
				} else if (input.startsWith("LOGINOK")) {
					frameLogin.setVisible(false);
					frameLogin.sendVisible(false);
					firstPanel.sendTxtUserStatus("Nickname: " + userName + "                   Status: ");
					clientOut.println("CONNECTED" + userName);
				} else if (input.startsWith("DUPLICATE")) {
					new FrameNotif("Duplicate account.");
					frameRegister.getPanelRegister().getUsernameRegister().setText("");
					frameRegister.getPanelRegister().getPasswordRegister().setText("");
					frameRegister.getPanelRegister().getPasswordRetypeRegister().setText("");
				} else if (input.startsWith("REGISTEROK")) {
					signupPanel.setVisible(false);
					frameRegister.getPanelRegister().getUsernameRegister().setText("");
					frameRegister.getPanelRegister().getPasswordRegister().setText("");
					frameRegister.getPanelRegister().getPasswordRetypeRegister().setText("");
					frameLogin.add(frameLogin.getPanelLogin());
					frameLogin.getPanelLogin().setVisible(true);
				} else if (input.startsWith("PUBLIC")) {
					String usernameTextField = input.substring(6, input.indexOf("|", 0));
					if (!listBlocked.contains(usernameTextField)) {
						printText(windowClient.getChatArea(), input.substring(6, input.indexOf("|", 0)) + ": "
								+ input.substring(input.indexOf("|", 0) + 1));
					}
				} else if (input.startsWith("CONNECTED")) {
					doc.insertString(doc.getLength(), "* " + input.substring(9) + " just joined our chat room * \n",
							doc.getStyle("notification"));
					clientOut.println("REFRESHONL");
					model.clear();
				} else if (input.startsWith("ONLINE")) {
					model.addElement(input.substring(6));
				} else if (input.startsWith("STATUS")) {
					String stupidShit = input.substring(6);
					StringTokenizer tokenizer = new StringTokenizer(stupidShit, "|");
					String usernameTextField = null;
					String status = null;
					while (tokenizer.hasMoreElements()) {
						usernameTextField = tokenizer.nextToken().toString().trim();
						status = tokenizer.nextToken().toString().trim();
					}
					if (!model.contains(usernameTextField)) {
						model.addElement(usernameTextField);
					}
					doc.insertString(doc.getLength(),
							"* " + usernameTextField + " just update his status to " + status + " *\n",
							doc.getStyle("notification"));
				} else if (input.startsWith("INVISIBLE")) {
					model.removeElement(input.substring(9));
				} else if (input.startsWith("PRIVATE")) {
					String combi = input.substring(7);
					String partner = combi.substring(0, combi.indexOf("|", 0));
					String userRequest = combi.substring(combi.indexOf("|", 0) + 1, combi.indexOf(",", 0));
					String content = combi.substring(combi.indexOf(",", 0) + 1);

					if (listBlocked.contains(userRequest)) {
						clientOut.println("BLOCKPRIVATE" + userRequest);
					} else if (isPrivateChat == false && partner.equals(userName)) {
						frameChatPrivate.sendTxtLabelPrivateChat("Private chat with ", userRequest);

						frameChatPrivate.setTitle("Nickname:" + userName);
						frameChatPrivate.setVisible(true);
						privatePartner = userRequest;
						frameChatPrivate.sendAppend(userRequest, content);
						frameChatPrivate.sendAppend(userRequest, content);
						isPrivateChat = true;
					} else if (isPrivateChat == true && partner.equals(userName)
							&& userRequest.equals(privatePartner)) {
						frameChatPrivate.sendAppend(userRequest, content);

					} else if (isPrivateChat == true && partner.equals(userName)
							&& !userRequest.equals(privatePartner)) {
						clientOut.println("BUSY" + userRequest);

					}
				} else if (input.startsWith("BUSY")) {
					if (input.substring(4).equals(userName)) {
						new FrameNotif("Requested user is busy! Try again later.");
						frameChatPrivate.sendTextPrivateChatTextFiel("");
						frameChatPrivate.sendTxtArea("");
						frameChatPrivate.dispose();
						isPrivateChat = false;
						privatePartner = null;
					}
				} else if (input.startsWith("BLOCKPRIVATE")) {
					if (input.substring(12).equals(userName)) {
						new FrameNotif("You has blocked this usernameTextField from chat.");
						frameChatPrivate.sendTextPrivateChatTextFiel("");
						frameChatPrivate.sendTextPrivateChatTextFiel("");
						frameChatPrivate.sendTextArea("");
						frameChatPrivate.dispose();
						isPrivateChat = false;
						privatePartner = null;
					}
				} else if (input.startsWith("VOICECHAT")) {
					String usernameTextField = input.substring(9, input.indexOf(";"));
					String ip = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
					int port = Integer.parseInt(input.substring(input.indexOf("*") + 1));
					if (usernameTextField.equals(userName)) {
						new FrameNotif("Server starts voice chat with you");
						final Socket conn = new Socket(ip, port);
						final JFrame callFr = new JFrame();
						JPanel callPn = new JPanel();
						JButton endCall = new JButton("End Call");
						callPn.add(endCall);
						callFr.add(callPn);
						callFr.setSize(400, 120);
						callFr.setVisible(true);
						callFr.setTitle("Client Audio Chat");
						callFr.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent evt) {
								System.out.println("shjt. close it for me!!!!!!!!");
								try {
									conn.close();
								} catch (IOException ex) {
									Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						});
						endCall.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent ae) {
								callFr.setVisible(false);
								try {
									conn.close();
									System.out.println("Call Ended!");
								} catch (IOException ex) {
									Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						});

						DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
						// Formatting audio data, start microphone recording
						AudioFormat af = new AudioFormat(8000.0f, 8, 1, true, false);
						DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
						TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
						microphone.open(af);
						microphone.start();

						int bytesRead = 0;
						byte[] soundData = new byte[1];

						Thread inThread = new Thread(new ReceiveAudio(conn));
						inThread.start();
						while (bytesRead != -1) {
							bytesRead = microphone.read(soundData, 0, soundData.length);
							if (!conn.isClosed()) {
								if (bytesRead >= 0) {
									try {
										dos.write(soundData, 0, bytesRead);
									} catch (Exception e) {
										conn.close();
										new FrameNotif("Data communication error! Exit!");
										break;
									}
								}
							} else {
								conn.close();
								new FrameNotif("Server disconnected audio chat!");
								break;
							}
						}
						microphone.close();
						microphone.stop();
						System.out.println("IT IS DONE.");
					}
				} else if (input.startsWith("REJECTFILE")) {
					if (input.substring(10, input.indexOf(";")).equals(userName)) {
						new FrameNotif(input.substring(input.indexOf(",") + 1) + " just declined your file.");
					}
				} else if (input.startsWith("ACCEPTFILE")) {
					String sender = input.substring(10, input.indexOf(";"));
					String remoteip = input.substring(input.indexOf("*") + 1, input.indexOf("|"));
					String onPort = input.substring(input.indexOf("|") + 1, input.indexOf(","));
					String reciever = input.substring(input.indexOf(",") + 1);
					String filepath = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
					int port = Integer.parseInt(onPort);
					System.out.println(input);
					if (sender.equals(userName)) {
						doc.insertString(doc.getLength(), "* File transfer to " + reciever + " begin * \n",
								doc.getStyle("notification"));
						try {
							clientOut.println("BEGINFILE" + input.substring(10));
							Socket client = fileSocket.accept();
							System.out.println("Accept incoming request");
							fileFile = new File(fileName);
							byte[] files = new byte[(int) fileFile.length()];
							FileInputStream fis = new FileInputStream(fileFile);
							BufferedInputStream bis = new BufferedInputStream(fis);
							bis.read(files, 0, files.length);
							OutputStream os = client.getOutputStream();
							os.write(files, 0, files.length);
							os.flush();
							os.close();
							bis.close();
							client.close();
							fileSocket.close();
							doc.insertString(doc.getLength(), "* File transfer to " + reciever + " completed * \n",
									doc.getStyle("notification"));
						} catch (Exception e) {
							new FrameNotif(e.getMessage());
						}
					}
				} else if (input.startsWith("FILE")) {
					String sender = input.substring(4, input.indexOf(";"));
					String remoteip = input.substring(input.indexOf("*") + 1, input.indexOf("|"));
					String onPort = input.substring(input.indexOf("|") + 1, input.indexOf(","));
					String reciever = input.substring(input.indexOf(",") + 1);
					String filepath = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
					int port = Integer.parseInt(onPort);
					if (reciever.equals(userName)) {
						int confirm = JOptionPane.showConfirmDialog(null,
								sender + " would like to transfer file to you, Do you accept ?", "File Transfer",
								JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							// fileRequest = fileName;
							clientOut.println("ACCEPTFILE" + input.substring(4));
						} else {
							clientOut.println("REJECTFILE" + input.substring(4));
						}
					}
				} else if (input.startsWith("BEGINFILE")) {
					String sender = input.substring(9, input.indexOf(";"));
					String remoteip = input.substring(input.indexOf("*") + 1, input.indexOf("|"));
					String onPort = input.substring(input.indexOf("|") + 1, input.indexOf(","));
					String reciever = input.substring(input.indexOf(",") + 1);
					String filepath = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
					int port = Integer.parseInt(onPort);
					System.out.println("file request: " + filepath);
					if (reciever.equals(userName)) {
						if (filepath != null) {
							doc.insertString(doc.getLength(), "* File transfer between you and server begin * \n",
									doc.getStyle("notification"));
							try {
								Socket client1 = new Socket(remoteip, port);
								System.out.println("requesting reliable socket");
								int sizeRead;
								int current;
								FileOutputStream fos = new FileOutputStream(
										"ClientDownload/" + reciever + "-" + filepath);
								BufferedOutputStream bos = new BufferedOutputStream(fos);
								InputStream is = client1.getInputStream();
								sizeRead = is.read(file, 0, file.length);
								current = sizeRead;
								do {
									sizeRead = is.read(file, current, (file.length - current));
									if (sizeRead >= 0) {
										current += sizeRead;
									}
								} while (sizeRead > -1);
								bos.write(file, 0, current);
								bos.flush();
								bos.close();
								client1.close();
								doc.insertString(doc.getLength(),
										"* File transfer between you and server completed * \n",
										doc.getStyle("notification"));
							} catch (Exception e1) {
								System.err.println(e1.getMessage());
							}
						}
					}
				} else if (input.startsWith("INVITE")) {
					String usernameTextField = input.substring(6, input.indexOf("|", 0));
					String master = input.substring(input.indexOf("|", 0) + 1);
					if (listBlocked.contains(master) && usernameTextField.equals(userName)) {
						clientOut.println("BLOCKEDGROUP" + master);
					} else if (!isGroupChat && usernameTextField.equals(userName)) {
						int confirm = JOptionPane.showConfirmDialog(new GroupChatFrame(),
								master + " would like to invite you to his group, Do you accept ?", "Group chat",
								JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							isGroupChat = true;
							isMemberGroupChat = true;
							masterGroupName = master;
							groupChatFrame.getGroupChatPanel().remove(groupChatFrame.getInviteGroupChatButton());
							groupChatFrame.getGroupChatPanel().revalidate();
							groupChatFrame.revalidate();
							groupChatFrame.getWelcomeGroupChatLabel().setText("Group chat of " + masterGroupName);
							groupChatFrame.setVisible(true);
							clientOut.println("ACCEPTINVITE" + master);
						} else {
							clientOut.println("REJECTINVITE" + usernameTextField);
						}
					} else if (isGroupChat && usernameTextField.equals(userName)) {
						clientOut.println("NOJOIN" + userName);
					}
				} else if (input.startsWith("CHATMES") && isGroupChat) {
					if (input.substring(7, input.indexOf("|", 0)).equals(masterGroupName)
							|| input.substring(7, input.indexOf("|", 0)).equals(userName))
						docGroup.insertString(docGroup.getLength(), input.substring(input.indexOf("|", 0) + 1) + "\n",
								docGroup.getStyle("regular"));
				} else if (input.startsWith("REJECTINVITE") && isMasterGroupChat) {
					new FrameNotif(input.substring(12) + " decline to join.");
				} else if (input.startsWith("NOJOIN") && isMasterGroupChat) {
					if (!input.substring(6).equals(userName))
						new FrameNotif(input.substring(6) + " already have a group chat.");
				} else if (input.startsWith("BLOCKEDGROUP")) {
					if (userName.equals(input.substring(12)))
						new FrameNotif("User has blocked you .");
				} else if (input.startsWith("DELETEPRIVATE") && input.substring(13).equals(userName)) {
					isPrivateChat = false;
					privatePartner = null;
//					privateTextArea.setText("");
//					privateChatTextField.setText("");
					frameChatPrivate.dispose();
					new FrameNotif("Private chat has been cancelled");
				} else if (input.startsWith("CANCELGROUP") && isGroupChat && isMemberGroupChat) {
					if (input.substring(11, input.indexOf("|", 0)).equals(masterGroupName)
							|| input.substring(11, input.indexOf("|", 0)).equals(username))
						docGroup.insertString(docGroup.getLength(),
								"* " + input.substring(input.indexOf("|", 0) + 1) + " just left your group chat * \n",
								docGroup.getStyle("notification"));
				} else if (input.startsWith("CANCELMASTER") && isGroupChat && isMemberGroupChat
						&& input.substring(12).equals(masterGroupName)) {
					isMemberGroupChat = false;
					isGroupChat = false;
					groupChatFrame.getGroupChatTextPanel().setText("");
					groupChatFrame.getGroupTypeArea().setText("");
					masterGroupName = null;
					new FrameNotif("Group chat is closed by group owner! Goodbye.");
					groupChatFrame.dispose();
				} else if (input.startsWith("BLOCKEDBEFORE")) {
					new FrameNotif(input.substring(13) + " has been blocked before .");
				} else if (input.startsWith("BLOCKEDSUCCESS")) {
					listBlocked.add(input.substring(14));
					new FrameNotif(input.substring(14) + " has been blocked successfully .");
				} else if (input.startsWith("BLOCKEDERROR")) {
					new FrameNotif("Cannot block Admin.");
				} else if (input.startsWith("UNBLOCKSUCCESS")) {
					listBlocked.remove(input.substring(14));
					new FrameNotif(input.substring(14) + " has been unblocked.");
				} else if (input.startsWith("UNBLOCKEDERROR")) {
					new FrameNotif(input.substring(14) + " is not blocked yet");
				}
			} catch (SocketException e) { 
				JOptionPane.showMessageDialog(null, "There was a problem communicating with server. Exitting.", null,
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			} catch (BadLocationException e) {
				new FrameNotif(e.getMessage());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			} finally {
				writeFile();
			}
		}
	}

	public void printText(JTextPane actionTextPane, String actionText) throws BadLocationException { 
		actionTextPane.setOpaque(false); 
		Pattern pattern = Pattern
				.compile("<SMILE>|<BSMILE>|<SAD>|<CRY>|<TOUNGE>|<ANGEL>|<DEVIL>|<CONFUSE>|<WINKING>|<SURPRISE>");
		Matcher matcher = pattern.matcher(actionText); 
		Style s = doc.addStyle("icon", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		int previousMatch = 0; 		while (matcher.find()) { 
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			String subText = actionText.substring(previousMatch, startIndex);
			if (!subText.isEmpty()) {
				doc.insertString(doc.getLength(), subText, doc.getStyle("regular"));
			}
			if (group.equals("<SMILE>")) {
				StyleConstants.setIcon(s, emoticon("../1.png"));
				doc.insertString(doc.getLength(), "<SMILE>", doc.getStyle("icon"));
			} else if (group.equals("<SAD>")) {
				StyleConstants.setIcon(s, emoticon("../3.png"));
				doc.insertString(doc.getLength(), "<SAD>", doc.getStyle("icon"));
			} else if (group.equals("<BSMILE>")) {
				StyleConstants.setIcon(s, emoticon("../2.png"));
				doc.insertString(doc.getLength(), "<BSMILE>", doc.getStyle("icon"));
			} else if (group.equals("<TOUNGE>")) {
				StyleConstants.setIcon(s, emoticon("../5.png"));
				doc.insertString(doc.getLength(), "<TOUNGE>", doc.getStyle("icon"));
			} else if (group.equals("<CRY>")) {
				StyleConstants.setIcon(s, emoticon("../4.png"));
				doc.insertString(doc.getLength(), "<CRY>", doc.getStyle("icon"));
			} else if (group.equals("<DEVIL>")) {
				StyleConstants.setIcon(s, emoticon("../7.png"));
				doc.insertString(doc.getLength(), "<DEVIL>", doc.getStyle("icon"));
			} else if (group.equals("<ANGEL>")) {
				StyleConstants.setIcon(s, emoticon("../6.png"));
				doc.insertString(doc.getLength(), "<ANGEL>", doc.getStyle("icon"));
			} else if (group.equals("<WINKING>")) {
				StyleConstants.setIcon(s, emoticon("../9.png"));
				doc.insertString(doc.getLength(), "<WINKING>", doc.getStyle("icon"));
			} else if (group.equals("<CONFUSE>")) {
				StyleConstants.setIcon(s, emoticon("../8.png"));
				doc.insertString(doc.getLength(), "<CONFUSE>", doc.getStyle("icon"));
			} else if (group.equals("<SURPRISE>")) {
				StyleConstants.setIcon(s, emoticon("../10.png"));
				doc.insertString(doc.getLength(), "<SURPRISE>", doc.getStyle("icon"));
			}
			previousMatch = endIndex;
		}
		String subText = actionText.substring(previousMatch); 
		if (!subText.isEmpty()) { 
			doc.insertString(doc.getLength(), subText, doc.getStyle("regular"));
		}
		doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));
	}

	public ImageIcon emoticon(String path) {
		URL imgURL = Server.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file path: " + path);
			return null;
		}
	}
}
