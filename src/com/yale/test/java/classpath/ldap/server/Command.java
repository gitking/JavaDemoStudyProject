package com.yale.test.java.classpath.ldap.server;

import com.beust.jcommander.Parameter;

/**
 * 依赖《jcommander-1.81.jar》
 * @author issuser
 *
 */
public class Command {
	@Parameter(names= {"-h", "--help"}, description = "Hello Info", help=true)
	public boolean help;
	
	@Parameter(names= {"--cmd"}, description = "Use Command")
	public String command;
	
	@Parameter(names = {"--reverse"}, description = "Reverse Shell IP and Port")
	public String reverse;
	
	@Parameter(names = {"--cc"}, description = "Use Common Collections ByPass JDK1.8.0_281-b09")
	public boolean cc;
}
