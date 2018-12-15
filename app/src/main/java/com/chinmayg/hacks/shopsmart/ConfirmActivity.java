package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfirmActivity extends Activity {
	
	TextView tv_confCode;
	ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ee0202")));
		
		tv_confCode = findViewById(R.id.tv_confCode);
		
		Intent intent = getIntent();
		String confirmCode = ""+intent.getIntExtra("CONFIRM_CODE",123456);
		tv_confCode.setText(confirmCode);
		
		String timeSlot = intent.getStringExtra("TIME_SLOT");
		
		//call asynctask(with WeakReference) to send request to server
		
		//https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg
		iv = findViewById(R.id.try_image);
		//new ShowImage(this).execute();
		
		String text=""; // Whatever you need to encode in the QR code
		String jsonStr="";
		try {
			jsonStr = new JSONObject()
					.put("time-slot",timeSlot)
					.put("confirm-code",confirmCode)
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		try {
			BitMatrix bitMatrix = multiFormatWriter.encode(jsonStr, BarcodeFormat.QR_CODE,200,200);
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
			Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
			iv.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
	}
	
}
