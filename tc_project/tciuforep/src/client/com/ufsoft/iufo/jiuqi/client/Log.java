package com.ufsoft.iufo.jiuqi.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 日志输出类
 * @author syang
 *
 * 2007-12-11
 */
public class Log {
	private static Log log = null;
	private FileOutputStream out = null;
	private PrintWriter writer = null;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Calendar calender = GregorianCalendar.getInstance();
	private File workingFilePath = null;
	private Log(){
	}
	public static Log getInstance(){
		if(log == null){
			log = new Log();
		}
		return log;
	}
	
	public void log(String logInfo){
		
		if(writer == null){
			try{
				File file = new File(workingFilePath, "log.txt");
				if(!file.exists()){
					file.createNewFile();
				}
				out = new FileOutputStream(file);
				writer = new PrintWriter(out);
			}catch(Exception e){
				
			}
		}
		
		String output = dateFormat.format(calender.getTime()) + " "+logInfo;
//		String output = logInfo;
		if(writer != null){
			writer.println(output);
			writer.flush();
		}else{
			System.out.println(output);
		}
	}
	
	public void close(){
		if(writer != null){
			writer.close();
		}
		if(out != null){
			try{
				out.close();
			}catch(IOException ignore){				
			}
		}
	}
	public File getWorkingFilePath() {
		return workingFilePath;
	}
	public void setWorkingFilePath(File workingFilePath) {
		this.workingFilePath = workingFilePath;
	}
}
