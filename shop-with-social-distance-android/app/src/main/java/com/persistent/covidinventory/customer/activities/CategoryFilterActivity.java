package com.persistent.covidinventory.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.customer.object.CategoryListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox[] checkBoxOptions;
    LinearLayout checkboxLayout ;
    boolean[] userSelection;
    String[] userSelectedCategory;

    CartDetails cartDetails;
    ArrayList<ItemDetails> itemList;
    JSONObject getItem;

    ArrayList<Boolean> arrayListUserSelection;
    ArrayList<String> categoryArrayList;
    ArrayList<String> selectedCategoryArrayList;
    CategoryListViewAdapter categoryListViewAdapter;

    ProgressDialog progressDialog;
    String getCategoriesAPI = Constants.endpoint + "/covid-inventory/api/items/category?categories=";

    RecyclerView recyclerViewCategory;
    LinearLayoutManager linearLayoutManager;
    Button apply , clear ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);

        Intent intent = getIntent();
        cartDetails = (CartDetails) intent.getSerializableExtra("cart");
        Log.v("CartDetail","CartDetail"+cartDetails.categoriesSelected);
        initializeUIObjects();
        getAllCategoryList();
        setRecyclerViewCategoryUI();
//        displayCheckedValue();
    }

    private void initializeUIObjects(){

        itemList = new ArrayList<>();
        categoryArrayList = new ArrayList<>();


//        arrayListUserSelection = new ArrayList<Boolean>(6);

//        checkboxLayout = (LinearLayout)findViewById(R.id.checkbox_linear_layout);
//        checkBoxOptions = new CheckBox[6];
//        userSelection = new boolean[6];
//        userSelectedCategory = new String[6];
//        checkBoxOptions[0]= (CheckBox) findViewById(R.id.checkbox_household);
//        checkBoxOptions[1]= (CheckBox) findViewById(R.id.checkBox_kitchen_appliance);
//        checkBoxOptions[2]= (CheckBox) findViewById(R.id.checkBox_utensils);
//        checkBoxOptions[3]= (CheckBox) findViewById(R.id.checkBox_electronics);
//        checkBoxOptions[4]= (CheckBox) findViewById(R.id.checkBox_watches);
//        checkBoxOptions[5]= (CheckBox) findViewById(R.id.checkBox_cosmetics);
//

        apply = (Button)findViewById(R.id.apply_btn);
        clear = (Button)findViewById(R.id.clearAll_btn);
        if(cartDetails.categoriesSelected.size()==0){
            apply.setEnabled(true);
            apply.setOnClickListener(this);
        }else {
            apply.setBackgroundColor(getResources().getColor(R.color.darker_gray));
            //apply.getBackground().setColorFilter(apply.getContext().getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            apply.setEnabled(false);
        }


        clear.setOnClickListener(this);
    }

    private void setRecyclerViewCategoryUI(){
        recyclerViewCategory  = (RecyclerView)findViewById(R.id.recyclerview_category);
        linearLayoutManager = new LinearLayoutManager(CategoryFilterActivity.this);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);
        categoryListViewAdapter = new CategoryListViewAdapter(CategoryFilterActivity.this, categoryArrayList, cartDetails);
        recyclerViewCategory.setAdapter(categoryListViewAdapter);

    }

//    public void getSelectedCheckboxValue(){
//
//        //update the array with user selected answers
//        for(int q = 0; q< checkBoxOptions.length; q++){
//            if(checkBoxOptions[q].isChecked()){
//                userSelection[q]=true;
//                cartDetails.arraySelected[q]= true;
//
//            }
//        }
//
//        for(int i =0; i<userSelection.length;i++) {
//            if(userSelection[i]){
//                switch (i){
//                    case 0 :
//                        String categoryValue = "Household";
//                        userSelectedCategory[i] = categoryValue;
//                        getCategoriesAPI = getCategoriesAPI+categoryValue+",";
//                        break;
//
//                    case 1 :
//                        String categoryValue1 = "Kitchen Appliance";
//                        userSelectedCategory[i] = categoryValue1;
//                        getCategoriesAPI = getCategoriesAPI+categoryValue1+",";
//                        break;
//
//                    case 2 :
//                        String categoryValue2 = "Utensils";
//                        userSelectedCategory[i] = categoryValue2;
//                        getCategoriesAPI = getCategoriesAPI+categoryValue2+",";
//                        break;
//
//                    case 3 :
//                        String categoryValue3 = "Electronics";
//                        userSelectedCategory[i] = categoryValue3;
//                        getCategoriesAPI = getCategoriesAPI+categoryValue3+",";
//                        break;
//
//                    case 4 :
//                        String categoryValue4 = "Watches";
//                        userSelectedCategory[i] = categoryValue4;
//                        getCategoriesAPI = getCategoriesAPI+categoryValue4+",";
//                        break;
//
//                    case 5 :
//                        String categoryValue5 = "Cosmetics";
//                        userSelectedCategory[i] = categoryValue5;
//                        getCategoriesAPI = getCategoriesAPI+categoryValue5+",";
//                        break;
//
//                }
//
//
//            }
//            Log.v("Selected Values", "Selected categories" + userSelection[i]);
//            Log.v("Selected Values", "Selected categories" + userSelectedCategory[i]);
//            Log.v("Selected Values length", "Selected categories length" + userSelectedCategory.length);
//
//        }
//
//        Log.v("API categories","API category "+getCategoriesAPI);
//
//        if (getCategoriesAPI.endsWith(",")) {
//            getCategoriesAPI = getCategoriesAPI.substring(0, getCategoriesAPI.length()-1);
//        }
//
//        Log.v("API categories","API category "+getCategoriesAPI);
//
//
//    }

    private void getAllCategoryList(){
        String getAllCategoriesApiURL = Constants.endpoint + "/covid-inventory/api/items/categories";

        StringRequest sr = new StringRequest(Request.Method.GET,getAllCategoriesApiURL,
                response -> {
                    try {
                        JSONObject requestResponse = new JSONObject(response);
                        parseJSONResponseAllCategories(requestResponse);
                        Log.v("HttpClient", "success! response: " + response);
//                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HttpClient", "Error in connection");
                    }
                    Log.v("HttpClient", "success! response: " + response);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                });
        sr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);




    }

    private void getItemsByCategoriesAPI(){

        displayProgress("Loading products...");
        StringRequest sr = new StringRequest(Request.Method.GET,getCategoriesAPI,
                response -> {
                    try {
                        getItem = new JSONObject(response);
                        parseJSONResponse(getItem);
                        Log.v("HttpClient", "success! response: " + response);
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HttpClient", "Error in connection");
                    }
                    Log.v("HttpClient", "success! response: " + response);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {


        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);




    }

//    public void displayCheckedValue(){
//        for(int q = 0; q< cartDetails.arraySelected.length; q++){
//            if(cartDetails.arraySelected[q]==null){
//                checkBoxOptions[q].setChecked(false);
//            }
//             else if(cartDetails.arraySelected[q]){
//                checkBoxOptions[q].setChecked(true);
//            }
//        }
//
//    }


    @Override
    public void onClick(View v) {

        if(v.getId()==apply.getId()){
          // getSelectedCheckboxValue();
            getSelectedCategories();
            Intent intent = new Intent(CategoryFilterActivity.this, LandingPageActivity.class);
            intent.putExtra("cart",cartDetails);
            startActivity(intent);
            finishAffinity();

            //getItemsByCategoriesAPI();

            Log.v("ArrayList for ","ArrayList for user selection  "+selectedCategoryArrayList);

        }
        else if(v.getId()== clear.getId()){
            Log.v("Set new Action","Set new Action");
            Intent intent = new Intent(CategoryFilterActivity.this, LandingPageActivity.class);
            cartDetails.getCategoryList().clear();
            cartDetails.categoriesSelected = new ArrayList<>();
            cartDetails.arraySelected = new Boolean[6];
            intent.putExtra("cart",cartDetails);
            startActivity(intent);
            finishAffinity();
        }

    }


    private void parseJSONResponse(JSONObject getItems){

        try
        {
            JSONObject getList = getItems;

            JSONArray jsonArray = getList.getJSONArray("result");
            Log.v("Item Array","Item Array"+jsonArray);


            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ItemDetails itemDetails = new ItemDetails();

                itemDetails.setId( jsonObject.getInt("id"));
                itemDetails.setItemNumber( jsonObject.getString("itemNumber"));
                itemDetails.setGroup( jsonObject.getString("group"));
                itemDetails.setItemName( jsonObject.getString("itemName"));
                itemDetails.setPrice( jsonObject.getDouble("price"));
                itemDetails.setStock(jsonObject.getInt("stock"));
                itemDetails.setCategory( jsonObject.getString("category"));
                itemDetails.setLowStockIndicator( jsonObject.getBoolean("lowStockIndicator"));
                itemDetails.setToBeSoldOneItemPerOrder( jsonObject.getBoolean("toBeSoldOneItemPerOrder"));


                String volumePerLiter = jsonObject.isNull("volumePerItem") ? null : jsonObject.getString("volumePerItem");

                itemDetails.setVolumePerItem(volumePerLiter);
                itemDetails.setMonthlyQuotaPerUser( jsonObject.getString("monthlyQuotaPerUser"));
                itemDetails.setItemType( jsonObject.getString("itemType"));
                itemDetails.setYearlyQuotaPerUser( jsonObject.getString("yearlyQuotaPerUser"));

                String noOfItemsOrderedStr = jsonObject.getString("noOfItemsOrderded");
                int noOfItemsOrdered = 0;
                if(noOfItemsOrderedStr != null && noOfItemsOrderedStr != "null") {
                    noOfItemsOrdered = Integer.parseInt(noOfItemsOrderedStr);
                }

                itemDetails.setNoOfItemsOrderded(noOfItemsOrdered);

                itemList.add(itemDetails);

            }

//            cartDetails.getCartItems().clear();
//            cartDetails.setCartItems(itemList);
            Log.v("itemList = ", "itemList = "+itemList);
            Log.v("getCartItems() =","cartDetails.getCartItems().size()"+cartDetails.getCartItems().size());

            Intent i = new Intent(CategoryFilterActivity.this,LandingPageActivity.class);

            Log.v("category  =","cartDetails.getCartItems().size()"+cartDetails.getCartItems().size());

            cartDetails.setCategoryList(itemList);
            i.putExtra("cart",cartDetails);

//            finishAffinity();
            startActivity(i);

        }
        catch (JSONException e)
        {   e.printStackTrace();    }




    }

    private void parseJSONResponseAllCategories(JSONObject response){
        try {
            JSONArray categoryArray = response.getJSONArray("result");
            for(int i =0; i<categoryArray.length();i++){
                String category = categoryArray.getString(i);
                categoryArrayList.add(category);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        categoryListViewAdapter.notifyDataSetChanged();
    }

    private void getSelectedCategories(){
        if(categoryListViewAdapter==null){
            //Do Nothing , Null check
        }else {
            selectedCategoryArrayList = categoryListViewAdapter.getSelectedCategoryList();
        }
        Log.v("CategoryFilterActivity", "selected Values from category"+selectedCategoryArrayList);
        Log.v("CategoryFilterActivity", "selected Values from category get function"+categoryListViewAdapter.getSelectedCategoryList());

        for(int i =0;i<selectedCategoryArrayList.size();i++){
            getCategoriesAPI = getCategoriesAPI+selectedCategoryArrayList.get(i)+",";
        }
        Log.v("CategoryFilterActivity", "selected Values from category With API"+getCategoriesAPI);


        if (getCategoriesAPI.endsWith(",")) {
            getCategoriesAPI = getCategoriesAPI.substring(0, getCategoriesAPI.length()-1);
        }

        Log.v("API categories","API category "+getCategoriesAPI);


    }

    private void displayProgress(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }
}



