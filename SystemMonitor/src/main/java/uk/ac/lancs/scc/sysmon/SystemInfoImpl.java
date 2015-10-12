package uk.ac.lancs.scc.sysmon;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.ProcessInfo;

/**
 * @author Rajiv Ramdhany
 * Base class for ISystemInfo implementation. Classes for platform-specific implementations
 * must extend this class.
 */
public class SystemInfoImpl implements ISystemInfo {

	private Logger logger = Logger.getLogger(SystemInfoImpl.class.getName()); 

	OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

	protected static JavaSysMon monitor;

	public SystemInfoImpl() {
		monitor = new JavaSysMon();
	}



	/**
	 * get host's Operating System name
	 */
	public String getOSName()
	{
		return operatingSystemMXBean.getName();
	}

	/**
	 * get host's Operating System version
	 */
	public String getOSVersion()
	{
		return operatingSystemMXBean.getVersion();
	}

	/**
	 * get host's architecture
	 */
	public String getArch()
	{
		return operatingSystemMXBean.getArch();
	}


	/* (non-Javadoc)
	 * @see uk.ac.lancs.scc.sysmon.ISystemInfo#getNetworkInterfaces()
	 */
	@Override
	public Enumeration<NetworkInterface> getNetworkInterfaces() throws SysMonException {
		try {
			return  NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new SysMonException(SysMonException.SOCKET_EXCEPTION, e.getMessage());
		}
	}



	/* (non-Javadoc)
	 * @see uk.ac.lancs.scc.sysmon.ISystemInfo#getHostIPAddress()
	 */
	@Override
	public InetAddress getHostIPAddress() throws SysMonException {

		InetAddress mainIP;
		try {
			// get main IP address
			Socket sock = new Socket("www.lancs.ac.uk", 80);
			mainIP = sock.getLocalAddress();
			return mainIP;
		} catch (UnknownHostException e) {
			throw new SysMonException(SysMonException.UNKNOWN_HOST, e.getMessage());

		} catch (IOException e) {
			throw new SysMonException(SysMonException.IOEXCEPTION, e.getMessage());

		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.lancs.scc.sysmon.ISystemInfo#getHardwareAddress()
	 */
	@Override
	public String getHardwareAddress() throws SysMonException {
		InetAddress mainIP;
		try {
			// get main IP address
			Socket sock = new Socket("www.lancs.ac.uk", 80);
			mainIP = sock.getLocalAddress();
//			logger.info("Host IP address : " + mainIP.getHostAddress());

			// get mac address of main network interface
			NetworkInterface network = NetworkInterface.getByInetAddress(mainIP);
			byte[] mac = network.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			return sb.toString();

		} catch (UnknownHostException e) {
			throw new SysMonException(SysMonException.UNKNOWN_HOST, e.getMessage());

		} catch (IOException e) {
			throw new SysMonException(SysMonException.IOEXCEPTION, e.getMessage());

		}
	}



	@Override
	public int getProcessID(String pname) {

		ProcessInfo p;
		ProcessInfo[] processes = monitor.processTable();

		for (int i = 0; i < processes.length; i++) {
			if (processes[i].getName().equalsIgnoreCase(pname))
				return processes[i].getPid();
		}
		return 0;
	}



	public List<Long> getProcessIDs(String pname) {
		// TODO Auto-generated method stub
		return null;
	}



	public long getJavaProcessID(String java_proc_name) {
		
		return 0;
	}



	@Override
	public String getHardwareAddress(NetworkInterface net_intf) throws SysMonException {
		InetAddress mainIP;
		try {

			byte[] mac = net_intf.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			return sb.toString();

		} catch (IOException e) {
			throw new SysMonException(SysMonException.IOEXCEPTION, e.getMessage());

		}
	}



	@Override
	public double getLoadAverage() throws SysMonException {

//		CpuTimes initialTimes = monitor.cpuTimes();
//		
//		try {
//			Thread.sleep(500);
//			return monitor.cpuTimes().getCpuUsage(initialTimes);
//		} catch (InterruptedException e) {
//			throw new SysMonException(SysMonException.INTERRUPTED_EXCEPTION, e.getMessage());
//		}
		
		return operatingSystemMXBean.getSystemLoadAverage();

	}



	@Override
	public int getNumCPUs() {
		
		return monitor.numCpus();
	}



	@Override
	public long getPhysicalMemory() {
		
		return monitor.physical().getTotalBytes();
	}



	@Override
	public long getFreePhysicalMemory() {
		return monitor.physical().getFreeBytes();
	}



	@Override
	public int getCurrentPID() {
		
		return monitor.currentPid();
	}



	@Override
	public String getHostName() throws SysMonException {
		
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new SysMonException(SysMonException.UNKNOWN_HOST_EXCEPTION, e.getMessage());
		}
	}



}
