package com.persistent.covidinventory.aggregator.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.OrderListModel;
import com.persistent.covidinventory.aggregator.SearchOrderActivity;
import com.persistent.covidinventory.aggregator.model.OrdersList;
import com.persistent.covidinventory.aggregator.object.OrdersListViewAdapter;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;


public class NewPendingOrdersFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OrdersListViewAdapter ordersListViewAdapter;
    ArrayList<OrdersList> orderList = new ArrayList<>();
    private TextView countView;
    private RecyclerView recyclerView;
    private LinearLayout listHeaderView;
    private RelativeLayout searchView;
    private RequestQueue volley = null;

    ProgressDialog progressDialog;
    private String token = "";

    public NewPendingOrdersFragment(String token) {
      this.token = token;
    }

    public NewPendingOrdersFragment(){

    }

    public static NewPendingOrdersFragment newInstance(int index) {
        NewPendingOrdersFragment fragment = new NewPendingOrdersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

//        getOrders();
//        setRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_pending_orders, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        countView = view.findViewById(R.id.section_label);
        recyclerView = view.findViewById(R.id.pending_orders_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listHeaderView = view.findViewById(R.id.list_header_view);
        searchView = view.findViewById(R.id.search_view);
        volley = Volley.newRequestQueue(this.getContext());

        final EditText searchOrderNo = view.findViewById(R.id.search_order_no);
        view.findViewById(R.id.search_view_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Utils.hideSoftKeyboard(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (searchOrderNo == null || searchOrderNo.getText() == null) return;

                String orderNo = searchOrderNo.getText().toString();
                if (orderNo.isEmpty()) {
                    searchOrderNo.setError(getString(R.string.order_no_cannot_be_empty));
                    return;
                }

                ArrayList<OrdersList> list = new ArrayList<>();
                for (OrdersList model : orderList) {
                    if (model.getOrderNo() == Integer.parseInt(searchOrderNo.getText().toString())) {
                        list.add(model);
                        break;
                    }
                }

                if (list.size() > 0) {
                    searchOrderNo.setText("");

                    orderList.clear();
                    orderList.addAll(list);
                    ordersListViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Order not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.advance_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchOrderActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }

    private void updateViews(int visible) {
        listHeaderView.setVisibility(visible);
        searchView.setVisibility(visible);
    }


    public void setRecyclerView(){

        ordersListViewAdapter = new OrdersListViewAdapter(getContext(),orderList);
        recyclerView.setAdapter(ordersListViewAdapter);
        updateViews(View.VISIBLE);
    }


    private void getOrders() {
        Log.e("getOrders", NewPendingOrdersFragment.class.getSimpleName());
        displayProgress("Loading Orders");
//        String url = Constants.endpoint + "/covid-inventory/api/orders?status=INITIATED";
        String url = Constants.endpoint + String.format("/covid-inventory/api/orders?status=%s&storeId=%s","INITIATED", 1);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.v("response pending","response pending"+response);
                        progressDialog.dismiss();
                        if (response.getString("status").equalsIgnoreCase("200")) {
                            Type reviewType = new TypeToken<List<OrderListModel>>() {
                            }.getType();

                            orderList.clear();
                            parseJSON(response);
                            countView.setText(String.format(getString(R.string.orders_found), orderList.size()));
                            setRecyclerView();

                        } else {
                            displayDialog("", "Orders not found!!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        updateViews(View.GONE);
                        countView.setText(R.string.orders_not_found);


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
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley.add(jsonObjectRequest);
    }

    public void parseJSON(JSONObject customer) throws JSONException {

        JSONObject jsonObject1 = customer;

        JSONArray jsonArray = jsonObject1.getJSONArray("result");

        for (int i =0;i < jsonArray.length();i++){

            OrdersList ordersList = new OrdersList();
            JSONObject jsonObjectOrder = jsonArray.getJSONObject(i);
            ordersList.setOrderNo(jsonObjectOrder.getInt("order_no"));
            if(jsonObjectOrder.getString("deliveryDate")==null){

            }
            ordersList.setDeliveryDate(jsonObjectOrder.getString("deliveryDate"));
            ordersList.setSlotFrom(jsonObjectOrder.getString("slotFrom"));
            ordersList.setSlotTo(jsonObjectOrder.getString("slotTo"));
            ordersList.setAmountPayable(jsonObjectOrder.getDouble( "amountPayable"));   //************************* need to uncomment
            ordersList.setStatus(jsonObjectOrder.getString( "status"));

            orderList.add(ordersList);
//            globalOrderList.add(ordersList);

        }


        Log.v("ordersList", "ordersList" + orderList);

    }



    public void displayDialog(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.getContext());
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder1.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void displayProgress(String message) {
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
        countView.setText("");
    }

}
