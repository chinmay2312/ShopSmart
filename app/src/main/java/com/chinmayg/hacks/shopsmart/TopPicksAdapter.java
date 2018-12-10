package com.chinmayg.hacks.shopsmart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
        private ItemClickListener clickListener;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            textShop = itemView.findViewById(R.id.topPick_shopname);
            itemShopName = itemView.findViewById(R.id.tv_toppick_item);
            textOfferDaysRemaining = itemView.findViewById(R.id.toppick_daysRem);
            shopPrice = itemView.findViewById(R.id.topPick_itemprice);
            
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
}
