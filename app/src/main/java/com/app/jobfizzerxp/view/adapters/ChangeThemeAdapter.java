package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.app.jobfizzerxp.utilities.customLibraries.CircleImageView;
import com.app.jobfizzerxp.utilities.helpers.SharedHelper;
import com.app.jobfizzerxp.view.activities.MainActivity;
import com.app.jobfizzerxp.R;

import java.util.List;

import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.ACCOUNTS_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;

/*
 * Created by Poyyamozhi on 28-Apr-18.
 */

public class ChangeThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Integer> mData;
    private LayoutInflater mInflater;

    public ChangeThemeAdapter(Context context, List<Integer> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.items_theme, parent, false);
        return new ChangeThemeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ChangeThemeHolder holder = (ChangeThemeHolder) viewHolder;

        holder.circleImageView.setImageDrawable(new ColorDrawable(mData.get(holder.getAdapterPosition())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(holder);
            }
        });
    }

    private void showAlert(RecyclerView.ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.want_change_theme)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int i = holder.getAdapterPosition() + 1;
                        SharedHelper.putKey(context, "theme_value", "" + i);
                        moveToMain();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        Window window = alert.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AlertDialogCustom;
        }
        alert.show();
    }

    private void moveToMain() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(LOGIN_TYPE, ACCOUNTS_TAB);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mData != null &&  mData.size() > 0 ? mData.size() : 0;
    }


    class ChangeThemeHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;

        ChangeThemeHolder(final View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.info_text);

        }
    }
}