package com.vivi.qq;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	
	Socket s = null;
	ServerSocket ss = null;
	boolean started = false;
	List<VClient> clients = new ArrayList<VClient>();
	
	public void launch() {
		try{
			ss = new ServerSocket(8888);
			started = true;
			System.out.println("server is start!");
			while(started){
				s = ss.accept();
				VClient vc = new VClient(s);
				clients.add(vc);
				new Thread(vc).start();				
			}
			ss.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	class VClient implements Runnable{
		
		Socket s = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		
		public VClient(Socket s) {
			this.s = s;
		}
		public void run() {
			try {
				boolean connected = true;
				System.out.println("a client has connected!");
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				while(connected) {
					int cid = clients.indexOf(this) + 1;
					try {
						String str = dis.readUTF();
						System.out.println(str);
						for(int i = 0; i < clients.size(); i++) {
							clients.get(i).dos.writeUTF("Client"+cid+":"+str);
						}
					} catch(IOException e) {
						connected = false;
					}
				}
				dis.close();
				dos.close();
				s.close();
				clients.remove(this);
				System.out.println("a client is qiut!");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		new Server().launch();
	}
	

}
