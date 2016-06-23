package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class FrameNotif  extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5983862439860043150L;
	JLabel notiMess = new JLabel();

	public FrameNotif(String noti) {
	 this.setLayout(new GridBagLayout());
     notiMess.setText(noti);
     notiMess.setFont(new Font("Serif", Font.BOLD, 17));
     notiMess.setForeground(Color.RED);
     GridBagConstraints notiFrameConstraint = new GridBagConstraints();
     notiFrameConstraint.insets = new Insets(10, 10, 10, 10);
     this.add(notiMess);
     this.setBackground(new Color(253, 170, 158));
     this.setVisible(true);
     this.setLocationRelativeTo(null);
     this.setSize(500, 100);
     this.setTitle("Notification");
     this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
}
