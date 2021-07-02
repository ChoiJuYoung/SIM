package simController;

import java.io.*;
import java.net.*;

// communicates with SIM
public class SIMCommunicator {
    // fields
    private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private static SIMCommunicator com;
	
	// singleton
	private SIMCommunicator() {
		try {
			this.client = new Socket("localhost", 7777);
			this.out = new PrintWriter(client.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static SIMCommunicator getInstance() {
        if(com == null)
        	com = new SIMCommunicator();
        
        return com;
    }
	   
	// methods
	public String moveRobot(String type) {
		sendMsg("move:" + type);
		return rcvMsg();
	}
	
	public String readSensor(String type) {
		sendMsg("sensor:" + type);
		return rcvMsg();
	}
	
	public String initSIM(String msg) {
		sendMsg(msg);
		return rcvMsg();
	}
	
	public void close() {
		try {
			sendMsg("END");
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMsg(String msg) {
		out.println(msg);
	}
	
	private String rcvMsg() {
		try {
			String line = in.readLine();
			System.out.println(line);
			return line;
		} catch (IOException e) {
			e.printStackTrace();
			return "err";
		}
	}
}