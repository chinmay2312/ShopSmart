package com.chinmayg.hacks.shopsmart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
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
        
        ImageView iv_itemPicCart = convertView.findViewById(R.id.iv_itempic_cart);
        new ShowImage(iv_itemPicCart).execute(cartItemList.get(pos).getImgUrl());

        /*TextView tvshopName = convertView.findViewById(R.id.textShop_cart);
        tvshopName.setText(cartItemList.get(pos).getShopName());*/
        
        TextView itemPrice = convertView.findViewById(R.id.shopPrice_cart);
        itemPrice.setText("$"+Math.round(cartItemList.get(pos).getShopPrice()*cartItemList.get(pos).getQuantity()*100f)/100f);
        
        TextView itemQuant = convertView.findViewById(R.id.itemQuant_cart);
        itemQuant.setText(""+cartItemList.get(pos).getQuantity());
	
		TextView btnAddQuant = convertView.findViewById(R.id.btn_addQuant);
		btnAddQuant.setTag(pos);
		
		TextView btnSubQuant = convertView.findViewById(R.id.btn_subQuant);
		btnSubQuant.setTag(pos);
		
        return convertView;//super.getView(pos, convertView, parent);
    }
	
	private static class ShowImage extends AsyncTask<String,Void, Void> {
		Drawable d=null;
		
		private WeakReference<ImageView> imageViewWeakReference;
		
		public ShowImage(ImageView imageView)	{
			//this.context = c;
			imageViewWeakReference = new WeakReference<>(imageView);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			
			URL url = null;
			try {
				//url = new URL("https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg");
				url = new URL(params[0]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStream content = null;
			try {
				content = (InputStream)url.getContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
			d = Drawable.createFromStream(content , "src");
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void aVoid) {
			
			if(imageViewWeakReference !=null) {
				
				ImageView imageView = imageViewWeakReference.get();
				if (imageView == null)
					return;
				
				
				imageView.setImageDrawable(d);
				
			}
			
		}
	}
}
