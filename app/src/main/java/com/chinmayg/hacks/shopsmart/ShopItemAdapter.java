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

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {

    private Context c;
    private ArrayList<ShopItem> siArrL;

    public ShopItemAdapter(Context c, ArrayList<ShopItem> siArrL)    {
        this.c = c;
        this.siArrL = siArrL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textShop.setText(siArrL.get(position).getShopName());
        holder.shopPrice.setText("$"+siArrL.get(position).getShopPrice());
        holder.textOfferDaysRemaining.setText(siArrL.get(position).getOfferDaysRemaining()+" day(s) remaining");
        final String itemName = siArrL.get(position).getItemShopName();
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
        return siArrL.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView textShop,itemShopName, textOfferDaysRemaining, shopPrice;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            textShop = itemView.findViewById(R.id.textShop);
            itemShopName = itemView.findViewById(R.id.itemShopName);
            textOfferDaysRemaining = itemView.findViewById(R.id.textOfferDaysRemaining);
            shopPrice = itemView.findViewById(R.id.shopPrice);

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
