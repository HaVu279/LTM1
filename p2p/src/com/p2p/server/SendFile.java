package com.p2p.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendFile extends Thread {
    static final int BUFSIZE = 1024; // define a constant used as size of buffer
    private Socket connSock;
    String filename;
    String filedowload;
    ClientF clientF = new ClientF();
    List<String> files;
    List<String> ipArray;
    List<String> portArray;
    String portClient;
    BufferedReader br;
    BufferedWriter bw;

    public SendFile(Socket connSock) {
        this.connSock = connSock;
    }

    public void run(){
        String hostClient = "" + connSock.getInetAddress();
        clientF.setIp(hostClient);
        try {
            files = new ArrayList<String>();

            bw = new BufferedWriter(new OutputStreamWriter(connSock.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(connSock.getInputStream()));
        }
        catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.printf("List file %s:%d is: \n",connSock.getInetAddress(),connSock.getPort());
        try {
            portClient = br.readLine();
            clientF.setPort(portClient);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while(true){
            try{
                filename = br.readLine();
                if(filename.equals("QUIT")){
                    break;
                }
                try{
                    System.out.printf("\n+ "+filename);
                    files.add(filename);
                }catch(Exception ex){
                    System.out.printf("Error !\n");
                }
            }
            catch(IOException ex){
                System.out.printf("Error !\n");
            }
        }
        System.out.printf("\n ");
        clientF.setFileName(files);
        Constants.LIST_CLIENTF.add(clientF);

        while(true){
            try{
                ipArray = new ArrayList<String>();
                portArray = new ArrayList<>();
                filedowload = br.readLine();
                if(filedowload.equals("QUIT")){
                    break;
                }
                int len = Constants.LIST_CLIENTF.size();
                for(int i=0; i< len;i++){
                    for(int j=0;j<Constants.LIST_CLIENTF.get(i).getFileName().size();j++){
                        if(filedowload.equals(Constants.LIST_CLIENTF.get(i).getFileName().get(j))){
                            if(!Constants.LIST_CLIENTF.get(i).getPort().equals(portClient)){
                                ipArray.add(Constants.LIST_CLIENTF.get(i).getIp());
                                portArray.add(Constants.LIST_CLIENTF.get(i).getPort());
                            }
                        }
                    }
                }

                int si = ipArray.size();
                String size="" + si;
                bw.write(size);
                bw.newLine();
                bw.flush();
                if(si==0){
                    continue;
                }
                for(int k=0; k< ipArray.size();k++){
                    bw.write(ipArray.get(k));
                    bw.newLine();
                    bw.flush();

                    bw.write(portArray.get(k));
                    bw.newLine();
                    bw.flush();
                }

                //Nếu dowload thành công thì update danh sách file
                String kq = "false";
                String reFileName;
                kq = br.readLine();
                reFileName = br.readLine();
                if(kq.equals("true")){
                    int number =0;
                    System.out.printf("\n Update List file %s:%d is: \n",connSock.getInetAddress(),connSock.getPort());
                    for(int i=0; i< len;i++){
                        if(Constants.LIST_CLIENTF.get(i).getPort().equals(portClient)){
                            number = i;
                            Constants.LIST_CLIENTF.get(i).getFileName().add(reFileName);
                        }
                    }
                    for(int i=0;i<Constants.LIST_CLIENTF.get(number).getFileName().size();i++){
                        System.out.printf(Constants.LIST_CLIENTF.get(number).getFileName().get(i)+"\n");
                    }
                }

            }catch(IOException ex){
                break;
            }
        }
	     try {
	         System.out.printf("\n DisConnect to client: %s !\n",hostClient );
	         connSock.close();
	     }
	     catch (IOException ex) {
	     }
    }
}
