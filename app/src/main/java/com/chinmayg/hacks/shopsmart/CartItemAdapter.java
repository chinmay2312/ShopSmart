package com.chinmayg.hacks.shopsmart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CartItemAdapter extends ArrayAdapter {

    private ArrayList<ShopItem> cartItemList;

    public CartItemAdapter(@NonNull Context context, ArrayList<ShopItem> cartItemList) {
        super(context,0, cartItemList);

        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);

        TextView tvitemName = convertView.findViewById(R.id.itemShopName_cart);
        tvitemName.setText(cartItemList.get(pos).getItemShopName());

        /*TextView tvshopName = convertView.findViewById(R.id.textShop_cart);
        tvshopName.setText(cartItemList.get(pos).getShopName());
*/
        TextView daysRem = convertView.findViewById(R.id.textOfferDaysRemaining_cart);
        daysRem.setText(cartItemList.get(pos).getOfferDaysRemaining()+" day(s) remaining");

        TextView itemPrice = convertView.findViewById(R.id.shopPrice_cart);
        itemPrice.setText("$"+Math.round(cartItemList.get(pos).getShopPrice()*cartItemList.get(pos).getQuantity()*100f)/100f);
        
        TextView itemQuant = convertView.findViewById(R.id.itemQuant_cart);
        itemQuant.setText(""+cartItemList.get(pos).getQuantity());
	
		Button btnAddQuant = convertView.findViewById(R.id.btn_addQuant);
		btnAddQuant.setTag(pos);
		
		Button btnSubQuant = convertView.findViewById(R.id.btn_subQuant);
		btnSubQuant.setTag(pos);
		
        return convertView;//super.getView(pos, convertView, parent);
    }
}
