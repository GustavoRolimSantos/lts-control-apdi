package converter.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.Constants;

public class LogUtils {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HH");

	public static void log(String log) {

		File logFile = new File(Constants.MAIN_DIRECTORY + "logs");

		if (!logFile.exists())
			logFile.mkdirs();
		
		logFile = new File(Constants.MAIN_DIRECTORY + "logs/log_" + dateFormat.format(new Date()) + ".txt");
		
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
			out.println(log);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
