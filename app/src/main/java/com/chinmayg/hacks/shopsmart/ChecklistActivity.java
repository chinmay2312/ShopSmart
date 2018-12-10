package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChecklistActivity extends Activity {
	
	EditText et_checklist;
	ListView lv_checklist;
	
	ArrayList<String> checklist_arrl;
	ArrayAdapter cl_ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklist);
		
		et_checklist =findViewById(R.id.et_checklist);
		lv_checklist = findViewById(R.id.checklist);
		
		checklist_arrl = new ArrayList<>();
		
		checklist_arrl.add("bread");
		
		cl_ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1, checklist_arrl);
		lv_checklist.setAdapter(cl_ad);
		
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}
	
	public void add2Checklist(View v) {
		
		String newItem = et_checklist.getText().toString().trim();
		
		if(newItem.isEmpty())
			Toast.makeText(getApplicationContext(),"Please enter an item",Toast.LENGTH_SHORT).show();
		else if(checklist_arrl.contains(newItem))
			Toast.makeText(getApplicationContext(),"Item already in checklist",Toast.LENGTH_SHORT).show();
		else {
			checklist_arrl.add(0,newItem);
			cl_ad.notifyDataSetChanged();
			
			et_checklist.setText("");
			Toast.makeText(getApplicationContext(), "Added " + newItem, Toast.LENGTH_SHORT).show();
			
			/*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/
		}
	}
}
