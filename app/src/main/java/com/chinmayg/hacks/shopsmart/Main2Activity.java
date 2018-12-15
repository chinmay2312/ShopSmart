package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends Activity {

    RecyclerView rv_topPicks, rv_dailyRecom;
    RecyclerView.LayoutManager topPicksLM, dailyRecomLM;
    RecyclerView.Adapter topPicksAd, dailyRecomAdapter;
    
    final ArrayList<ShopItem> topPicks_arrl = new ArrayList<>();
    final ArrayList<ShopItem> dailyRecom_arrl = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ee0202")));
	
		new PostRecomm(this).execute();
		new PostTopPicks(this).execute();
        
        rv_topPicks = findViewById(R.id.top_picks);
		//dynaRecos.setHasFixedSize(true);
		topPicksLM = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
		LinearLayoutManager llm = new LinearLayoutManager(Main2Activity.this, LinearLayoutManager.HORIZONTAL, false);
		rv_topPicks.setLayoutManager(topPicksLM);
		rv_topPicks.setLayoutManager(llm);
		
		
		String imgUrl = "https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg";
	
		ShopItem si = new ShopItem(5.8f, 3, "White eggs","Pete's",1, imgUrl);
		ShopItem si2 = new ShopItem(3.2f, 6, "Whole wheat bread","Pete's",1, imgUrl);
		ShopItem si3 = new ShopItem(2.6f, 2, "Kirkland low-fat milk","Pete's",1, imgUrl);
		topPicks_arrl.add(si);
		topPicks_arrl.add(si2);
		topPicks_arrl.add(si3);
	
		topPicksAd = new TopPicksAdapter(Main2Activity.this, topPicks_arrl);
		rv_topPicks.setAdapter(topPicksAd);
		
		
		
        EditText addSearch = findViewById(R.id.search_box);
        addSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
				searchIntent.putExtra("SEARCH_STRING","");
				startActivity(searchIntent);
			}
		});


        rv_dailyRecom = findViewById(R.id.daily_recom);
        dailyRecomLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager llm2 = new LinearLayoutManager(Main2Activity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_dailyRecom.setLayoutManager(llm2);
        dailyRecom_arrl.add(si);
		dailyRecom_arrl.add(si2);
		dailyRecom_arrl.add(si3);
        dailyRecomAdapter = new TopPicksAdapter(this, dailyRecom_arrl);
        rv_dailyRecom.setAdapter(dailyRecomAdapter);

    }
    
    public void searchItem(View v)  {
        LinearLayout parentRow = (LinearLayout) v.getParent();
        TextView child = (TextView)parentRow.getChildAt(1);

        String newItem = child.getText().toString().trim();

        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        searchIntent.putExtra("SEARCH_STRING",newItem);
        startActivity(searchIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())   {
            case R.id.item_main:    {
            	
            	Intent clIntent = new Intent(this, ChecklistActivity.class);
            	startActivity(clIntent);

                return true;
            }
            case R.id.item_my_profile:  {

                return true;
            }
            case R.id.item_cart_menu:   {
                Intent cartIntent = new Intent(this, CartActivity.class);
                startActivity(cartIntent);

                return true;
            }
            case R.id.item_search:    {

                Intent searchIntent = new Intent(this, SearchActivity.class);
                searchIntent.putExtra("SEARCH_STRING","");
                startActivity(searchIntent);

                return true;
            }
            default:    {
                return super.onOptionsItemSelected(item);
            }
        }
    }
    
    private static class PostRecomm extends AsyncTask<String,Void, Void>{
    	JSONObject dailyRecJson=null;
    	JSONArray dailyRecsArrJson=null;
    	
    	private WeakReference<Main2Activity> main2ActivityWeakReference;
    	
    	public PostRecomm(Main2Activity c)	{
    		//this.context = c;
			main2ActivityWeakReference = new WeakReference<>(c);
		}
	
		@Override
		protected Void doInBackground(String... params) {
			
    		HttpURLConnection conn=null;
			try{
				URL url = new URL("http://ec2-35-171-182-214.compute-1.amazonaws.com:3000/recommendations");
				conn = (HttpURLConnection) url.openConnection();
				//String urlParams = "user=chin";
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
				
				conn.setDoOutput(true);
				conn.setDoInput(true);
				
				JSONObject postParams = new JSONObject();
				postParams.put("user","chin");
				
				conn.connect();
				
				DataOutputStream dStream = new DataOutputStream(conn.getOutputStream());
				dStream.writeBytes(postParams.toString());
				dStream.flush();
				dStream.close();
				int resCode = conn.getResponseCode();
				
				Log.d("post","Response code: "+resCode);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				StringBuilder responseOutput = new StringBuilder();
				while((line = br.readLine()) != null ) {
					responseOutput.append(line);
				}
				br.close();
				
				Log.d("post","Output:"+responseOutput.toString());
				
				dailyRecJson = new JSONObject(responseOutput.toString());
				Log.d("post","Rec-Type:"+dailyRecJson.get("recommendation_type"));
				
				
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
    		
    		Main2Activity main2Activity = main2ActivityWeakReference.get();
    		if(main2Activity==null || main2Activity.isFinishing())
    			return;
			
			main2Activity.dailyRecom_arrl.clear();
			try {
				dailyRecsArrJson = dailyRecJson.getJSONArray("recommendations");
				ShopItem si;
				for(int dailyRecIndex=0;dailyRecIndex<dailyRecsArrJson.length();dailyRecIndex++)	{
					JSONObject jsonObject = dailyRecsArrJson.getJSONObject(dailyRecIndex);
					String imgUrl = jsonObject.getString("url");
					//String imgUrl = "https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg";
					String prodName = jsonObject.getString("name");
					float itemPrice = Float.valueOf(jsonObject.getString("price").substring(1));
					si = new ShopItem(itemPrice, 3, prodName,"Pete's",dailyRecIndex+1, imgUrl);
					main2Activity.dailyRecom_arrl.add(si);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			main2Activity.dailyRecomAdapter.notifyDataSetChanged();
			//Toast.makeText(context, "Updated recommendations",Toast.LENGTH_SHORT).show();
    		super.onPostExecute(aVoid);
		}
	}
	
	private static class PostTopPicks extends AsyncTask<String,Void, Void>{
		JSONObject dailyRecJson=null;
		JSONArray dailyRecsArrJson=null;
		
		private WeakReference<Main2Activity> main2ActivityWeakReference;
		
		public PostTopPicks(Main2Activity c)	{
			//this.context = c;
			main2ActivityWeakReference = new WeakReference<>(c);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			HttpURLConnection conn=null;
			try{
				URL url = new URL("http://ec2-35-171-182-214.compute-1.amazonaws.com:3000/recommendations");
				conn = (HttpURLConnection) url.openConnection();
				//String urlParams = "user=chin";
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
				
				conn.setDoOutput(true);
				conn.setDoInput(true);
				
				JSONObject postParams = new JSONObject();
				postParams.put("user","chin");
				
				conn.connect();
				
				DataOutputStream dStream = new DataOutputStream(conn.getOutputStream());
				dStream.writeBytes(postParams.toString());
				dStream.flush();
				dStream.close();
				int resCode = conn.getResponseCode();
				
				Log.d("post","Response code: "+resCode);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				StringBuilder responseOutput = new StringBuilder();
				while((line = br.readLine()) != null ) {
					responseOutput.append(line);
				}
				br.close();
				
				Log.d("post","Output:"+responseOutput.toString());
				
				dailyRecJson = new JSONObject(responseOutput.toString());
				Log.d("post","Rec-Type:"+dailyRecJson.get("recommendation_type"));
				
				
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
			
			Main2Activity main2Activity = main2ActivityWeakReference.get();
			if(main2Activity==null || main2Activity.isFinishing())
				return;
			
			main2Activity.topPicks_arrl.clear();
			try {
				dailyRecsArrJson = dailyRecJson.getJSONArray("recommendations");
				ShopItem si;
				for(int dailyRecIndex=0;dailyRecIndex<dailyRecsArrJson.length();dailyRecIndex++)	{
					JSONObject jsonObject = dailyRecsArrJson.getJSONObject(dailyRecIndex);
					String imgUrl = jsonObject.getString("url");
					//String imgUrl = "https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg";
					String prodName = jsonObject.getString("name");
					float itemPrice = Float.valueOf(jsonObject.getString("price").substring(1));
					si = new ShopItem(itemPrice, 3, prodName,"Pete's",dailyRecIndex+1, imgUrl);
					//si = new ShopItem(5.8f, 3, "White eggs","Pete's",dailyRecIndex+1);
					main2Activity.topPicks_arrl.add(si);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			main2Activity.topPicksAd.notifyDataSetChanged();
			//Toast.makeText(context, "Updated recommendations",Toast.LENGTH_SHORT).show();
			super.onPostExecute(aVoid);
		}
	}
}
