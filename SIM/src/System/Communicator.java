package System;
import java.net.*;
import java.io.*;

public class Communicator 
{
	private ServerSocket serverSock = null;
	private Socket sock = null;
	private PrintWriter pw = null;
	private BufferedReader in = null;
	
	
	public Communicator() 
	{
		getConnect();
	}
	
	private void getConnect()
	{
		try
		{
			System.out.println("LISTENING");
			serverSock = new ServerSocket(7777);
			sock = serverSock.accept();
			System.out.println("READY TO WORK");
			in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	
	private void closeConnect()
	{
		try {
			pw.close();
			in.close();
			pw.close();
			sock.close();
			serverSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reconnect()
	{
		closeConnect();
		getConnect();
	}
	
	public void sendMsg(String msg)
	{
		pw.println(msg);
		pw.flush();
	}
	
	public String rcvMsg()
	{
		try 
		{
			String line = in.readLine();
			return line;
		} 
		catch (IOException e) 
		{
			System.out.println("CLIENT OUT");
			closeConnect();
			getConnect();
			return "REGAME";
		}
	}
	

}
