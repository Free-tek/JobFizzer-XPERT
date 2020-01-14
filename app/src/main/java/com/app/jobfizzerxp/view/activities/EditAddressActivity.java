package com.app.jobfizzerxp.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.UpdateAddressApiModel;
import com.app.jobfizzerxp.model.viewProfileApi.ProviderDetails;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.EditAddressViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.jobfizzerxp.utilities.helpers.Constants.INTENT_KEYS.PROVIDER_DETAILS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.HOME_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;

public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private EditText addressOne, addressTwo, city, state, zipCode;
    private Button bottomButton;
    private ImageView backButton;

    private EditAddressViewModel editAddressViewModel;
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initViews();
        setValues();
        initListeners();
    }

    private void initListeners() {
        bottomButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void setValues() {
        ProviderDetails providerDetails = (ProviderDetails) getIntent().getSerializableExtra(PROVIDER_DETAILS);

        if (providerDetails.getAddressline1().length() == 0) {
            addressOne.setText("");

        } else {
            addressOne.setText(providerDetails.getAddressline1());
        }

        if (providerDetails.getAddressline2().length() == 0) {
            addressTwo.setText("");

        } else {
            addressTwo.setText(providerDetails.getAddressline2());
        }

        if (providerDetails.getCity().length() == 0) {
            city.setText("");

        } else {
            city.setText(providerDetails.getCity());
        }

        if (providerDetails.getState().length() == 0) {
            state.setText("");

        } else {
            state.setText(providerDetails.getState());
        }

        if (providerDetails.getZipcode().length() == 0) {
            zipCode.setText("");

        } else {
            zipCode.setText(providerDetails.getZipcode());
        }
    }

    private void initViews() {
        rootView = findViewById(R.id.rootView);
        addressOne = findViewById(R.id.addressOne);
        addressTwo = findViewById(R.id.addressTwo);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        zipCode = findViewById(R.id.zipCode);
        bottomButton = findViewById(R.id.bottomButton);
        backButton = findViewById(R.id.backButton);

        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.edit_address);

        addressOne.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        addressTwo.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        city.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        state.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});
        zipCode.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});

        editAddressViewModel = ViewModelProviders.of(this).get(EditAddressViewModel.class);
    }

    @Override
    public void onClick(View view) {
        if (view == bottomButton) {
            UiUtils.closeKeyboard(view);
            try {
                registerValues();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (view == backButton) {
            finish();
        }
    }

    private void registerValues() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("addressline1", addressOne.getText().toString());
        jsonObject.put("addressline2", addressTwo.getText().toString());
        jsonObject.put("city", city.getText().toString());
        jsonObject.put("state", state.getText().toString());
        jsonObject.put("zipcode", zipCode.getText().toString());

        editProfileUpdate(jsonObject);

    }

    public void editProfileUpdate(JSONObject jsonObject) {
        InputForAPI inputForAPI = new InputForAPI(this);
        inputForAPI.setUrl(UrlHelper.UPDATE_ADDRESS);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setShowLoader(true);
        getValue(inputForAPI);
    }

    private void getValue(InputForAPI inputForAPI) {

        editAddressViewModel.updateAddress(inputForAPI).observe(this, new Observer<UpdateAddressApiModel>() {
            @Override
            public void onChanged(@Nullable UpdateAddressApiModel response) {

                if (response != null) {
                    if (!response.getError()) {
                        moveMainActivity();
                    } else {
                        UiUtils.snackBar(rootView, response.getErrorMessage());
                    }
                }
            }
        });
    }

    private void moveMainActivity() {
        Intent intent = new Intent(EditAddressActivity.this, MainActivity.class);
        intent.putExtra(LOGIN_TYPE, HOME_TAB);
        startActivity(intent);
        finish();
    }
}