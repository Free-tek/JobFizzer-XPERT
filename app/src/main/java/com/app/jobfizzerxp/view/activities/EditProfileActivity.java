package com.app.jobfizzerxp.view.activities;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.jobfizzerxp.model.ImageUploadApiModel;
import com.app.jobfizzerxp.model.UpdateProfileApiModel;
import com.app.jobfizzerxp.model.viewProfileApi.ProviderDetails;
import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.utilities.helpers.GlideHelper;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.helpers.ValidationsUtils;
import com.app.jobfizzerxp.utilities.interfaces.ImageUpload;
import com.app.jobfizzerxp.utilities.networkUtils.ConnectionUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.viewModel.EditProfileViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.app.jobfizzerxp.utilities.helpers.Constants.INTENT_KEYS.PROVIDER_DETAILS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.HOME_TAB;
import static com.app.jobfizzerxp.utilities.helpers.Constants.LOGIN.LOGIN_TYPE;
import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.READ_STORAGE_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.CAMERA_INTENT;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.GALLERY_INTENT;


public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private EditText firstName, lastName, mobileNumber, dateOfBirth, aboutYou;
    private Spinner genderSpinner;
    private Button bottomButton;
    private ImageView profileImage;
    private String[] genders;
    private ImageView backButton;
    private File uploadFile;
    private String uploadImagepath;
    private AppSettings appSettings;
    private EditProfileViewModel editProfileViewModel;
    private CommonViewModel commonViewModel;
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        initAdapters();
        initListners();
        setValues();
    }

    private void initListners() {
        bottomButton.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void setValues() {
        ProviderDetails providerDetails = (ProviderDetails) getIntent().getSerializableExtra(PROVIDER_DETAILS);

        firstName.setText(providerDetails.getFirstName());
        lastName.setText(providerDetails.getLastName());
        mobileNumber.setText(providerDetails.getMobile());
        dateOfBirth.setText(providerDetails.getDob());
        aboutYou.setText(providerDetails.getAbout());

        BaseUtils.log(TAG, "setValues: " + providerDetails.getGender());

        if (providerDetails.getGender().equalsIgnoreCase(getString(R.string.male))) {
            genderSpinner.setSelection(0);
        } else if (providerDetails.getGender().equalsIgnoreCase(getString(R.string.female))) {
            genderSpinner.setSelection(1);
        } else {
            genderSpinner.setSelection(2);
        }

        uploadImagepath = providerDetails.getImage();

        loadImage(uploadImagepath, profileImage, UiUtils.getProfilePicture(EditProfileActivity.this));


    }


    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        GlideHelper.setImage(url, imageView, drawable);
    }


    private void initAdapters() {
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.items_spinner, genders);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(aa);
    }

    private void initViews() {
        editProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        appSettings = new AppSettings(EditProfileActivity.this);
        firstName = findViewById(R.id.firstName);
        rootView = findViewById(R.id.rootView);
        lastName = findViewById(R.id.lastName);
        mobileNumber = findViewById(R.id.mobileNumber);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        aboutYou = findViewById(R.id.aboutYou);
        genderSpinner = findViewById(R.id.genderSpinner);
        profileImage = findViewById(R.id.profileImage);
        bottomButton = findViewById(R.id.bottomButton);
        backButton = findViewById(R.id.backButton);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);

        genders = new String[]{getResources().getString(R.string.male), getResources().getString(R.string.female), getResources().getString(R.string.other)};
        dateOfBirth.setEnabled(false);
        UiUtils.setProfilePicture(EditProfileActivity.this, profileImage);
        toolBarTitle.setText(R.string.edit_profile);

        firstName.setFilters(new InputFilter[]{ValidationsUtils.emojiSpecialFilter, new InputFilter.LengthFilter(15)});
        lastName.setFilters(new InputFilter[]{ValidationsUtils.emojiSpecialFilter, new InputFilter.LengthFilter(15)});
        aboutYou.setFilters(new InputFilter[]{ValidationsUtils.emojiChatFilter});

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseUtils.log(TAG, "onRequestPermissionsResult:" + requestCode);
        switch (requestCode) {
            case CAMERA_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    UiUtils.openCamera(EditProfileActivity.this, Constants.CAMERA.FILE_NAME_PROFILE);
                } else {
                    UiUtils.snackBar(rootView, getString(R.string.camera_permission_error));

                }
                break;

            case READ_STORAGE_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UiUtils.openGallery(EditProfileActivity.this);
                } else {
                    UiUtils.snackBar(rootView, getString(R.string.storage_permission_error));

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseUtils.log(TAG, "onActivityResult: " + requestCode);
        switch (requestCode) {
            case GALLERY_INTENT://gallery
                if (data != null) {

                    Uri uri = data.getData();
                    if (uri != null) {
                        handleimage(uri);
                    } else {
                        UiUtils.snackBar(rootView, getString(R.string.unable_to_select));
                    }
                }
                break;
            case CAMERA_INTENT://camera
                uploadFile = new File(String.valueOf(appSettings.getImageUploadPath()));
                if (uploadFile.exists()) {
                    loadImage(appSettings.getImageUploadPath(), profileImage, UiUtils.getProfilePicture(this));
                    BaseUtils.log(TAG, "onActivityResult: " + appSettings.getImageUploadPath());
                }
                break;
        }
    }

    private void handleimage(Uri uri) {
        loadImage(BaseUtils.getRealPathFromUriNew(EditProfileActivity.this, uri), profileImage, UiUtils.getProfilePicture(this));
        uploadFile = new File(BaseUtils.getRealPathFromURI(EditProfileActivity.this, uri));

    }


    @Override
    public void onClick(View view) {
        if (view == profileImage) {
            UiUtils.showPictureDialog(EditProfileActivity.this, Constants.CAMERA.FILE_NAME_PROFILE);
        } else if (view == bottomButton) {
            UiUtils.closeKeyboard(view);

            if (ConnectionUtils.isNetworkConnected(EditProfileActivity.this)) {

                registerValues(uploadFile);
            } else {
                UiUtils.snackBar(rootView, getString(R.string.no_internet_connection));
            }
        } else if (view == backButton) {
            finish();
        }
    }


    @NonNull
    private JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", firstName.getText().toString().trim());
            jsonObject.put("last_name", lastName.getText().toString().trim());
            jsonObject.put("dob", dateOfBirth.getText().toString());
            jsonObject.put("mobile", mobileNumber.getText().toString().trim());
            jsonObject.put("about", aboutYou.getText().toString().trim());
            jsonObject.put("gender", "" + genderSpinner.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void registerValues(File uploadFile) {
        uploadImageRequest(EditProfileActivity.this, uploadFile, getJsonObject());
    }


    private void uploadImageRequest(Activity activity, File imageFile, JSONObject jsonObject) {

        if (imageFile != null && imageFile.exists()) {
            new HitImageUpload(activity, imageFile, new ImageUpload() {
                @Override
                public void onFinish(String imageUrl) {
                    if (imageUrl == null) {
                        UiUtils.snackBar(rootView, activity.getString(R.string.image_failed));
                    } else {
                        try {
                            jsonObject.put("image", imageUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BaseUtils.log(TAG, "jsonObject: " + jsonObject);
                        editProfileUpdate(jsonObject);
                    }
                }
            }).execute();

        } else {
            try {
                jsonObject.put("file", uploadImagepath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BaseUtils.log(TAG, "jsonObject: " + jsonObject);
            editProfileUpdate(jsonObject);
        }
    }

    private class HitImageUpload extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        private File imageFile;
        private ImageUpload imageUpload;

        public HitImageUpload(Activity activity, File uploadFile, ImageUpload imageUpload) {
            this.activity = activity;
            this.imageFile = uploadFile;
            this.imageUpload = imageUpload;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UiUtils.show(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imageUpload(activity, imageFile, imageUpload);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UiUtils.dismiss();
        }
    }

    private void imageUpload(Context context, File imageFile, ImageUpload imageUpload) {
        File imageFiles = UiUtils.getCompressedFile(context, imageFile);
        final InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setHeaderStatus(true);
        inputForAPI.setFile(imageFiles);

        commonViewModel.uploadImageApi(inputForAPI).observe((LifecycleOwner) context,
                new Observer<ImageUploadApiModel>() {
                    @Override
                    public void onChanged(@Nullable ImageUploadApiModel imageUploadApiModel) {
                        if (imageUploadApiModel != null) {
                            if (!imageUploadApiModel.getError()) {
                                BaseUtils.log(TAG, "file: " + imageUploadApiModel.getImage());
                                imageUpload.onFinish(imageUploadApiModel.getImage());
                            } else {
                                imageUpload.onFinish(null);
                            }
                        } else {
                            imageUpload.onFinish(null);
                        }
                    }
                });
    }

    public void editProfileUpdate(JSONObject jsonObject) {
        if (jsonObject != null) {
            InputForAPI inputForAPI = new InputForAPI(this);
            inputForAPI.setUrl(UrlHelper.UPDATE_PROFILE);
            inputForAPI.setJsonObject(jsonObject);
            inputForAPI.setHeaderStatus(true);
            inputForAPI.setShowLoader(true);
            updateProfileApi(inputForAPI);
        }
    }

    private void updateProfileApi(InputForAPI inputForAPI) {

        editProfileViewModel.updateProfile(inputForAPI).observe(this, new Observer<UpdateProfileApiModel>() {
            @Override
            public void onChanged(@Nullable UpdateProfileApiModel response) {
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
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra(LOGIN_TYPE, HOME_TAB);
        startActivity(intent);
        finish();
    }
}