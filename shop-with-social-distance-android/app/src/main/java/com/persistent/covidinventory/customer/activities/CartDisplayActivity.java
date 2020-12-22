package com.persistent.covidinventory.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.persistent.covidinventory.customer.object.CartListViewAdapter;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.database.CartItem;
import com.persistent.covidinventory.database.CartViewModel;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;

public class CartDisplayActivity extends AppCompatActivity {

    CartDetails cartDetails;
    TextView totalAmountPayable, noOfItems;
    Button checkout;
    public double totalAmount = 0;
    int quantity = 0;

    private GoldenKatarRepository goldenKatarRepository;

    CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_display);
        goldenKatarRepository = GoldenKatarRepository.getRepository(this);

        getTimeSlotData();
        initializeUI();


        Intent intent = getIntent();
        cartDetails = (CartDetails) intent.getSerializableExtra("cart");
//        Log.v("CartDetail", "CartDetail" + cartDetails.getCustomer());



        setUIRecyclerView();

    }

    public void initializeUI(){

        totalAmountPayable = (TextView) findViewById(R.id.total_amount_in_rupee);
        noOfItems = (TextView) findViewById(R.id.noOfItems);
        checkout = findViewById(R.id.checkout_btn);

    }


    public void setUIRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartDisplayActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        final CartListViewAdapter cartListViewAdapter = new CartListViewAdapter(CartDisplayActivity.this, cartDetails, goldenKatarRepository);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        cartViewModel.getAllItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(final List<CartItem> cartItems) {
                cartListViewAdapter.setProductList(cartItems);
//                Log.v("CartItems ","CartItems = "+cartItems.get(0).itemName);
                totalAmount = 0;
                quantity = 0;
                totalAmountPayable(cartItems);

                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cartItems.size() != 0) {
                            Intent i = new Intent(CartDisplayActivity.this, TimeslotActivity.class);
                            i.putExtra("FinalOrder", cartDetails);
                            i.putExtra("AmountPayable", totalAmount);
                            i.putExtra("Amount", totalAmount);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Cart is empty please add some items to cart first  ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        recyclerView.setAdapter(cartListViewAdapter);


        //Adding Divider in recycler view
//        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(decoration);


    }

    public void totalAmountPayable(List<CartItem> cartItems) {

        for (int i = 0; i < cartItems.size(); i++) {

            quantity = cartItems.get(i).itemQuantity;
            totalAmount = cartItems.get(i).itemPrice * quantity + totalAmount;
        }


        String finalTotalAmount = String.format("%.2f", totalAmount);
        ;
        totalAmountPayable.setText(finalTotalAmount);
        noOfItems.setText(String.valueOf(cartItems.size() + " "));

        Log.v("Total Amount", "Total Amount" + totalAmount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CartDisplayActivity.this, LandingPageActivity.class);


        intent.putExtra("cart", cartDetails);

        startActivity(intent);


    }

    public void getTimeSlotData() {
//        displayProgress("Loading...");

//        String getTimeSlot = Constants.endpoint + "/covid-inventory/api/availableSlots ";
        String getTimeSlot = String.format(Constants.endpoint + "/covid-inventory/api/availableSlots?userId=%1$s&amountPayable=%2$s",
                Constants.getInstance().userId,
                totalAmount);

        StringRequest sr = new StringRequest(Request.Method.GET, getTimeSlot,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject getTimeSlot = new JSONObject(response);
//                            Constants.timeslotObject =getTimeSlot;
//                            parseJSONResponse(getTimeSlot);
                            Constants.dates = new ArrayList<>();
                            JSONObject jObject = getTimeSlot.getJSONObject("result");
                            Iterator<String> keys = jObject.keys();
                            while (keys.hasNext()) {

                                String key = keys.next();
                                Constants.dates.add(key);
                            }
                            Constants.day1 = Constants.dates.get(0);
                            Constants.day2 = Constants.dates.get(1);
                            Constants.day3 = Constants.dates.get(2);

                            Log.v("Constants.dates ", "Constants.dates" + Constants.dates);
                            Log.v("Constants.day1 ", "Constants.dates" + Constants.day1);


                            Log.v("HttpClient", "success! response: " + response);
//                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("HttpClient", "Error in connection");
                        }
                        Log.v("Cart Display", "success! response: " + response);
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
}
