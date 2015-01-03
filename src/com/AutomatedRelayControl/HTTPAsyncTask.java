package com.AutomatedRelayControl;

import java.text.DateFormat;
import java.util.Date;

import android.os.AsyncTask;

class HttpAsyncTask extends AsyncTask<String, Void, String> {
	
	WriteFile wf;
	@Override
	protected String doInBackground(String... urls) {
		return GETRequest.GET(urls[0]);
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		if (!result.isEmpty()){
		wf = new WriteFile();
		wf.WriteData(result);
		}
		
		//String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		//wf.WriteData(currentDateTimeString + result.charAt(25));
		//String json = wf.parseJSON(result);
		//wf.WriteData(currentDateTimeString);
		//wf.WriteData(json);
		//wf.WriteData("\n");
	}
}