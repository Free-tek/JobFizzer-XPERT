package com.app.jobfizzerxp.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzerxp.model.AboutUsResponseModel;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.AboutUsViewModel;
import com.app.jobfizzerxp.R;

import java.util.List;

public class AboutUsActivity extends BaseActivity {
    private TextView contentTv;
    private ImageView backButton;
    private AboutUsViewModel aboutUsViewModel;
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initViews();
        initList();
        postToken();
    }


    private void initViews() {
        aboutUsViewModel = ViewModelProviders.of(this).get(AboutUsViewModel.class);
        rootView = findViewById(R.id.rootView);
        contentTv = findViewById(R.id.contentTv);
        backButton = findViewById(R.id.backButton);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.about_us);
    }

    private void initList() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void postToken() {
        InputForAPI inputForAPI = new InputForAPI(AboutUsActivity.this);
        inputForAPI.setUrl(UrlHelper.ABOUT_US);
        inputForAPI.setJsonObject(null);
        inputForAPI.setHeaderStatus(false);
        inputForAPI.setShowLoader(true);
        getAboutUs(inputForAPI);
    }

    private void getAboutUs(InputForAPI inputForAPI) {
        aboutUsViewModel.getAboutUs(inputForAPI).observe(this, new Observer<AboutUsResponseModel>() {
            @Override
            public void onChanged(@Nullable AboutUsResponseModel mainScreenResponseModel) {
                handelResponse(mainScreenResponseModel);
            }
        });
    }

    private void handelResponse(AboutUsResponseModel aboutUsResponseModel) {
        if (aboutUsResponseModel != null) {
            if (!aboutUsResponseModel.getError()) {
                setValues(aboutUsResponseModel);
            } else {
                UiUtils.snackBar(rootView, aboutUsResponseModel.getErrorMessage());
            }
        }
    }

    private void setValues(AboutUsResponseModel aboutUsResponseModel) {
        List<AboutUsResponseModel.Page> pageList = aboutUsResponseModel.getPage();
        if (pageList != null) {
            String terms = pageList.get(0).getTermsAndCondition();
            contentTv.setText(terms);
        }
    }
}