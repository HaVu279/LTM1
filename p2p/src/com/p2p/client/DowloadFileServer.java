package com.p2p.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.p2p.server.Constants;

class DownloadFileServer extends Thread {
	
    static final int BUFSIZE = 1024; // define a constant used as size of buffer
    String filename;
    BufferedReader is;
    BufferedWriter os;
    ServerSocket servSocket;

    public void run(){
        try {
            int port = 9100;
            servSocket = new ServerSocket(port, 5);
            System.out.println("Listening for connections on port "
                    + servSocket.getLocalPort( ));
            while (true) {
                Socket connection = servSocket.accept( );
                System.out.printf("Connection established with client %s:%d \n" ,connection.getInetAddress( ),connection.getPort( ));

                try {
                    os = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch(IOException e){
                    System.out.println(e);
                    e.printStackTrace();
                }
                try{

                    filename = is.readLine();
                    if(filename.equals("QUIT")){
                        System.out.printf("DísConnect to client: %s : %d. \n",connection.getInetAddress( ),connection.getPort( ));
                        break;
                    }
                    System.out.printf("client is host: %s:%d want dowload filename: %s. \n",connection.getInetAddress( ),connection.getPort( ),filename);
                    File file = new File(Constants.DIR_CLIENT1+"//"+filename);
                    if(!file.exists()){
                        os.write("0");
                        os.newLine();
                        os.flush();
                        System.out.printf("file not found !\n");
                        continue;
                    }
                    // Get the size of the file
                    long length = file.length();
                    System.out.printf("@@@Data: "+length);
                    System.out.printf("\n");
                    os.write(""+length);
                    os.newLine();
                    os.flush();
                    byte[] bytes = new byte[BUFSIZE];
                    InputStream in = new FileInputStream(file);
                    OutputStream out = connection.getOutputStream();
                    System.out.printf("Sending %s to the Client[%s:%d]...\n",filename,connection.getInetAddress( ),connection.getPort( ));
                    try{
                        int count;
                        while ((count = in.read(bytes)) > 0) {
                            out.write(bytes, 0, count);

                        }
                    }
                    catch(IOException ex){
                        System.out.printf("error sent to client !\n");
                    }
                    System.out.printf("Ok sent to client success!\n");
                    out.flush();
                    in.close();
                    try {
                        connection.close();
                    }
                    catch (IOException ex) {
                    }
                }
                catch(IOException ex){
                    System.out.printf("Error !\n");
                }
            }
        }

        catch (IOException ex) {
            ex.printStackTrace( );
        }
    }
}