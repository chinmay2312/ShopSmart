package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class CheckoutActivity extends Activity {
	
	TextView checkoutTotal,tv_shopName,tv_shopAddr;
	Button placeOrder;
	Spinner timeSlots;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ef2d26")));
		
		Intent intent = getIntent();
		String cartTotal = "$"+intent.getFloatExtra("CART_TOTAL",0.0f);
		String shopName = intent.getStringExtra("SHOP_NAME");
		String shopAddr = intent.getStringExtra("SHOP_ADDR");
		
		checkoutTotal = findViewById(R.id.tv_checkout_total);
		checkoutTotal.setText(cartTotal);
		
		tv_shopName = findViewById(R.id.tv_checkout_shopname);
		tv_shopName.setText(shopName);
		tv_shopAddr = findViewById(R.id.tv_checkout_shopaddr);
		tv_shopAddr.setText(shopAddr);
		
		timeSlots = findViewById(R.id.time_slots);
		ArrayList<String> timeSlotsArrl = new ArrayList<>();
		timeSlotsArrl.add("");
		timeSlotsArrl.add("10am - 12pm");
		timeSlotsArrl.add("12pm - 2pm");
		timeSlotsArrl.add("2pm - 4pm");
		timeSlotsArrl.add("4pm - 6pm");
		ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeSlotsArrl){
			@Override
			public boolean isEnabled(int position) {
				return (position>=2);
			}
			
			@Override
			public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				TextView tv = (TextView)v;
				tv.setTextColor(Color.GRAY);
				if(position>=2)
					tv.setTextColor(Color.BLACK);
				
				return v;
			}
		};
		timeSlots.setAdapter(timeSlotAdapter);
		
		placeOrder = findViewById(R.id.btn_place_order);
		placeOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent confirmIntent = new Intent(CheckoutActivity.this, ConfirmActivity.class);
				Random rnd = new Random();
				int n = 100000 + rnd.nextInt(900000);
				confirmIntent.putExtra("CONFIRM_CODE", n);
				startActivity(confirmIntent);
			}
		});
		
	}
}
