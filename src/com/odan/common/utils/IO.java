package com.odan.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.odan.common.application.Application;

public class IO {
	public static String loadFile(String path) {
		String result = null;
		try {

			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();

			result = stringBuffer.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String loadSchema(String schema) {
		ServletContext sc = Application.getServletContext();
		String output = "";
		try {
			String path = sc.getRealPath("/schema/" + schema + ".json");
			output = loadFile(path);
		} catch (Exception ex) {
			System.out.println(ex);
			// Do nothing
		}
		return output;
	}

	public static String loadUI(String url) {
		ServletContext sc = Application.getServletContext();
		String output = "";
		try {
			String path = sc.getRealPath("/ui/" + url);
			output = loadFile(path);
		} catch (Exception ex) {
			System.out.println(url);
			System.out.println(ex);
			// Do nothing
		}
		return output;
	}

	public static String uploadAttachment(File attachment, String uploadName, String folder) {
		String name = DateTime.getCurrentTimestamp().toString().replace("-", "").replace(" ", "").replace(":", "")
				.replace(".", "");
		try {

			ServletContext sc = Application.getServletContext();

			String output = "";
			String extension = FilenameUtils.getExtension(uploadName);
			if(!extension.equals("")){
				name += "." + extension;
			}

			String path = sc.getRealPath("/upload/"+folder+"/" + name);
			File fileToCreate = new File(path);

			FileUtils.copyFile(attachment, fileToCreate);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			name = null;
		} catch (IOException e) {
			e.printStackTrace();
			name = null;
		}
		return name;
	}
	
	public static boolean deleteAttachment(String name, String folder) {
		boolean success = false;
		ServletContext sc = Application.getServletContext();

		String path = sc.getRealPath("/upload/"+folder+"/" + name);
		File file = new File(path);
		
		if(file.delete()){
			success = true;
		}
		
		return success;
	}

}
