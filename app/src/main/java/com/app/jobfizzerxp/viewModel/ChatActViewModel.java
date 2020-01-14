package com.app.jobfizzerxp.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzerxp.model.messageListApi.ChatMessageApiModel;
import com.app.jobfizzerxp.model.messageListApi.Result;
import com.app.jobfizzerxp.repository.ChatActRepository;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.jobfizzerxp.utilities.helpers.Constants.CHATS.TYPE_RECEIVER;
import static com.app.jobfizzerxp.utilities.helpers.Constants.CHATS.TYPE_SENDER;

public class ChatActViewModel extends AndroidViewModel {

    private ChatActRepository chatActRepository;
    private String TAG = ChatActRepository.class.getSimpleName();

    public ChatActViewModel(@NonNull Application application) {
        super(application);
        chatActRepository = new ChatActRepository();
    }

    public List<Result> updateMessages(JSONObject jsonObject, List<Result> chatsAdapterList) {
        Result result = new Gson().fromJson(jsonObject.toString(), Result.class);

        List<Result> finalList = new ArrayList<>();
        finalList.add(0, result);

        if (chatsAdapterList != null) {
            BaseUtils.log(TAG, "updateMessages: " + chatsAdapterList.size());
            for (int i = 0; i < chatsAdapterList.size(); i++) {
                finalList.add(i + 1, chatsAdapterList.get(i));

            }
        }
        return finalList;
    }

    public List<Result> reverse(List<Result> resultList) {
        List<Result> toReturn = new ArrayList<>();
        int length = resultList.size() - 1;
        for (int i = length; i >= 0; i--) {
            toReturn.add(resultList.get(i));
        }
        return toReturn;
    }

    public LiveData<ChatMessageApiModel> chatMessageList(InputForAPI inputs) {
        return chatActRepository.chatMessageList(inputs);
    }

    public List<Result> setChatType(List<Result> chatType, String receiverId) {
        List<Result> messageList = new ArrayList<>();

        for (int i = 0; i < chatType.size(); i++) {
            Result resultModel = chatType.get(i);
            if (String.valueOf(resultModel.getSenderID()).equalsIgnoreCase(receiverId)) {
                resultModel.setType(TYPE_RECEIVER);

            } else {
                resultModel.setType(TYPE_SENDER);
            }
            messageList.add(resultModel);
        }
        return messageList;
    }
}