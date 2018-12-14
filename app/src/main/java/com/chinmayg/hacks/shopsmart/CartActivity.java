package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends Activity {

    ListView lvcart;
    TextView cartTotal;
	CartItemAdapter cartItemAdapter;
	TextView tvCartShopName;
	
	TextView btn_addQuant,btn_subQuant;

    ArrayList<ShopItem> cart_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ee0202")));
        setTitle("Cart for Costco");

        lvcart = findViewById(R.id.cart_lv);
        tvCartShopName = findViewById(R.id.cart_shopname);
        cartTotal = findViewById(R.id.cart_total);
        btn_addQuant = findViewById(R.id.btn_addQuant);
        btn_subQuant = findViewById(R.id.btn_subQuant);

        cart_list = new ArrayList<>();
	
		String imgUrl = "https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg";

        ShopItem si = new ShopItem(5.8f, 3, "White eggs","Jewel Osco",1, imgUrl);
        ShopItem si2 = new ShopItem(3.2f, 6, "Whole wheat bread","Pete's",1, imgUrl);
        ShopItem si3 = new ShopItem(2.6f, 2, "Kirkland low-fat milk","Costco",1, imgUrl);
        cart_list.add(si);
        cart_list.add(si2);
        cart_list.add(si3);
        

        cartItemAdapter = new CartItemAdapter(this, cart_list);
        lvcart.setAdapter(cartItemAdapter);
        
        lvcart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				ShopItem remSi = cart_list.remove(i);
				cartItemAdapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "Removed " + remSi.getItemShopName(), Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	
		cartTotal.setText("$"+getCartTotal());

        Button btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
				checkoutIntent.putExtra("CART_TOTAL",getCartTotal());
				checkoutIntent.putExtra("SHOP_NAME","Costco Wholesale");
				checkoutIntent.putExtra("SHOP_ADDR","1430 S Ashland Ave, \nChicago, IL 60608");
				startActivity(checkoutIntent);
			}
		});
  
  
		tvCartShopName.setText(cart_list.get(0).getShopName());
    }
    
    void updateQuantity(char action, int pos)	{
    	int currQuant = cart_list.get(pos).getQuantity();
    	
    	switch(action)	{
			case '+':	{
				cart_list.get(pos).setQuantity(currQuant+1);
				cartItemAdapter.notifyDataSetChanged();
				break;
			}
			case '-':	{
				if(currQuant!=1) {
					cart_list.get(pos).setQuantity(currQuant - 1);
					cartItemAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
		cartTotal.setText("$"+getCartTotal());
	}
	
	void updateCartTotal()	{
	
	}
	
	float getCartTotal()	{
		float cartPrice = 0.0f;
		for(ShopItem ci:cart_list)  {
			cartPrice += ci.getShopPrice()*ci.getQuantity();
		}
		cartPrice = (float) Math.round(cartPrice*100f)/100f;
		
		return  cartPrice;
	}
	
	public void addQuant(View v)	{
    	Log.d("cart","Increment at "+v.getTag());
    	//int clickPos = (int)v.getTag();
    	updateQuantity('+',(int)v.getTag());
	}
	public void subQuant(View v)	{
		Log.d("cart","Decrement at "+v.getTag());
		//int clickPos = (int)v.getTag();
		updateQuantity('-',(int)v.getTag());
	}
}
