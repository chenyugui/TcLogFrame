package com.taichuan.code.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {
	public static void uncompress(ZipInputStream zipReader, String targetDir) throws IOException {
		ZipEntry zipEntry ;
		while ((zipEntry = zipReader.getNextEntry()) != null) {
			if (zipEntry.isDirectory()) {
				File tempDir = new File(targetDir, zipEntry.getName());
				if (!tempDir.exists()) {
					tempDir.mkdirs();
					try {
						Runtime.getRuntime().exec("chmod 777 "+tempDir.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				zipReader.closeEntry();
				uncompress(zipReader, targetDir);
			} else {
				File tempFile = new File(targetDir, zipEntry.getName());
				if (!tempFile.exists()) {
					tempFile.createNewFile();
					try {
						Runtime.getRuntime().exec("chmod 777 "+tempFile.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					BufferedOutputStream zipWriter = new BufferedOutputStream(new FileOutputStream(tempFile));
					byte[] cash = new byte[2048];
					int readed = 0;
					while ((readed = zipReader.read(cash)) > 0) {
						zipWriter.write(cash, 0, readed);
					}
					zipWriter.flush();
					zipWriter.close();
				}
			}
		}
	}
	
}
