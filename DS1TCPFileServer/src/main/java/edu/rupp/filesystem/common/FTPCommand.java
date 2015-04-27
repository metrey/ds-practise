/**
 * 
 */
package edu.rupp.filesystem.common;

/**
 * @author sok.pongsametrey
 * @version 1.0
 */
public enum FTPCommand {
	/**
	 * Get file from server
	 */
	GET("get", 0),
	/**
	 * Put file to server
	 */
	PUT("put", 0),
	/**
	 * List all files in server
	 */
	LIST("list", 0),
	/**
	 * Help
	 */
	HELP("help", 0),
	/**
	 * Quit
	 */
	QUIT("quit", 0);
	
	
    private final int typefin_defaut;
    private final String usage;
    
    FTPCommand(String usage, int typefin_default) {
        this.typefin_defaut = typefin_default;
        this.usage = usage;
    }
    
    /**
     * Defines if Financing Type is a default one 
     * @return the typefin_defaut
     */
    public int getTypefin_defaut() {
        return typefin_defaut;
    }

	public String getUsage() {
		return usage;
	}
	
	public String toString() {
		return usage;
	}
    /**
     * 
     * @param command
     * @return
     */
    public static FTPCommand getFtpCommand (String command) {
    	if (command == null || "".equals(command.trim())) {
    		System.out.println("Command requires, can't provide empty. Now use help command.");
    		return FTPCommand.HELP;
    	}
    	
    	if ("get".equalsIgnoreCase(command)) {
    		return FTPCommand.GET;
    	} else if ("put".equalsIgnoreCase(command)) {
    		return FTPCommand.PUT;
    	} else if ("list".equalsIgnoreCase(command)) {
    		return FTPCommand.LIST;
    	} else if ("help".equalsIgnoreCase(command)) {
    		return FTPCommand.HELP;
    	} else if ("quit".equalsIgnoreCase(command)) {
    		return FTPCommand.QUIT;
    	} else {
    		System.out.println("Unknown command [" + command + "], use help command by default.");
    		return FTPCommand.HELP;
    	}
    	
    }
}
