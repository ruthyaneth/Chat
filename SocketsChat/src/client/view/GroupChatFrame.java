package client.view;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class GroupChatFrame extends JFrame {
	
	private static final long serialVersionUID = 8795554980122164104L;
	private JPanel groupChatPanel;
	private JButton inviteGroupChatButton;
	private JLabel welcomeGroupChatLabel;
	JScrollPane groupChatScroll ;
	JTextPane groupChatTextPanel = new JTextPane();
	JTextField groupTypeArea = new JTextField();
	JButton groupCloseButton = new JButton("Close group");
	
	public GroupChatFrame() {
		super();
		addGroupChatPanel();
		addInviteGroupChatButton();
		addWelcomeGroupChatLabel();
		groupChatScroll = new JScrollPane(groupChatTextPanel);
	}
	private void addGroupChatPanel(){
		groupChatPanel = new JPanel();
		this.add(groupChatPanel);
	}
	private void addInviteGroupChatButton(){
		 inviteGroupChatButton = new JButton();
	}
	private void addWelcomeGroupChatLabel(){
		welcomeGroupChatLabel = new JLabel();
		groupChatPanel.add(welcomeGroupChatLabel);
	}
	public JPanel getGroupChatPanel() {
		return groupChatPanel;
	}
	
	public JLabel getWelcomeGroupChatLabel() {
		return welcomeGroupChatLabel;
	}
	public JButton getInviteGroupChatButton() {
		return inviteGroupChatButton;
	}
	
	public JScrollPane getGroupChatScroll() {
		return groupChatScroll;
	}
	public void setGroupChatScroll(JScrollPane groupChatScroll) {
		this.groupChatScroll = groupChatScroll;
	}
	public JTextPane getGroupChatTextPanel() {
		return groupChatTextPanel;
	}
	public void setGroupChatTextPanel(JTextPane groupChatTextPanel) {
		this.groupChatTextPanel = groupChatTextPanel;
	}
	public void setGroupChatPanel(JPanel groupChatPanel) {
		this.groupChatPanel = groupChatPanel;
	}
	
	public JTextField getGroupTypeArea() {
		return groupTypeArea;
	}
	public void setGroupTypeArea(JTextField groupTypeArea) {
		this.groupTypeArea = groupTypeArea;
	}
	public void openChatGroupFrame() {
		this.setLayout(new BorderLayout());
		groupChatPanel.add(welcomeGroupChatLabel);
		groupChatPanel.add(inviteGroupChatButton);
		this.add(groupChatPanel, BorderLayout.NORTH); 

		this.add(groupChatScroll, BorderLayout.CENTER); 
																
		JPanel openChatPanel = new JPanel(new BorderLayout());
		this.add(openChatPanel, BorderLayout.SOUTH);
		openChatPanel.add(groupTypeArea, BorderLayout.CENTER);
		groupChatTextPanel.setEditable(false);
		openChatPanel.add(groupCloseButton, BorderLayout.SOUTH);
		this.setVisible(false);
		this.setTitle("Group chat");
		this.setSize(400, 330);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		inviteGroupChatButton.setText(handlerProperties.getProperty(ConstantsView.INVITE_GROUP_CHAT_BUTTON));
	}
}
