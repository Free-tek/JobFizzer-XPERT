package com.app.jobfizzerxp.view.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.app.jobfizzerxp.model.messageListApi.Result;
import com.app.jobfizzerxp.utilities.helpers.ConversionUtils;
import com.app.jobfizzerxp.R;

import java.util.List;

import static com.app.jobfizzerxp.utilities.helpers.Constants.CHATS.TYPE_SENDER;

/*
 * Created by yuvaraj on 30/11/17.
 */

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Result> resultList;
    private Context context;
    private String TAG = ChatsAdapter.class.getSimpleName();
    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayEnterAnimation = true;

    public ChatsAdapter(List<Result> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public List<Result> getChatList() {
        return this.resultList;
    }

    public void swapItems(List<Result> items) {
        this.resultList = items;
        notifyDataSetChanged();
    }

    public void changeValues(List<Result> resultList) {
        this.resultList.addAll(resultList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_message, parent, false);
        return new ChatHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        ChatHolder holder = (ChatHolder) viewHolder;
        runEnterAnimation(holder.itemView, position);
        Result result = resultList.get(holder.getAdapterPosition());
        if (result.getType().equalsIgnoreCase(TYPE_SENDER)) {
            holder.receiverLay.setVisibility(View.GONE);
            holder.senderLay.setVisibility(View.VISIBLE);
            holder.stimeStamp.setText(ConversionUtils.convertDate(result.getTime()));
            holder.smessage.setText(result.getContent());

        } else {
            holder.senderLay.setVisibility(View.GONE);
            holder.receiverLay.setVisibility(View.VISIBLE);
            holder.timeStamp.setText(ConversionUtils.convertDate(result.getTime()));
            holder.message.setText(result.getContent());
        }
    }

    //animation for adapter
    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;
        if (position > lastAnimatedPosition) {
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return resultList != null && resultList.size() > 0 ? resultList.size() : 0;
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        private TextView message, timeStamp;
        private TextView smessage, stimeStamp;
        private View senderLay;
        private View receiverLay;

        ChatHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            smessage = itemView.findViewById(R.id.smessage);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            stimeStamp = itemView.findViewById(R.id.stimeStamp);
            senderLay = itemView.findViewById(R.id.senderLay);
            receiverLay = itemView.findViewById(R.id.receiverLay);

        }
    }
}