package com.developers.vmrapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developers.vmrapp.databinding.LayoutItemMarketBinding;
import com.developers.vmrapp.fragments.main.MarketFragment;
import com.developers.vmrapp.models.DummyMarket;
import com.developers.vmrapp.models.Product_sold_Model;
import com.developers.vmrapp.models.StoresSHopApiModel;
import com.developers.vmrapp.service.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {

    private final Context context;
    private final OnMarketClickListener listener;
    private final DummyMarket marketModelList;


    public MarketAdapter(Context context, OnMarketClickListener listener, DummyMarket marketModelList) {
        this.context = context;
        this.listener = listener;
        this.marketModelList = marketModelList;
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MarketViewHolder(LayoutItemMarketBinding
                .inflate(LayoutInflater.from(context), parent, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        SharedPreferences editor=context.getSharedPreferences("my_token", Context.MODE_PRIVATE);

        if (marketModelList.data.get(position).getPartname()==null)
        {
            holder.binding.layout.setVisibility(View.GONE);
        }
        if (marketModelList.data.get(position).getPrice()==null)
        {
            holder.binding.layout.setVisibility(View.GONE);
        }
        if (marketModelList.data.get(position).getPartimage()==null)
        {
            holder.binding.layout.setVisibility(View.GONE);
        }



        if (marketModelList.data.get(position).user!=null) {
            if (marketModelList.data.get(position).getPartname() != null) {
                if (marketModelList.data.get(position).getPrice() != null) {
                    if (marketModelList.data.get(position).getPartimage() != null) {
                        Log.e("modelId", "" + marketModelList.data.get(position).user.getId());


                        Log.e("userId", "" + editor.getString("userId", "1"));
                        Log.e("sold", "" + marketModelList.data.get(position).isSold());
                        Log.e("position", "" + position);
                        Log.e("position", "" + marketModelList.data.get(position).getPartimage());

                        if (marketModelList.data.get(position).isSold() == false) {

                            if (marketModelList.data.get(position).getPartimage() != null) {

                                Glide.with(context)
                                        .load(Api.Image_URL + marketModelList.data.get(position).getPartimage())
                                        .into(holder.binding.itemImageView);
                            }


                            if (marketModelList.data.get(position).getPrice() != null) {
                                holder.binding.itemPrice.setText("$" + marketModelList.data.get(position).getPrice());
                            }


                            if (marketModelList.data.get(position).getPartname() != null) {
                                holder.binding.partName.setText(marketModelList.data.get(position).getPartname() + "");
                            }


                        }
//        else
//        {
//
//           marketModelList.data.remove(position);
//           notifyDataSetChanged();
////            holder.binding.layout.setVisibility(View.GONE);
//        }


                        if (marketModelList.data.get(position).user != null) {
                            if (marketModelList.data.get(position).user.getId() == Integer.parseInt(editor.getString("userId", "1"))) {
                                holder.binding.btnSold.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.btnSold.setVisibility(View.INVISIBLE);
                            }
                        }
                    }else
                        holder.binding.layout.setVisibility(View.GONE);
                }else
                    holder.binding.layout.setVisibility(View.GONE);
            }else
                holder.binding.layout.setVisibility(View.GONE);
        }

//        if (name[position].contains(filter)) {
//
//        } else {
//            holder.binding.layout.setVisibility(View.GONE);
//        }
//
//
//        if(name[position].contains(filter)){
//
//        }else {
//            holder.binding.layout.setVisibility(View.GONE);
//        }

        holder.binding.btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(8, TimeUnit.MINUTES)
                        .writeTimeout(8, TimeUnit.MINUTES)
                        .readTimeout(8, TimeUnit.MINUTES)
                        .build();

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build();

                Api api = retrofit.create(Api.class);
                Log.e("useid","e"+editor.getString("userId", "1"));
                Log.e("productid","e"+marketModelList.data.get(position).getId());
                Call<Product_sold_Model> call=api.soldItem(String.valueOf(marketModelList.data.get(position).getId()),editor.getString("userId", "1"),"Token " + editor.getString("access_token", ""));
                call.enqueue(new Callback<Product_sold_Model>() {
                    @Override
                    public void onResponse(Call<Product_sold_Model> call, Response<Product_sold_Model> response) {
                        if (response.code()==200)
                        {
                            Toast.makeText(context, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            holder.binding.btnSold.setText("SOLD");
                        }else
                        {
                            Log.e("error","e"+response.code());
                            Log.e("error","e"+response.message());

                        }
                    }

                    @Override
                    public void onFailure(Call<Product_sold_Model> call, Throwable t) {
                        Log.e("error","e"+t.getMessage());

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {

        if (marketModelList.data != null) {
            return marketModelList.data.size();
        }
        else {
            return 0;
        }

    }


    class MarketViewHolder extends RecyclerView.ViewHolder {

        private final LayoutItemMarketBinding binding;
        OnMarketClickListener listener;

        public MarketViewHolder(@NonNull LayoutItemMarketBinding binding, OnMarketClickListener listener) {

            super(binding.getRoot());

            this.listener=listener;


            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v ->
                    listener.onMarketClick(marketModelList.data.get(getAdapterPosition())));

        }
    }

    public interface OnMarketClickListener {

        void onMarketClick(DummyMarket.Datum position);

    }

}