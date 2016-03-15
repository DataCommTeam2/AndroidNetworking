package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
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
	--	DATE:		March 14, 2014
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
		this.context = context.getApplicationContext();
	}

	/*---------------------------------------------------------------------------------
	--	METHOD:     doInBackground
	--
	--	DATE:		March 14, 2014
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
            if(input.length == 2) {
                client = new Socket(input[0], Integer.parseInt(input[1]));
				if(!client.isConnected()){
					return false;
				}
                OutputStream outToServer = client.getOutputStream();
                out = new BufferedWriter(new OutputStreamWriter(outToServer));
            }else if(input.length == 0){
				client.close();
			}else if(out != null){
                out.write(input[0]);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

	/*---------------------------------------------------------------------------------
	--	METHOD:     onPostExecute
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	protected void onPostExecute(Boolean result)
	--
	--  PARAMETERS: Boolean result result of doInBackground
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Executes when the server fails to connect.
	--
	---------------------------------------------------------------------------------*/
	protected void onPostExecute(Boolean result) {
		if (result == false){
			Toast.makeText(context, "Fail to connect to server",
					Toast.LENGTH_LONG).show();
			context.startActivity(new Intent(context, MainActivity.class));
		}
	}
}
