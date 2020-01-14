package com.app.jobfizzerxp.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.jobfizzerxp.model.messageListApi.ChatMessageApiModel;
import com.app.jobfizzerxp.model.messageListApi.Result;
import com.app.jobfizzerxp.services.ConnectivityReceiver;
import com.app.jobfizzerxp.services.ServiceClass;
import com.app.jobfizzerxp.utilities.customLibraries.CircleImageView;
import com.app.jobfizzerxp.utilities.events.MessageEvent;
import com.app.jobfizzerxp.utilities.helpers.AnimationHelper;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.interfaces.ConnectivityListener;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.adapters.ChatsAdapter;
import com.app.jobfizzerxp.viewModel.ChatActViewModel;
import com.app.jobfizzerxp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.app.jobfizzerxp.utilities.helpers.Constants.CHATS.CONTENT_TEXT;
import static com.app.jobfizzerxp.utilities.helpers.Constants.CHATS.TYPE_SENDER;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private int currentPage = 1;
    private int totalPage = 1;
    private String displayPic;
    private ServiceClass.Emitters emitters = new ServiceClass.Emitters(ChatActivity.this);
    private RecyclerView messageItems;
    private String booking_id;
    private TextView userName, waitingForInternet;
    private CircleImageView userImage;
    private ImageView backIcon;
    private LinearLayout sendLayout;
    private String userID;
    private String providerID;
    private EditText messageInput;
    private AppSettings appSettings = new AppSettings(ChatActivity.this);
    private ChatsAdapter chatsAdapter;

    private LinearLayoutManager linearLayoutManager;
    private String TAG = ChatActivity.class.getSimpleName();
    private boolean internetStatus;
    private ConnectivityReceiver connectivityReceiver;
    private ChatActViewModel mChatActViewModel;
    private LinearLayout rootView;
    private ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        BaseUtils.log(TAG, "onMessageEvent: " + event.getJsonObject());
        chatsAdapter.swapItems(mChatActViewModel.updateMessages(event.getJsonObject(), chatsAdapter.getChatList()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        initListners();
        setValues();
    }

    private void initViews() {
        mChatActViewModel = ViewModelProviders.of(this).get(ChatActViewModel.class);
        rootView = findViewById(R.id.rootView);
        progressBar = findViewById(R.id.progressBar);
        messageItems = findViewById(R.id.messageItems);
        userName = findViewById(R.id.toolBarTitle);
        waitingForInternet = findViewById(R.id.waitingForInternet);
        userImage = findViewById(R.id.userImage);
        sendLayout = findViewById(R.id.sendLayout);
        backIcon = findViewById(R.id.backButton);
        messageInput = findViewById(R.id.messageInput);
        initChatAdapter();

        messageInput.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        userImage.setVisibility(View.VISIBLE);
        //for internet connectivity
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        // registering broadcast receiver
        registerReceiver(connectivityReceiver, filter);
    }

    private void initChatAdapter() {
        chatsAdapter = new ChatsAdapter(new ArrayList<>(), ChatActivity.this);
        linearLayoutManager = new LinearLayoutManager
                (ChatActivity.this, LinearLayoutManager.VERTICAL, true);
        messageItems.setLayoutManager(linearLayoutManager);
        messageItems.setAdapter(chatsAdapter);
    }


    private void initListners() {
        backIcon.setOnClickListener(this);
        sendLayout.setOnClickListener(this);

        connectivityReceiver.setConnectivityListener(new ConnectivityListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                BaseUtils.log(TAG, "onNetworkConnectionChanged: " + isConnected);
                internetStatus = isConnected;
                loadValue(internetStatus);
            }
        });

        messageItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                BaseUtils.log(TAG, "onScrollStateChanged: " + linearLayoutManager.findLastVisibleItemPosition());
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (chatsAdapter.getItemCount() > 8) {
                        if (linearLayoutManager.findLastVisibleItemPosition() >= chatsAdapter.getItemCount() - 1) {
                            if (internetStatus) {
                                try {
                                    getData(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void setValues() {
        Intent intent = getIntent();
        try {
            if (intent != null) {
                userID = intent.getStringExtra(Constants.INTENT_KEYS.USER_ID);
                booking_id = intent.getStringExtra(Constants.INTENT_KEYS.BOOKING_ID);
                displayPic = intent.getStringExtra(Constants.INTENT_KEYS.USER_IMAGE);
                userName.setText(intent.getStringExtra(Constants.INTENT_KEYS.USER_NAME));
                BaseUtils.log(TAG, "booking_id: " + booking_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlideHelper.setImage(displayPic, userImage, UiUtils.getProfilePicture(this));
        providerID = appSettings.getProviderId();


        try {
            getData(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadValue(boolean isConnected) {
        if (isConnected) {
            if (waitingForInternet.getVisibility() == View.VISIBLE) {
                waitingForInternet.setBackgroundColor(ContextCompat.getColor(ChatActivity.this, R.color.fb_connect));
                waitingForInternet.setText(R.string.connecting);
                waitingForInternet.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        waitingForInternet.setVisibility(View.GONE);
                    }
                }, 3000);
            } else {
                waitingForInternet.setVisibility(View.GONE);
            }


        } else {
            waitingForInternet.setBackgroundColor(ContextCompat.getColor(ChatActivity.this, R.color.fb_messenger));
            waitingForInternet.setText(R.string.waiting_for_internet);
            waitingForInternet.setVisibility(View.VISIBLE);
        }
    }


    private void getData(boolean nextData) throws JSONException {
        InputForAPI inputForAPI = null;
        if (nextData) {
            BaseUtils.log(TAG, "currentPage: " + currentPage + ", totalPage: " + totalPage);
            if (currentPage < totalPage) {
                int curr = currentPage + 1;
                inputForAPI = getInputForAPI("" + curr);
            }
        } else {
            inputForAPI = getInputForAPI("1");
        }

        if (inputForAPI != null) {
            if (nextData) {
                AnimationHelper.buildAndStartAnimation(progressBar);
            }
            mChatActViewModel.chatMessageList(inputForAPI).observe(this, new Observer<ChatMessageApiModel>() {
                @Override
                public void onChanged(@Nullable ChatMessageApiModel chatMessageApiModel) {
                    handleMessageListResponse(chatMessageApiModel, nextData);
                }
            });
        }
    }

    private void handleMessageListResponse(@Nullable ChatMessageApiModel chatMessageApiModel, boolean nextData) {
        if (chatMessageApiModel != null) {
            if (!chatMessageApiModel.getError()) {
                setSuccessResponse(chatMessageApiModel, nextData);

            } else {
                UiUtils.snackBar(rootView, chatMessageApiModel.getMessage());
            }
        }
    }

    private void setSuccessResponse(@NonNull ChatMessageApiModel chatMessageApiModel, boolean nextData) {
        List<Result> resultList = chatMessageApiModel.getResults();

        if (resultList.size() != 0) {
            List<Result> messageList = mChatActViewModel.setChatType(resultList, userID);
            if (!nextData) {
                booking_id = String.valueOf(resultList.get(0).getBookingId());
                if (messageList.size() > 0) {
                    totalPage = chatMessageApiModel.getPageCount();
                    currentPage = Integer.parseInt(chatMessageApiModel.getCurrentpage());
                    messageList = mChatActViewModel.reverse(messageList);
                } else {
                    totalPage = 1;
                    currentPage = 1;
                }
                AnimationHelper.buildAndStartAnimation(progressBar);
                setAdapter(messageList);
            } else {
                totalPage = chatMessageApiModel.getPageCount();
                currentPage = Integer.parseInt(chatMessageApiModel.getCurrentpage());
                messageList = mChatActViewModel.reverse(messageList);
                AnimationHelper.buildAndStartAnimation(progressBar);
                chatsAdapter.changeValues(messageList);
            }
        } else {
            AnimationHelper.buildAndStartAnimation(progressBar);
        }
    }

    private void setAdapter(List<Result> messageList) {
        chatsAdapter = new ChatsAdapter(messageList, ChatActivity.this);
        linearLayoutManager = new LinearLayoutManager
                (ChatActivity.this, LinearLayoutManager.VERTICAL, true);
        messageItems.setLayoutManager(linearLayoutManager);
        messageItems.setAdapter(chatsAdapter);
        chatsAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }

    @NonNull
    private InputForAPI getInputForAPI(String curr) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderID", appSettings.getProviderId());
        jsonObject.put("receiverID", userID);
        jsonObject.put("page", curr);
        jsonObject.put("senderType", "provider");

        InputForAPI inputForAPI = new InputForAPI(ChatActivity.this);
        inputForAPI.setUrl(UrlHelper.CHAT_MESSAGES);
        inputForAPI.setHeaderStatus(false);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setShowLoader(false);
        return inputForAPI;
    }


    @Override
    public void onClick(View view) {
        if (view == backIcon) {
            onBackPressed();

        } else if (view == sendLayout) {
            UiUtils.closeKeyboard(view);
            sendMessage();
        }
    }

    private void sendMessage() {
        if (!ValidationsUtils.invalidText(messageInput.getText().toString().trim())) {
            JSONObject jsonObject = new JSONObject();
            providerID = appSettings.getProviderId();

            try {
                jsonObject.put("booking_id", booking_id);
                jsonObject.put("sender_id", providerID);
                jsonObject.put("reciever_id", userID);
                jsonObject.put("message_id", UUID.randomUUID());
                jsonObject.put("Time", System.currentTimeMillis());
                jsonObject.put("content", messageInput.getText().toString().trim());
                jsonObject.put("content_type", CONTENT_TEXT);

                BaseUtils.log(TAG, "sendmessage: " + jsonObject);
                emitters.sendMessage(jsonObject);

                jsonObject.put("type", TYPE_SENDER);
                BaseUtils.log(TAG, "messagebinding: " + jsonObject);

                chatsAdapter.swapItems(mChatActViewModel.updateMessages(jsonObject, chatsAdapter.getChatList()));
            } catch (Exception e) {
                e.printStackTrace();

            }
            messageInput.setText("");
            messageItems.smoothScrollToPosition(0);

        } else {
            sendLayout.startAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.shake_error));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
        EventBus.getDefault().unregister(this);
    }

}