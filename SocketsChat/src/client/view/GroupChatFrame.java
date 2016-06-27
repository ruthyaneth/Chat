package client.view;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class GroupChatFrame extends JFrame {
	
	private static final long serialVersionUID = 8795554980122164104L;
	private JPanel groupChatPanel;
	private JButton inviteGroupChatButton;
	private JLabel welcomeGroupChatLabel;
	
	public GroupChatFrame() {
		super();
		addGroupChatPanel();
		addInviteGroupChatButton();
		addWelcomeGroupChatLabel();
	}
	private void addGroupChatPanel(){
		groupChatPanel = new JPanel();
		this.add(groupChatPanel);
	}
	private void addInviteGroupChatButton(){
		 inviteGroupChatButton = new JButton();
		 groupChatPanel.add(inviteGroupChatButton);
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
	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		inviteGroupChatButton.setText(handlerProperties.getProperty(ConstantsView.INVITE_GROUP_CHAT_BUTTON));
	}
}
