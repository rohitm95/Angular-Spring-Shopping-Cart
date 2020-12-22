package com.persistent.covidinventory.customer.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.TimeSlotData;
import com.persistent.covidinventory.customer.ui.SectionsPagerAdapter;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.database.CartItem;
import com.persistent.covidinventory.database.Customer;
import com.persistent.covidinventory.database.GoldenKatarRepository;
import com.persistent.covidinventory.customer.ui.TimeslotFragmentDayOne;
import com.persistent.covidinventory.customer.ui.TimeslotFragmentDayTwo;
import com.persistent.covidinventory.customer.ui.TimeslotFragmentDayThree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;

public class TimeslotActivity extends AppCompatActivity implements View.OnClickListener {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private JSONObject obj = new JSONObject();

    TimeslotFragmentDayOne timeslotFragmentDay1;
    TimeslotFragmentDayTwo timeslotFragmentDay2;
    TimeslotFragmentDayThree timeslotFragmentDay3;

    FloatingActionButton submit;

    CartDetails cartDetails;
    private RequestQueue volley = null;

    ArrayList<TimeSlotData> listData;
    SectionsPagerAdapter sectionsPagerAdapter;
    private AlertDialog alertDialog;
    ArrayList<ItemDetails> itemDetails = new ArrayList<>();

    public double amount = 0;
    String date1, date2, date3;
    String value1, value2, value3;
    String finalDate, finalTimeslot;
    String today, dayAfterTomorrow, tomorrow;

    private GoldenKatarRepository goldenKatarRepository;
    ProgressDialog progressDialog;
    ProgressBar progressBar;

    public String placeOrder = Constants.endpoint + "/covid-inventory/api/availableSlots/bookOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot);
        progressBar = findViewById(R.id.progressBar);
        goldenKatarRepository = GoldenKatarRepository.getRepository(this);

        try {
            setDates();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        volley = Volley.newRequestQueue(this);

        VolleyLog.DEBUG = true;
        Intent intent = getIntent();
        cartDetails = (CartDetails) intent.getSerializableExtra("FinalOrder");
        amount = intent.getDoubleExtra("Amount", 0);

        listData = new ArrayList<>();


        timeslotFragmentDay1 = new TimeslotFragmentDayOne();
        timeslotFragmentDay2 = new TimeslotFragmentDayTwo();
        timeslotFragmentDay3 = new TimeslotFragmentDayThree();

        sectionsPagerAdapter.addFragment(timeslotFragmentDay1, today);
        sectionsPagerAdapter.addFragment(timeslotFragmentDay2, tomorrow);
        sectionsPagerAdapter.addFragment(timeslotFragmentDay3, dayAfterTomorrow);

        Log.v("List Data", "List Data" + listData);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


    }


    public void bookOrder() {
        Log.v("JsonParam", "" + obj.toString());

//        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, placeOrder, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
//                            progressBar.setVisibility(View.INVISIBLE);
                            Log.v("API", "API");

                            displayDialog("Order Placed", "Happy purchasing ");
                            Log.v("REsponse", "Response" + response);

                        } catch (Exception e) {
                            e.printStackTrace();
                            displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            displayDialog("Server not respomding", "Please try again after some time.");

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley.add(jsonObjectRequest);
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


    public void displayDialog(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent popupChangeIntent = new Intent(TimeslotActivity.this, LandingPageActivity.class);

                        cartDetails.setCartItems(itemDetails);
                        popupChangeIntent.putExtra("cart", cartDetails);

                        goldenKatarRepository.deleteAllCartItems();

                        startActivity(popupChangeIntent);
                        dialog.cancel();
                    }
                });

        alertDialog = builder1.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void setDates() throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today1 = inputFormat.parse(Constants.day1);
        Date tomorrow1 = inputFormat.parse(Constants.day2);
        Date dayAfterTomorrow1 = inputFormat.parse(Constants.day3);
        Log.v("@@@today1 =", String.valueOf(today1));
        SimpleDateFormat outputFormat = new SimpleDateFormat("EE, yyyy-MM-dd ", Locale.ENGLISH);
        today = outputFormat.format(today1);
        tomorrow = outputFormat.format(tomorrow1);
        dayAfterTomorrow = outputFormat.format(dayAfterTomorrow1);
        Log.v("@@@today =", today);

    }

    public void fillData() {
        compositeDisposable.add(goldenKatarRepository.getCartItems()
                .doOnSuccess(this::fillCartItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    goldenKatarRepository.getCustomer()
                            .doOnSuccess(this::fillCustomer)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.computation())

                            .subscribe();
                }));
    }

    public void fillCartItems(List<CartItem> cartItems) throws JSONException {

        JSONArray itemDetailsArray = new JSONArray();
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            JSONObject item = new JSONObject();
            item.put("category", cartItem.itemCategory);
            item.put("group", cartItem.itemGroup);
            item.put("id", cartItem.itemId);
            item.put("itemName", cartItem.itemName);
            item.put("itemNumber", cartItem.itemNumber);
            item.put("itemType", cartItem.itemType);
            item.put("lowStockIndicator", cartItem.itemLowStockIndicator);
            item.put("monthlyQuotaPerUser", cartItem.itemMonthlyQuotaPerUser);
            item.put("noOfItemsOrderded", cartItem.itemQuantity);
            item.put("price", cartItem.itemPrice);
            item.put("stock", cartItem.itemStock);
            item.put("toBeSoldOneItemPerOrder", cartItem.itemToBeSoldPerOrder);
            item.put("volumePerItem", cartItem.itemVolumePerItem == null ? JSONObject.NULL : cartItem.itemVolumePerItem);
            item.put("yearlyQuotaPerUser", cartItem.itemYearlyQuotaPerUser);
            itemDetailsArray.put(item);
        }

        obj.put("inventoryDatas", itemDetailsArray);
    }

    public void fillCustomer(Customer customer) throws JSONException {
        JSONObject user = new JSONObject();
        user.put("dateOfJoin", customer.dateOfJoin == null ? JSONObject.NULL : customer.dateOfJoin);
        user.put("emailId", customer.emailId);
        user.put("firstName", customer.firstName);
        user.put("gender", customer.gender);
        user.put("id", customer.id);
        user.put("isActive", customer.isActive);
        user.put("lastName", customer.lastName);
        user.put("mobileNumber", customer.mobileNumber);
        user.put("newUser", customer.newUser);
        user.put("password", customer.password);
        user.put("username", customer.username);

        JSONObject role = new JSONObject();
        role.put("id", customer.roleId);
        role.put("name", customer.roleName);
        user.put("role", role);

        obj.put("customer", user);

        try {
            fillOtherParams();
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void fillOtherParams() throws ParseException, JSONException {
        String str_date = finalDate;

        DateFormat formatter;

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(str_date);

        Log.v("Delivery date ", "delivery date " + date.getTime());

        String[] timearray = finalTimeslot.split("-");
        String firstTimeSlot = timearray[0];
        String secondTimeSlot = timearray[1];


        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");

        String slotFrom1 = date24Format.format(date12Format.parse(firstTimeSlot));
        String slotTo2 = date24Format.format(date12Format.parse(secondTimeSlot));

        obj.put("amountPayable", amount);
        obj.put("deliveryDate", date.getTime());
        obj.put("slotFrom", slotFrom1);
        obj.put("slotTo", slotTo2);
        obj.put("status", "INITIATED");

        bookOrder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==submit.getId()){
            Snackbar.make(v, "Please Wait we sending your order", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            value1 = timeslotFragmentDay1.getSelectedValue();
            date1 = timeslotFragmentDay1.getSelectedDate();

            value2 = timeslotFragmentDay2.getSelectedValue();
            date2 = timeslotFragmentDay2.getSelectedDate();

            value3 = timeslotFragmentDay3.getSelectedValue();
            date3 = timeslotFragmentDay3.getSelectedDate();

            if (value1 != null && date1 != null) {
                finalDate = date1;
                finalTimeslot = value1;
            } else if (value2 != null && date2 != null) {
                finalDate = date2;
                finalTimeslot = value2;
            } else if (value3 != null && date3 != null) {
                finalDate = date3;
                finalTimeslot = value3;
            }

            if (finalDate != null && finalTimeslot != null) {
                       displayProgress("Please wait, we are placing your order...");

                fillData();

            } else {
                Toast.makeText(getApplicationContext(), "Please Select Time slot first  ", Toast.LENGTH_SHORT).show();
            }
        }
        }

}