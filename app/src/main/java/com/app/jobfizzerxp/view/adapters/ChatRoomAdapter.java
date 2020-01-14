package com.app.jobfizzerxp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.jobfizzerxp.model.chatListApi.MsgList;
import com.app.jobfizzerxp.utilities.customLibraries.CircleImageView;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.view.activities.ChatActivity;
import com.app.jobfizzerxp.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by user on 22-11-2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<MsgList> mainMsgList;
    private List<MsgList> defMsgList;
    private Context context;
    private CustomFilter customFilter;


    public ChatRoomAdapter(List<MsgList> msgLists, Context context) {
        this.context = context;
        this.mainMsgList = msgLists;
        this.defMsgList = msgLists;
        customFilter = new CustomFilter(ChatRoomAdapter.this, msgLists);
    }

    public ChatRoomAdapter() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_chat_rooms, parent, false);
        return new ChatRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ChatRoomHolder holder = (ChatRoomHolder) viewHolder;
        MsgList msgList;
        msgList = mainMsgList.get(holder.getAdapterPosition());
        holder.userName.setText(msgList.getName());
        GlideHelper.setImage(msgList.getProfilePic(), holder.userImage, UiUtils.getProfilePicture(context));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMain(holder);
            }
        });
    }

    private void moveToMain(@NonNull ChatRoomHolder holder) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.INTENT_KEYS.USER_IMAGE, mainMsgList.get(holder.getAdapterPosition()).getProfilePic());
        intent.putExtra(Constants.INTENT_KEYS.USER_NAME, mainMsgList.get(holder.getAdapterPosition()).getName());
        intent.putExtra(Constants.INTENT_KEYS.USER_ID, "" + mainMsgList.get(holder.getAdapterPosition()).getId());
        intent.putExtra(Constants.INTENT_KEYS.BOOKING_ID, "");
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return mainMsgList != null &&  mainMsgList.size() > 0 ? mainMsgList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    private class ChatRoomHolder extends RecyclerView.ViewHolder {
        private CircleImageView userImage;
        private TextView userName;

        private ChatRoomHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
        }
    }

    private class CustomFilter extends Filter {
        private ChatRoomAdapter mAdapter;
        private List<MsgList> resultsList;

        private CustomFilter(ChatRoomAdapter mAdapter, List<MsgList> resultList) {
            super();
            this.mAdapter = mAdapter;
            this.resultsList = resultList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();
            List<MsgList> resultList;

            if (charString.isEmpty()) {
                resultList = defMsgList;

            } else {
                List<MsgList> filterArray = new ArrayList<>();

                for (int i = 0; i < resultsList.size(); i++) {
                    if (resultsList.get(i).getName().toLowerCase()
                            .contains(charString.toLowerCase())) {

                        filterArray.add(resultsList.get(i));
                    }
                }
                resultList = filterArray;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = resultList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            mainMsgList = (List<MsgList>) filterResults.values;
            mAdapter.notifyDataSetChanged();
        }
    }
}