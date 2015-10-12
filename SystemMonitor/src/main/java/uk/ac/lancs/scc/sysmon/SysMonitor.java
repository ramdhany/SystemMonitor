package uk.ac.lancs.scc.sysmon;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.jezhumble.javasysmon.Monitor;

public class SysMonitor {

	private Logger logger = Logger.getLogger(SysMonitor.class.getName());
	private MonitorThread monitorThr;
	private boolean stopped;
	private Object lock;
	private InetAddress mainIP;
	private String macAddr;
	private ISystemInfo sysInfo; // a platform-independent library for status information
	
	public SysMonitor() throws SysMonException{

		stopped = false;

			
		sysInfo = new SystemInfoImpl();
		
		try {
			// get main IP address
			Socket sock = new Socket("www.lancs.ac.uk", 80);
			mainIP = sock.getLocalAddress();
			// get mac address of main network interface
			NetworkInterface network = NetworkInterface.getByInetAddress(mainIP);
			byte[] mac = network.getHardwareAddress();
	 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			macAddr = sb.toString();
			
			monitorThr = new MonitorThread();
			

		} catch (UnknownHostException e) {
			throw new SysMonException(SysMonException.UNKNOWN_HOST, e.getMessage());
		
		} catch (IOException e) {
			throw new SysMonException(SysMonException.IOEXCEPTION, e.getMessage());
			
		}

		

	}
	
	/**
	 * Start the System Monitor
	 */
	public void start(){
		
		monitorThr.start();
	}
	
	
	public void stop()
	{
		
		setStoppedValue(true);

	}

	boolean getStoppedValue()
	{
		synchronized (lock) {
			return stopped;
		}
	}
	
	void setStoppedValue(boolean val)
	{
		synchronized (lock) {
			 stopped = val;
		}
	}



	private class MonitorThread extends Thread{
		
		private Logger mylogger = Logger.getLogger(MonitorThread.class.getName()); 
		
		@Override
		public void run() {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss S");
			Calendar cal;
			long sleepTime = AppConfiguration.getSleepTime(); 

			
			
			try {
				File StatusLogfile = new File(AppConfiguration.getSystemStatusFileName());
				
				FileWriter fw = new FileWriter(StatusLogfile, true);
				String inputLine;
//				String inputLine="Timestamp \t PID \t Processes \t hostname \t load average \t ipaddress \t mac address";
//				BufferedWriter bfw = new BufferedWriter(fw);
//		        bfw.write(inputLine);
//		        
				BufferedWriter bfw = new BufferedWriter(fw);
				while(!stopped)
				{
					
					cal = Calendar.getInstance();
					
					
					List<String> proc_names = AppConfiguration.getProcessNames(); 
					
					inputLine = dateFormat.format(cal.getTime()) + " \t ";
					
					for(String pname: proc_names)
					{
						inputLine += sysInfo.getProcessID(pname) + " \t ";
					}
					
					inputLine += sysInfo.getHostName() + " \t ";
					
					inputLine += sysInfo.getLoadAverage() + " \t ";
					
					inputLine += sysInfo.getHostIPAddress() + " \t ";
					
					inputLine += sysInfo.getHardwareAddress() + " \t ";
					mylogger.debug(inputLine);
					bfw.write(inputLine);
					bfw.newLine();
					bfw.flush();
					Thread.sleep(sleepTime);
				}
				bfw.close();
				fw.close();

			} catch (InterruptedException e) {
				mylogger.error("Error, thread.sleep():" + e.getMessage());
			} catch (IOException e) {
				mylogger.error("Error, thread.sleep():" + e.getMessage());
			} catch (SysMonException e) {
				mylogger.error("Error, thread.sleep():" + e.getMessage());
			}	
		}

	}
	
	
	
	
	/**
	 * gets the pids of the process with name <code>pname</code>
	 * @param pname name of the process
	 * @return 
	 * @return pid
	 */
	private List<Long> getProcessID(String pname){
		
		Process p;
		ArrayList<Long> pidList = null;

		try {
			p = Runtime.getRuntime().exec("ps -C " + pname + " -o pid");

			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = reader.readLine();
			while (line != null) {
				if (!line.equalsIgnoreCase("PID")){
					if (pidList == null)
						pidList = new ArrayList<Long>();
					pidList.add(Long.getLong(line));
				}
				reader.readLine();
			}
		} catch (IOException e) {
			logger.error("Error! SysMonitor.getProcessID() - " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Error! SysMonitor.getProcessID() - " + e.getMessage());
		}
		return pidList;
	}
	
	private List<Long> getJavaProcessID(String java_proc_name){
		
		Process p;
		ArrayList<Long> pidList = null;

		try {
			p = Runtime.getRuntime().exec("ps -ef | grep " + java_proc_name + " | grep -v grep | awk '{print $2}' ");

			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = reader.readLine();
			while (line != null) {
				if (!line.equalsIgnoreCase("PID")){
					if (pidList == null)
						pidList = new ArrayList<Long>();
					pidList.add(Long.getLong(line));
				}
				reader.readLine();
			}
		} catch (IOException e) {
			logger.error("Error! SysMonitor.getProcessID() - " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Error! SysMonitor.getProcessID() - " + e.getMessage());
		}
		return pidList;
	}
	
	
	
}
