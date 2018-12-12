package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmActivity extends Activity {
	
	TextView tv_confCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		
		tv_confCode = findViewById(R.id.tv_confCode);
		
		Intent intent = getIntent();
		String confirmCode = ""+intent.getIntExtra("CONFIRM_CODE",123456);
		tv_confCode.setText(confirmCode);
		//call asynctask(with WeakReference) to send request to server
	}
}
