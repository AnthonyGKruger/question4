package learning;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    static JFrame frame;
    static JPanel panel;
    static JTextField txtMessage;
    static JButton btnExit;
    static DataInputStream dis;
    static DataOutputStream dos;
    static Cipher cipher;
    static SecretKey key;
    static String messageIn;

    public static void main(String[] args) {
        InitializeForm();
        doConnection();
    }

    public static void doConnection() {

        System.out.println("Waiting for client ...");
        txtMessage.setText("Waiting for client...");

        Socket socket;

        try {

            ServerSocket server = new ServerSocket(1234);
            socket = server.accept();
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            messageIn = "";

            while (!messageIn.equals("exit")) {

                messageIn = dis.readUTF();
                JOptionPane.showMessageDialog(null, "encrypted message received: " + messageIn);

                decrypt();

                txtMessage.setText("Client: " + messageIn);

                dos.writeUTF("ok");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getCause() + "         ");
            e.printStackTrace();
        }

    }

    private static void InitializeForm() {

        frame = new JFrame("server");
        frame.setSize(400, 160);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        btnExit = new JButton("exit");
        btnExit.setBounds(300, 80, 70, 40);
        panel.add(btnExit);


        txtMessage = new JTextField();
        txtMessage.setBounds(20, 20, 350, 60);
        panel.add(txtMessage);

        frame.setVisible(true);

        btnExit.addActionListener(e -> System.exit(0));
    }

    private static void decrypt(){

        key = new SecretKeySpec("PBKDF2WithHmacA1".getBytes(), "AES");

        try {

            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] bb = new byte[messageIn.length()];
            for (int i = 0; i < messageIn.length(); i++) {
                bb[i] = (byte) messageIn.charAt(i);
            }

            messageIn = new String(cipher.doFinal(bb));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}


