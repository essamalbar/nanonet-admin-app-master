package com.crazyiter.nanonet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonet.db.Provider;

import java.util.ArrayList;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Provider> providers;

    public ProvidersAdapter(Context context, ArrayList<Provider> providers) {
        this.context = context;
        this.providers = providers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_provider, parent, false));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupData(context, providers.get(position));
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, ProviderInfoActivity.class)
                .putExtra("item", providers.get(position))));
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout providerLL;
        private final TextView daysTV;
        private final TextView nameTV;
        private final ImageView isOnlineIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            providerLL = itemView.findViewById(R.id.providerLL);
            daysTV = itemView.findViewById(R.id.daysTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            isOnlineIV = itemView.findViewById(R.id.isOnlineTV);
        }

        void setupData(Context context, Provider provider) {
            providerLL.setBackgroundResource(provider.getDiffDaysColor());
            daysTV.setText(String.valueOf(provider.getDiffDays()));
            nameTV.setText(provider.getName());

            if (provider.isOnline()) {
                isOnlineIV.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
            } else {
                isOnlineIV.setColorFilter(context.getResources().getColor(R.color.white));
            }
        }

    }

}
