package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*---------------------------------------------------------------------------------
--	CLASS FILE:	    MainActivity.java -
--
--	PROGRAM:		Android GPS app
--
--	METHODS:      
--					protected void onCreate(Bundle savedInstanceState)
--					public void done(View v)
--	
--	DATE:			March 14, 2016
--
--	DESIGNER:		Tom Tang
--
--	PROGRAMMER:		Tom Tang
--
--	NOTES:
--	Initial activity that asks for user information.
--
---------------------------------------------------------------------------------*/
public class MainActivity extends AppCompatActivity {

	/*---------------------------------------------------------------------------------
	--	METHOD:     onCreate
	--
	--	DATE:		March 14, 2016
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	protected void onCreate(Bundle savedInstanceState)
	--
	--  PARAMETERS: Bundle savedInstanceState a Bundle containing the activity's previously frozen state
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Entry point of the activity.
	--
	---------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	/*---------------------------------------------------------------------------------
	--	METHOD:     done
	--
	--	DATE:		March 14, 2016
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	public void done(View v)
	--
	--  PARAMETERS: View v view of the calling button
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Button listener for the done button. This method starts the mapActivity
	--  and passes in the user input.
	--
	---------------------------------------------------------------------------------*/
    public void done(View v){
        Intent intent = new Intent(this, MapActivity.class);

        EditText editText = (EditText) findViewById(R.id.name);
        String message = editText.getText().toString();
		if (message.isEmpty())
		{
			Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
			return;
		}
        intent.putExtra("name", message);

        editText = (EditText) findViewById(R.id.ip);
        message = editText.getText().toString();
		if (message.isEmpty())
		{
			Toast.makeText(this, R.string.enter_address, Toast.LENGTH_SHORT).show();
			return;
		}
        intent.putExtra("ip", message);

        editText = (EditText) findViewById(R.id.port);
        message = 0 + editText.getText().toString();
		if (message.isEmpty())
		{
			Toast.makeText(this, R.string.enter_port, Toast.LENGTH_SHORT).show();
			return;
		}
        intent.putExtra("port", message);

        startActivity(intent);
    }
}
