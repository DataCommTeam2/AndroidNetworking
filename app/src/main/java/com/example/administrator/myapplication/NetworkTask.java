package com.example.administrator.myapplication;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Tom on 2016-03-07.
 */
public class NetworkTask extends AsyncTask<String, Void, Void>{
    static private DataOutputStream out = null;
    protected Void doInBackground(String... input) {
        try {
            if(input.length == 2) {
                Socket client = new Socket(input[0], Integer.parseInt(input[1]));
                OutputStream outToServer = client.getOutputStream();
                out = new DataOutputStream(outToServer);
            }else if(out != null){
                out.writeUTF(input[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
