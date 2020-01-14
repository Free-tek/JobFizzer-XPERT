package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.app.jobfizzerxp.model.homeDashboardApi.MaterialDetails;
import com.app.jobfizzerxp.R;

import java.util.List;

public class PaymentMiscellaneousAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MaterialDetails> materialDetails;
    private Context context;


    public PaymentMiscellaneousAdapter(Context context, List<MaterialDetails> materialDetails) {
        this.context = context;
        this.materialDetails = materialDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_miscellaneous_items, parent, false);

        return new PaymentMiscellanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PaymentMiscellanHolder holder = (PaymentMiscellanHolder) viewHolder;

        holder.miscellaneous_amount.setText(materialDetails.get(position).getMaterial_cost());
        holder.miscellaneousName.setText(materialDetails.get(position).getMaterial_name());

    }

    @Override
    public int getItemCount() {
        return materialDetails != null && materialDetails.size() > 0 ? materialDetails.size() : 0;

    }

    class PaymentMiscellanHolder extends RecyclerView.ViewHolder {
        private TextView miscellaneous_amount, miscellaneousName;

        PaymentMiscellanHolder(View view) {
            super(view);
            miscellaneous_amount = view.findViewById(R.id.miscellaneous_amount);
            miscellaneousName = view.findViewById(R.id.miscellaneousName);
        }
    }
}