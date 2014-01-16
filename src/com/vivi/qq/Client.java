package com.vivi.qq;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
public class Client extends Frame{
	
	Socket s = null;
	Thread t = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
		
	TextField tf = new TextField();
	TextArea ta = new TextArea();
	
	public void launchFrame(){
		setBounds(300,400,300,300);
		setTitle("Client");
		ta.setEditable(false);
		ta.setBackground(null);
		add(tf,BorderLayout.SOUTH);
		add(ta,BorderLayout.CENTER);
		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				System.exit(0);
			}			
		});
		setVisible(true);
		tf.addActionListener(new TFListener());
		connect();
		t = new Thread(new GetT());
		t.start();
	}
	class TFListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = tf.getText().trim();
			tf.setText("");
			send(str);
		}
		
	}
	
	public void connect() {
		try {
			s = new Socket("127.0.0.1",8888);
			dos = new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void disconnect() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
	
	public void send(String str) {
		try {
			dos.writeUTF(str);
			dos.flush();
			//dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	class GetT implements Runnable {
		public void run() {
			while (true) {
				try {
					dis = new DataInputStream(s.getInputStream());
					String s = dis.readUTF();
					ta.append(s+"\n");
				} catch (IOException e) {
					return;
				}
			}
		}
	}
	
	public static void main(String[] args){
		new Client().launchFrame();
	}

}
