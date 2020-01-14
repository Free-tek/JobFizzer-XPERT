package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzerxp.model.MiscellaneousModel;
import com.app.jobfizzerxp.utilities.interfaces.MiscellaneousAdapterHandler;
import com.app.jobfizzerxp.R;

import java.util.ArrayList;
import java.util.List;

public class MiscellaneousAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MiscellaneousModel> itemIncrease = new ArrayList<>();

    private MiscellaneousAdapterHandler miscellaneousAdapterHandler;


    public MiscellaneousAdapter(Context context, MiscellaneousAdapterHandler miscellaneousAdapterHandler) {
        this.context = context;
        this.miscellaneousAdapterHandler = miscellaneousAdapterHandler;
    }

    public void setMiscellaneousAdapterHandler(MiscellaneousAdapterHandler miscellaneousAdapterHandler) {
        this.miscellaneousAdapterHandler = miscellaneousAdapterHandler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.miscellaneous_item, parent, false);

        return new MiscellaneousHolder(itemView, new DescriptionEditTextListener(), new AmountEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MiscellaneousHolder holder = (MiscellaneousHolder) viewHolder;
//        holder.descriptionText.setTag(position);
        holder.descriptionEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.amountEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.descriptionText.setText(itemIncrease.get(position).getName());
        holder.amount.setText(itemIncrease.get(position).getPrice());


        holder.cancel.setOnClickListener(v -> {
            removeItem(position);
        });
    }

    private void removeItem(int position) {
        for (MiscellaneousModel miscellaneousModel : itemIncrease) {
            Log.e("REmoveitem", "removeItem: " + miscellaneousModel.getName());
        }
        Log.e("Adapter", "removeItem: " + position + " item: " + itemIncrease.get(position).getName());
        itemIncrease.remove(position);
        notifyDataSetChanged();
        miscellaneousAdapterHandler.Count(itemIncrease.size());
    }

    @Override
    public int getItemCount() {
        return itemIncrease.size();
    }

    public void addValue() {
        itemIncrease.add(new MiscellaneousModel("", ""));
        notifyItemInserted(itemIncrease.size());
    }


    public List<MiscellaneousModel> getItemList() {
        return itemIncrease;
    }

    class MiscellaneousHolder extends RecyclerView.ViewHolder {
        private ImageView cancel;
        private TextView amount, descriptionText;
        public DescriptionEditTextListener descriptionEditTextListener;
        public AmountEditTextListener amountEditTextListener;

        MiscellaneousHolder(View view, DescriptionEditTextListener descriptionEditTextListener, AmountEditTextListener amountEditTextListener) {
            super(view);
            cancel = view.findViewById(R.id.cancel);

            amount = view.findViewById(R.id.amount);
            descriptionText = view.findViewById(R.id.descriptionText);
            this.descriptionEditTextListener = descriptionEditTextListener;
            this.amountEditTextListener = amountEditTextListener;
            this.descriptionText.addTextChangedListener(descriptionEditTextListener);
            this.amount.addTextChangedListener(amountEditTextListener);
        }
    }

    private class AmountEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            itemIncrease.get(position).setPrice(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class DescriptionEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            itemIncrease.get(position).setName(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}