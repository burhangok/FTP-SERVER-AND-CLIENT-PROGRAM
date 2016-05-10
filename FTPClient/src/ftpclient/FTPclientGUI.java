package ftpclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FTPclientGUI extends JFrame implements ActionListener, WindowListener {

    private JPanel panel_main,                                  
            cardPanel;
    private DirectoryPanel directoryPanel;
    private LoginPanel loginPanel;
    private JoinPanel joinPanel;            
    private CardLayout cardLayout;
    private JMenu fileMenu;
    private Toolkit toolkit;
    private JLabel lblname;    
    private JButton btnNew, btnExisting;   
    private String serverName = "   BURHAN GOK FTP SERVER BİLGİSAYAR AĞLARI PROJESİ";
    private Container cPane;
    private Client client;

    public FTPclientGUI() {
        client = Client.getInstance();
        setTitle("FTP - Client - BURHAN GOK FTP SERVER BİLGİSAYAR AĞLARI PROJESİ");
        setSize(400, 300);
        setResizable(false);
        addWindowListener(this);       
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        toolkit = this.getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation((size.width - getWidth()) / 2, (size.height - getHeight()) / 2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        cPane = getContentPane();
        InitializeMainGUIpanel();
        JMenuBar menuBar = new JMenuBar();
        createFileMenu();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);        
        directoryPanel = new DirectoryPanel();
        loginPanel = new LoginPanel(cardLayout, cardPanel, directoryPanel); 
        joinPanel = new JoinPanel(cardLayout, cardPanel); 
        cardPanel.add(loginPanel, "2");
        cardPanel.add(joinPanel, "3");
        cardPanel.add(directoryPanel, "4");
        cPane.add(cardPanel);
    }
   
    private void InitializeMainGUIpanel() {

        ///  ANA EKRAN
        panel_main = new JPanel(new GridLayout(2, 1, 5, 5));
        panel_main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel temp = new JPanel(new GridLayout(1, 1, 5, 5));
        lblname = new JLabel(serverName);
        temp.add(lblname);
        panel_main.add(temp);

        temp = new JPanel(new GridLayout(1, 2, 5, 5));
        temp.setBorder(BorderFactory.createEmptyBorder(30, 5, 30, 5));   // t l b r
        btnNew = new JButton("Yeni Kullanıcı Oluştur");
        btnNew.addActionListener(this);
        btnExisting = new JButton("Kayıtlı Kullanıcıyım");
        btnExisting.addActionListener(this);
        temp.add(btnNew);
        temp.add(btnExisting);
        panel_main.add(temp);
        cardPanel.add(panel_main, "1");
    }  
    //Ust Menu Olusturma
    private void createFileMenu() {
        JMenuItem item;
        fileMenu = new JMenu("Kullanıcı İşlemleri");
        item = new JMenuItem("Çıkış Yap");
        item.addActionListener(this);
        fileMenu.add(item);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String actionName;

        actionName = actionEvent.getActionCommand();

        if (actionName.equals("Çıkış Yap")) {
            boolean success = client.logout();
            if (success)
                JOptionPane.showMessageDialog(null, "Başarılı şekilde çıkış yapıldı.");
            System.exit(0);
        } else if (actionEvent.getSource().equals(btnNew)) {
            // Yeni Kullanici Ekleme Ekrani
           cardLayout.show(cardPanel, "3"); // Yeni Kullanici Ekrani
        } else if (actionEvent.getSource().equals(btnExisting)) {
            // Show the login screen     
            cardLayout.show(cardPanel, "2"); // Kullanici Giris Ekrani
        }    
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
      
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        
        client.exit();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
       
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
       
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
      
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
     
    }

    public static void main(String[] args) {

try {
            // gorunum uı ayarlanmasi
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } 
    catch (UnsupportedLookAndFeelException e) {
       // exception
    }
    catch (ClassNotFoundException e) {
       // exception
    }
    catch (InstantiationException e) {
       // exception
    }
    catch (IllegalAccessException e) {
       // exception
    }
        FTPclientGUI f = new FTPclientGUI();
        f.setVisible(true);

    }
}
