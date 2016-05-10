package ftpclient;

import java.net.*;
import java.io.*;

/**
 *
 * @author burhan
 */
public class MyStreamSocket extends Socket {
   protected Socket socket;
   private BufferedReader input;
   private PrintWriter output;
   protected InputStream inStream;
   protected OutputStream outStream;

   MyStreamSocket(InetAddress acceptorHost,
                  int acceptorPort ) throws SocketException,
                                   IOException{
      socket = new Socket(acceptorHost, acceptorPort );
      setStreams( );

   }

   MyStreamSocket(Socket socket)  throws IOException {
      this.socket = socket;
      setStreams( );
   }

   private void setStreams( ) throws IOException{
      
      inStream = socket.getInputStream();
      input = new BufferedReader(new InputStreamReader(inStream));
      outStream = socket.getOutputStream();
     
      output = 
         new PrintWriter(new OutputStreamWriter(outStream));
   }

   public void sendMessage(String message)
   		          throws IOException {	
      output.print(message + "\n");   
     
      output.flush();               
   } 
   public String receiveMessage( )
		throws IOException {	
     
      String message = input.readLine( );  
      return message;
   } 

   public void close( )
		throws IOException {	
      socket.close( );
   }
} 
