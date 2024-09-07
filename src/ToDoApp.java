package com.mkyong;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.text.Format;
import java.text.SimpleDateFormat;





public class ToDoApp {
    private final ArrayList<String> taskList;
    private final ArrayList<JCheckBox> checkBoxList;

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "todolistemailclient@gmail.com";
    private static final String PASSWORD = "vebbqsgnrskkeycq";

    private static final String EMAIL_FROM = "todolistemailclient@gmail.com";
    private static final String EMAIL_TO_CC = "";


    public ToDoApp() {
        this.checkBoxList = new ArrayList<JCheckBox>();
        this.taskList = new ArrayList<String>();
        JFrame frame = new JFrame("To Do App");

        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(350, 600));
        westPanel.setLayout(new BorderLayout());
        JPanel eastPanel = new JPanel();

        JLabel projectsLabel = new JLabel("Reflection");
        projectsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        projectsLabel.setFont(new Font("Serif", Font.BOLD, 35));
        projectsLabel.setForeground(Color.BLACK);
        westPanel.add(projectsLabel, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        JTextArea reflectionText = new JTextArea();
        reflectionText.setPreferredSize(new Dimension(340, 500));
        reflectionText.setLineWrap(true);
        reflectionText.setWrapStyleWord(true);

        JPanel westSouthPanel = new JPanel();
        westSouthPanel.setLayout(new FlowLayout());
        JTextField enterEmail = new JTextField("Enter Email ", 10);

        westSouthPanel.add(enterEmail);
        JButton emailButtonWest = new JButton("Email");
        emailButtonWest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String body = "To Do:";
                for (int l = 0; l < taskList.size(); l++) {
                    body += "\n" + "-" + taskList.get(l);
                }
                body += "\n \n" + "Notes: " + reflectionText.getText();

                Format f = new SimpleDateFormat("MM/dd/yy");
                String subject = "To Do List " + f.format(new Date()) + ":";

                sendEmail(enterEmail.getText(), body, subject);
            }
        });
        westSouthPanel.add(emailButtonWest);
        westPanel.add(westSouthPanel, BorderLayout.SOUTH);


        textPanel.add(reflectionText);
        westPanel.add(textPanel, BorderLayout.CENTER);

        frame.add(westPanel, BorderLayout.WEST);

        JTextField enterTask = new JTextField("Add a Task ", 25);
        enterTask.setPreferredSize(new Dimension(25, 35));
        eastPanel.setLayout(new BorderLayout());
        eastPanel.setBackground(Color.WHITE);
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout());

        enterTask.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    taskList.add(enterTask.getText());
                    enterTask.setText("");
                    System.out.println(taskList);

                    checkBoxList.add(new JCheckBox(taskList.get(taskList.size() - 1)));
                    checkBoxPanel.add(checkBoxList.get(checkBoxList.size()-1));
                    checkBoxPanel.setBackground(Color.WHITE);
                    checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

                    eastPanel.add(checkBoxPanel, BorderLayout.CENTER);

                    SwingUtilities.updateComponentTreeUI(frame);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JPanel eastSouthPanel = new JPanel();
        eastSouthPanel.setLayout(new FlowLayout());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int x = 0; x < checkBoxList.size(); x++) {
                    if (checkBoxList.get(x).isSelected()) {
                        checkBoxList.remove(x);
                        taskList.remove(x);
                        x = -1;
                    }

                }
                checkBoxPanel.removeAll();
                for (JCheckBox jCheckBox : checkBoxList) {
                    checkBoxPanel.add(jCheckBox);
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        eastSouthPanel.add(deleteButton);
        eastSouthPanel.add(enterTask);
        eastSouthPanel.setBackground(Color.WHITE);
        eastPanel.add(eastSouthPanel, BorderLayout.SOUTH);

        JLabel tasksLabel = new JLabel("Tasks");
        tasksLabel.setHorizontalAlignment(SwingConstants.LEFT);
        tasksLabel.setFont(new Font("Serif", Font.BOLD, 35));
        tasksLabel.setForeground(Color.RED);
        eastPanel.add(tasksLabel, BorderLayout.NORTH);


        frame.add(eastPanel);
        frame.repaint();


        //4. Size the frame.
        frame.setSize(800, 600);
        frame.setResizable(false);


        //5. Show it.
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        ToDoApp newApp = new ToDoApp();
    }

    public static void sendEmail(String emailTo, String emailBody, String subject) {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587"); // default port 25
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {

            // from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

            // to
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailTo, false));

            // cc
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

            // subject
            msg.setSubject(subject);

            // content
            msg.setText(emailBody);

            msg.setSentDate(new Date());

            // Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            // connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

            // send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

}

/* Resources:
https://mkyong.com/java/java-how-to-send-email/
https://docs.oracle.com/javase/8/docs/api/javax/swing/JCheckBox.html
https://www.tutorialspoint.com/format-date-with-simpledateformat-mm-dd-yy-in-java#:~:text=Format%20date%20with%20SimpleDateFormat('MM%2Fdd%2Fyy')%20in%20Java&text=%2F%2F%20displaying%20date%20Format%20f,Current%20Date%20%3D%20%22%2BstrDate)%3B
https://www.youtube.com/watch?v=A7HAB5whD6I

* */