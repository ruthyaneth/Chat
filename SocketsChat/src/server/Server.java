/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Server extends JFrame {

    ServerSocket serverSocket = null;
    PrintWriter serverOut = null;
    PrintWriter serverIn = null;

    private static HashSet<PrintWriter> accounts = new HashSet<PrintWriter>();
    private static LinkedList<String> listOnl = new LinkedList();                         
    private LinkedList<String> usersBlock = new LinkedList();                                     

    int filePort;

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;                        
    boolean isPrivate = false;

    String privateChatPartner = null;
    String ip;
    String status = "Available";    //UI
    String nickName = "Admin";
    String fileName = null;
    String[] arrayEmotions = {"<SMILE>", "<BSMILE>", "<SAD>", "<CRY>", "<TOUNGE>", "<ANGEL>", "<DEVIL>", "<CONFUSE>", "<WINKING>", "<SURPRISE>"};  //UI

    JFrame privateChatFrame = new JFrame();                                              
    JFrame notificationFrame = new JFrame();

    JPanel blockPanel = new JPanel(new FlowLayout());

    JLabel privateChatLabel = new JLabel("Private Chat");
    JLabel labelUser = new JLabel();
    JLabel notiMess = new JLabel();
    ArrayList<JLabel> listIcon = new ArrayList<JLabel>();

    JTextPane chatArea = new JTextPane();
    JPanel emotionPanel = new JPanel(new GridLayout(0, 10, 0, 0));

    JTextArea privateChatTextArea = new JTextArea();

    JTextField privateChatTextField = new JTextField();                                 
    JTextField typeArea = new JTextField();

    JButton privateChatCloseButton = new JButton("Close");
    JButton blockUserButton = new JButton("Block");
    JButton unBlockUserButton = new JButton("Unblock");
    JButton sendMessageButton = new JButton("Send");

    JMenuBar menu = new JMenuBar();

    Icon atIcon = new ImageIcon("src/send-file-xxl.png");
    Icon atIcon1 = new ImageIcon("src/Mic-Icon-Square.png");
    Icon atIcon2 = new ImageIcon("src/video.png");
    JMenuItem fileTransfer = new JMenuItem("Send File", atIcon);
    JMenuItem voiceCall = new JMenuItem("Voice Call", atIcon1);
    JMenuItem videoCall = new JMenuItem("Video Call", atIcon2);

    DefaultListModel model = new DefaultListModel();    

    JList online = new JList(model);   

    JScrollPane listScrollPane = new JScrollPane(online); 
    JScrollPane chatScroll = new JScrollPane(chatArea);    
    JScrollPane privateChatScroll = new JScrollPane(privateChatTextArea);  

    File fileFile = null;

    StyledDocument doc = null;
    Style regular = null;
    Style def = null;
    Style notification = null;

    ServerSocket fileSocket;

    public Server() throws Exception {     //chat window
        this.fileSocket = new ServerSocket(0);
        this.filePort = fileSocket.getLocalPort();
        this.ip = InetAddress.getLocalHost().getHostAddress();

        for (int i = 1; i < 11; i++) {
            JLabel cell = new JLabel() {
                {
                    setVisible(true);
                }
            };      
            listIcon.add(cell);
            cell.setIcon(new ImageIcon("src/" + i + ".png"));
            emotionPanel.add(cell);
            listIcon.get(i - 1).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int j = 0; j < listIcon.size(); j++) {
                        if (e.getSource() == listIcon.get(j)) {
                            typeArea.setText(typeArea.getText() + arrayEmotions[j]);
                            typeArea.requestFocusInWindow();
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
        }
        // setting up all text styles
        doc = chatArea.getStyledDocument();
        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        regular = doc.addStyle("regular", def);
        notification = doc.addStyle("notification", regular);
        StyleConstants.setBold(notification, true);
        StyleConstants.setItalic(notification, true);

        sendMessageButton.setBackground(new Color(67, 96, 156));
        sendMessageButton.setForeground(Color.WHITE);

        blockUserButton.setBackground(new Color(67, 96, 156));
        blockUserButton.setForeground(Color.WHITE);
        blockUserButton.setPreferredSize(new Dimension(80, 25));

        unBlockUserButton.setBackground(new Color(67, 96, 156));
        unBlockUserButton.setForeground(Color.WHITE);
        unBlockUserButton.setPreferredSize(new Dimension(80, 25));

        this.getContentPane().setBackground(new Color(228, 245, 237));
        emotionPanel.setBackground(new Color(228, 245, 237));
        blockPanel.setBackground(new Color(228, 245, 237));

        privateChatFrame();

        this.setLayout(new GridBagLayout());
        GridBagConstraints mainFrameConstraint = new GridBagConstraints();

        labelUser.setText("Nickname: " + nickName + "                   Status: " + status);
        mainFrameConstraint.gridx = 0;
        mainFrameConstraint.gridy = 0;
        mainFrameConstraint.anchor = GridBagConstraints.SOUTH;
        mainFrameConstraint.insets = new Insets(5, 5, 1, 5);
        this.add(new JLabel("Online Users"), mainFrameConstraint);

        mainFrameConstraint.gridx = 1;
        mainFrameConstraint.insets = new Insets(0, 5, 1, 0);
        mainFrameConstraint.anchor = GridBagConstraints.SOUTH;
        this.add(labelUser, mainFrameConstraint);

        mainFrameConstraint.fill = GridBagConstraints.HORIZONTAL;
        mainFrameConstraint.ipady = 300;
        mainFrameConstraint.ipadx = 400;
        mainFrameConstraint.gridx = 1;
        mainFrameConstraint.gridy = 2;
        mainFrameConstraint.insets = new Insets(0, 5, 0, 5);
        this.add(chatScroll, mainFrameConstraint);

        mainFrameConstraint.fill = GridBagConstraints.HORIZONTAL;
        mainFrameConstraint.weightx = 0.5;
        mainFrameConstraint.ipady = 300;
        mainFrameConstraint.ipadx = 20;
        mainFrameConstraint.gridx = 0;
        mainFrameConstraint.insets = new Insets(0, 5, 0, 5);
        online.setModel(model);
        this.add(listScrollPane, mainFrameConstraint);

        mainFrameConstraint.fill = GridBagConstraints.HORIZONTAL;
        mainFrameConstraint.ipady = 10;
        mainFrameConstraint.ipadx = 40;
        mainFrameConstraint.gridx = 1;
        mainFrameConstraint.gridy = 3;
        mainFrameConstraint.insets = new Insets(0, 3, 5, 0);
        this.add(emotionPanel, mainFrameConstraint);

        mainFrameConstraint.fill = GridBagConstraints.HORIZONTAL;
        mainFrameConstraint.ipady = 40;
        mainFrameConstraint.ipadx = 200;
        mainFrameConstraint.weighty = 2;
        mainFrameConstraint.gridy = 4;
        mainFrameConstraint.insets = new Insets(0, 4, 5, 5);
        this.add(typeArea, mainFrameConstraint);

        mainFrameConstraint.fill = GridBagConstraints.HORIZONTAL;
        mainFrameConstraint.ipady = 1;
        mainFrameConstraint.ipadx = 40;
        mainFrameConstraint.gridx = 0;
        mainFrameConstraint.gridy = 3;
        mainFrameConstraint.insets = new Insets(3, 3, 3, 3);
        this.add(blockPanel, mainFrameConstraint);

        blockPanel.add(blockUserButton);
        blockPanel.add(unBlockUserButton);

        mainFrameConstraint.fill = GridBagConstraints.HORIZONTAL;
        mainFrameConstraint.ipady = 30;
        mainFrameConstraint.ipadx = 30;
        mainFrameConstraint.gridy = 4;
        mainFrameConstraint.insets = new Insets(0, 4, 5, 4);
        this.add(sendMessageButton, mainFrameConstraint);

        menu.add(fileTransfer);
        menu.add(voiceCall);
        menu.add(videoCall);

        this.setJMenuBar(menu);
        this.setTitle("Admin Chat Room");
        this.setVisible(true);
        this.setSize(630, 515);
        chatArea.setEditable(false);
        this.setVisible(true);
        model.addElement("Admin");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    writeFile();
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        MyActionListener al = new MyActionListener();
        MouseAdapter ma = new MouseAdapter();
        sendMessageButton.addActionListener(al);
        typeArea.addActionListener(al);
        privateChatTextField.addActionListener(al);
        privateChatCloseButton.addActionListener(al);
        fileTransfer.addActionListener(al);
        blockUserButton.addMouseListener(ma);
        unBlockUserButton.addMouseListener(ma);
        online.addMouseListener(ma);

        voiceCall.addMouseListener(ma);
        videoCall.addMouseListener(ma);
        try {
            serverSocket = new ServerSocket(60000);
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            System.out.println(serverSocket.getLocalPort());
            doc.insertString(doc.getLength(), "* Your chat room has been created successfully * \n", doc.getStyle("notification"));
            while (true) {          //accept all connection requests
                Runnable client = new Client(serverSocket.accept(), this);
                Thread clientThread = new Thread(client);
                clientThread.start();
            }
        } catch (IOException e) {
        } finally {
            serverSocket.close();
        }
    }

    public void openNotiFrame(String noti) {
        notificationFrame.setLayout(new GridBagLayout());
        notiMess.setText(noti);
        notiMess.setFont(new Font("Serif", Font.BOLD, 17));
        notiMess.setForeground(Color.RED);
        GridBagConstraints notiFrameConstraint = new GridBagConstraints();
        notiFrameConstraint.insets = new Insets(10, 10, 10, 10);
        notificationFrame.add(notiMess);
        notificationFrame.setBackground(new Color(253, 170, 158));
        notificationFrame.setVisible(true);
        notificationFrame.setLocationRelativeTo(null);
        notificationFrame.setSize(500, 100);
        notificationFrame.setTitle("Notification");
        notificationFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void privateChatFrame() {     //UI: Private Chat Window
        privateChatFrame.setLayout(new BorderLayout());
        privateChatFrame.add(privateChatLabel,BorderLayout.NORTH);
        privateChatTextArea.setEditable(false);
        privateChatFrame.add(privateChatScroll,BorderLayout.CENTER);
        JPanel privateChatPanel = new JPanel(new BorderLayout());
        privateChatFrame.add(privateChatPanel,BorderLayout.SOUTH);
        privateChatPanel.add(privateChatTextField,BorderLayout.CENTER);
        privateChatPanel.add(privateChatCloseButton,BorderLayout.SOUTH);
        privateChatFrame.setVisible(false);
        privateChatFrame.setSize(300, 200);
        privateChatFrame.setResizable(false);
        privateChatFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void printText(JTextPane actionTextPane, String actionText) throws BadLocationException {
        actionTextPane.setOpaque(false);
        Pattern pattern = Pattern.compile("<SMILE>|<BSMILE>|<SAD>|<CRY>|<TOUNGE>|<ANGEL>|<DEVIL>|<CONFUSE>|<WINKING>|<SURPRISE>");
        Matcher matcher = pattern.matcher(actionText);
        Style s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        int previousMatch = 0;
        while (matcher.find()) {
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

    class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendMessageButton && !typeArea.getText().equals("")) {
                for (PrintWriter outPrint : accounts) {
                    outPrint.println("PUBLIC" + nickName + "|" + typeArea.getText());
                }
                try {
                    printText(chatArea, nickName + ": " + typeArea.getText());
                } catch (BadLocationException e1) {
                    System.err.println(e1.getMessage());
                }
                typeArea.setText("");
            } else if (!typeArea.getText().equals("")) {
                for (PrintWriter outPrint : accounts) {
                    outPrint.println("PUBLIC" + nickName + "|" + typeArea.getText());
                }
                try {
                    printText(chatArea, nickName + ": " + typeArea.getText());
                } catch (BadLocationException e1) {
                    System.err.println(e1.getMessage());
                }
                typeArea.setText("");
            }
            if (!privateChatTextField.getText().equals("")) {
                for (PrintWriter outPrint : accounts) {
                    outPrint.println("PRIVATE" + privateChatPartner + "|" + nickName + "," + privateChatTextField.getText());
                }
                privateChatTextArea.append("You: " + privateChatTextField.getText() + "\n");
                privateChatTextField.setText("");
            }
            if (e.getSource() == privateChatCloseButton) {
                // send DELETEPRIVATE signal.
                for (PrintWriter outPrint : accounts) {
                    outPrint.println("DELETEPRIVATE" + privateChatPartner);
                }
                privateChatTextField.setText("");
                isPrivate = false;
                privateChatTextArea.setText("");
                privateChatFrame.dispose();
            }
            if (e.getSource() == fileTransfer) {
                final JFrame inputframe = new JFrame();
                JPanel selectedUser = new JPanel();
                JLabel req = new JLabel("Select User: ");
                model.removeElement(nickName);
                String[] onl = new String[model.getSize()];
                for (int i = 0; i < model.getSize(); i++) {
                    onl[i] = model.getElementAt(i).toString();
                }
                final JComboBox listOnlUsers = new JComboBox(onl);
                selectedUser.add(req);
                JButton closeFrame = new JButton("OK");
                model.addElement(nickName);
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
                inputframe.setTitle("File Sharing");
                inputframe.add(selectedUser);
                inputframe.pack();
                inputframe.setVisible(true);
                inputframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                closeFrame.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String sender = nickName;
                        try {
                            String reciever = listOnlUsers.getSelectedItem().toString();
                            try {
                                JFileChooser chooser = new JFileChooser();
                                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, JAVA, TXT, ZIP", "pdf", "java", "txt", "zip");
                                chooser.setFileFilter(filter);
                                double returnVal = chooser.showOpenDialog(fileTransfer);
                                System.out.println(ip);
                                String type = chooser.getSelectedFile().getName();
                                System.out.print(filePort);
                                if (returnVal == JFileChooser.APPROVE_OPTION && (type.substring(type.lastIndexOf(".") + 1).equals("pdf") || type.substring(type.lastIndexOf(".") + 1).equals("java") || type.substring(type.lastIndexOf(".") + 1).equals("txt") || type.substring(type.lastIndexOf(".") + 1).equals("zip"))) {
                                    for (PrintWriter outPrint : accounts) {
                                        outPrint.println("FILE" + sender + ";" + chooser.getSelectedFile().getName() + "*" + ip + "|" + filePort + "," + reciever);
                                    }
                                    fileName = chooser.getSelectedFile().getAbsolutePath();     //save full path if "ACCEPTFILE" request
                                } else {
                                    openNotiFrame("Invalid File Location or File Type!");
                                }
                            } catch (NullPointerException npe) {
                                openNotiFrame("No file was selected.");
                            }
                        } catch (NullPointerException e) {
                            openNotiFrame("No user was selected.");
                        }
                        inputframe.setVisible(false);
                    }
                });
            }
        }
    }

    class MouseAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2 && me.getSource() == online && !nickName.equals(online.getSelectedValue()) && !isPrivate) {
                isPrivate = true;
                privateChatPartner = (String) online.getSelectedValue();
                privateChatFrame.setTitle("Nickname:" + nickName);
                privateChatLabel.setText("Private chat with " + privateChatPartner);
                for (PrintWriter outPrint : accounts) {
                    outPrint.println("PRIVATE" + privateChatPartner + "|" + nickName + ",Hey what's up! It's me - ");
                }
                privateChatFrame.setVisible(true);
            }
            if (me.getSource() == blockUserButton) {
                LinkedList<String> account = new LinkedList<String>();
                File file = new File("account.txt");
                updateAccount(file, account);
                final JFrame inputframe = new JFrame();
                JPanel selectedUser = new JPanel();
                JLabel req = new JLabel("Select User: ");
                String[] allUsers = new String[account.size()];
                for (int i = 0; i < account.size(); i++) {
                    allUsers[i] = account.get(i).toString().substring(0, account.get(i).toString().indexOf("|", 0));
                }
                final JComboBox listOnlUsers = new JComboBox(allUsers);
                selectedUser.add(req);
                JButton closeFrame = new JButton("OK");
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
                inputframe.add(selectedUser);
                inputframe.setTitle("Block User");
                inputframe.pack();
                inputframe.setVisible(true);
                inputframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                closeFrame.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            String user = listOnlUsers.getSelectedItem().toString();
                            if (user != null && !usersBlock.contains(user)) {
                                usersBlock.add(user);
                                for (PrintWriter outPrint : accounts) {
                                    outPrint.println("ADMINBLOCKED" + user);
                                }
                                String noti = user + " has been blocked successfully!";
                                openNotiFrame(noti);
                            } else {
                                String noti = user + " has been blocked before";
                                openNotiFrame(noti);
                            }
                        } catch (Exception e) {
                            openNotiFrame("No user to block.");
                        }
                        inputframe.setVisible(false);
                    }
                });
            }

            if (me.getSource() == unBlockUserButton) {
                LinkedList<String> account = new LinkedList<String>();
                File file = new File("account.txt");
                updateAccount(file, account);
                final JFrame inputframe = new JFrame();
                JPanel selectedUser = new JPanel();
                JLabel req = new JLabel("Select User: ");
                String[] allUsers = new String[account.size()];
                for (int i = 0; i < account.size(); i++) {
                    allUsers[i] = account.get(i).toString().substring(0, account.get(i).toString().indexOf("|", 0));
                }
                final JComboBox listOnlUsers = new JComboBox(allUsers);
                selectedUser.add(req);
                JButton closeFrame = new JButton("OK");
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
                inputframe.add(selectedUser);
                inputframe.setTitle("Unblock User");
                inputframe.pack();
                inputframe.setVisible(true);
                inputframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                closeFrame.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String user = listOnlUsers.getSelectedItem().toString();
                        int flag = 0;
                        if (user != null) {
                            if (usersBlock.size() >= 1) {
                                for (int i = 0; i < usersBlock.size(); i++) {
                                    if (user.equals(usersBlock.get(i))) {
                                        usersBlock.remove(user);
                                        flag++;
                                        String noti = user + " has been unblocked successfully ";
                                        openNotiFrame(noti);
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    String noti = user + " has not been blocked yet.";
                                    openNotiFrame(noti);
                                }
                            } else {
                                openNotiFrame(" User has not been blocked yet.");
                            }
                        } else {
                            openNotiFrame("Username cannot be empty.");
                        }
                        inputframe.setVisible(false);
                    }
                });
            }

            if (me.getSource() == voiceCall) {
                final JFrame inputframe = new JFrame();
                JPanel selectedUser = new JPanel();
                JLabel req = new JLabel("Select User: ");
                model.removeElement(nickName);
                String[] onl = new String[model.getSize()];
                for (int i = 0; i < model.getSize(); i++) {
                    onl[i] = model.getElementAt(i).toString();
                }
                final JComboBox listOnlUsers = new JComboBox(onl);
                selectedUser.add(req);
                JButton closeFrame = new JButton("OK");
                model.addElement(nickName);
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
                inputframe.setTitle("Audio Chat");
                inputframe.add(selectedUser);
                inputframe.pack();
                inputframe.setVisible(true);
                inputframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                closeFrame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        inputframe.setVisible(false);
                        try {
                            String user = listOnlUsers.getSelectedItem().toString();
                            try {
                                final ServerSocket ss = new ServerSocket(0);
                                String ip = ss.getInetAddress().getLocalHost().getHostAddress();
                                int port = ss.getLocalPort();
                                for (PrintWriter outPrint : accounts) {
                                    outPrint.println("VOICECHAT" + user + ";" + ip + "*" + port);
                                }
                                final Socket conn = ss.accept();
                                ss.close();
                                final JFrame callFr = new JFrame();
                                JPanel callPn = new JPanel();
                                JButton endCall = new JButton("End Call");
                                callPn.add(endCall);
                                callFr.add(callPn);
                                callFr.pack();
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
                                // Formatting audio data, start microphone recording
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
                                                openNotiFrame("Data communication error! Exit!");
                                                break;
                                            }
                                        }
                                    } else {
                                        conn.close();
                                        ss.close();
                                        openNotiFrame("Client disconnected!");
                                        break;
                                    }
                                }
                                microphone.close();
                                microphone.stop();
                                System.out.println("IT IS DONE.");

                            } catch (Exception ex) {
                                openNotiFrame("Error!");
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                ex.printStackTrace();
                            }
                        } catch (Exception e) {
                            inputframe.setVisible(false);
                            openNotiFrame("No user was selected");
                        }
                    }
                });
            }

        }

        @Override
        public void mousePressed(MouseEvent me) {}
        public void mouseReleased(MouseEvent me) {}
        public void mouseEntered(MouseEvent me) {}
        public void mouseExited(MouseEvent me) {}

    }

    public void updateAccount(File file, LinkedList<String> account) {   // update all existing accounts
        account.clear();
        try (Scanner input = new Scanner(file)) {
            synchronized (input) {
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    account.add(line);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /////////////////////////// create each single thread to handle each client
    private class Client implements Runnable {
        Socket client;
        Server server;
        private PrintWriter out;
        private BufferedReader in;
        String filename, masterGroup, username;
        LinkedList<String> account = new LinkedList<String>();
        boolean isMaster, checkBlock, isMember = false;
        File file = new File("account.txt");
        LinkedList<String> listBlocked = new LinkedList();

        public Client(Socket client, Server server) {
            this.client = client;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));            // dataIntoServer
                out = new PrintWriter(client.getOutputStream(), true);                              // dataOutFromServer
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    if (input.startsWith("CHECKUSER")) {
                        updateAccount(file, account);
                        String name = input.substring(9, input.indexOf("|", 0));
                        String pass = input.substring(input.indexOf("|", 0) + 1);
                        boolean check = false, checkExist = false;
                        for (String s : listOnl) {
                            if (name.equals(s)) {
                                checkExist = true;
                            }
                        }
                        for (int j = 0; j < usersBlock.size(); j++) {
                            if (name.equals(usersBlock.get(j))) {
                                checkBlock = true;
                            }
                        }
                        for (int i = 0; i < account.size(); i++) {
                            String userCheck = account.get(i).substring(0, account.get(i).indexOf("|", 0));
                            String passCheck = account.get(i).substring(account.get(i).indexOf("|", 0) + 1);

                            if (!checkBlock && !checkExist && userCheck.equals(name) && passCheck.equals(pass)) {
                                out.println("LOGINOK");
                                username = name;
                                accounts.add(out);
                                listOnl.add(name);
                                check = true;
                                break;
                            }
                        }
                        if (checkExist) {
                            out.println("ISONLINE");
                        } else if (checkBlock) {
                            out.println("ISBLOCKED");
                        } else if (!check) {
                            out.println("INCORRECT");
                        }
                    }
                    //END CHECK LOGIN

                    //REGISTER SIGNUP
                    if (input.startsWith("REGISTER")) {
                        System.out.println(input);
                        updateAccount(file, account);
                        System.out.println(input.substring(8, input.indexOf("|", 0)) + "   " + input.substring(input.indexOf("|", 0) + 1));
                        int flag = 0;
                        for (String acc : account) {
                            if (acc.substring(0, acc.indexOf("|", 0)).equals(input.substring(8, input.indexOf("|", 0)))) {
                                out.println("DUPLICATE");
                                flag++;
                                break;
                            }
                        }
                        if (flag==0) {   // write to account to account.txt file
                            try {
                                BufferedWriter fbw = new BufferedWriter(new FileWriter("account.txt", true));
                                fbw.write(input.substring(8, input.indexOf("|", 0)).trim() + "|" + String.valueOf(input.substring(input.indexOf("|", 0) + 1).trim()));
                                fbw.newLine();
                                fbw.close();
                                out.println("REGISTEROK");
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                    //END REGISTER

                    byte[] file = new byte[8000000];
                    if (input == null) {
                        return;
                    } else if (input.startsWith("REFRESHONL")) {       // refresh current online users everytime there is connection/disconnection is made
                        for (int i = 0; i < model.size(); i++) {
                            out.println("ONLINE" + model.get(i));
                        }
                    } else if (input.startsWith("FILE")) {      // starting request for sharing file
                        String sender = input.substring(4, input.indexOf(";"));
                        String remoteip = input.substring(input.indexOf("*") + 1, input.indexOf("|"));
                        String onPort = input.substring(input.indexOf("|") + 1, input.indexOf(","));
                        String reciever = input.substring(input.indexOf(",") + 1);
                        String filepath = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
                        int port = Integer.parseInt(onPort);
                        if (reciever.equals("Admin")) {
                            int confirm = JOptionPane.showConfirmDialog(null, sender + " would like to transfer file to you, do you accept ?", "File Transfer", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                filename = filepath;
                                out.println("ACCEPTFILE" + input.substring(4));
                            } else {
                                out.println("REJECTFILE" + input.substring(4));
                            }
                        } else {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println(input);          //forward signal ("FILE"+.....) to all clients
                            }
                        }
                    } else if (input.startsWith("VOICECHAT")) {
                        String user = input.substring(9, input.indexOf(";"));
                        String ip = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
                        int port = Integer.parseInt(input.substring(input.indexOf("*") + 1));
                        try {
                            if (user.equals("Admin")) {
                                openNotiFrame("Server starts voice chat with you");
                                final Socket conn = new Socket(ip, port);
                                final JFrame callFr = new JFrame();
                                JPanel callPn = new JPanel();
                                JButton endCall = new JButton("End Call");
                                callPn.add(endCall);
                                callFr.add(callPn);
                                callFr.pack();
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
                                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
                                                openNotiFrame("Data communication error! Exit!");
                                                break;
                                            }
                                        }
                                    } else {
                                        conn.close();
                                        openNotiFrame("Server disconnected audio chat!");
                                        break;
                                    }
                                }
                                microphone.close();
                                microphone.stop();
                                System.out.println("IT IS DONE.");
                            } else {
                                for (PrintWriter outPrint : accounts) {
                                    outPrint.println(input);
                                }
                            }
                        } catch (Exception e) {
                            openNotiFrame("Fatal Error!");
                        }
                    } else if (input.startsWith("BEGINFILE")) {     // already accept to recieve file. now recieve file
                        String sender = input.substring(9, input.indexOf(";"));
                        String remoteip = input.substring(input.indexOf("*") + 1, input.indexOf("|"));
                        String onPort = input.substring(input.indexOf("|") + 1, input.indexOf(","));
                        String reciever = input.substring(input.indexOf(",") + 1);
                        String filepath = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
                        int port = Integer.parseInt(onPort);
                        if (reciever.equals("Admin")) {
                            if (filename != null) {
                                doc.insertString(doc.getLength(), "* File transfer from " + sender + " begins * \n", doc.getStyle("notification"));
                                try {
                                    Socket client = new Socket(remoteip, port);
                                    InputStream is = client.getInputStream();
                                    int sizeRead = is.read(file, 0, file.length);;                                    int current;
                                    FileOutputStream fos = new FileOutputStream("ServerDownload/" + filename);    // now truly begin share file
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
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
                                    client.close();
                                    doc.insertString(doc.getLength(), "* File transfer from " + sender + " completed * \n", doc.getStyle("notification"));
                                } catch (Exception e1) {
                                    System.err.println(e1.getMessage());
                                }
                            }
                        } else {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println(input);
                            }
                        }
                    } else if (input.startsWith("ACCEPTFILE")) {    // Accept file signal from target client/server, to server, send back to source client/user wanna send file
                        String sender = input.substring(10, input.indexOf(";"));
                        String remoteip = input.substring(input.indexOf("*") + 1, input.indexOf("|"));
                        String onPort = input.substring(input.indexOf("|") + 1, input.indexOf(","));
                        String reciever = input.substring(input.indexOf(",") + 1);
                        String filepath = input.substring(input.indexOf(";") + 1, input.indexOf("*"));
                        int port = Integer.parseInt(onPort);
                        System.out.println("input: " + input);
                        System.out.println("sender: " + sender);
                        System.out.println("ip: " + remoteip);
                        System.out.println("port: " + port);
                        System.out.println("reciever: " + reciever);
                        if (sender.equals("Admin")) {
                            doc.insertString(doc.getLength(), "* File transfer to " + reciever + " begin * \n", doc.getStyle("notification"));
                            try {
                                ServerSocket server = new ServerSocket(8888);       //open a new socket for sending, reading file.
                                String ip = InetAddress.getLocalHost().getHostAddress();
                                out.println("BEGINFILE" + input.substring(10));       // provide info to targer client
                                Socket client = server.accept();
                                fileFile = new File(fileName);                      // now truly begin share file
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
                                server.close();
                                doc.insertString(doc.getLength(), "* File transfer to " + reciever + " completed * \n", doc.getStyle("notification"));
                            } catch (Exception e) {
                                openNotiFrame(e.getMessage());
                            }
                        } else {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println(input);
                            }
                        }
                    } else if (input.startsWith("REJECTFILE")) {
                        if (input.substring(10, input.indexOf(";")).equals("Admin")) {
                            String noti = input.substring(input.indexOf(",") + 1) + " just declined your file.";
                            openNotiFrame(noti);
                        } else {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println(input);
                            }
                        }

                        //broadcast message
                    } else if (input.startsWith("ACCEPTINVITE")) {
                        masterGroup = input.substring(12);
                        isMember = true;
                    } else if (input.startsWith("REJECTINVITE")) {
                        for (PrintWriter outPrint : accounts) {
                            outPrint.println("REJECTINVITE" + input.substring(12));
                        }
                    } else if (input.startsWith("GROUP")) {               // this happens after user hit "Invite" button
                        isMaster = true;
                        for (PrintWriter outPrint : accounts) {        //send INVITE signal to all users
                            outPrint.println("INVITE" + input.substring(5) + "|" + username);         // send info about target user - (input.substring(5))
                        }
                    } else if (input.startsWith("NOJOIN")) {
                        for (PrintWriter outPrint : accounts) {
                            outPrint.println("NOJOIN" + input.substring(6));
                        }
                    } else if (input.startsWith("CHATMASTER")) {
                        for (PrintWriter outPrint : accounts) {
                            outPrint.println("CHATMES" + username + "|" + username + ": " + input.substring(10));
                        }
                    } else if (input.startsWith("CHATMEMBER")) {
                        if (input.substring(10, input.indexOf("|", 0)).equals(masterGroup)) {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println("CHATMES" + masterGroup + "|" + username + ": " + input.substring(input.indexOf("|", 0) + 1));
                            }
                        }
                    } else if (input.startsWith("CANCELGROUP")) {
                        if (isMaster == true && input.substring(11).equals(username)) {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println("CANCELMASTER" + username);
                            }
                            isMaster = false;
                        } else if (isMember && input.substring(11).equals(masterGroup)) {
                            isMember = false;
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println("CANCELGROUP" + masterGroup + "|" + username);
                            }
                            masterGroup = null;
                        }
                    } else if (input.startsWith("DELETEPRIVATE")) {
                        if (input.substring(13).equals("Admin")) {
                            isPrivate = false;
                            privateChatTextField.setText("");
                            privateChatPartner = null;
                            privateChatTextArea.setText("");
                            privateChatFrame.dispose();
                            openNotiFrame("Private chat has been cancelled.");
                        } else {
                            for (PrintWriter outPrint : accounts) {
                                outPrint.println(input);
                            }
                        }
                    } else if (input.startsWith("BLOCKPRIVATE")) {
                        for (PrintWriter outPrint : accounts) {
                            outPrint.println("BLOCKPRIVATE" + input.substring(12));
                        }
                    } else if (input.startsWith("BLOCKEDGROUP")) {
                        for (PrintWriter outPrint : accounts) {
                            outPrint.println("BLOCKEDGROUP" + input.substring(12));
                        }
                    } else if (input.startsWith("BLOCKUSER")) {
                        if (input.substring(9).equals("Admin")) {
                            out.println("BLOCKEDERROR" + input.substring(9));
                        } else if (listBlocked.size() == 0) {
                            listBlocked.add(input.substring(9));
                            out.println("BLOCKEDSUCCESS" + input.substring(9));
                        } else {
                            for (String s : listBlocked) {
                                if (input.substring(9).equals(s)) {
                                    out.println("BLOCKEDBEFORE" + input.substring(9));
                                }
                            }
                        }
                    } else if (input.startsWith("UNBLOCKUSER")) {
                        boolean check1 = false;
                        if (listBlocked.size() >= 1) {
                            for (String s : listBlocked) {
                                if (input.substring(11).equals(s)) {
                                    listBlocked.remove(input.substring(11));
                                    check1 = true;
                                }
                            }
                        }
                        if (check1) {out.println("UNBLOCKSUCCESS" + input.substring(11));}
                        else
                            out.println("UNBLOCKEDERROR" + input.substring(11));
                    } else if (!accounts.isEmpty()) {
                        double flag = 0;
                        double flag1 = 0;
                        double flag2 = 0;
                        double flag3 = 0;
                        for (String s : usersBlock) {
                            if (username.equals(s)) {
                                checkBlock = true;
                            }
                        }
                        for (PrintWriter outPrint : accounts) {
                            if (checkBlock) {
                                accounts.remove(out);
                                listOnl.remove(username);
                                outPrint.println("ADMINBLOCKED" + username);
                            } else if (input.startsWith("PUBLIC")) {
                                outPrint.println("PUBLIC" + username + "|" + input.substring(6));
                                if (flag == 0) {
                                    printText(chatArea, username + ": " + input.substring(6));
                                    flag++;
                                }
                            } else if (input.startsWith("CONNECTED")) {
                                outPrint.println("CONNECTED" + username);
                                if (flag1 == 0) {
                                    model.addElement(username);
                                    doc.insertString(doc.getLength(), "* " + username + " just joined our chat room * \n", doc.getStyle("notification"));
                                    flag1++;
                                }
                            } else if (input.startsWith("STATUS")) {
                                if (input.contains("Invisible")) {
                                    outPrint.println("INVISIBLE" + username);
                                    model.removeElement(username);
                                } else {
                                    if (!model.contains(username)) {
                                        model.addElement(username);
                                    }
                                    outPrint.println("STATUS" + username + "|" + input.substring(6));
                                }
                                if (flag2 == 0) {
                                    if (!input.contains("Invisible")) {
                                        doc.insertString(doc.getLength(), "* " + username + " just update his status to " + input.substring(6) + " *\n", doc.getStyle("notification"));
                                        flag2++;
                                    }
                                }
                            } else if (input.startsWith("PRIVATE")) {
                                System.out.println(input);
                                String stupidShit = input.substring(7);
                                String partner = stupidShit.substring(0, stupidShit.indexOf(",", 0));
                                String content = stupidShit.substring(stupidShit.indexOf(",", 0) + 1);

                                if (!isPrivate && partner.equals(nickName)) {
                                    privateChatLabel.setText("Private chat with " + username);
                                    privateChatFrame.setTitle(nickName);
                                    privateChatFrame.setVisible(true);
                                    privateChatPartner = username;
                                    if (flag3 == 0) {
                                        privateChatTextArea.append(username + ": " + content + "\n");
                                        flag3++;
                                    }
                                    isPrivate = true;
                                } else if (isPrivate && partner.equals(nickName) && username.equals(privateChatPartner)) {
                                    if (flag3 == 0) {
                                        privateChatTextArea.append(username + ": " + content + "\n");
                                        flag3++;
                                    }
                                } else if (isPrivate && partner.equals(nickName) && !username.equals(privateChatPartner))
                                    outPrint.println("BUSY" + username);
                                else
                                    outPrint.println("PRIVATE" + partner + "|" + username + "," + content);
                            } else if (input.startsWith("BUSY"))
                                outPrint.println("BUSY" + input.substring(4));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("User disconnected");;
            } finally {
                if (out != null) {
                    accounts.remove(out);
                    listOnl.remove(username);
                }
                try {
                    int flag = 0;
                    if (accounts.isEmpty()) {if (username != null) {doc.insertString(doc.getLength(), "* " + username + " just left our chat room * \n", doc.getStyle("notification"));}}
                    for (PrintWriter outPrint : accounts) {
                        if (username != null) {
                            outPrint.println("ADMINBLOCKED" + username);
                        }
                        if (flag == 0) {if (username != null) {doc.insertString(doc.getLength(), "* " + username + " just left our chat room * \n", doc.getStyle("notification"));}flag++;}
                    }
                    model.removeElement(username);
                    client.close();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void writeFile() throws Exception {
        FileWriter outPrint = new FileWriter(new File("ServerChatRecord.txt"));
        outPrint.write(chatArea.getText());
        outPrint.close();
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }
}
