package com.yuil.game.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpTest {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		ExecutorService executor=Executors.newFixedThreadPool(10);
		TcpTest tcpTest=new TcpTest(); 
	
		Runnable server=tcpTest.new Server();
		Runnable client=tcpTest.new Client();
		
		
		executor.execute(server);
		executor.execute(client);
		

		
		
	}
	

	public static void tcpServer() throws IOException{
		ServerSocket listen = new ServerSocket(5050);
		
        Socket server  = listen.accept();
        
        InputStream in = server.getInputStream();
        OutputStream out = server.getOutputStream();
        char c = (char)in.read();
        System.out.println("收到:" + c);
        out.write('s');
        
        out.close();
        in.close();
        server.close();
        listen.close();
	}

	public static void tcpClient() throws UnknownHostException, IOException{
		 Socket client = new Socket("127.0.0.1" , 5050);
	        InputStream in = client.getInputStream();
	        OutputStream out = client.getOutputStream();
	        long time=System.nanoTime();
	        out.write('c');
	        char c = (char)in.read();
	        time=System.nanoTime()-time;
	        System.out.println("收到:" + c);
	        System.out.println("time:"+time);
	        out.close();
	        in.close();
	        client.close();
	}
	
	public class Server implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				tcpServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public class Client implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				tcpClient();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
