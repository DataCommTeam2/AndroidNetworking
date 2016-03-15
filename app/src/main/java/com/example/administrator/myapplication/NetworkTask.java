package com.example.administrator.myapplication;

import android.os.AsyncTask;

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
public class NetworkTask extends AsyncTask<String, Void, Void>{
    static private BufferedWriter out = null;
	Socket client;
	
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
	--	RETURNS:	void
	--
	--	NOTES:
	--	Handles all the network activities such as connect, disconnect, and send message
	--
	---------------------------------------------------------------------------------*/
    protected Void doInBackground(String... input) {
        try {
            if(input.length == 2) {
                client = new Socket(input[0], Integer.parseInt(input[1]));
                OutputStream outToServer = client.getOutputStream();
                out = new BufferedWriter(new OutputStreamWriter(outToServer));
            }else if(out != null){
                out.write(input[0]);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
