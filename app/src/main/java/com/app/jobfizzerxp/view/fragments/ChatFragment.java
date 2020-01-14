package com.app.jobfizzerxp.view.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.chatListApi.ChatListApiModel;
import com.app.jobfizzerxp.model.chatListApi.MsgList;
import com.app.jobfizzerxp.utilities.events.MessageEvent;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.activities.MainActivity;
import com.app.jobfizzerxp.view.adapters.ChatRoomAdapter;
import com.app.jobfizzerxp.viewModel.ChatFragViewModel;
import com.app.jobfizzerxp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class ChatFragment extends Fragment {

    private RecyclerView chatRooms;
    private FrameLayout rootView;
    private EditText searchBox;
    private AppSettings appSettings;
    private ChatRoomAdapter chatRoomAdapter;
    private MainActivity activity;
    private ChatFragViewModel mChatFragViewModel;
    private View empty_layout;
    private LinearLayout chatLayout;
    private TextView empty_text;
    private ImageView emptyIcon;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        activity = (MainActivity) view.getContext();

        initViews(view);
        initListeners();
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            getChatLists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getChatLists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        UiUtils.closeKeyboard(activity.getCurrentFocus());
    }


    public void getChatLists() throws JSONException {
        InputForAPI inputForAPI = getInputForAPI();
        mChatFragViewModel.chatList(inputForAPI).observe(this, new Observer<ChatListApiModel>() {
            @Override
            public void onChanged(@Nullable ChatListApiModel chatListApiModel) {
                handleChatListResponse(chatListApiModel);
            }
        });
    }

    private void handleChatListResponse(@Nullable ChatListApiModel chatListApiModel) {
        if (chatListApiModel != null) {
            if (!chatListApiModel.getError()) {
                setAdapter(chatListApiModel);
            } else {
                UiUtils.snackBar(rootView, chatListApiModel.getErrorMessage());
            }
        }
    }

    private void setAdapter(@NonNull ChatListApiModel chatListApiModel) {
        List<MsgList> msgLists = chatListApiModel.getMsglist();
        if (msgLists != null && msgLists.size() > 0) {
            empty_layout.setVisibility(View.GONE);
            chatLayout.setVisibility(View.VISIBLE);
            chatRoomAdapter = new ChatRoomAdapter(msgLists, activity);
            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            chatRooms.setLayoutManager(linearLayoutManager);
            chatRooms.setAdapter(chatRoomAdapter);
            chatRoomAdapter.notifyDataSetChanged();
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            chatLayout.setVisibility(View.GONE);
            empty_text.setText(getString(R.string.you_have_no_chats_available));
            GlideHelper.setImage("", emptyIcon, ContextCompat.getDrawable(activity, R.drawable.empty_message));
        }
    }

    @NonNull
    private InputForAPI getInputForAPI() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("providerid", appSettings.getProviderId());

        InputForAPI inputForAPI = new InputForAPI(activity);
        inputForAPI.setUrl(UrlHelper.CHAT_LISTS);
        inputForAPI.setHeaderStatus(false);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(true);
        return inputForAPI;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initViews(View view) {
        mChatFragViewModel = ViewModelProviders.of(this).get(ChatFragViewModel.class);
        appSettings = new AppSettings(activity);
        rootView = view.findViewById(R.id.parentLayoutChat);
        chatRooms = view.findViewById(R.id.chatRooms);
        searchBox = view.findViewById(R.id.searchBox);

        chatLayout = view.findViewById(R.id.chatLayout);
        empty_layout = view.findViewById(R.id.empty_layout);

        empty_text = view.findViewById(R.id.empty_text);
        emptyIcon = view.findViewById(R.id.emptyIcon);
        chatRoomAdapter = new ChatRoomAdapter();

        if (appSettings.getIsLogged()) {
            chatLayout.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            empty_text.setText(getString(R.string.you_have_no_chats_available));
            GlideHelper.setImage("", emptyIcon, ContextCompat.getDrawable(activity, R.drawable.empty_message));
        } else {
            chatLayout.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            empty_text.setText(getString(R.string.please_login_again));
            GlideHelper.setImage("", emptyIcon, ContextCompat.getDrawable(activity, R.drawable.empty_icon));
        }
        searchBox.setFilters(new InputFilter[]{ValidationsUtils.emojiSpecialFilter});


    }

    private void initListeners() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchNames();

            }
        });
    }

    private void searchNames() {
        String newText = searchBox.getText().toString();
        try {
            chatRoomAdapter.getFilter().filter(newText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}