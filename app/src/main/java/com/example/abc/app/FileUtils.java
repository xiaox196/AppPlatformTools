package com.example.abc.app;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FileUtils {
	private String SDCardRoot;
	public static UpdateFrame ui;

	public FileUtils() {
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator;
	}

	public File createFileInSDCard(String fileName, String dir)
			throws IOException {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		System.out.println("file---->" + file);
		file.createNewFile();
		return file;
	}


	public File creatSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir + File.separator);
		System.out.println(dirFile.mkdirs());
		return dirFile;
	}


	public void isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);

		Log.v("xiaoming", "安装路径：" + SDCardRoot + path + File.separator + fileName);
		if(file.exists())
		{
			file.delete();
		}
	}


	public File write2SDFromInput(String path, String fileName,
								  HttpURLConnection input01) {

		File file = null;
		InputStream input=null;
		OutputStream output = null;

		try {
			input=input01.getInputStream();
			creatSDDir(path);
			file = createFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			if(input01.getResponseCode()==200)
			{
				int length=input01.getContentLength();
				int position=0;
				while ((temp = input.read(buffer)) != -1) {
					output.write(buffer, 0, temp);
					position=position+temp;
					ui.updatePrecess(length,position);
				}
				output.flush();
				ui.down();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(output!=null)
				{
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}


}