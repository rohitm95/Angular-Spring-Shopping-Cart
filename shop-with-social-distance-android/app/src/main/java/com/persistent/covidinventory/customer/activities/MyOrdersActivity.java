package com.persistent.covidinventory.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.customer.model.CustomerOrders;
import com.persistent.covidinventory.customer.object.OrderListViewAdapter;
import com.persistent.covidinventory.database.Customer;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;


public class MyOrdersActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GoldenKatarRepository goldenKatarRepository;

    ArrayList<CustomerOrders>orderlist = new ArrayList<>();

    public String getCustomerOrder = Constants.endpoint + "/covid-inventory/api/orders/customer";

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    OrderListViewAdapter orderListViewAdapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        goldenKatarRepository = GoldenKatarRepository.getRepository(this);

        displayProgress("Loading...");



        fillData();
        setUIRecyclerView();
    }


    public void setUIRecyclerView(){

        recyclerView = (RecyclerView)findViewById(R.id.orders_recyclerview);
        linearLayoutManager = new LinearLayoutManager(MyOrdersActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        orderListViewAdapter = new OrderListViewAdapter(MyOrdersActivity.this,orderlist);
        recyclerView.setAdapter(orderListViewAdapter);



    }



    public void fillData() {
        compositeDisposable.add(goldenKatarRepository.getCustomer()
                .doOnSuccess(this::getCustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
//                .subscribe(error -> Toast.makeText(MyOrdersActivity.this, "Toast...", Toast.LENGTH_SHORT).show()));

//                .doOnSubscribe(disposable ->
//                   // Toast.makeText(SendConversationsActivity.this, "Toast...", Toast.LENGTH_SHORT).show();
//                    //ringProgressDialog.show();
//                })
//                .doOnTerminate(() -> {
//                    //ringProgressDialog.dismiss();
//                })
//                .subscribe(s -> {
//                    //Toast.makeText(SendConversationsActivity.this, s, Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }));

    }



    public void getCustomerId(Customer customer){
        int customerId = customer.id;
        Log.v("CustomerId API ","get customer order API is  ###  "+customerId);
        getCustomerOrder = getCustomerOrder+"/"+customerId;
        Log.v("CustomerId API ","get customer order API is  ###  "+getCustomerOrder);

        //Load API getCustomerOrder
        getCustomerOrdersAPI();
    }

    public void getCustomerOrdersAPI() {

        StringRequest sr = new StringRequest(Request.Method.GET, getCustomerOrder,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject orderObject = new JSONObject(response);
                            parseJSONResponse(orderObject);
                            Log.v("My Orders", "success! response: " + response);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("HttpClient", "Error in connection");
                        }
                        Log.v("My Orders", "success! response: " + response);
                    }
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

    public  void parseJSONResponse(JSONObject orderObject) throws JSONException {

        JSONArray result = orderObject.getJSONArray("result");

        for(int i =0; i<result.length(); i++){

            JSONObject jsonObject = result.getJSONObject(i);
            CustomerOrders customerOrderDetails = new CustomerOrders();

            customerOrderDetails.setOrderNo(jsonObject.getInt("orderNo"));

            customerOrderDetails.setDeliveryDate(jsonObject.getLong("deliveryDate"));

            customerOrderDetails.setSlotFrom(jsonObject.getString("slotFrom"));
            customerOrderDetails.setSlotTo(jsonObject.getString("slotTo"));
            customerOrderDetails.setAmountPayable(jsonObject.getDouble("amountPayable"));
            customerOrderDetails.setStatus(jsonObject.getString("status"));
            customerOrderDetails.setActive(jsonObject.getBoolean("active"));
            orderlist.add(customerOrderDetails);

        }
        orderListViewAdapter.notifyDataSetChanged();
    }

    private void displayProgress(String message) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
