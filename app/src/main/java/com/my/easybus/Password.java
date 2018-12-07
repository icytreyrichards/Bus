package com.my.easybus;

import BusinessLogic.LogicClass;
import utility.Utility;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

@SuppressLint("NewApi")
public class Password extends AppCompatActivity implements  OnClickListener {

	private EditText old, npass, confirm;
	private Button change;
	private String newpassword,oldpassword,conpassword;
	private ProgressBar progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		// TODO Auto-generated method stub
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("Change Password");
		setSupportActionBar(toolbar);
		old = (EditText) findViewById(R.id.editTexoldpassword);
		npass = (EditText) findViewById(R.id.editTextpnewpassword);
		confirm=(EditText)findViewById(R.id.editTextpnewpasswordconfirm);
		change = (Button) findViewById(R.id.buttonchangepassword);
		progress=(ProgressBar)findViewById(R.id.progressBar1);
		change.setOnClickListener(this);
	}

	public class ChangeTast extends
			AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... arg0) {
			return LogicClass.ChangePassword(Password.this,oldpassword,newpassword);
		}

		// Show Dialog Box with Progress bar
		@Override
		protected void onPostExecute(String result) {
			Utility.showAlert(result,Password.this);
			progress.setVisibility(View.GONE);
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.buttonchangepassword)
		{
			oldpassword=old.getText().toString();
			newpassword=npass.getText().toString();
			conpassword=confirm.getText().toString();

			ChangeTast task=new ChangeTast();
			if(!conpassword.equals(newpassword))
			{
				Snackbar.make(v,"passwords dont match",Snackbar.LENGTH_INDEFINITE).show();
			}
			else {
				task.execute();
			}
		}
	}
}
