package frc.molib.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for writing to the Driver Station Console.
 */
public class Console {
	private Console() { throw new AssertionError("Utility Class"); }

	/**
	 * Gets a formatted timestamp at the current time
	 * to be appended to messages logged in the console.
	 * @return Formatted timestamp
	 */
	private static String getTimeStamp() { return new SimpleDateFormat("HH.mm.ss").format(new Date()); }

	/**
	 * Prints a formatted message to the Console.
	 * @param message Message to be logged
	 */
	public static void logMsg(String message) { System.out.println("[Log] [" + getTimeStamp() + "] " + message); }

	/**
	 * Prints a formatted error message to the Console.
	 * @param message Message to be logged
	 */
	public static void logErr(String message) { System.err.println("[Err] [" + getTimeStamp() + "] " + message); }
}