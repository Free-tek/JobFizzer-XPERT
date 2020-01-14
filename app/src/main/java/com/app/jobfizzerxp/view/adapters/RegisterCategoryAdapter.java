package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzerxp.utilities.interfaces.CategoryListener;
import com.app.jobfizzerxp.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by karthik on 12/10/17.
 */
public class RegisterCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private JSONArray categoryDetails;
    private CategoryListener categoryListener;


    public RegisterCategoryAdapter(Context context, JSONArray categoryDetails) {
        this.context = context;
        this.categoryDetails = categoryDetails;
    }

    public void setCategoryListener(CategoryListener categoryListener) {
        this.categoryListener = categoryListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_category, parent, false);

        return new RegisterCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        RegisterCategoryHolder holder = (RegisterCategoryHolder) viewHolder;
        int position = holder.getAdapterPosition();

        holder.removeIcon.setImageResource(R.drawable.ic_close_white);
        holder.removeIcon.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.black),
                PorterDuff.Mode.SRC_IN));


        JSONObject jsonObject;
        jsonObject = categoryDetails.optJSONObject(position);
        String catname = jsonObject.optString("sub_category_name");
        String experience = jsonObject.optString("experience");
        String priceperhour = jsonObject.optString("priceperhour");
        String quickpitch = jsonObject.optString("quickpitch");
        String categoryMainName = jsonObject.optString("categoryMainName");

        holder.categoryName.setText(catname);
        holder.experience.setText(experience);
        holder.pricePerHour.setText(priceperhour);
        holder.quickPitch.setText(quickpitch);
        holder.categoryMainName.setText(categoryMainName);

        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryListener.removeCategory(position, context);
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryDetails != null && categoryDetails.length() > 0 ? categoryDetails.length() : 0;
    }

    class RegisterCategoryHolder extends RecyclerView.ViewHolder {

        private TextView categoryName, experience, pricePerHour, quickPitch, categoryMainName;
        private ImageView removeIcon;

        RegisterCategoryHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            experience = view.findViewById(R.id.experience);
            pricePerHour = view.findViewById(R.id.pricePerHour);
            quickPitch = view.findViewById(R.id.quickPitch);
            categoryMainName = view.findViewById(R.id.categoryMainName);
            removeIcon = view.findViewById(R.id.removeIcon);

        }
    }
}