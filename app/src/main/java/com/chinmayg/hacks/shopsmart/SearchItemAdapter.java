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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchItemAdapter extends ArrayAdapter {

    private ArrayList<ShopItem> cartItemList;

    public SearchItemAdapter(@NonNull Context context, ArrayList<ShopItem> cartItemList) {
        super(context,0, cartItemList);

        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search, parent, false);

        TextView tvitemName = convertView.findViewById(R.id.tv_itemname_search);
        tvitemName.setText(cartItemList.get(pos).getItemShopName());
        
        ImageView iv_itemPicCart = convertView.findViewById(R.id.iv_itemimg_search);
        new ShowImage(iv_itemPicCart).execute(cartItemList.get(pos).getImgUrl());

        TextView tvshopName = convertView.findViewById(R.id.tv_shopname_search);
        tvshopName.setText(cartItemList.get(pos).getShopName());
        
        TextView itemPrice = convertView.findViewById(R.id.tv_price_search);
        itemPrice.setText("$"+Math.round(cartItemList.get(pos).getShopPrice()*cartItemList.get(pos).getQuantity()*100f)/100f);
        
        return convertView;
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
