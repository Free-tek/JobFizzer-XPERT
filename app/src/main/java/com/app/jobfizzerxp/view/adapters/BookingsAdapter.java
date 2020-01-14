package com.app.jobfizzerxp.view.adapters;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzerxp.model.homeDashboardApi.Accepted;
import com.app.jobfizzerxp.model.jobStatusApi.StartToPlaceApiModel;
import com.app.jobfizzerxp.utilities.events.Status;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.interfaces.StartImageJob;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.activities.DetailedBookActivity;
import com.app.jobfizzerxp.view.activities.MiscellaneousActivity;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by karthik on 12/10/17.
 */
public class BookingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String bookingId = "";
    private String bookingStatus = "";
    private Activity context;
    private String TAG = BookingsAdapter.class.getSimpleName();
    private List<Accepted> subCategories;
    private CommonViewModel commonViewModel;
    private StartImageJob startImageJob;
    private CardView rootView;

    public BookingsAdapter(Activity activity, List<Accepted> subCategories) {
        this.context = activity;
        this.subCategories = subCategories;
        commonViewModel = ViewModelProviders.of((FragmentActivity) activity).get(CommonViewModel.class);
    }

    public void setStartImageJob(StartImageJob startImageJob) {
        this.startImageJob = startImageJob;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_bookings, parent, false);
        rootView = itemView.findViewById(R.id.shadow_view);
        return new BookingsHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        BookingsHolder holder = (BookingsHolder) viewHolder;

        final Accepted allBooking = subCategories.get(position);
        try {
            String str_subcategoryName = allBooking.getSubCategoryName();
            String str_getBookingDate = allBooking.getBookingDate();
            String providerName = allBooking.getName();
            String prvoider_pic;
            String str_timing = allBooking.getTiming();

            holder.serviceName.setText(str_subcategoryName);
            String dateVal = ConversionUtils.getConvertedString(str_getBookingDate);

            prvoider_pic = subCategories.get(position).getUserimage();
            holder.providerName.setText(providerName);
            holder.serviceDate.setText(dateVal);
            GlideHelper.setImage(prvoider_pic, holder.providerPic, UiUtils.getProfilePicture(context));

            holder.serviceTiming.setText(str_timing);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String status = allBooking.getStatus();
        int colorvalue = 0;
        String statusValue = "";
        Drawable statusIcon = null;
        if (status.equalsIgnoreCase("Pending")) {
            holder.updateStatus.setVisibility(View.GONE);
            statusValue = context.getResources().getString(R.string.pending);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_pending);
        } else if (status.equalsIgnoreCase("Rejected")) {
            statusValue = context.getResources().getString(R.string.rejected);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.red);
            statusIcon = context.getResources().getDrawable(R.drawable.new_cancelled);
        } else if (status.equalsIgnoreCase("Accepted")) {
            statusValue = context.getResources().getString(R.string.accepted);
            colorvalue = context.getResources().getColor(R.color.green);
            statusIcon = context.getResources().getDrawable(R.drawable.new_accepted);
            holder.updateStatus.setVisibility(View.VISIBLE);
            holder.updateStatus.setText(context.getResources().getString(R.string.start_to_place));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.updateStatus.setBackgroundTintList(ColorStateList.valueOf(colorvalue));
            }
        } else if (status.equalsIgnoreCase("CancelledbyUser")) {
            statusValue = context.getResources().getString(R.string.cancelled) + " " + context.getResources().getString(R.string.by_user);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.red);
            statusIcon = context.getResources().getDrawable(R.drawable.new_cancelled);
        } else if (status.equalsIgnoreCase("CancelledbyProvider")) {
            statusValue = context.getResources().getString(R.string.cancelled) + " " + context.getResources().getString(R.string.by_provider);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.red);
            statusIcon = context.getResources().getDrawable(R.drawable.new_cancelled);
        } else if (status.equalsIgnoreCase("StarttoCustomerPlace")) {
            statusValue = context.getResources().getString(R.string.on_the_way);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_start_to_place);
            holder.updateStatus.setVisibility(View.VISIBLE);
            holder.updateStatus.setText(context.getResources().getString(R.string.start_job));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.updateStatus.setBackgroundTintList(ColorStateList.valueOf(colorvalue));
            }
        } else if (status.equalsIgnoreCase("Startedjob")) {
            statusValue = context.getResources().getString(R.string.job_started);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_started_job);
            holder.updateStatus.setVisibility(View.VISIBLE);
            holder.updateStatus.setText(context.getResources().getString(R.string.complete_job));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.updateStatus.setBackgroundTintList(ColorStateList.valueOf(colorvalue));
            }
        } else if (status.equalsIgnoreCase("Completedjob")) {
            statusValue = context.getResources().getString(R.string.completed_job);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.green);
            statusIcon = context.getResources().getDrawable(R.drawable.new_complete_job);
        } else if (status.equalsIgnoreCase("Waitingforpaymentconfirmation")) {
            statusValue = context.getResources().getString(R.string.waiting_for);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.status_orange);
            statusIcon = context.getResources().getDrawable(R.drawable.new_pay_conifrmation);
        } else if (status.equalsIgnoreCase("Reviewpending")) {
            statusValue = context.getResources().getString(R.string.review_pending);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.ratingColor);
            statusIcon = context.getResources().getDrawable(R.drawable.new_review);
        } else if (status.equalsIgnoreCase("Finished")) {
            statusValue = context.getResources().getString(R.string.finished);
            holder.updateStatus.setVisibility(View.GONE);
            colorvalue = context.getResources().getColor(R.color.green);
            statusIcon = context.getResources().getDrawable(R.drawable.new_finished);

        }
        holder.statusText.setText(statusValue);
        holder.statusText.setTextColor(colorvalue);
        holder.statusIcon.setImageDrawable(statusIcon);
        holder.updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.updateStatus.getText().toString().equalsIgnoreCase(context.getResources()
                        .getString(R.string.start_to_place))) {
                    updateStatus(String.valueOf(allBooking.getId()));
                } else if (holder.updateStatus.getText().toString().equalsIgnoreCase(context.getResources().getString(R.string.start_job))) {
                    bookingId = String.valueOf(allBooking.getId());
                    bookingStatus = Constants.BOOKINGS.STATUS_START;
                    startImageJob.getBookingDetails(context, bookingId, bookingStatus);

                } else {
                    bookingId = String.valueOf(allBooking.getId());
                    bookingStatus = Constants.BOOKINGS.STATUS_COMPLETE;
//                    startImageJob.getBookingDetails(context, bookingId, bookingStatus);

                    Intent intent = new Intent(context, MiscellaneousActivity.class);
                    intent.putExtra(Constants.INTENT_KEYS.BOOKING_ID, bookingId);
                    intent.putExtra(Constants.INTENT_KEYS.BOOKING_STATUS, bookingStatus);
                    context.startActivity(intent);

                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detailedBooking = new Intent(context, DetailedBookActivity.class);
                BaseUtils.log(TAG, "subCategories:" + allBooking);
                detailedBooking.putExtra("bookingValues", allBooking);
                context.startActivity(detailedBooking);

            }
        });
    }


    private void updateStatus(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.START_TO_PLACE);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        getValue(inputForAPI);
    }

    private void getValue(InputForAPI inputForAPI) {

        commonViewModel.startToPlace(inputForAPI).observe((LifecycleOwner) inputForAPI.getContext(),
                new Observer<StartToPlaceApiModel>() {
                    @Override
                    public void onChanged(@Nullable StartToPlaceApiModel response) {
                        if (response != null) {
                            if (!response.getError()) {
                                EventBus.getDefault().post(new Status());
                            } else {
                                UiUtils.snackBar(rootView, response.getErrorMessage());
                            }
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return subCategories != null && subCategories.size() > 0 ? subCategories.size() : 0;
    }

    class BookingsHolder extends RecyclerView.ViewHolder {
        private TextView serviceName, serviceTiming, statusText, serviceDate, updateStatus;
        private ImageView providerPic, statusIcon;
        private TextView providerName;

        BookingsHolder(View view) {
            super(view);
            statusText = view.findViewById(R.id.statusText);
            providerPic = view.findViewById(R.id.providerPic);
            providerName = view.findViewById(R.id.providerName);
            serviceName = view.findViewById(R.id.serviceName);
            serviceTiming = view.findViewById(R.id.serviceTiming);
            serviceDate = view.findViewById(R.id.serviceDate);
            statusIcon = view.findViewById(R.id.statusIcon);
            updateStatus = view.findViewById(R.id.updateStatus);
            UiUtils.setProfilePicture(context, providerPic);
        }
    }
}