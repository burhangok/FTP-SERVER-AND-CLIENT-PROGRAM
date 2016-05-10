
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author burhan
 */
public class StreamExtender extends MyStreamSocket {

    
    byte[] byteArr;
    FileInputStream fis;
    BufferedInputStream bis;
    OutputStream os;

    public StreamExtender(InetAddress acceptorHost, int acceptorPort) throws SocketException, IOException {
        super(acceptorHost, acceptorPort);
    }

    public StreamExtender(Socket socket) throws IOException {
        super(socket);
    }

    public boolean recieveFile(File file, int size) { 

        try {    
                                   
            FileOutputStream fos = new FileOutputStream(file);
            byte[] byteme = new byte[1024];
            int recieveCount;
            int totalRecieved = 0;
            while (-1 != (recieveCount = inStream.read(byteme)))
            {
                fos.write(byteme, 0, recieveCount);
                System.out.println("Dosya Alındı " + recieveCount);
                totalRecieved += recieveCount;                
                if (totalRecieved == size)    
                    break;               
            }
            fos.flush();
            fos.close();   
             System.out.println("Dosya Gönderme İşlemi Tamamlandı.");
            
            return true;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean SendFile(File file) {               
        try {
            outStream.flush();
            System.out.println("gönderiliyor...");
            byte[] byteme = new byte[1024];
           
            FileInputStream fis = new FileInputStream(file);
            int count = 0;
            while (-1 != (count = fis.read(byteme, 0, byteme.length)))
            {
                outStream.write(byteme, 0, count);
                System.out.println("Gönderilen " + count);                
                outStream.flush();
            }
            
            fis.close();
            System.out.println("Dosya Gönderme İşlemi Tamamlandı.");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Dosya Gönderilemedi.");
        }
        return false;
    }
}
