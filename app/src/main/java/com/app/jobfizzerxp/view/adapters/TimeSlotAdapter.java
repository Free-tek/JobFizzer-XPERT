package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.appSettingsApi.TimeSlotApi;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.R;

import java.util.List;

/**
 * Created by karthik on 12/10/17.
 */
public class TimeSlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TimeSlotApi> categoryDetails;
    private List<TimeSlotApi> timeslotList;
    private int selectedtime;
    private Context context;
    private String TAG = TimeSlotAdapter.class.getSimpleName();

    public TimeSlotAdapter(Context context, List<TimeSlotApi> categoryDetails,
                           List<TimeSlotApi> timeslotList, int selectedTime) {
        this.context = context;
        this.categoryDetails = categoryDetails;
        this.selectedtime = selectedTime;
        this.timeslotList = timeslotList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_time_slot, parent, false);

        return new TimeSlotHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        TimeSlotHolder holder = (TimeSlotHolder) viewHolder;
        int position = holder.getAdapterPosition();

        TimeSlotApi timeslot = categoryDetails.get(position);
        final String isSelected = timeslot.getSelected();
        holder.timeSlot.setText(timeslot.getTiming());

        if (isSelected.equalsIgnoreCase("false")) {
            holder.backgroundLay.setBackground(context.getResources().getDrawable(R.drawable.timeslotwhite));
        } else {
            holder.backgroundLay.setBackground(UiUtils.getRLayout(context));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected.equalsIgnoreCase("false")) {
                    categoryDetails.get(position).setSelected("true");

                    for (int i = 0; i < timeslotList.size(); i++) {
                        TimeSlotApi timeSlotApi = timeslotList.get(i);
                        if (i % 7 == selectedtime) {
                            BaseUtils.log(TAG, "values: " + timeSlotApi.getTime_Slots_id() + "," + categoryDetails.get(position).getId());

                            if (String.valueOf(timeSlotApi.getTime_Slots_id()).equalsIgnoreCase(String.valueOf(categoryDetails.get(position).getId()))) {
                                BaseUtils.log(TAG, ":insidebefore " + timeSlotApi);

                                timeSlotApi.setStatus("1");
                                timeSlotApi.setSelected_text(categoryDetails.get(position).getTiming());
                                BaseUtils.log(TAG, ":insideafter " + timeSlotApi);
                            }
                        }
                    }
                } else {
                    categoryDetails.get(position).setSelected("false");

                    for (int i = 0; i < timeslotList.size(); i++) {
                        TimeSlotApi timeSlotApi = timeslotList.get(i);
                        if (i % 7 == selectedtime) {
                            BaseUtils.log(TAG, "values: " + timeSlotApi.getTime_Slots_id() + "," + categoryDetails.get(position).getTiming());
                            if (String.valueOf(timeSlotApi.getTime_Slots_id()).equalsIgnoreCase(String.valueOf(categoryDetails.get(position).getId()))) {
                                BaseUtils.log(TAG, ":insidebefore " + timeSlotApi);

                                timeSlotApi.setStatus("0");
                                timeSlotApi.setSelected_text(categoryDetails.get(position).getTiming());

                                BaseUtils.log(TAG, ":insideafter " + timeSlotApi);
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDetails != null && categoryDetails.size() > 0 ? categoryDetails.size() : 0;
    }

    class TimeSlotHolder extends RecyclerView.ViewHolder {
        private TextView timeSlot;
        private LinearLayout backgroundLay;

        TimeSlotHolder(View view) {
            super(view);
            timeSlot = view.findViewById(R.id.timeSlot);
            backgroundLay = view.findViewById(R.id.background);
        }
    }
}