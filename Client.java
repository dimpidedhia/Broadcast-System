package signinsignup;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.*;

import java.net.Socket;
import javax.swing.*;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Client signing IN");
        Socket soc = new Socket("127.0.0.1", 9081);
        PrintWriter nos = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                soc.getOutputStream()
                        )
                ), true);
        BufferedReader nis = new BufferedReader(
                new InputStreamReader(
                        soc.getInputStream()
                )
        );
        SignInFrame l = new SignInFrame();
        l.signInFrame(nos, "3");
       
        String check = nis.readLine();
        
        while (true) {
            if (check.equals("signedup")) {
                
                l.signInFrame(nos, "cong");
                
            }
            else if (check.equals("Yes")) {
                String name = nis.readLine();
               
                ChatFrame cf = new ChatFrame(nos, nis, name);
                cf.chatFrame();
            } else {
                if (check.equals("1") || check.equals("0")) {
                    l.signInFrame(nos, check);
                } else {
                    break;
                }

            }
            
            check = nis.readLine();
        }
        
        System.out.println("Client signing OFF");
        nos.close();
        System.exit(1);

    }
}

class ChatFrame extends JFrame {

    PrintWriter nos;
    BufferedReader nis;
    String name;

    public ChatFrame(PrintWriter nos, BufferedReader nis, String name) {
        this.nos = nos;
        this.nis = nis;
        this.name = name;
    }

    public void chatFrame() throws Exception {
        
        JFrame f1 = new JFrame("Chat Window");
        JButton b1 = new JButton("Send");
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        JTextField tf = new JTextField(20);
        JPanel p1 = new JPanel();
        p1.add(tf);
        p1.add(b1);
        f1.add(ta);
        f1.add(BorderLayout.SOUTH, p1);
        ChatListener l1 = new ChatListener(tf, nos, name);
        b1.addActionListener(l1);
        tf.addActionListener(l1);
        f1.setSize(600, 600);
        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ta.append(name + " logged in \n");
        String str = nis.readLine();
        while (!str.equals("End")) {
            ta.append(str + "\n");
            str = nis.readLine();
        }

    }
}

class ChatListener implements ActionListener {

    JTextField tf;
    PrintWriter nos;
    String name;

    public ChatListener(JTextField tf, PrintWriter nos, String name) {
        this.tf = tf;
        this.nos = nos;
        this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = tf.getText();
        nos.println(name + ": " + str);
        tf.setText("");
    }

}

class SignInFrame extends JFrame {

    public void signInFrame(PrintWriter nos, String count) throws Exception {
        JFrame f1 = new JFrame("SignIn Window");
        f1.setSize(400, 400);
        JTextField tf1 = new JTextField(10);
        JTextField tf2 = new JTextField(10);
        JButton b1 = new JButton("SignIn");
        JButton b2 = new JButton("SignUp");
        JLabel l1 = new JLabel("Name");
        JLabel l2 = new JLabel("Password");
        JLabel l3 = new JLabel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        if (count.equals("1")) {
            l3 = new JLabel("Wrong Username or Password. You still have " + count + " chance");
        }
        if (count.equals("0")) {
            l3 = new JLabel("Wrong Username or Password. This is your last chance");
        }
        if (count.equals("cong")) {
            
            l3 = new JLabel("You have successfully SignedIn");
        }
        f1.add(p1, BorderLayout.NORTH);
        f1.add(p2, BorderLayout.CENTER);
        f1.add(p3, BorderLayout.SOUTH);
        p1.add(l1, BorderLayout.WEST);
        p1.add(tf1, BorderLayout.EAST);
        p2.add(l2, BorderLayout.WEST);
        p2.add(tf2, BorderLayout.EAST);
        p3.add(b1, BorderLayout.WEST);
        p3.add(b2, BorderLayout.EAST);
        if (count.equals("1") || count.equals("0")) {
            p3.add(l3, BorderLayout.SOUTH);
        }
        if(count.equals("cong"))
        {
            p3.add(l3, BorderLayout.SOUTH);
        }
        tf1.setEditable(true);
        tf2.setEditable(true);
        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SignInListener s1 = new SignInListener(tf1, tf2, nos, f1);
        SignUpListener s2 = new SignUpListener(nos, f1);
        b1.addActionListener(s1);
        b2.addActionListener(s2);
        tf2.addActionListener(s1);
        tf1.addActionListener(s1);
        
    }
}

class SignInListener implements ActionListener {

    JTextField ta1;
    JTextField ta2;
    PrintWriter nos;
    JFrame f1;

    SignInListener(JTextField ta1, JTextField ta2, PrintWriter nos, JFrame f1) {
        this.ta1 = ta1;
        this.ta2 = ta2;
        this.nos = nos;
        this.f1 = f1;

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String name = ta1.getText();
        String psw = ta2.getText();
        nos.println("signin");
        nos.println(name);
        nos.println(psw);
        ta1.setText("");
        ta2.setText("");
        f1.dispose();

    }

}

class SignUpListener implements ActionListener {

    PrintWriter nos;
    JFrame f1;

    SignUpListener(PrintWriter nos, JFrame f1) {
        this.nos = nos;
        this.f1 = f1;
    }

//    SignUpListener() {
//
//    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        SignUpFrame su=new SignUpFrame();
        su.signUpFrame(nos);
        f1.dispose();

    }

}

class SignUpFrame extends JFrame {

    public static void signUpFrame(PrintWriter nos) {
        JFrame f = new JFrame("SignUp Window");
        JLabel l1 = new JLabel("Name");
        JLabel l2 = new JLabel("Age");
        JLabel l3 = new JLabel("Address");
        JLabel l4 = new JLabel("Email-id");
        JLabel l5 = new JLabel("Username");
        JLabel l6 = new JLabel("Password");
        JButton b = new JButton("OK");
        JTextField tf1 = new JTextField(10);
        JTextField tf2 = new JTextField(10);
        JTextField tf3 = new JTextField(10);
        JTextField tf4 = new JTextField(10);
        JTextField tf5 = new JTextField(10);
        JTextField tf6 = new JTextField(10);
        FlowLayout flow = new FlowLayout();
        f.setLayout(flow);
        /*JPanel p1 = new JPanel();
         JPanel p2 = new JPanel();
         JPanel p3 = new JPanel();
         JPanel p4 = new JPanel();
         JPanel p5 = new JPanel();
         JPanel p6 = new JPanel();*/
        f.setSize(199, 400);
        /*f.add(p1);
         f.add(p2);
         f.add(p3);
         f.add(p4);
         f.add(p5);
         f.add(p6);*/
        f.add(l1);
        f.add(tf1);
        f.add(l2);
        f.add(tf2);
        f.add(l3);
        f.add(tf3);
        f.add(l4);
        f.add(tf4);
        f.add(l5);
        f.add(tf5);
        f.add(l6);
        f.add(tf6);
        f.add(b);
        OkListener ok = new OkListener(tf1, tf2, tf3, tf4, tf5, tf6, nos, f);
        b.addActionListener(ok);
        tf6.addActionListener(ok);
        
        tf1.setEditable(true);
        tf2.setEditable(true);
        tf3.setEditable(true);
        tf4.setEditable(true);
        tf5.setEditable(true);
        tf6.setEditable(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class OkListener implements ActionListener {

    JTextField tf1;
    JTextField tf2;
    JTextField tf3;
    JTextField tf4;
    JTextField tf5;
    JTextField tf6;
    PrintWriter nos;
    JFrame f;

    OkListener(JTextField tf1, JTextField tf2, JTextField tf3, JTextField tf4, JTextField tf5, JTextField tf6, PrintWriter nos, JFrame f) {
        this.tf1 = tf1;
        this.tf2 = tf2;
        this.tf3 = tf3;
        this.tf4 = tf4;
        this.tf5 = tf5;
        this.tf6 = tf6;
        this.nos = nos;
        this.f = f;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String name = tf1.getText();
        String age = tf2.getText();
        String address = tf3.getText();
        String email = tf4.getText();
        String uname = tf5.getText();
        String password = tf6.getText();
        nos.println("signup");
        nos.println(name);
        nos.println(age);
        nos.println(address);
        nos.println(email);
        nos.println(uname);
        nos.println(password);
        tf1.setText("");
        tf2.setText("");
        tf3.setText("");
        tf4.setText("");
        tf5.setText("");
        tf6.setText("");

         f.dispose();
    }

}
