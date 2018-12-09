package com.chinmayg.hacks.shopsmart;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends Activity {

    ListView lvcart;
    TextView cartTotal;

    ArrayList<ShopItem> cart_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        lvcart = findViewById(R.id.cart_lv);
        cartTotal = findViewById(R.id.cart_total);

        cart_list = new ArrayList<>();

        ShopItem si = new ShopItem(5.8f, 3, "White eggs","Jewel Osco");
        ShopItem si2 = new ShopItem(3.2f, 6, "Whole wheat bread","Pete's");
        ShopItem si3 = new ShopItem(2.6f, 2, "Kirkland low-fat milk","Costco");
        cart_list.add(si);
        cart_list.add(si2);
        cart_list.add(si3);

        CartItemAdapter cartItemAdapter = new CartItemAdapter(this, cart_list);
        lvcart.setAdapter(cartItemAdapter);

        float cartPrice = 0.0f;
        for(ShopItem ci:cart_list)  {
            cartPrice += ci.getShopPrice();
        }

        cartTotal.setText("Your cart total is : $"+cartPrice);

    }
}
