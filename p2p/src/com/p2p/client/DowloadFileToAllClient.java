package com.p2p.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.p2p.server.Constants;

class DownloadFileToAllClient extends Thread  {
    private String kq;
    private String host;
    private String port;
    private String fileName;
    
    public DownloadFileToAllClient(String host, String port, String fileName ) {
        this.host = host;
        this.port = port;
        this.fileName = fileName;
    }
    public void run(){
        Socket socket = null;
        BufferedWriter os = null;
        BufferedReader is = null;
        InputStream in = null;
        OutputStream out = null;
        try{
            int portClientDowload = Integer.parseInt(port);
            String[] output = host.split("/");
            String hostClientDowload = output[1];
            socket = new Socket(hostClientDowload,portClientDowload);
            System.out.printf("OK connect \n");
            
            os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // sent file name -> server
            os.write(fileName);
            os.newLine();
            os.flush();
            int length;
            length = Integer.parseInt(is.readLine());
            if(length == 0){
                System.out.printf("can't dowload the file : %s.\n ",fileName);
            }
            try {
                in = socket.getInputStream();
                out = new FileOutputStream(Constants.DIR_CLIENT1+"//"+reNameFile(fileName));
            } catch (FileNotFoundException ex) {
                System.out.printf("File not found. \n");
            }

            byte[] bytes = new byte[length];

            int count;
            int dem=0;
            //sent file -> client
            while ((count = in.read(bytes)) > 0) {
                dem = dem + count;
                out.write(bytes, 0, count);
                if(dem>=length){break;}
            }
            System.out.printf("dowload successed \n\n");
            setKq("true");
            socket.close();
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about this host \n");
            return;
        }
        catch(IOException ex) {
            System.out.println("Can't jsdjf skf kshf \n");
        }
    }
    public String getKq() {
        return kq;
    }

    public void setKq(String kq) {
        this.kq = kq;
    }
    public String reNameFile(String filename){
        Random random = new Random();
        int Number = random.nextInt();
        String reFileName = Number + "_" + filename;
        return reFileName;
    }
}
