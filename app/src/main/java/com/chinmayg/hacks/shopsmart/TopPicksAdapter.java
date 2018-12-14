package com.chinmayg.hacks.shopsmart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TopPicksAdapter extends RecyclerView.Adapter<TopPicksAdapter.ViewHolder> {

    private ArrayList<ShopItem> toppicks_arrl;
    private Context c;

    public TopPicksAdapter(@NonNull Context context, ArrayList<ShopItem> toppicks_arrl) {
        this.c = context;
        this.toppicks_arrl = toppicks_arrl;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.toppicks_item, parent, false);
		return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.textShop.setText(toppicks_arrl.get(position).getShopName());
		holder.shopPrice.setText("$"+toppicks_arrl.get(position).getShopPrice());
		int daysRem = toppicks_arrl.get(position).getOfferDaysRemaining();
		if(daysRem==1)
			holder.textOfferDaysRemaining.setText("Last day!");
		else
			holder.textOfferDaysRemaining.setText(daysRem+" days left");
		final String itemName = toppicks_arrl.get(position).getItemShopName();
		holder.itemShopName.setText(itemName);
		
		//new ShowImage(holder.prodImg).execute("https://target.scene7.com/is/image/Target/GUEST_1d0330d7-eb98-413f-9f3d-c5bf6d51db3b?wid=488&hei=488&fmt=pjpeg");
		new ShowImage(holder.prodImg).execute(toppicks_arrl.get(position).getImgUrl());
    
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Toast.makeText(c, "Added "+itemName+" to cart",Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return toppicks_arrl.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        
        public TextView textShop,itemShopName, textOfferDaysRemaining, shopPrice;
        public ImageView prodImg;
        private ItemClickListener clickListener;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            textShop = itemView.findViewById(R.id.topPick_shopname);
            itemShopName = itemView.findViewById(R.id.tv_toppick_item);
            textOfferDaysRemaining = itemView.findViewById(R.id.toppick_daysRem);
            shopPrice = itemView.findViewById(R.id.topPick_itemprice);
            prodImg = itemView.findViewById(R.id.iv_toppick_item);
            
            itemView.setOnClickListener(this);
        }
        
        public void setClickListener(ItemClickListener itemClickListener)   {
            this.clickListener = itemClickListener;
        }
        
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition());
        }
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
