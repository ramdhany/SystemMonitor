/**
 * 
 */
package uk.ac.lancs.scc.sysmon;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Singleton AppConfiguration class. It gets property values from
 * configuration file
 * @author Rajiv Ramdhany
 * 15/09/2013
 */
public class AppConfiguration {
	
	private static final XMLConfiguration instance;
	
	
	static{
		try {
			instance = new XMLConfiguration();
			
			if (System.getProperty("configFile") == null)
						instance.load("config.xml");
			else
				instance.load(System.getProperty("configFile"));
			
		} catch (ConfigurationException e) {
				throw new RuntimeException(e.getMessage());
		} 
	}
	
	/**
	 * gets single instance of XMLConfiguration object 
	 * @return
	 */
	public static XMLConfiguration getInstance()
	{
		return instance;
	}
	
	/**
	 * gets a String value associated with the key <code>property</code>
	 * @param property property key
	 * @return property value
	 */
	public static String getString(String property)
	{
		return instance.getString(property);
	}
	
	/**
	 * gets an int value associated with the key <code>property</code>
	 * @param property property key
	 * @return property value
	 */
	public static int getInt(String property)
	{
		return instance.getInt(property);
	}
	
	/**
	 * gets a long value associated with the key <code>property</code>
	 * @param property property key
	 * @return property value
	 */
	public static long getLong(String property)
	{
		return instance.getLong(property);
	}
	
	/**
	 * gets a boolean value associated with the key <code>property</code>
	 * @param property property key
	 * @return property value
	 */
	public static boolean getBoolean(String property)
	{
		return instance.getBoolean(property);
	}
	
	/**
	 * gets sleeptime property value
	 */
	public static long getSleepTime()
	{
		return instance.getLong("sleeptime");
	}
	
	
	public static List<String> getProcessNames() throws SysMonException
	{
		String pnames = instance.getString("processes");
		
		if (pnames == null)
			throw new SysMonException(SysMonException.INVALID_PROPERTY,  AppConfiguration.class.getName() + "-getProcessNames(): Invalid property");
		
		StringTokenizer st = new StringTokenizer(pnames , " "); 
		
		if (st.countTokens() > 0)
		{
			ArrayList<String> processes = new ArrayList<String>();
			
			while (st.hasMoreTokens())
				processes.add(st.nextToken());
			
			return processes;
		}else
			return null;
		
	}
	
	/**
	 * gets filename for log file
	 * @return
	 */
	public static String getLogFileName() 
	{
		return instance.getString("logfile");
	}
	
	/**
	 * gets filename for system status
	 * @return
	 */
	public static String getSystemStatusFileName() 
	{
		return instance.getString("systemStatusFile");
	}
	
	
	
	private AppConfiguration(){ }
	
	
	
	
	
	
	
}
