package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

/*---------------------------------------------------------------------------------
--	CLASS FILE:	    NetworkTask.java -
--
--	PROGRAM:		Android GPS app
--
--	METHODS:      
--					protected Void doInBackground(String... input)
--	
--	DATE:			March 14, 2016
--
--	DESIGNER:		Tom Tang
--
--	PROGRAMMER:		Tom Tang
--
--	NOTES:
--	AsyncTask that handles all the network activities
--
---------------------------------------------------------------------------------*/
public class NetworkTask extends AsyncTask<String, Void, Boolean>{
    
	static private BufferedWriter out = null;
	Socket client;
	Context context;

	/*---------------------------------------------------------------------------------
	--	Constructor:     NetworkTask
	--
	--	DATE:		March 14, 2016
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	public NetworkTask(Context context)
	--
	--  PARAMETERS: Context context context from calling parent
	--
	--	NOTES:
	--	Makes a NetworkTask object
	--
	---------------------------------------------------------------------------------*/
	public NetworkTask(Context context) {
		this.context = context;
	}

	/*---------------------------------------------------------------------------------
	--	METHOD:     doInBackground
	--
	--	DATE:		March 14, 2016
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	protected Void doInBackground(String... input)
	--
	--  PARAMETERS: String... input array of strings passed in from calling function.
	--
	--	RETURNS:	If the AsyncTask has been successfully completed
	--
	--	NOTES:
	--	Handles all the network activities such as connect, disconnect, and send message
	--
	---------------------------------------------------------------------------------*/
    protected Boolean doInBackground(String... input) {
        try {
			if (input.length == 2) {
				client = new Socket();
				client.connect(new InetSocketAddress(input[0], Integer.parseInt(input[1])), 500);
				OutputStream outToServer = client.getOutputStream();
				out = new BufferedWriter(new OutputStreamWriter(outToServer));
			} else if (input.length == 0) {
				client.close();
			} else if (out != null) {
				out.write(input[0]);
				out.flush();
			}
		}catch (Exception e) {
			return false;
		}
        return true;
    }

	/*---------------------------------------------------------------------------------
	--	METHOD:     onPostExecute
	--
	--	DATE:		March 14, 2016
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	protected void onPostExecute(Boolean result)
	--
	--  PARAMETERS: Boolean connected result of doInBackground
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Executes when the server fails to connect.
	--
	---------------------------------------------------------------------------------*/
	protected void onPostExecute(Boolean connected) {
		if (!connected){
			Toast.makeText(context, context.getString(R.string.connection_error),
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(context, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
