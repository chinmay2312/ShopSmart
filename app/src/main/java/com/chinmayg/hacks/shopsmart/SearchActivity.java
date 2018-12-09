package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    RecyclerView dynaRecos;
    RecyclerView.LayoutManager dynaRecosLM;
    RecyclerView.Adapter dynaRecosAd;
    ArrayList<ShopItem> siArrL;

    ListView srchRes;
    ArrayList<String> srchResArrL;

    EditText etSearch;

    String[] res = {
            "Apple",
            "Ripe apples",
            "Chocolate crispies",
            "Hershey's chocolate",
            "Banana",
            "Banana flavored yoghurt",
            "Grapes",
            "Guava",
            "Kiwi",
            "Lychee",
            "Mango",
            "Melon",
            "Orange",
            "Minute maid : Orange flavor",
            "Papaya", "Pineapple",
            "whole milk",
            "Kirkland 2% fat milk",
            "crystal sugar",
            "Water Melon"
    };
    ArrayList<String> arr_srchRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etSearch = findViewById(R.id.et_search);

        Intent srchQryIntent = getIntent();
        String srchStr = srchQryIntent.getStringExtra("SEARCH_STRING");
        etSearch.setText(srchStr);


        siArrL = new ArrayList<>();
        ShopItem si = new ShopItem(5.8f, 3, "White eggs","Jewel Osco");
        siArrL.add(si);
        ShopItem si2 = new ShopItem(3.2f, 6, "Whole wheat bread","Pete's");
        siArrL.add(si2);
        ShopItem si3 = new ShopItem(2.6f, 2, "Kirkland low-fat milk","Costco");
        siArrL.add(si3);

        dynaRecos = findViewById(R.id.dyna_recos);
        //dynaRecos.setHasFixedSize(true);
        dynaRecosLM = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        dynaRecos.setLayoutManager(dynaRecosLM);
        dynaRecosAd = new ShopItemAdapter(SearchActivity.this, siArrL);
        dynaRecos.setAdapter(dynaRecosAd);
        dynaRecos.post(new Runnable() {
            @Override
            public void run() {
                int maxItemCount = dynaRecosAd.getItemCount()-1;
                for(int i=1;i<=maxItemCount;i++) {
                    dynaRecos.smoothScrollToPosition(i);
                    /*try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });


        srchRes = findViewById(R.id.search_res_list);
        srchResArrL = new ArrayList<>();
        srchResArrL.add("banana");
        srchResArrL.add("baking soda");

        final ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, res);
        srchRes.setAdapter(adapter);


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString().trim());
                /*replace by server-elasticsearch-call,
                 *update results to srchResArrL
                 * notify adapter of change in dataset
                 */
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
