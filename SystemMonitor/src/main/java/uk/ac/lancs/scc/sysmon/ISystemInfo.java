/**
 * 
 */
package uk.ac.lancs.scc.sysmon;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Rajiv Ramdhany
 * @since 15/09/2013
 * 
 * An interface specifying operations to query system info
 * on a number of supported platforms e.g. Linux, Windows and MacOSX
 */
public interface ISystemInfo {
	
	/**
	 * get host's Operating System name
	 */
	public String getOSName();
	
	/**
	 * get host's Operating System version
	 */
	public String getOSVersion();
	
	/**
	 * get host's architecture
	 */
	public String getArch();
	
	
	/**
	 * get PID of process with name <code>pname</code> 
	 * @param pname
	 * @return
	 */
	public int getProcessID(String pname); 
	
		
	/**
	 * get list of network interfaces on host machine
	 * @return
	 * @throws SysMonException 
	 */
	public Enumeration<NetworkInterface> getNetworkInterfaces() throws SysMonException;
	
	/**
	 * get host machine's main IP address.
	 * @return
	 * @throws SysMonException 
	 */
	public InetAddress getHostIPAddress() throws SysMonException;
	
	/**
	 * get hardware address of main network interface
	 * @return
	 * @throws SysMonException 
	 */
	public String getHardwareAddress() throws SysMonException;
	
	
	/**
	 * get hardware address of network interface
	 * @return
	 * @throws SysMonException 
	 */
	public String getHardwareAddress(NetworkInterface net_intf) throws SysMonException;
	
	
	/**
	 * get the host's hostname
	 * @return
	 * @throws SysMonException 
	 */
	public String getHostName() throws SysMonException;
	
	/**
	 * get the load average of the host machine
	 * @return
	 * @throws SysMonException 
	 */
	public double getLoadAverage() throws SysMonException;
	
	
	/**
	 * get number of CPUs on host
	 * @return
	 */
	public int getNumCPUs();
	
	/**
	 * get total physical memory available on host
	 * @return
	 */
	public long getPhysicalMemory();
	
	/**
	 * get free physical memory available on host
	 * @return
	 */
	public long getFreePhysicalMemory();
	
	
	/**
	 * get this process's PID.
	 * @return
	 */
	public int getCurrentPID();
	
	
}
