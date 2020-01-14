package com.app.jobfizzerxp.view.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.jobfizzerxp.model.categoryApi.CategoryApiModel;
import com.app.jobfizzerxp.model.categoryApi.ListCategory;
import com.app.jobfizzerxp.model.categoryApi.ListSubcategory;
import com.app.jobfizzerxp.model.categoryApi.SubCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.AddCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.Category;
import com.app.jobfizzerxp.model.viewCategoryApi.DeleteCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.EditCategoryApiModel;
import com.app.jobfizzerxp.model.viewCategoryApi.ViewCategoryApiModel;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UiUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;
import com.app.jobfizzerxp.utilities.interfaces.ViewCategoryListener;
import com.app.jobfizzerxp.utilities.networkUtils.ConnectionUtils;
import com.app.jobfizzerxp.utilities.networkUtils.InputForAPI;
import com.app.jobfizzerxp.view.adapters.ViewCategoryAdapter;
import com.app.jobfizzerxp.viewModel.CommonViewModel;
import com.app.jobfizzerxp.viewModel.ViewCategoryViewModel;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCategory extends BaseActivity {
    private RecyclerView categoryItems;
    private List<ListCategory> listCategories = new ArrayList<>();
    private List<ListSubcategory> listSubcategories = new ArrayList<>();
    private String[] categoryName;
    private String[] subcategoryName;
    private int subCategoryposition;
    private int categoryPosition;
    private boolean isEditenabled = false;
    private String TAG = ViewCategory.class.getSimpleName();
    private ImageView backButton;
    private LinearLayout addCategory;

    private ViewCategoryViewModel categoryViewModel;
    private CommonViewModel commonViewModel;
    private LinearLayout rootView;


    public void showEditDialog(final Context context, final Category category) {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_category_edit);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        dialog.show();
        final String catName, subCatName;
        catName = category.getCategoryName();
        subCatName = category.getSubCategoryName();

        final Spinner categorySpinner, subCategorySpinner;
        final EditText pricePerhour, quickPitch, experience;
        final Button confirmButton, deleteButton;
        CardView cardView;

        categorySpinner = dialog.findViewById(R.id.categorySpinner);
        subCategorySpinner = dialog.findViewById(R.id.subCategorySpinner);
        pricePerhour = dialog.findViewById(R.id.pricePerhour);
        quickPitch = dialog.findViewById(R.id.quickPitch);
        experience = dialog.findViewById(R.id.experience);
        confirmButton = dialog.findViewById(R.id.confirmButton);
        deleteButton = dialog.findViewById(R.id.deleteButton);
        cardView = dialog.findViewById(R.id.cardView);

        experience.setText(category.getExperience());
        pricePerhour.setText(category.getPriceperhour());
        quickPitch.setText(category.getQuickpitch());

        isEditenabled = false;
        categorySpinner.setEnabled(false);
        subCategorySpinner.setEnabled(false);
        experience.setEnabled(false);
        pricePerhour.setEnabled(false);
        quickPitch.setEnabled(false);

        for (int i = 0; i < categoryName.length; i++) {
            if (categoryName[i].equalsIgnoreCase(catName)) {
                categoryPosition = i;
            }
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, R.layout.items_spinner, categoryName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(aa);
        categorySpinner.setSelection(categoryPosition, true);


        categorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    UiUtils.closeKeyboard(view);
                }
                return false;
            }
        });
        subCategorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    UiUtils.closeKeyboard(view);
                }
                return false;
            }
        });

        hitListSubApi(context, subCatName, subCategorySpinner, categoryPosition);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                hitListSubApi(context, subCatName, subCategorySpinner, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deleteValues(dialog, String.valueOf(category.getId()), context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                UiUtils.closeKeyboard(view);
                checkEditValues(cardView, quickPitch, pricePerhour, experience, categorySpinner, subCategorySpinner, dialog, context, category, confirmButton);
            }
        });
    }

    private void checkEditValues(CardView cardView, EditText quickPitch, EditText pricePerhour, EditText experience, Spinner categorySpinner, Spinner subCategorySpinner, Dialog dialog, Context context, Category category, Button confirmButton) {
        if (isEditenabled) {
            if (categoryName[categorySpinner.getSelectedItemPosition()].length() > 0) {
                if (subcategoryName.length > 0) {
                    if (subcategoryName[subCategorySpinner.getSelectedItemPosition()].length() > 0) {
                        if (experience.getText().toString().trim().length() > 0) {
                            if (pricePerhour.getText().toString().trim().length() > 0 && Integer.parseInt(pricePerhour.getText().toString()) != 0) {
                                if (quickPitch.getText().toString().trim().length() > 0) {
                                    try {
                                        updateValues(dialog, context, pricePerhour.getText().toString(), experience.getText().toString(), quickPitch.getText().toString(), String.valueOf(category.getId()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    UiUtils.snackBar(cardView, getString(R.string.please_enter_quick_pitch));
                                }
                            } else {
                                UiUtils.snackBar(cardView, getString(R.string.please_enter_price_per_ho));
                            }
                        } else {
                            UiUtils.snackBar(cardView, getString(R.string.please_enter_experience));
                        }
                    } else {
                        UiUtils.snackBar(cardView, getString(R.string.please_select_subcategroy));
                    }
                } else {
                    UiUtils.snackBar(cardView, getString(R.string.no_sub_categroy));
                }
            } else {
                UiUtils.snackBar(cardView, getString(R.string.please_select_categroy));
            }

        } else {
            confirmButton.setText(context.getResources().getString(R.string.save));
            isEditenabled = true;
            categorySpinner.setEnabled(false);
            subCategorySpinner.setEnabled(false);
            experience.setEnabled(true);
            pricePerhour.setEnabled(true);
            quickPitch.setEnabled(true);
        }
    }

    @NonNull
    private JSONObject getJsonObjectListSub(int categoryPosition) {
        JSONObject input = new JSONObject();
        try {
            input.put("id", listCategories.get(categoryPosition).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return input;
    }

    private void hitListSubApi(Context context, String subCatName, Spinner subCategorySpinner, int categoryPosition) {
        InputForAPI inputForAPI = getInputForListSubAPI(context, categoryPosition);

        commonViewModel.listSubCategory(inputForAPI).observe(this, new Observer<SubCategoryApiModel>() {
            @Override
            public void onChanged(@Nullable SubCategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        handleListSubResponse(addCategoryApiModel, subCatName, context, subCategorySpinner);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    @NonNull
    private InputForAPI getInputForListSubAPI(Context context, int categoryPosition) {
        JSONObject input = getJsonObjectListSub(categoryPosition);

        InputForAPI inputForAPI = new InputForAPI(context);
        inputForAPI.setUrl(UrlHelper.LIST_SUB_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(input);
        inputForAPI.setHeaderStatus(true);
        return inputForAPI;
    }

    private void handleListSubResponse(SubCategoryApiModel addCategoryApiModel, String subCatName,
                                       Context context, Spinner subCategorySpinner) {
        listSubcategories = addCategoryApiModel.getListSubcategory();
        subcategoryName = new String[listSubcategories.size()];
        for (int in = 0; in < listSubcategories.size(); in++) {

            subcategoryName[in] = listSubcategories.get(in).getSubCategoryName();
            if (listSubcategories.get(in).getSubCategoryName().equalsIgnoreCase(subCatName)) {
                subCategoryposition = in;
            }

        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, R.layout.items_spinner, subcategoryName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategorySpinner.setAdapter(aa);
        subCategorySpinner.setSelection(subCategoryposition, true);

    }

    private void deleteValues(final Dialog dialog, String id, final Context context) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("provider_service_id", id);

        InputForAPI inputForAPI = new InputForAPI(ViewCategory.this);
        inputForAPI.setUrl(UrlHelper.DELETE_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);

        categoryViewModel.deleteCategory(inputForAPI).observe(this, new Observer<DeleteCategoryApiModel>() {
            @Override
            public void onChanged(@Nullable DeleteCategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        dialog.dismiss();
                        getValues(context);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void updateValues(final Dialog dialog, final Context context, String priceper, String experience, String quickpitch, String id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("provider_service_id", id);
        jsonObject.put("priceperhour", priceper);
        jsonObject.put("quickpitch", quickpitch);
        jsonObject.put("experience", experience);

        InputForAPI inputForAPI = new InputForAPI(ViewCategory.this);
        inputForAPI.setUrl(UrlHelper.EDIT_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);

        categoryViewModel.editCategory(inputForAPI).observe(this, new Observer<EditCategoryApiModel>() {
            @Override
            public void onChanged(@Nullable EditCategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        dialog.dismiss();
                        getValues(context);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void getValues(final Context context) {

        InputForAPI inputForAPI = new InputForAPI(ViewCategory.this);
        inputForAPI.setUrl(UrlHelper.VIEW_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(true);

        categoryViewModel.viewCategory(inputForAPI).observe(this, new Observer<ViewCategoryApiModel>() {
            @Override
            public void onChanged(@Nullable ViewCategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        handleViewCategorySuccess(addCategoryApiModel, context);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void handleViewCategorySuccess(ViewCategoryApiModel addCategoryApiModel, Context context) {
        List<Category> categoryList = addCategoryApiModel.getCategory();
        BaseUtils.log(TAG, "response:" + categoryList.toString());

        for (int i = 0; i < categoryList.size(); i++) {
            categoryList.get(i).setCategoryName(categoryList.get(i).getCategoryName());
            categoryList.get(i).setStatus("1");
        }
        ViewCategoryAdapter viewProviderCategoryAdapter = new ViewCategoryAdapter(context, categoryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        categoryItems.setLayoutManager(linearLayoutManager);
        viewProviderCategoryAdapter.setViewCategoryListener(new ViewCategoryListener() {
            @Override
            public void onEditDialog(Context context, Category category) {

                if (ConnectionUtils.isNetworkConnected(context)) {
                    showEditDialog(context, category);

                } else {
                    UiUtils.snackBar(rootView, context.getString(R.string.no_internet_connection));
                }
            }
        });
        categoryItems.setAdapter(viewProviderCategoryAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        initViews();
        initListners();
        getValues(ViewCategory.this);
        getCategoryData();

    }

    private void initListners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ConnectionUtils.isNetworkConnected(ViewCategory.this)) {
                        showAddDialog();

                    } else {
                        UiUtils.snackBar(rootView, getString(R.string.no_internet_connection));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCategoryData() {

        InputForAPI inputForAPI = new InputForAPI(ViewCategory.this);
        inputForAPI.setUrl(UrlHelper.LIST_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setHeaderStatus(false);

        commonViewModel.listCategory(inputForAPI).observe(this, new Observer<CategoryApiModel>() {
            @Override
            public void onChanged(@Nullable CategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        handleListCategorySuccess(addCategoryApiModel);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void handleListCategorySuccess(CategoryApiModel addCategoryApiModel) {
        listCategories = addCategoryApiModel.getListCategory();
        categoryName = new String[listCategories.size()];
        for (int i = 0; i < listCategories.size(); i++) {
            categoryName[i] = listCategories.get(i).getCategoryName();
        }
    }

    private void showAddDialog() {
        final Dialog dialog = new Dialog(ViewCategory.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_category_add);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        dialog.show();

        final Spinner categorySpinner, subCategorySpinner;
        final EditText pricePerhour, quickPitch, experience;
        Button confirmButton;
        CardView cardView;

        categorySpinner = dialog.findViewById(R.id.categorySpinner);
        subCategorySpinner = dialog.findViewById(R.id.subCategorySpinner);
        pricePerhour = dialog.findViewById(R.id.pricePerhour);
        quickPitch = dialog.findViewById(R.id.quickPitch);
        experience = dialog.findViewById(R.id.experience);
        confirmButton = dialog.findViewById(R.id.addButton);
        cardView = dialog.findViewById(R.id.cardView);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.items_spinner, categoryName);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    UiUtils.closeKeyboard(view);
                }
                return false;
            }
        });
        subCategorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    UiUtils.closeKeyboard(view);
                }
                return false;
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                requestListSubApi(i, subCategorySpinner, ViewCategory.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.closeKeyboard(view);

                if (ConnectionUtils.isNetworkConnected(ViewCategory.this)) {
                    checkAddValues(cardView, quickPitch, pricePerhour, experience, categorySpinner, subCategorySpinner, dialog);

                } else {
                    UiUtils.snackBar(rootView, getString(R.string.no_internet_connection));
                }
            }
        });
    }

    private void checkAddValues(CardView cardView, EditText quickPitch, EditText pricePerhour,
                                EditText experience, Spinner categorySpinner,
                                Spinner subCategorySpinner, Dialog dialog) {
        if (categoryName[categorySpinner.getSelectedItemPosition()].length() > 0) {
            if (subcategoryName.length > 0) {
                if (subcategoryName[subCategorySpinner.getSelectedItemPosition()].length() > 0) {
                    if (experience.getText().toString().trim().length() > 0) {
                        if (pricePerhour.getText().toString().trim().length() > 0 && Integer.parseInt(pricePerhour.getText().toString()) != 0) {
                            if (quickPitch.getText().toString().trim().length() > 0) {
                                setAddAdapter(quickPitch, pricePerhour, experience, categorySpinner, subCategorySpinner, dialog);
                            } else {
                                UiUtils.snackBar(cardView, getString(R.string.please_enter_quick_pitch));
                            }
                        } else {
                            UiUtils.snackBar(cardView, getString(R.string.please_enter_price_per_ho));
                        }
                    } else {
                        UiUtils.snackBar(cardView, getString(R.string.please_enter_experience));
                    }
                } else {
                    UiUtils.snackBar(cardView, getString(R.string.please_select_subcategroy));
                }
            } else {
                UiUtils.snackBar(cardView, getString(R.string.no_sub_categroy));
            }
        } else {
            UiUtils.snackBar(cardView, getString(R.string.please_select_categroy));
        }
    }

    private void setAddAdapter(EditText quickPitch, EditText pricePerhour, EditText experience, Spinner categorySpinner, Spinner subCategorySpinner, Dialog dialog) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sub_category_id", listSubcategories.get(subCategorySpinner.getSelectedItemPosition()).getId());
            jsonObject.put("quickpitch", quickPitch.getText().toString());
            jsonObject.put("priceperhour", pricePerhour.getText().toString());
            jsonObject.put("experience", experience.getText().toString());
            jsonObject.put("category_id", listCategories.get(categorySpinner.getSelectedItemPosition()).getId());

            addValues(jsonObject, dialog);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    private void requestListSubApi(int i, Spinner subCategorySpinner, Context context) {
        InputForAPI inputForAPI = getInputForListSubAPI(context, i);
        commonViewModel.listSubCategory(inputForAPI).observe(this,
                new Observer<SubCategoryApiModel>() {
                    @Override
                    public void onChanged(@Nullable SubCategoryApiModel subCategoryApiModel) {

                        if (subCategoryApiModel != null) {
                            if (!subCategoryApiModel.getError()) {
                                responseListSub(subCategoryApiModel, subCategorySpinner);
                            } else {
                                UiUtils.snackBar(rootView, subCategoryApiModel.getErrorMessage());
                            }
                        }
                    }
                });
    }

    private void responseListSub(SubCategoryApiModel subCategoryApiModel, Spinner subCategorySpinner) {
        listSubcategories = subCategoryApiModel.getListSubcategory();
        subcategoryName = new String[listSubcategories.size()];
        for (int in = 0; in < listSubcategories.size(); in++) {
            subcategoryName[in] = listSubcategories.get(in).getSubCategoryName();

        }
        ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(ViewCategory.this, android.R.layout.simple_spinner_item, subcategoryName);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategorySpinner.setAdapter(subCategoryAdapter);
    }

    private void addValues(JSONObject jsonObject, final Dialog dialog) {
        InputForAPI inputForAPI = new InputForAPI(ViewCategory.this);
        inputForAPI.setUrl(UrlHelper.ADD_CATEGORY);
        inputForAPI.setShowLoader(true);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaderStatus(true);

        categoryViewModel.addCategory(inputForAPI).observe(this, new Observer<AddCategoryApiModel>() {
            @Override
            public void onChanged(@Nullable AddCategoryApiModel addCategoryApiModel) {
                if (addCategoryApiModel != null) {
                    if (!addCategoryApiModel.getError()) {
                        dialog.dismiss();
                        getValues(ViewCategory.this);
                    } else {
                        UiUtils.snackBar(rootView, addCategoryApiModel.getErrorMessage());
                    }
                }
            }
        });
    }


    private void initViews() {
        rootView = findViewById(R.id.rootView);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(ViewCategoryViewModel.class);
        categoryItems = findViewById(R.id.categoryItems);
        backButton = findViewById(R.id.backButton);
        addCategory = findViewById(R.id.addCategory);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(R.string.services);
    }
}