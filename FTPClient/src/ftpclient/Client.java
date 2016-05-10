package ftpclient;


import javax.swing.JOptionPane;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author burhan
 */
public class Client {

    private static Client uniqueInstance;
    private StreamExtender streamSocket;
    private String message;
    private String serverName;
    private SystemInformation sysinfo;

    private Client() {
        connect();
    }

    public static Client getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Client();
        }

        return uniqueInstance;
    }
//baglantimizin yapildigi fonksiyon
    public boolean connect() {
        try {
            try {
                //sysinfo = new SystemInformation(new File(getClass().getResource("./config/systeminfo.xml")));
                File f = new File(getClass().getResource("config/systeminfo.xml").toURI());
                sysinfo = new SystemInformation(f);
                streamSocket = new StreamExtender(InetAddress.getByName(sysinfo.getAddress()), sysinfo.getPort());
            } catch (Exception ex) {
                // Cant read file so default to localhost and port 12000
                streamSocket = new StreamExtender(InetAddress.getByName("localhost"), 12000);
            }
            serverName = streamSocket.receiveMessage();
            JOptionPane.showMessageDialog(null, serverName);
            //System.out.println(serverName);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        return false;
    }
//yeni kullanici baglantisi olusturan fonksiyon
    public boolean createNew(String txtUserName) {
        try {

            boolean wasSuccessful = false;
            streamSocket.sendMessage("101"); 

            message = streamSocket.receiveMessage();

            if (message.contains("102")) // yeni kullanici baglanti id
            {
                streamSocket.sendMessage(txtUserName);
            } else {
                JOptionPane.showMessageDialog(null, "102 id li kullanici baglantisinda hata olustu !");
            }

            message = streamSocket.receiveMessage();

            if (message.contains("103")) {
                JOptionPane.showMessageDialog(null, "Yeni kullanıcı olusturuldu.");
                wasSuccessful = true;
            } else if (message.contains("104")) {
                JOptionPane.showMessageDialog(null, "Bu kullanici zaten kayitli.");
            } else {
                JOptionPane.showMessageDialog(null, "Baglantı Hatası");
            }

            return wasSuccessful;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    //yeni kullanici ile giris yapmamizi saglayan fonksyion
    public boolean login(String txtUserName) {
        try {

            // Send user name to server
            streamSocket.sendMessage("105");
            message = streamSocket.receiveMessage();
            if (message.contains("102")) // yeni kullanici baglanti id
            {
                streamSocket.sendMessage(txtUserName);
            } else {
                JOptionPane.showMessageDialog(null, "Bağlantı Hatası");
                return false;
            }
            message = streamSocket.receiveMessage();
            if (message.contains("106")) {
                JOptionPane.showMessageDialog(null, "Kullanıcı Girişi Yapıldı.");
                return true;
            } else if (message.contains("107")) {
                JOptionPane.showMessageDialog(null, "Kullanıcı Bulunamadı");
            } else {
                JOptionPane.showMessageDialog(null, "Bağlantı Hatası 106 - 107");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    //kullanici baglanti sonlandiran fonksyion
    public boolean logout() {
        boolean wasSuccessful = false;
        try {
            if (!streamSocket.isClosed()) {
                streamSocket.sendMessage("300");
                message = streamSocket.receiveMessage();
                System.out.println(message);
                if (message.contains("301")) //ack biti gonderilip cikis yapiliyor
                {
                    streamSocket.close();
                    wasSuccessful = true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return wasSuccessful;
    }

    //kullanici servera dosya yukluyor
    public void upload(File file) {
        try {
            streamSocket.sendMessage("202");
            message = streamSocket.receiveMessage();
            System.out.println(message);
            if (message.contains("203")) {
                streamSocket.sendMessage(file.getName() + ";" + (int) file.length());

                message = streamSocket.receiveMessage();
                System.out.println(message);
                if (message.contains("204")) {

                    boolean result = streamSocket.SendFile(file);
                    System.out.println(result);
                    message = streamSocket.receiveMessage();
                    if (message.contains("205")) {
                        JOptionPane.showMessageDialog(null, "Yükleme Başarılı.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Dosya Yükleme İşlemi Hatalı");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    //serverdan dosya indirme isleminin gerceklestigi fonksiyon
    public boolean download(File file) {

        try {
            int fileSize = 0;

            streamSocket.sendMessage("207");  // requesting to download a file
            message = streamSocket.receiveMessage(); // wait for server to acknowledge
            //System.out.println(message);
            if (message.contains("208")) {
                // server acknowledges, send file name
                streamSocket.sendMessage(file.getName());
            }

            message = streamSocket.receiveMessage();

            if (message.contains("213")) {
               //serverdan dosyanin alinmasi islemleri
                streamSocket.sendMessage("209"); 


                fileSize =
                        Integer.parseInt(streamSocket.receiveMessage()); 

                streamSocket.sendMessage("210"); 
                

                streamSocket.recieveFile(file, fileSize);


                return true;
            }

            return false;

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("İndirme İşlemi Hatalı.");
        }
        return false;
    }

    //kullaniciya ait dosyalarin goruntulenmesi
    public DefaultListModel<String> fetchDirectoryListing() {
        DefaultListModel<String> list = new DefaultListModel<String>();
        try {
            streamSocket.sendMessage("200"); 
            message = streamSocket.receiveMessage();
            //System.out.println(message);
            if (message.contains("201")) 
            {
                message = "";
                message = streamSocket.receiveMessage();

                String[] array = message.split(";");
                for (String file : array) {
                    list.addElement(file);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Klasör bulunmada hata!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    //serverdan baglantinin kesilmesi ve programın sonlandirilmasi
    public void exit() {
        if (streamSocket.isClosed()) {
            System.exit(0);
        } else {
            logout();
            System.exit(0);
        }

    }
}
