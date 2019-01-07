package com.p2p.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.p2p.server.Constants;

public class Client1 {
    public static Scanner scanner = new Scanner(System.in);
    public static Scanner scanner2 = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        String host = args[0];
        List<String> portArray;
        List<String> hostArray;
        String kq = null;
        String post = args[1];
        BufferedWriter bw = null;
        BufferedReader br = null;
        String filename = null;
        try{
            Thread sendFile = new DownloadFileServer();
            sendFile.start( );
            socket = new Socket(host,Integer.parseInt(post));
            System.out.printf("OK connect to server \n");
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            bw.write("9100");
            bw.newLine();
            bw.flush();

            while(true){
                File folder = new File(Constants.DIR_CLIENT1);
                File[] listOfFiles = folder.listFiles();

                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        bw.write(listOfFiles[i].getName());
                        bw.newLine();
                        bw.flush();
                    }
                }
                bw.write("QUIT");
                bw.newLine();
                bw.flush();
                break;
            }

            while(true){

                System.out.println("Nhap file can download: ");
                filename = scanner.nextLine();
                bw.write(filename);
                bw.newLine();
                bw.flush();
                if(filename.equals("QUIT")){
                    break;
                }
                String ipAddr = br.readLine();
                int size = Integer.parseInt(ipAddr);
                if(size==0){
                    System.out.println("not found ! ");
                    continue;
                }
                hostArray = new ArrayList<>();
                portArray = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    String ip = br.readLine();
                    String port = br.readLine();
                    hostArray.add(ip);
                    portArray.add(port);
                    System.out.println("\\"+ip+":"+port);

                }
                System.out.println("mời nhập số để chọn thao tác \n");
                System.out.println("1: dowload từ 1 client bất kỳ chứa file cần dowload \n");
                System.out.println("2: dowload từ tất cả client chứa file cần dowload \n");
                int number = scanner2.nextInt();
                switch (number){
                    case 1:
                        DownloadFileClient dowloadFile = new DownloadFileClient();
                        dowloadFile.dowload(filename);
                        String reFileName;
                        kq = dowloadFile.getKq();
                        reFileName = dowloadFile.getReFileName();
                        bw.write(kq);
                        bw.newLine();
                        bw.flush();
                        bw.write(reFileName);
                        bw.newLine();
                        bw.flush();
                        break;
                    case 2:
                        for(int i=0;i<hostArray.size();i++){
                            Thread dowloadFileToAllClient = new DownloadFileToAllClient(hostArray.get(i),portArray.get(i),filename);
                            dowloadFileToAllClient.start();
                        }
                        kq="true";
                        reFileName = filename;
                        bw.write(kq);
                        bw.newLine();
                        bw.flush();
                        bw.write(reFileName);
                        bw.newLine();
                        bw.flush();
                        break;

                }

            }
        }

        catch (UnknownHostException e) {
            System.err.println("Don't know about this host \n");
            return;
        }
        catch(IOException ex) {
            System.out.println("Can't connect to server \n");
        }
        try {
            System.out.printf("\n DisConnect to server !\n");
            socket.close();
        }catch (IOException ex) {
        }

    }
}

