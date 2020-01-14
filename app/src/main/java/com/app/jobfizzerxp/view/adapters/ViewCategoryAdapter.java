package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzerxp.model.viewCategoryApi.Category;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.interfaces.ViewCategoryListener;
import com.app.jobfizzerxp.utilities.networkUtils.ConnectionUtils;
import com.app.jobfizzerxp.R;

import java.util.List;

/**
 * Created by karthik on 12/10/17.
 */
public class ViewCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Category> categoryDetails;
    private ViewCategoryListener viewCategoryListener;

    public ViewCategoryAdapter(Context context, List<Category> categoryDetails) {
        this.context = context;
        this.categoryDetails = categoryDetails;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_category, parent, false);

        return new ViewProviderCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ViewProviderCategoryHolder holder = (ViewProviderCategoryHolder) viewHolder;

        final Category category = categoryDetails.get(holder.getAdapterPosition());

        holder.removeIcon.setImageResource(R.drawable.edit_black);

        if (category.getStatus().equalsIgnoreCase("0")) {
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));


        } else {
            String catname = category.getSubCategoryName();
            String experience = category.getExperience();
            String priceperhour = category.getPriceperhour();
            String quickpitch = category.getQuickpitch();
            String categoryMainName = category.getCategoryName();

            holder.categoryName.setText(catname);
            holder.experience.setText(experience);
            holder.pricePerHour.setText(priceperhour);
            holder.quickPitch.setText(quickpitch);
            holder.categoryMainName.setText(categoryMainName);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(context)) {
                    viewCategoryListener.onEditDialog(context, category);

                } else {
//                    UiUtils.snackBar(context.rootView, context.getString(R.string.no_internet_connection));
                    UiUtils.snackBar(holder.removeIcon, context.getString(R.string.no_internet_connection));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryDetails != null && categoryDetails.size() > 0 ? categoryDetails.size() : 0;
    }

    public void setViewCategoryListener(ViewCategoryListener viewCategoryListener) {
        this.viewCategoryListener = viewCategoryListener;
    }

    class ViewProviderCategoryHolder extends RecyclerView.ViewHolder {

        private TextView categoryName, experience, pricePerHour, quickPitch, categoryMainName;
        private ImageView removeIcon;


        ViewProviderCategoryHolder(View view) {
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