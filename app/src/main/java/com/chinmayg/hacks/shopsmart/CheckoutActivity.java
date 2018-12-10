package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckoutActivity extends Activity {
	
	TextView checkoutTotal,tv_shopName,tv_shopAddr;
	Button placeOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
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
		
		placeOrder = findViewById(R.id.btn_place_order);
		placeOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			
			}
		});
		
	}
}
