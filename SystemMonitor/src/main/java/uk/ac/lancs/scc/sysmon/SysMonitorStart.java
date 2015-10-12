package uk.ac.lancs.scc.sysmon;


/**
 * 
 * @author Rajiv Ramdhany
 *
 * Class for instantiating and running the SystemMonitor
 */
public class SysMonitorStart 
{
	public static void main( String[] args )
	{
		try {
			SysMonitor sysmon = new SysMonitor();
			
			sysmon.start();
		} catch (SysMonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
