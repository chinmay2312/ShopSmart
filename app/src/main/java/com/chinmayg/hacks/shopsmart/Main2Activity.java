package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends Activity {

    ListView topPicks, dailyRecom;
    boolean isTopPicksExpanded, isDailyRecomExpanded;

    final ArrayList<String> topPicks_arrl = new ArrayList<>();
    final ArrayList<String> dailyRecom_arrl = new ArrayList<>();

    CheckListAdapter topPicksAdapter;
    DailyRecomAdapter dailyRecomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	
		new PostRecomm(this).execute();
        
        topPicks = findViewById(R.id.top_picks);

        topPicks_arrl.add("bread");
        topPicks_arrl.add("butter");
        topPicks_arrl.add("ketchup");
        topPicks_arrl.add("milk");
        topPicks_arrl.add("mixed fruit jam");
        topPicks_arrl.add("salt");

        ViewGroup.LayoutParams params = topPicks.getLayoutParams();
        params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(topPicks_arrl.size(),5);
        topPicks.setLayoutParams(params);
        topPicks.requestLayout();
        isTopPicksExpanded = false;

        final ImageButton expandTopPicks = findViewById(R.id.expand_top_picks);
        expandTopPicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewGroup.LayoutParams params;

                if(!isTopPicksExpanded) {
                    params = topPicks.getLayoutParams();
                    //params.height = 110 * Math.min(topPicks_arrl.size(), 10);
                    params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(topPicks_arrl.size(),10);
                    topPicks.setLayoutParams(params);
                    topPicks.requestLayout();
                    isTopPicksExpanded = true;
                    expandTopPicks.setImageDrawable(getDrawable(R.drawable.ic_collapse));
                }
                else    {
                    params = topPicks.getLayoutParams();
                    //params.height = 109 * Math.min(topPicks_arrl.size(), 5);
                    params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(topPicks_arrl.size(),5);
                    topPicks.setLayoutParams(params);
                    topPicks.requestLayout();
                    isTopPicksExpanded = false;
                    expandTopPicks.setImageDrawable(getDrawable(R.drawable.ic_expand));
                }
            }
        });
        topPicksAdapter = new CheckListAdapter(this, topPicks_arrl);
        topPicks.setAdapter(topPicksAdapter);


        final EditText addSearch = findViewById(R.id.search_box);

        ImageButton btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newItem = addSearch.getText().toString().trim();

                if(newItem.isEmpty())
                    Toast.makeText(getApplicationContext(),"Please enter a search string",Toast.LENGTH_SHORT).show();
                else    {
                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    searchIntent.putExtra("SEARCH_STRING",newItem);
                    startActivity(searchIntent);
                }
            }
        });


        dailyRecom = findViewById(R.id.daily_recom);
        dailyRecom_arrl.add("jelly");
        dailyRecom_arrl.add("cookies");
        dailyRecom_arrl.add("sugar");
        dailyRecom_arrl.add("coffee");
        dailyRecomAdapter = new DailyRecomAdapter(this, dailyRecom_arrl);
        dailyRecom.setAdapter(dailyRecomAdapter);

        ViewGroup.LayoutParams paramsRecom = dailyRecom.getLayoutParams();
        paramsRecom.height = 109*Math.min(dailyRecom_arrl.size(),5);
        dailyRecom.setLayoutParams(paramsRecom);
        dailyRecom.requestLayout();
        isDailyRecomExpanded = false;

        final ImageButton expandDailyRecoms = findViewById(R.id.expand_daily_recoms);
        expandDailyRecoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewGroup.LayoutParams paramsRecom;

                if(!isDailyRecomExpanded) {
                    paramsRecom = dailyRecom.getLayoutParams();
                    //paramsRecom.height = 109*Math.min(dailyRecom_arrl.size(),15);
                    paramsRecom.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(dailyRecom_arrl.size(),10);
                    dailyRecom.setLayoutParams(paramsRecom);
                    dailyRecom.requestLayout();
                    isDailyRecomExpanded = true;
                    expandDailyRecoms.setImageDrawable(getDrawable(R.drawable.ic_collapse));
                }
                else    {
                    paramsRecom = dailyRecom.getLayoutParams();
                    //paramsRecom.height = 109*Math.min(dailyRecom_arrl.size(),5);
                    paramsRecom.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(dailyRecom_arrl.size(),5);
                    dailyRecom.setLayoutParams(paramsRecom);
                    dailyRecom.requestLayout();
                    isDailyRecomExpanded = false;
                    expandDailyRecoms.setImageDrawable(getDrawable(R.drawable.ic_expand));
                }
            }
        });


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
                startActivity(searchIntent);

                return true;
            }
            default:    {
                return super.onOptionsItemSelected(item);
            }
        }
    }
    
    private class PostRecomm extends AsyncTask<String,Void, Void>{
    	private final Context context;
    	
    	public PostRecomm(Context c)	{
    		this.context = c;
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
				
				JSONObject dailyRecJson = new JSONObject(responseOutput.toString());
				Log.d("post","Rec-Type:"+dailyRecJson.get("recommendation_type"));
				JSONArray dailyRecsArrJson = dailyRecJson.getJSONArray("recommendations");
				dailyRecom_arrl.clear();
				for(int dailyRecIndex=0;dailyRecIndex<dailyRecsArrJson.length();dailyRecIndex++)	{
					dailyRecom_arrl.add(dailyRecsArrJson.getString(dailyRecIndex));
				}
				
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
			dailyRecomAdapter.notifyDataSetChanged();
			Toast.makeText(context, "Updated recommendations",Toast.LENGTH_SHORT).show();
    		super.onPostExecute(aVoid);
		}
	}
}
