package signinsignup;

import java.awt.FlowLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class Server {

    public static ArrayList<PrintWriter> a1 = new ArrayList<>();
    public static ArrayList<PrintWriter> a2 = new ArrayList<>();
    public static ArrayList<RegisterUsers> registeredUsers = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        System.out.println("Server signing IN");
        ServerSocket ss = new ServerSocket(9081);
        for (int i = 0; i < 5; i++) {
            Socket soc = ss.accept();
            Conversation c = new Conversation(soc);
            c.start();
        }
        System.out.println("Server signing Off");
    }

}

class Conversation extends Thread {

    Socket soc;

    public Conversation(Socket soc) {
        this.soc = soc;
    }

    public void run() {
        int count = 2;
        int flag = 0;
        int result = 0;
        int uid = 0;
        try {
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
            String check = nis.readLine();

            while (true) {

                if (check.equals("signin")) {
                    String name = nis.readLine();
                    String psw = nis.readLine();

                    while (count >= 0) {
                        JDBC jdbc = new JDBC();
                        result = jdbc.compare(name, psw);

                        if (result == 1) {
                            Server.a2.add(nos);
                            nos.println("Yes");
                            nos.println(name);
                            flag = 11;
                            break;
                        }
                        if (result == 0) {

                            count--;
                            nos.println(count);
                        }
                        check = nis.readLine();
                        if (check.equals("signup")) {
                            flag = 1;
                            break;
                        }
                        name = nis.readLine();
                        psw = nis.readLine();

                    }
                    if (flag == 11) {
                        break;
                    }
                }

                if (check.equals("signup") || flag == 1) {

                    String name = nis.readLine();
                    String age = nis.readLine();
                    String address = nis.readLine();
                    String email = nis.readLine();
                    String uname = nis.readLine();
                    String password = nis.readLine();
                    uid++;
                    JDBC jdbc = new JDBC();
                    jdbc.insert(uid, name, age, address, email, uname, password);
                    RegisterUsers rs = new RegisterUsers(nos, uname);
                    Server.registeredUsers.add(rs);
                    nos.println("signedup");
                    count = 2;

                }
                check = nis.readLine();

            }
            String str = nis.readLine();
            while (!str.equals("End")) {
                System.out.println("Server Recieved  " + str);
                for (PrintWriter o : Server.a2) {
                    o.println(str);
                }
                str = nis.readLine();
            }

            nos.close();
            Server.a2.remove(nos);
            System.out.println("Server signing OFF");
            System.exit(0);

        } catch (Exception e) {

        }

    }
}

class JDBC {

    int uid = 1;

    public int compare(String name, String psw) throws Exception {

        Class.forName("org.apache.derby.jdbc.ClientDriver");

        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ChatData", "root", "root");

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select uname,upassword from user04");

        while (rs.next()) {
            String uname = rs.getString("uname");
            String upassword = rs.getString("upassword");
            if (name.equals(uname) && psw.equals(upassword)) {

                return 1;
            }

        }
        return 0;
    }

    public void insert(int uid, String name,
            String age,
            String address,
            String email,
            String uname,
            String password) throws ClassNotFoundException, SQLException {

        Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ChatData", "root", "root");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("insert into user04 " + "values  (" + uid + ",'" + name + "','" + age + "','" + address + "','" + email + "','" + uname + "','" + password + "')");

    }
}

class RegisterUsers {

    PrintWriter nos;
    String name;

    public RegisterUsers(PrintWriter nos, String name) {
        this.nos = nos;
        this.name = name;
    }

    public String toString() {
        return "name: " + name;
    }
}
