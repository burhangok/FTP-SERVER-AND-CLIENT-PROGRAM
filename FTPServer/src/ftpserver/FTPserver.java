import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public  class FTPserver {

    public static String names = "";
    public static ArrayList<Directory> serversDirectoryListing;
    private String servername;
    private int serverPort;
    private File root;
    private String defaultServername = "FTP Server - BURHAN GOK";
    private int defaultPort = 12000;

    public FTPserver() {
        servername = defaultServername;
        serverPort = defaultPort;
        initialiseServer();
    }

    public void initialiseServer() {

        try {
            serversDirectoryListing = new ArrayList<>();
            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);
            
            JOptionPane.showMessageDialog(null, "-------- FTP Server - Bilgisayar Ağları Projesi - Burhan GÖK ------------","    SERVER BAŞLATILIYOR" , JOptionPane.INFORMATION_MESSAGE);
           
           

            String input =JOptionPane.showInputDialog("Server Adını Giriniz");
            if (!input.equals(""))
                servername = input;
          
            input = JOptionPane.showInputDialog("Port Numarasını Giriniz (varsayılan (12000)");
            if (!input.equals(""))
                serverPort = Integer.parseInt(input);
            
           
            root = new File("ROOT");
       
            if (!root.exists()) {
                boolean mkdir = root.mkdir();

                if (mkdir) {
                    System.out.println("İşlem Hatası");
                }
            } else {
                System.out.println("ROOT klasoru ayalarlandi");
            }

            buildDirectoryListing();


            ServerSocket myServerSocket = new ServerSocket(serverPort);

            JOptionPane.showMessageDialog(null, "Server " + servername + " hazır");
            System.out.println("Server " + servername + " hazır");

            while (true) {
                System.out.println("Baglanti bekliyor");
JOptionPane.showMessageDialog(null, "Baglanti bekliyor");
                StreamExtender myStreamSocket = new StreamExtender(
                        myServerSocket.accept());

                JOptionPane.showMessageDialog(null, "Baglanti Kabul Edildi.");
                System.out.println("Baglanti Kabul Edildi.");
                // thread dinleme basladi
                Thread thread = new Thread(new FTPServerThread(myStreamSocket,
                        servername, root));

                thread.start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void buildDirectoryListing() {
        
        FTPserver.serversDirectoryListing.clear();
        
        for (File sub : root.listFiles())
        {          
            Directory d = new Directory();
            d.setOwner(sub.getName());
            for (File f : sub.listFiles())
            {
                d.dirListing.add(f.getName());
            }            
            FTPserver.serversDirectoryListing.add(d);            
        }
                
    }
    
    public static void main(String[] args) throws IOException {

        FTPserver ftp = new FTPserver();
    }
    
    
}