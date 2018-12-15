package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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

public class SearchActivity extends Activity {

    RecyclerView dynaRecos;
    RecyclerView.LayoutManager dynaRecosLM;
    RecyclerView.Adapter dynaRecosAd;
    ArrayList<ShopItem> siArrL;

    ListView srchRes;
    ArrayList<ShopItem> srchResArrL;
    
    EditText etSearch;
	
	ArrayAdapter<String> adapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ef2d26")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		Log.d("searchactivity_create",""+this.getClass().toString());
        etSearch = findViewById(R.id.et_search);
        
        new PostDynaRecs(this).execute();

        Intent srchQryIntent = getIntent();
        String srchStr = srchQryIntent.getStringExtra("SEARCH_STRING");
        if(!srchStr.trim().isEmpty()) {
			etSearch.setText(srchStr);
			new PostSearch(this, srchStr).execute();
		}
		
		String imgUrl = "https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg";
		
        siArrL = new ArrayList<>();
        ShopItem si = new ShopItem(5.8f, 3, "White eggs","Jewel Osco",1, imgUrl);
        siArrL.add(si);
        ShopItem si2 = new ShopItem(3.2f, 6, "Whole wheat bread","Pete's", 1,imgUrl);
        siArrL.add(si2);
        ShopItem si3 = new ShopItem(2.6f, 2, "Kirkland low-fat milk","Costco",1,imgUrl);
        siArrL.add(si3);

        dynaRecos = findViewById(R.id.dyna_recos);
        //dynaRecos.setHasFixedSize(true);
        dynaRecosLM = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        dynaRecos.setLayoutManager(dynaRecosLM);
        //dynaRecosAd = new ShopItemAdapter(SearchActivity.this, siArrL);
        dynaRecosAd = new TopPicksAdapter(this, siArrL);
        dynaRecos.setAdapter(dynaRecosAd);
        /*dynaRecos.post(new Runnable() {
            @Override
            public void run() {
                int maxItemCount = dynaRecosAd.getItemCount()-1;
                for(int i=1;i<=maxItemCount;i++) {
                    dynaRecos.smoothScrollToPosition(i);
                    *//*try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*//*
                }
            }
        });*/


        srchRes = findViewById(R.id.search_res_list);
        srchResArrL = new ArrayList<>();
        srchResArrL.add(si);
        
		//adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, srchResArrL);
		adapter = new SearchItemAdapter(this, srchResArrL);
		srchRes.setAdapter(adapter);
        
        TextWatcher tw = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		
			}
	
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				//adapter.getFilter().filter(charSequence.toString().trim());
				/*replace by server-elasticsearch-call,
				 *update results to srchResArrL
				 * notify adapter of change in dataset
				 */
				//Log.d("et_change","");
				mytry(charSequence.toString());
				//new PostSearch(getCallingActivity()).execute();
			}
	
			@Override
			public void afterTextChanged(Editable editable) {
		
			}
		};
        etSearch.addTextChangedListener(tw);

    }
    
	void mytry(String str){
		new PostSearch(this, str).execute();
	}
    
    private static class PostSearch extends AsyncTask<String,Void, Void> {
        //private final Context context;
	
		String srchStr;
		JSONObject dailyRecJson=null;
		JSONArray dailyRecsArrJson=null;
        private WeakReference<SearchActivity> searchActivityWeakReference;
        private  WeakReference<Application> appRef;
        
        public PostSearch(SearchActivity context, String srchStr)	{
			//this.context = c;
			this.srchStr = srchStr;
			searchActivityWeakReference = new WeakReference<>(context);
		}
        
        @Override
        protected Void doInBackground(String... params) {
            
            HttpURLConnection conn=null;
            try{
                URL url = new URL("http://ec2-35-171-182-214.compute-1.amazonaws.com:3000/search");
                conn = (HttpURLConnection) url.openConnection();
                //String urlParams = "user=chin";
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                
                conn.setDoOutput(true);
                conn.setDoInput(true);
                
                JSONObject postParams = new JSONObject();
                postParams.put("name",srchStr);
                
                conn.connect();
                
                DataOutputStream dStream = new DataOutputStream(conn.getOutputStream());
                dStream.writeBytes(postParams.toString());
                dStream.flush();
                dStream.close();
                int resCode = conn.getResponseCode();
                
                Log.d("post_search","Response code: "+resCode);
                
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                
                Log.d("post_search","Output:"+responseOutput.toString());
                
                dailyRecJson = new JSONObject(responseOutput.toString());
                //Log.d("post_search","Rec-Type:"+dailyRecJson.get("recommendation_type"));
                dailyRecsArrJson = dailyRecJson.getJSONArray("candidates");
                
                
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
        	
        	SearchActivity searchActivity = searchActivityWeakReference.get();
        	if(searchActivity==null || searchActivity.isFinishing())
        		return;
	
			searchActivity.srchResArrL.clear();
			//Log.d("post_search","Json_arr="+dailyRecsArrJson.toString());
			if(dailyRecsArrJson!=null) {
				try {
					ShopItem si;
					for (int dailyRecIndex = 0; dailyRecIndex < dailyRecsArrJson.length(); dailyRecIndex++) {
						//searchActivity.srchResArrL.add(dailyRecsArrJson.getString(dailyRecIndex));
						JSONObject resObj = dailyRecsArrJson.getJSONObject(dailyRecIndex);
						String prodName = resObj.getString("name");
						String imgUrl = resObj.getString("url");
						float shopPrice = Float.valueOf(resObj.getString("price").substring(1));
						si = new ShopItem(1, shopPrice, 3, prodName,"Jewel Osco",1, imgUrl);
						searchActivity.srchResArrL.add(si);
						//Log.d("post_search",searchActivity.srchResArrL.get(0));
					}
				} catch (JSONException e)	{
					e.printStackTrace();
				}
			}
        	
            searchActivity.adapter.notifyDataSetChanged();
            //Toast.makeText(appRef, "Updated search results",Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }
	
	private static class PostDynaRecs extends AsyncTask<String,Void, Void> {
		//private final Context context;
		
		JSONObject dailyRecJson=null;
		JSONArray dailyRecsArrJson=null;
		private WeakReference<SearchActivity> searchActivityWeakReference;
		private  WeakReference<Application> appRef;
		
		public PostDynaRecs(SearchActivity context)	{
			//this.context = c;
			searchActivityWeakReference = new WeakReference<>(context);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			HttpURLConnection conn=null;
			try{
				URL url = new URL("http://ec2-35-171-182-214.compute-1.amazonaws.com:3000/dynamicrecs");
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
				
				Log.d("post_search","Response code: "+resCode);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				StringBuilder responseOutput = new StringBuilder();
				while((line = br.readLine()) != null ) {
					responseOutput.append(line);
				}
				br.close();
				
				Log.d("post_search","Output:"+responseOutput.toString());
				
				dailyRecJson = new JSONObject(responseOutput.toString());
				//Log.d("post_search","Rec-Type:"+dailyRecJson.get("recommendation_type"));
				dailyRecsArrJson = dailyRecJson.getJSONArray("recommendations");
				
				
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
			
			SearchActivity searchActivity = searchActivityWeakReference.get();
			if(searchActivity==null || searchActivity.isFinishing())
				return;
			
			searchActivity.siArrL.clear();
			//Log.d("post_search","Json_arr="+dailyRecsArrJson.toString());
			if(dailyRecsArrJson!=null) {
				try {
					ShopItem si;
					for (int dailyRecIndex = 0; dailyRecIndex < dailyRecsArrJson.length(); dailyRecIndex++) {
						//searchActivity.srchResArrL.add(dailyRecsArrJson.getString(dailyRecIndex));
						JSONObject resObj = dailyRecsArrJson.getJSONObject(dailyRecIndex);
						String prodName = resObj.getString("name");
						String imgUrl = resObj.getString("url");
						float shopPrice = Float.valueOf(resObj.getString("price").substring(1));
						si = new ShopItem(1, shopPrice, 3, prodName,"Jewel Osco",1, imgUrl);
						searchActivity.siArrL.add(si);
						//Log.d("post_search",searchActivity.srchResArrL.get(0));
					}
				} catch (JSONException e)	{
					e.printStackTrace();
				}
			}
			
			searchActivity.dynaRecosAd.notifyDataSetChanged();
			//Toast.makeText(appRef, "Updated search results",Toast.LENGTH_SHORT).show();
			super.onPostExecute(aVoid);
		}
	}
}
