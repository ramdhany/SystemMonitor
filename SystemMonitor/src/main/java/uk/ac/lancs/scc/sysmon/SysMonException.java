package uk.ac.lancs.scc.sysmon;
/**
 * @author Rajiv Ramdhany
 *
 */
@SuppressWarnings("serial")
public class SysMonException extends Exception {
	public static final byte INVALID_PROPERTY			 		= 1;
	public static final byte UNKNOWN_HOST						= 2;
	public static final byte SOCKET_EXCEPTION 				= 3;
	public static final byte INTERRUPTED_EXCEPTION 				= 4;
	public static final byte IOEXCEPTION					 	= 5;
	public static final byte UNKNOWN_HOST_EXCEPTION			= 6;
	

	private final byte kind;

	public byte getKind() {
		return kind;
	}

	public SysMonException(byte kind)
	{
		this.kind = kind;
	}

	public SysMonException(byte kind, String msg)
	{
		super(msg);
		this.kind = kind;
	}
}


