package com.p2p.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.Scanner;

import com.p2p.server.Constants;

class DownloadFileClient{
	final static Scanner scanner = new Scanner(System.in);
	
    private String kq;
    private String reFileName;
    
    public void dowload(String filename){
        Socket socket = null;
        String host ;
        String post;
        BufferedWriter os = null;
        BufferedReader is = null;
        InputStream in = null;
        OutputStream out = null;
        try{
            System.out.println("nhap host Client ban muon dowload file:");
            host = scanner.nextLine();
            
            System.out.println("nhap post Client ban muon dowload file:");
            post = scanner.nextLine();
            
            socket = new Socket(host,Integer.parseInt(""+post));
            System.out.printf("OK connect \n");
            
            os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // sent file name -> server
            os.write(filename);
            os.newLine();
            os.flush();
            int length;
            length = Integer.parseInt(is.readLine());
            if(length == 0){
                System.out.printf("can't dowload the file : %s.\n ",filename);
            }
            try {
                in = socket.getInputStream();
                File folder = new File(Constants.DIR_CLIENT1);
                File[] listOfFiles = folder.listFiles();

                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        if(listOfFiles[i].getName().equals(filename)){
                           setReFileName(reNameFile(filename));
                           reFileName = getReFileName();
                            out = new FileOutputStream(Constants.DIR_CLIENT1+"//"+getReFileName());
                        }else{
                            setReFileName(filename);
                            out = new FileOutputStream(Constants.DIR_CLIENT1+"//"+getReFileName());
                        }
                    }
                }

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

    public String getReFileName() {
        return reFileName;
    }

    public void setReFileName(String reFileName) {
        this.reFileName = reFileName;
    }

    public String reNameFile(String filename){
        Random random = new Random();
        int Number = random.nextInt();
        String reFileName = Number + "_" + filename;
        return reFileName;
    }
}
