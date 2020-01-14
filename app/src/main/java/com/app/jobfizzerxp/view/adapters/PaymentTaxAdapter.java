package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jobfizzerxp.model.homeDashboardApi.Alltax;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.R;

import java.util.List;

public class PaymentTaxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Alltax> alltaxList;
    private String TAG = PaymentTaxAdapter.class.getSimpleName();

    public PaymentTaxAdapter(Context context, List<Alltax> j_Tax) {
        this.context = context;
        this.alltaxList = j_Tax;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_tax_items, parent, false);

        return new PaymentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        PaymentHolder holder = (PaymentHolder) viewHolder;
        Alltax alltax = alltaxList.get(position);

        holder.txt_taxName.setText(alltax.getTaxname());
        holder.txt_taxPercentage.setText(ConversionUtils.appendPercentage(context, alltax.getTaxAmount()));
        holder.txt_bookingGst.setText(ConversionUtils.appendCurrencySymbol(context, ConversionUtils.getRoundUpValue(alltax.getTaxTotalamount())));
    }

    @Override
    public int getItemCount() {
        return alltaxList != null && alltaxList.size() > 0 ? alltaxList.size() : 0;
    }

    class PaymentHolder extends RecyclerView.ViewHolder {
        private TextView txt_taxName, txt_taxPercentage, txt_bookingGst;

        PaymentHolder(View view) {
            super(view);
            txt_taxName = view.findViewById(R.id.taxName);
            txt_taxPercentage = view.findViewById(R.id.taxPercentage);
            txt_bookingGst = view.findViewById(R.id.bookingGst);

        }
    }
}