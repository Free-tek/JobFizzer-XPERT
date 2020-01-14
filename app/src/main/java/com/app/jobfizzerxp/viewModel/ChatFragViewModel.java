package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.chatListApi.ChatListApiModel;
import com.app.jobfizzerxp.repository.ChatFragRepository;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;

public class ChatFragViewModel extends AndroidViewModel {

    private ChatFragRepository chatFragRepository;

    public ChatFragViewModel(@NonNull Application application) {
        super(application);
        chatFragRepository = new ChatFragRepository();
    }

    public LiveData<ChatListApiModel> chatList(InputForAPI inputs) {
        return chatFragRepository.chatList(inputs);
    }
}