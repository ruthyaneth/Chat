package client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FrameFileTransfer extends JFrame{
	
	String filename;

	private static final long serialVersionUID = 1L;
	DefaultListModel model = new DefaultListModel();
	JList online = new JList(model);
	
	public FrameFileTransfer(final String username, final JMenuItem fileTransfer, final PrintWriter clientOut, final String ip , final int filePort ) {
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
				fileTransfer.setVisible(false);
			}
		});
		this.add(selectedUser);
		this.setTitle("File Transfer");
		this.setSize(400, 120);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		closeFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String sender = username;
				String reciever = listOnlUsers.getSelectedItem().toString();
				try {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, JAVA, TXT, ZIP", "pdf",
							"java", "txt", "zip");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(fileTransfer);
					String type = chooser.getSelectedFile().getName();
					if (returnVal == JFileChooser.APPROVE_OPTION
							&& (type.substring(type.lastIndexOf(".") + 1).toLowerCase().equals("pdf")
									|| type.substring(type.lastIndexOf(".") + 1).toLowerCase().equals("java")
									|| type.substring(type.lastIndexOf(".") + 1).toLowerCase().equals("txt")
									|| type.substring(type.lastIndexOf(".") + 1).toLowerCase().equals("zip"))) {
						clientOut.println("FILE" + sender + ";" + chooser.getSelectedFile().getName() + "*" + ip
								+ "|" + filePort + "," + reciever);
						filename = chooser.getSelectedFile().getAbsolutePath(); 																						
																																																																																						
					} else {
						new FrameNotif("Invalid File Location or File Type!");
					}
				} catch (NullPointerException npe) {
					new FrameNotif("No proper file was selected.");
				}
				setVisible(false);
			}
		});
		}

}
