package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends Activity {

    ListView checklist, dailyRecom;
    boolean isChecklistExpanded, isDailyRecomExpanded;

    final ArrayList<String> checklist_arrl = new ArrayList<>();
    final ArrayList<String> dailyRecom_arrl = new ArrayList<>();

    CheckListAdapter checkListAdapter;
    DailyRecomAdapter dailyRecomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        checklist = findViewById(R.id.checklist);

        checklist_arrl.add("bread");
        checklist_arrl.add("butter");
        checklist_arrl.add("ketchup");
        checklist_arrl.add("milk");
        checklist_arrl.add("mixed fruit jam");
        checklist_arrl.add("salt");

        ViewGroup.LayoutParams params = checklist.getLayoutParams();
        params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(checklist_arrl.size(),5);
        checklist.setLayoutParams(params);
        checklist.requestLayout();
        isChecklistExpanded = false;

        final ImageButton expandChecklist = findViewById(R.id.expand_checklist);
        expandChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewGroup.LayoutParams params;

                if(!isChecklistExpanded) {
                    params = checklist.getLayoutParams();
                    //params.height = 110 * Math.min(checklist_arrl.size(), 10);
                    params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(checklist_arrl.size(),10);
                    checklist.setLayoutParams(params);
                    checklist.requestLayout();
                    isChecklistExpanded = true;
                    expandChecklist.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
                else    {
                    params = checklist.getLayoutParams();
                    //params.height = 109 * Math.min(checklist_arrl.size(), 5);
                    params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(checklist_arrl.size(),5);
                    checklist.setLayoutParams(params);
                    checklist.requestLayout();
                    isChecklistExpanded = false;
                    expandChecklist.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                }
            }
        });
        checkListAdapter = new CheckListAdapter(this, checklist_arrl);
        checklist.setAdapter(checkListAdapter);


        final EditText addSearch = findViewById(R.id.search_box);
        ImageButton add2checklist = findViewById(R.id.list_add);
        add2checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newItem = addSearch.getText().toString().trim();

                if(newItem.isEmpty())
                    Toast.makeText(getApplicationContext(),"Please enter an item",Toast.LENGTH_SHORT).show();
                else {
                    checklist_arrl.add(newItem);
                    checkListAdapter.notifyDataSetChanged();

                    addSearch.setText("");
                    Toast.makeText(getApplicationContext(), "Added " + newItem, Toast.LENGTH_SHORT).show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

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
                    expandDailyRecoms.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
                else    {
                    paramsRecom = dailyRecom.getLayoutParams();
                    //paramsRecom.height = 109*Math.min(dailyRecom_arrl.size(),5);
                    paramsRecom.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(dailyRecom_arrl.size(),5);
                    dailyRecom.setLayoutParams(paramsRecom);
                    dailyRecom.requestLayout();
                    isDailyRecomExpanded = false;
                    expandDailyRecoms.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                }
            }
        });


    }

    public void add2Checklist(View v) {
        LinearLayout parentRow = (LinearLayout) v.getParent();
        TextView child = (TextView)parentRow.getChildAt(1);

        String newItem = child.getText().toString().trim();

        checklist_arrl.add(newItem);
        checkListAdapter.notifyDataSetChanged();
        ViewGroup.LayoutParams params = checklist.getLayoutParams();
        params.height = Math.round(40*getResources().getDisplayMetrics().density)*Math.min(checklist_arrl.size(),5);
        checklist.setLayoutParams(params);
        checklist.requestLayout();

        dailyRecom_arrl.remove(newItem);
        dailyRecomAdapter.notifyDataSetChanged();
        ViewGroup.LayoutParams paramsRecom = dailyRecom.getLayoutParams();
        paramsRecom.height = 109*Math.min(dailyRecom_arrl.size(),5);
        dailyRecom.setLayoutParams(paramsRecom);
        dailyRecom.requestLayout();

        Toast.makeText(getApplicationContext(),"Added"+newItem,Toast.LENGTH_SHORT).show();
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
}
