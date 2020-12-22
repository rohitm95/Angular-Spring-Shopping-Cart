package com.persistent.covidinventory.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.model.OrdersList;
import com.persistent.covidinventory.aggregator.model.StoreDetails;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.customer.model.CustomerOrderDetails;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.customer.model.Role;
import com.persistent.covidinventory.customer.model.UserDetails;
import com.persistent.covidinventory.customer.object.CustomerOrderDetailsViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;

public class CustomerOrderDisplayActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public int orderNo;
    public static final String COVID_INVENTORY_API_ORDERS_ORDER_DETAILS = "/covid-inventory/api/orders/orderDetails/";

    ArrayList<CustomerOrderDetails> orderDetailsArrayList;
    TextView textViewOrderNo , textViewTotalAmountPayable;

    OrdersList ordersListObj;
    JSONObject orderDetailsJSON;

    double totalAmount =0 ;
    CustomerOrderDetailsViewAdapter customerOrderDetailsViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_display);
        Intent intent = getIntent();
        orderNo = intent.getIntExtra("orderNo",0);

        initializeUIObjects();
        loadCustomerOrders(orderNo);
//        setRecyclerView();


    }

    public void initializeUIObjects(){
        ordersListObj = new OrdersList();
        orderDetailsArrayList = new ArrayList<>() ;
        textViewOrderNo = (TextView)findViewById(R.id.txt_order_no);
        textViewTotalAmountPayable = (TextView)findViewById(R.id.txt_amount_payable);

    }
    public void setValuesToUI(OrdersList ordersListObj){
        int orderNo = ordersListObj.getOrderNo();
        double totalAmount = ordersListObj.getAmountPayable();

        String.format("%.2f", totalAmount);
        textViewTotalAmountPayable.setText(String.format("%.2f", totalAmount));
        textViewOrderNo.setText(Integer.toString(orderNo));
    }

    public void setRecyclerView(OrdersList ordersListObj){
        recyclerView = (RecyclerView)findViewById(R.id.customer_order_details_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerOrderDisplayActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        customerOrderDetailsViewAdapter = new CustomerOrderDetailsViewAdapter(CustomerOrderDisplayActivity.this,ordersListObj); //orderDetailsArrayList
        recyclerView.setAdapter(customerOrderDetailsViewAdapter);
    }

    public void loadCustomerOrders(int orderNo){
        //        displayProgress("Loading...");
        String url = Constants.endpoint+COVID_INVENTORY_API_ORDERS_ORDER_DETAILS+orderNo;

        StringRequest sr = new StringRequest(Request.Method.GET,url ,
                response -> {
                    try {
                        JSONObject orderDetailsObject = new JSONObject(response);
                            parseJSONResponse(orderDetailsObject);
                        Log.v("My Orders", "success! response: " + response);
//                            progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HttpClient", "Error in connection");
                    }
                    Log.v("My Orders", "success! response: " + response);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                Intent i = getIntent();
//                String nuggetId = i.getStringExtra("nuggetID");
//                params.put("email_id","supriya_pawar@gmail.com");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

//    public void parseJSONResponse(JSONObject orderObject) throws JSONException {
//        JSONArray result = orderObject.getJSONArray("result");
//
//        for(int i=0; i<result.length(); i++){
//
//            CustomerOrderDetails customerOrderDetails = new CustomerOrderDetails();
//
//            JSONObject innerJSONObject = result.getJSONObject(i);
//            customerOrderDetails.setOrderId(innerJSONObject.getInt("id"));
//            totalAmount = totalAmount+innerJSONObject.getDouble("amount");
////            textViewTotalAmountPayable.setText(Double.toString(innerJSONObject.getDouble("amount")));
//            customerOrderDetails.setOrderTotalAmount(innerJSONObject.getDouble("amount"));
//            customerOrderDetails.setItemName(innerJSONObject.getString("item_name"));
//
//            customerOrderDetails.setOrderNo(innerJSONObject.getInt("order_no"));
//            customerOrderDetails.setItemPrice(innerJSONObject.getDouble("price"));
//            customerOrderDetails.setItemQuantity(innerJSONObject.getInt("quantity"));
//            orderDetailsArrayList.add(customerOrderDetails);
//
//        }
//
//        customerOrderDetailsViewAdapter.notifyDataSetChanged();
//        setValuesToUI(orderNo,totalAmount);
//
//
//
//    }


    public void parseJSONResponse(JSONObject orderObject) throws JSONException {

        orderDetailsJSON = orderObject.getJSONObject("result");

        ordersListObj.setOrderNo(orderDetailsJSON.getInt("order_no"));
        ///customerOBJ
        UserDetails userDetails = new UserDetails();
        JSONObject jsonObject = orderDetailsJSON.getJSONObject("customer");
//      String dateOfJoin = jsonObject.isNull("dateOfJoin") ? null : jsonObject.getString("dateOfJoin");
//      userDetails.setDateOfJoin(dateOfJoin);
        userDetails.setDateOfJoin(jsonObject.getString("dateOfJoin"));
        userDetails.setEmailId(jsonObject.getString("emailId"));
        userDetails.setFirstName(jsonObject.getString("firstName"));
//      String gender = jsonObject.isNull("gender") ? null : jsonObject.getString("gender");
//      userDetails.setGender(gender);
        userDetails.setGender(jsonObject.getString("gender"));
        userDetails.setId(jsonObject.getInt("id"));
        userDetails.setActive(jsonObject.getBoolean("isActive"));
        userDetails.setLastName(jsonObject.getString("lastName"));
        userDetails.setMobileNumber(jsonObject.getString("mobileNumber"));
        userDetails.setNewUser(jsonObject.getBoolean("newUser"));
//      String userName = jsonObject.isNull("username") ? null : jsonObject.getString("username");
//      String password = jsonObject.isNull("password") ? null : jsonObject.getString("password");
//      userDetails.setPassword(password);
//      userDetails.setUsername(userName);
        userDetails.setUsername(jsonObject.getString("username"));
        userDetails.setPassword(jsonObject.getString("password"));
        Role role = new Role();
        JSONObject roleJson = jsonObject.getJSONObject("role");
        role.setId(roleJson.getInt("id"));
        role.setName(roleJson.getString("name"));
        userDetails.setRole(role);
//      String store = jsonObject.isNull("store") ? null : jsonObject.getString("store");
//      userDetails.setStore(store);
        userDetails.setStore(jsonObject.getString("store"));
//      String category = jsonObject.isNull("category") ? null : jsonObject.getString("category");
//      userDetails.setCategory(category);
        userDetails.setCategory(jsonObject.getString("category"));
//      String afdPurchaseLimit = jsonObject.isNull("afd_purchase_limit") ? null : jsonObject.getString("afd_purchase_limit");
//      userDetails.setAfdPurchaseLimit(afdPurchaseLimit);
        userDetails.setAfdPurchaseLimit(jsonObject.getString("afd_purchase_limit"));
//      String nonAfdPurchaseLimit = jsonObject.isNull("nonAFD_purchase_limit") ? null : jsonObject.getString("nonAFD_purchase_limit");
//      userDetails.setNonAFDPurchaseLimit(nonAfdPurchaseLimit);
        userDetails.setNonAFDPurchaseLimit(jsonObject.getString("nonAFD_purchase_limit"));
        ordersListObj.setUserDetails(userDetails);


//            //////storeOBJ
        JSONObject jsonObjectStore = orderDetailsJSON.getJSONObject("store");

        StoreDetails storeDetails = new StoreDetails();

        storeDetails.setStoreId(jsonObjectStore.getInt("id"));
//      String storeName = jsonObject.isNull("nonAFD_purchase_limit") ? null : jsonObject.getString("nonAFD_purchase_limit");
//      storeDetails.setStoreName(storeName);
        storeDetails.setStoreName(jsonObjectStore.getString("storeName"));
        storeDetails.setSlotDuration(jsonObjectStore.getInt("slotDuration"));
        storeDetails.setDeliveryInStlot(jsonObjectStore.getInt("deliveryInSlot"));
        String storeTimings = jsonObject.isNull("storeTimings") ? null : jsonObject.getString("storeTimings");
        storeDetails.setStoreTiming(storeTimings);
        String breakTimings = jsonObject.isNull("breakTimings") ? null : jsonObject.getString("breakTimings");
        storeDetails.setBreakTiming(breakTimings);
        String storeHolidays = jsonObject.isNull("storeHolidays") ? null : jsonObject.getString("storeHolidays");
        storeDetails.setStoreHolidays(storeHolidays);
        storeDetails.setActive(jsonObjectStore.getBoolean("active"));

        ordersListObj.setStoreDetails(storeDetails);

        ////

        ordersListObj.setDeliveryDate(orderDetailsJSON.getString("deliveryDate"));
        ordersListObj.setSlotFrom(orderDetailsJSON.getString("slotFrom"));
        ordersListObj.setSlotTo(orderDetailsJSON.getString("slotTo"));
        ordersListObj.setAmountPayable(orderDetailsJSON.getDouble( "amountPayable"));   //************************* need to uncomment
        ordersListObj.setStatus(orderDetailsJSON.getString( "status"));


        //// inventoryData

        JSONArray jsonArrayInventoryData = orderDetailsJSON.getJSONArray("inventoryDatas");
        ArrayList<ItemDetails> inventoryData = new ArrayList<>();
        for(int d =0; d<jsonArrayInventoryData.length();d++){
            JSONObject jsonObjectItem = jsonArrayInventoryData.getJSONObject(d);

            ItemDetails itemDetails = new ItemDetails();

            itemDetails.setId(jsonObjectItem.getInt("id"));
            itemDetails.setItemNumber(jsonObjectItem.getString("itemNumber"));
//             String group = jsonObjectItem.isNull("group") ? null : jsonObjectItem.getString("group");
//             itemDetails.setGroup(group);
            itemDetails.setGroup(jsonObjectItem.getString("group"));
            itemDetails.setItemName(jsonObjectItem.getString("itemName"));
            itemDetails.setPrice(jsonObjectItem.getDouble("price"));
             itemDetails.setStock(jsonObjectItem.getInt("stock"));
             itemDetails.setCategory(jsonObjectItem.getString("category"));
             itemDetails.setLowStockIndicator(jsonObjectItem.getBoolean("lowStockIndicator"));
             itemDetails.setToBeSoldOneItemPerOrder(jsonObjectItem.getBoolean("toBeSoldOneItemPerOrder"));

//           String volumePerLiter = jsonObjectItem.isNull("volumePerItem") ? null : jsonObjectItem.getString("volumePerItem");
//           itemDetails.setVolumePerItem(volumePerLiter);
             itemDetails.setVolumePerItem(jsonObjectItem.getString("volumePerItem"));
             itemDetails.setMonthlyQuotaPerUser(jsonObjectItem.getString("monthlyQuotaPerUser"));

//           String itemType = jsonObjectItem.isNull("itemType") ? null : jsonObjectItem.getString("itemType");
//           itemDetails.setItemType(itemType);

             itemDetails.setItemType(jsonObjectItem.getString("itemType"));

//           String yearlyQuota = jsonObjectItem.isNull("yearlyQuotaPerUser") ? null : jsonObjectItem.getString("yearlyQuotaPerUser");
//           itemDetails.setYearlyQuotaPerUser(yearlyQuota);
             itemDetails.setYearlyQuotaPerUser(jsonObjectItem.getString("yearlyQuotaPerUser"));

            String noOfItemsOrderedStr = jsonObjectItem.getString("noOfItemsOrderded");
            int noOfItemsOrdered = 0;
            if (noOfItemsOrderedStr != null && noOfItemsOrderedStr != "null") {
                noOfItemsOrdered = Integer.parseInt(noOfItemsOrderedStr);
            }
            itemDetails.setNoOfItemsOrderded(noOfItemsOrdered);

            String storeItem = jsonObjectItem.isNull("store") ? null : jsonObjectItem.getString("store");
            itemDetails.setStore(storeItem);

            inventoryData.add(itemDetails);

        }
        ordersListObj.setInventoryData(inventoryData);

        ///

        Log.v("Customer","ordersListObj"+ordersListObj);


//        }

//        customerOrderDetailsViewAdapter.notifyDataSetChanged();
        setValuesToUI(ordersListObj);

        setRecyclerView(ordersListObj);

    }

}
