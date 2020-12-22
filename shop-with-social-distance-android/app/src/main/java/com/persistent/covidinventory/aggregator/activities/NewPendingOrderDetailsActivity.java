package com.persistent.covidinventory.aggregator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.model.OrdersList;
import com.persistent.covidinventory.aggregator.model.StoreDetails;
import com.persistent.covidinventory.aggregator.object.PendingOrderDetailsAdapter;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.customer.model.CustomerOrderDetails;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.customer.model.Role;
import com.persistent.covidinventory.customer.model.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;

public class NewPendingOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    OrdersList ordersListObj;
    JSONObject orderDetailsJSON;
    private ProgressDialog progressDialog;
    int orderNo ;
    public static final String COVID_INVENTORY_API_ORDERS_ORDER_DETAILS = "/covid-inventory/api/orders/orderDetails/";

    ArrayList<CustomerOrderDetails> orderDetailsArrayList;
    PendingOrderDetailsAdapter pendingOrderDetailsAdapter;
    RecyclerView recyclerView;

    ArrayList<ItemDetails> itemDetailsList;

    double totalAmount =0 ;
    private RequestQueue volley = null;

    TextView orderStatus, deliveryDate, timeSlot, amountPayable, textOrderNo, customerName, customerContact;
    Button doneBtn , resetBtn;
    LinearLayout commentBoxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pending_order_details);

        Intent intent = getIntent();
//        ordersListObj = (OrdersList) intent.getSerializableExtra("orderObj");
        orderNo = intent.getIntExtra("orderNo", 0);

        initializeUI();
        loadCustomerOrders(orderNo);
//        setRecyclerView();

    }

    private void initializeUI(){

        ordersListObj = new OrdersList();

        volley = Volley.newRequestQueue(this);
        orderDetailsArrayList = new ArrayList<>() ;
        itemDetailsList = new ArrayList<ItemDetails>();
        textOrderNo = (TextView)findViewById(R.id.aggregator_pending_order_no);
        orderStatus = (TextView)findViewById(R.id.aggregator_pending_status);
        deliveryDate = (TextView)findViewById(R.id.aggregator_pending_delivery_date);
        timeSlot = (TextView)findViewById(R.id.aggregator_pending_time_slot);
        amountPayable = (TextView) findViewById(R.id.aggregator_pending_amount_payable);
        customerName = findViewById(R.id.aggregator_pending_customer_name);
        customerContact = findViewById(R.id.aggregator_pending_customer_contact);
        doneBtn = (Button)findViewById(R.id.aggregator_pending_order_done_btn);
        resetBtn = (Button)findViewById(R.id.aggregator_pending_order_reset_btn);
        commentBoxLayout = (LinearLayout)findViewById(R.id.comment_box_layout);
        doneBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
    }

    public void setRecyclerView(OrdersList ordersListObj){
        recyclerView = (RecyclerView)findViewById(R.id.aggregator_pending__recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewPendingOrderDetailsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        pendingOrderDetailsAdapter = new PendingOrderDetailsAdapter(NewPendingOrderDetailsActivity.this, ordersListObj);
        recyclerView.setAdapter(pendingOrderDetailsAdapter);
    }

    public void setValuesToUI(OrdersList ordersListObj){
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy"); //yyyy-MM-dd
//            Date date = new Date(orderList.getDeliveryDate());
        java.sql.Date date = new Date(Long.parseLong(ordersListObj.getDeliveryDate()));

        totalAmount = ordersListObj.getAmountPayable();
        amountPayable.setText(String.format("%.2f", ordersListObj.getAmountPayable()));
        textOrderNo.setText(Integer.toString(orderNo));
        orderStatus.setText(ordersListObj.getStatus());
//        deliveryDate.setText((ordersListObj.getDeliveryDate()));
        deliveryDate.setText(sf.format(date));
        String timeslotCombine = ordersListObj.getSlotFrom()+"-"+ordersListObj.getSlotTo();
        timeSlot.setText(timeslotCombine);
        customerName.setText(ordersListObj.getUserDetails().getFirstName() + " " + ordersListObj.getUserDetails().getLastName());
        customerContact.setText(ordersListObj.getUserDetails().getMobileNumber());

        if(ordersListObj.getStatus().equals(Constants.ORDER_STATUS_COMPLETED) || ordersListObj.getStatus().equals(Constants.ORDER_STATUS_READY_TO_DELIVER)){
            doneBtn.setVisibility(View.INVISIBLE);
            resetBtn.setVisibility(View.INVISIBLE);
            commentBoxLayout.setVisibility(View.INVISIBLE);
        }else{
            doneBtn.setVisibility(View.VISIBLE);
            resetBtn.setVisibility(View.VISIBLE);
            commentBoxLayout.setVisibility(View.VISIBLE);
        }
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




//        }

//        pendingOrderDetailsAdapter.notifyDataSetChanged();
        setValuesToUI(ordersListObj);

        setRecyclerView(ordersListObj);

    }

    private JSONObject generateRoleJson() throws JSONException {
        JSONObject role = new JSONObject();
        role.put("id", Constants.getInstance().userRoleId);
        if (Constants.getInstance().userRoleId == 3) {
            role.put("name", Constants.ROLE_AGGREGATOR);
        } else if (Constants.getInstance().userRoleId == 2) {
            role.put("name", Constants.ROLE_AGGREGATOR);
        } else {
            role.put("name", Constants.ROLE_ADMIN);
        }
        return role;
    }


    private void generateJSONObj() {

        JSONObject jsonObjectMain = new JSONObject();

        JSONObject orderObj = new JSONObject();

        try {


            ///CustomerObj
            JSONObject jsonObjectCustomer = new JSONObject();
            jsonObjectCustomer.put("id", ordersListObj.getUserDetails().getId());
            jsonObjectCustomer.put("firstName", ordersListObj.getUserDetails().getFirstName());
            jsonObjectCustomer.put("lastName", ordersListObj.getUserDetails().getLastName());
            jsonObjectCustomer.put("emailId", ordersListObj.getUserDetails().getEmailId());
            jsonObjectCustomer.put("mobileNumber", ordersListObj.getUserDetails().getMobileNumber());
            jsonObjectCustomer.put("gender", ordersListObj.getUserDetails().getGender());
            jsonObjectCustomer.put("isActive", ordersListObj.getUserDetails().isActive());
            jsonObjectCustomer.put("dateOfJoin", ordersListObj.getUserDetails().getDateOfJoin());
            jsonObjectCustomer.put("username", ordersListObj.getUserDetails().getUsername());
            jsonObjectCustomer.put("password", ordersListObj.getUserDetails().getPassword());
            JSONObject jsonObjectRole = new JSONObject();
            jsonObjectRole.put("id", ordersListObj.getUserDetails().getRole().getId());
            jsonObjectRole.put("name",  ordersListObj.getUserDetails().getRole().getName());
            jsonObjectCustomer.put("role", jsonObjectRole);
            jsonObjectCustomer.put("newUser", ordersListObj.getUserDetails().isNewUser());


            ///StoreObj

            JSONObject jsonObjectStore = new JSONObject();
            jsonObjectStore.put("id", ordersListObj.getStoreDetails().getStoreId());
            jsonObjectStore.put("storeName",  ordersListObj.getStoreDetails().getStoreName());
            jsonObjectStore.put("slotDuration", ordersListObj.getStoreDetails().getSlotDuration());
            jsonObjectStore.put("deliveryInSlot", ordersListObj.getStoreDetails().getDeliveryInStlot());
            jsonObjectStore.put("active", ordersListObj.getStoreDetails().isActive());

            ///InventoryDataObj

            double totalAmountPayable =0;
            ArrayList<ItemDetails> inventoryData = ordersListObj.getInventoryData();

            JSONArray itemDetailsArray = new JSONArray();

            for(int i =0; i<inventoryData.size();i++){
                JSONObject item = new JSONObject();
                ItemDetails itemDetails = inventoryData.get(i);

                item.put("id", itemDetails.getId());
                item.put("itemNumber", itemDetails.getItemNumber());
                item.put("group", itemDetails.getGroup());
                item.put("itemName", itemDetails.getItemName());
                item.put("price", itemDetails.getPrice());
                item.put("stock", itemDetails.getStock());
                item.put("category", itemDetails.getCategory());
                item.put("lowStockIndicator", itemDetails.isLowStockIndicator());
                item.put("toBeSoldOneItemPerOrder", itemDetails.getToBeSoldOneItemPerOrder());
                item.put("volumePerItem", itemDetails.getVolumePerItem());
                item.put("monthlyQuotaPerUser", itemDetails.getMonthlyQuotaPerUser());
                item.put("itemType", itemDetails.getItemType());
                item.put("yearlyQuotaPerUser", itemDetails.getYearlyQuotaPerUser());
                item.put("noOfItemsOrderded", itemDetails.getNoOfItemsOrderded());
//                totalAmountPayable = itemDetails.getPrice()*itemDetails.getNoOfItemsOrderded();

                itemDetailsArray.put(item);


            }


            orderObj.put("order_no", ordersListObj.getOrderNo());
            orderObj.put("customer", jsonObjectCustomer);
            orderObj.put("status", ordersListObj.getStatus());
            orderObj.put("store", jsonObjectStore);
            orderObj.put("deliveryDate", ordersListObj.getDeliveryDate());
            orderObj.put("slotFrom", ordersListObj.getSlotFrom());
            orderObj.put("slotTo", ordersListObj.getSlotTo());
            orderObj.put("amountPayable",ordersListObj.getAmountPayable());
            orderObj.put("isOrderChanged", true);
            orderObj.put("inventoryDatas", itemDetailsArray);

            JSONObject role = generateRoleJson();

            jsonObjectMain.put("orderDto", orderObj);
            jsonObjectMain.put("roleDto", role);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v("Generated JSON", "OrderStatus API JSON "+jsonObjectMain);
        updateOrderStatus(jsonObjectMain);

    }

    private void updateOrderStatus(JSONObject jsonObject){

        Log.v("Request Body ","json object ="+jsonObject);
        displayProgress("Updating Order Status");
        String url = Constants.endpoint + "/covid-inventory/api/availableSlots/orderStatus";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Log.v("onResponse", "Success!! "+response);
                            if(response.getInt("status")!=200){
                                displayDialog("Info", response.getString("statusText"));
                            }else{
                                Log.v("onResponse", "Success"+response);
                                JSONObject result = response.getJSONObject("result");
                                JSONObject orderDto = result.getJSONObject("orderDto");
                                displayDialog("Info", "Order status updated successfully!!");

//                                if (orderDto.get("order_no").equals(selectedOrder.getOrderNo())) {
//                                    onBackPressed();
//                                } else {
//                                    displayDialog("Network Error", "Can not connect to server, please try after sometime.");
//                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            displayDialog("Authentication Failed", "Please try after sometime.");
                        } else {
                            displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + Constants.token);
                return headers;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley.add(request);


    }


    public void displayDialog(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(NewPendingOrderDetailsActivity.this,NewDashboardActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = builder1.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void displayProgress(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==doneBtn.getId()){
            generateJSONObj();
        }
        else if (v.getId() == resetBtn.getId())
        {
            Intent intent = new Intent(NewPendingOrderDetailsActivity.this,NewDashboardActivity.class);
            startActivity(intent);
        }

    }
}
