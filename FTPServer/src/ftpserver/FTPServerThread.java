import java.io.*;
import java.security.acl.Owner;
import java.util.ArrayList;

public class FTPServerThread implements Runnable {

    private String serverName;
    private StreamExtender myStreamSocket;
    private String message;
    private Boolean session;    
    private Directory currentUsersDirectory;
    private File root;

    public FTPServerThread(StreamExtender myStreamSocket, String servername, File root) {

        this.myStreamSocket = myStreamSocket;
        this.serverName = servername;
        this.root = root;      
        
    }

    public void run() {
        session = true;

        try {

            myStreamSocket.sendMessage("100 - id bağlantı " + serverName);

            while (session) {

                message = myStreamSocket.receiveMessage();

                switch (message) {
                    case "101":
                        addNewUser();
                        break;
                    case "105":
                        login();
                        break;
                    case "200":
                        sendDirectoryListingForCurrentUser();
                        break;
                    case "202":
                        fileRecieve();
                        break; 
                    case "207":
                        fileTransmit();
                        break;
                    case "300":
                        closeConnection(); 
                        break;
                }

            }

            myStreamSocket.close();
            System.out.println("Oturum sonlandırıldı.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void addNewUser() {
        try {
            myStreamSocket.sendMessage("102");  
            message = myStreamSocket.receiveMessage(); 
            myStreamSocket.sendMessage(createDirectory(message)); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            myStreamSocket.sendMessage("301 Oturum sonlandırıldı.");
            session = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void login() {
        String signalToReturn = "107";

        try {
            myStreamSocket.sendMessage("102");  
            message = myStreamSocket.receiveMessage(); 

            for (Directory d : FTPserver.serversDirectoryListing) {
                if (d.getOwner().toLowerCase().equals(message.toLowerCase())) {
                    
                    currentUsersDirectory = d;
                    signalToReturn = "106"; 
                    System.out.println(d.getOwner() + " bağlandı");
                }
            }
            
            myStreamSocket.sendMessage(signalToReturn); 

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }   
    
    
    public void sendDirectoryListingForCurrentUser()
    {
        try {            
            myStreamSocket.sendMessage("201"); 
            message = ""; 

            for (String f : currentUsersDirectory.dirListing) {                
                message += f + ";"; 
            }            
            myStreamSocket.sendMessage(message); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String createDirectory(String name) {
        
        for (Directory d : FTPserver.serversDirectoryListing) {
            if (d.getOwner().toLowerCase().equals(name.toLowerCase())) {
                return "104";
            }
        }

       //dosyaların saklanacagi klasor olusturuluyor
        Directory d = new Directory();
        d.setOwner(name.toLowerCase());
        File root = new File("ROOT");
        File combined = new File(root, name);
        boolean created = combined.mkdir();
        FTPserver.serversDirectoryListing.add(d);
        System.out.println(name + "- klasör olusturuldu.");
        
        return "103";

    }
    
    public void fileTransmit()
    {
        try {
            
            String file = "";
            File f = new File("");
            
            myStreamSocket.sendMessage("208");
            message = myStreamSocket.receiveMessage();
            
            boolean fileExists = false;
            for(String fi : currentUsersDirectory.getDirListing())
            {
                if (fi.equals(message));
                    fileExists = true;
            }            
            if (fileExists)
            {
                file = message;
                myStreamSocket.sendMessage("213");
            }
            else
            {
                myStreamSocket.sendMessage("214"); 
            }
            message = myStreamSocket.receiveMessage();
            
            if (message.contains("209"))
            {
                f = new File("ROOT/" + currentUsersDirectory.getOwner(), file);
                int size = (int)f.length();
                myStreamSocket.sendMessage("" + size);
            }
            
            message = myStreamSocket.receiveMessage();
            
            if (message.contains("210"))
            {
                File here = new File("ROOT/" + currentUsersDirectory.getOwner(), f.getName());
                
                
                myStreamSocket.SendFile(here);
                System.out.println("Dosya gönderiliyor " + f.getPath());
            }
            
            
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void fileRecieve()
    {
        try {
            myStreamSocket.sendMessage("203"); 
            message = myStreamSocket.receiveMessage(); 

            String name = message.split(";")[0];
            int size = Integer.parseInt(message.split(";")[1]);
            System.out.println("boyut" + size);
            File f = new File("ROOT", currentUsersDirectory.getOwner());
            
          
            myStreamSocket.sendMessage("204"); 
            
            File fcombined = new File(f, name);
            
            boolean success = myStreamSocket.recieveFile(fcombined, size);
            
            if (success)
            {
                myStreamSocket.sendMessage("205");
                currentUsersDirectory.dirListing.add(name);
            }
            else
                myStreamSocket.sendMessage("206");
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        
} 