package com.AutomatedRelayControl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

public class WriteFile implements InterfaceWrite {

	Context context;

	public WriteFile() {
	}

	public void WriteData(String data) {
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Android/data/" + "/RELAY/";
		// if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
		try {
			boolean exists = (new File(path)).exists();
			if (!exists) {
				new File(path).mkdirs();
			}
			// Open output stream
			FileOutputStream fOut = new FileOutputStream(path
					+ "relay_logs.txt", true);
			// write integers as separated ascii's
			data = parseJSON(data);//
			
			data += '\n' + currentDateTimeString + "\n\n";
			//fOut.write(("\n").getBytes());
			//fOut.write((currentDateTimeString).getBytes());
			//fOut.write(("\n").getBytes());
			//data = data + '\n';
			fOut.write((data).getBytes());

			// Close output stream
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String parseJSON(String data){
		String buffer = data.substring(15,27);
		if (buffer.endsWith("1")){
			return "RELAY IS TURNED ON";
		}
		else
			return "RELAY IS TURNED OFF";
	}

}
