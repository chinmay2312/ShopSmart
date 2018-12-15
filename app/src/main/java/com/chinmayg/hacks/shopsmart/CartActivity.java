package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
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
		String whiteeggsurl = "https://preparedfoodphotos.com/samples/MDQ1ODNkNWJlNjA1YjU=/NDczZDViZTYwNWI1/Eighteen-White-Eggs-in-Carton.jpg";
		String breadurl ="https://dye1fo42o13sl.cloudfront.net/product-images/c9x83O3AI3Yidgm03uToasoQcoXxm6vRx4Ii4gpZ.jpg";
		String milkurl = "https://d1fywv0iz2cv2w.cloudfront.net/product-image/v201610/17793e7e2248c0f5bae30dfda8a5760e.jpeg";

        ShopItem si = new ShopItem(5.8f, 3, "White eggs","Pete's",1, whiteeggsurl);
        ShopItem si2 = new ShopItem(3.2f, 6, "Whole wheat bread","Pete's",1, breadurl);
        ShopItem si3 = new ShopItem(2.6f, 2, "Kirkland low-fat milk","Pete's",1, milkurl);
        cart_list.add(si);
        cart_list.add(si2);
        cart_list.add(si3);
        

        cartItemAdapter = new CartItemAdapter(this, cart_list);
        lvcart.setAdapter(cartItemAdapter);
        //new GetCart(this).execute();
        
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
	
	private static class GetCart extends AsyncTask<String,Void, Void> {
		JSONObject dailyRecJson=null;
		JSONArray dailyRecsArrJson=null;
		
		private WeakReference<CartActivity> main2ActivityWeakReference;
		
		public GetCart(CartActivity c)	{
			//this.context = c;
			main2ActivityWeakReference = new WeakReference<>(c);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			HttpURLConnection conn=null;
			try{
				URL url = new URL("http://ec2-3-82-10-206.compute-1.amazonaws.com:3000/cart");
				conn = (HttpURLConnection) url.openConnection();
				//String urlParams = "user=chin";
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
				
				conn.setDoOutput(true);
				conn.setDoInput(true);
				
				JSONObject postParams = new JSONObject();
				postParams.put("username","abel");
				
				conn.connect();
				
				DataOutputStream dStream = new DataOutputStream(conn.getOutputStream());
				dStream.writeBytes(postParams.toString());
				dStream.flush();
				dStream.close();
				int resCode = conn.getResponseCode();
				
				Log.d("get-cart","Response code: "+resCode);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				StringBuilder responseOutput = new StringBuilder();
				while((line = br.readLine()) != null ) {
					responseOutput.append(line);
				}
				br.close();
				
				Log.d("get-cart","Output:"+responseOutput.toString());
				
				dailyRecJson = new JSONObject(responseOutput.toString());
				//Log.d("post-cart","Rec-Type:"+dailyRecJson.get("recommendation_type"));
				
				
			} catch (IOException | JSONException e)	{
				e.printStackTrace();
			} finally {
				if(conn!=null)
					conn.disconnect();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void aVoid) {
			
			CartActivity main2Activity = main2ActivityWeakReference.get();
			if(main2Activity==null || main2Activity.isFinishing())
				return;
			
			main2Activity.cart_list.clear();
			try {
				//dailyRecsArrJson = dailyRecJson.getJSONArray("products");
				JSONArray prodnames = dailyRecJson.getJSONArray("product_name");
				//JSONArray prodnames
				Log.d("get-cart","");
				JSONArray urls = dailyRecJson.getJSONArray("url");
				JSONArray prices = dailyRecJson.getJSONArray("price");
				ShopItem si;
				for(int dailyRecIndex=0;dailyRecIndex<dailyRecsArrJson.length();dailyRecIndex++)	{
					//JSONObject jsonObject = dailyRecsArrJson.getJSONObject(dailyRecIndex);
					String imgUrl = urls.get(dailyRecIndex).toString();
					//String imgUrl = "https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg";
					String prodName = prodnames.getString(dailyRecIndex);
					//float itemPrice = Float.valueOf(prices.getDouble(dailyRecIndex));//Float.valueOf(jsonObject.getString("price").substring(1));
					float itemPrice = BigDecimal.valueOf(prices.getDouble(dailyRecIndex)).floatValue();
					si = new ShopItem(itemPrice, 3, prodName,"Pete's",dailyRecIndex+1, imgUrl);
					//si = new ShopItem(5.8f, 3, "White eggs","Pete's",dailyRecIndex+1);
					main2Activity.cart_list.add(si);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			main2Activity.cartItemAdapter.notifyDataSetChanged();
			//Toast.makeText(context, "Updated recommendations",Toast.LENGTH_SHORT).show();
			super.onPostExecute(aVoid);
		}
	}
}
