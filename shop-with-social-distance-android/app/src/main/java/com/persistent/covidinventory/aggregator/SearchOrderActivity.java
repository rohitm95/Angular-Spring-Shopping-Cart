package com.persistent.covidinventory.aggregator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.persistent.covidinventory.aggregator.activities.NewPendingOrderDetailsActivity;
import com.persistent.covidinventory.aggregator.object.PendingOrderDetailsAdapter;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.common.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.EXTRA_FLOW;
import static com.persistent.covidinventory.common.Constants.FLOW_SEARCH;
import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;

public class SearchOrderActivity extends AppCompatActivity {

    public static final String COVID_INVENTORY_API_SEARCH = "/covid-inventory/api/search/";
    private RequestQueue volley = null;
    private ProgressDialog progressDialog;
    private ArrayList<OrderListModel> orderItemLst = new ArrayList<>();
    private String token;
    private ListView searchOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_order);

        volley = Volley.newRequestQueue(this);
        token = getIntent().getExtras().getString("token");

        searchOrderList = (ListView) findViewById(R.id.list);

        findViewById(R.id.search_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText orderNoView = findViewById(R.id.order_no);
                String orderNo = orderNoView.getText().toString();
                if (orderNo.isEmpty()) {
                    orderNoView.setError(getString(R.string.order_no_cannot_be_empty));
                    return;
                }

                searchOrderItem(orderNo);
                Utils.hideSoftKeyboard(SearchOrderActivity.this);
            }
        });

/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setDateFromCalendar();
        }

        setTimeSlot();*/
        onBackButtonPressed();
    }

/*    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDateFromCalendar() {
        findViewById(R.id.calendar_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(SearchOrderActivity.this);
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = getFormattedDate(i, (i1 + 1), i2);
                        ((TextView) findViewById(R.id.delivery_date)).setText(date);
                    }
                });
                dialog.show();
            }
        });
    }

    private void setTimeSlot() {
        findViewById(R.id.time_slot_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                int hour = instance.get(Calendar.HOUR_OF_DAY);
                int minute = instance.get(Calendar.MINUTE);

                final TimePickerDialog dialog = new TimePickerDialog(
                        SearchOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getFormattedTime(selectedHour, selectedMinute);
                        ((TextView) findViewById(R.id.time_slot)).setText(time);
                    }
                }, hour, minute, false);
                dialog.show();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String getFormattedDate(int year, int month, int day) {
        return String.format("%02d/%02d/%d", day, month, year);
    }

    @SuppressLint("DefaultLocale")
    private String getFormattedTime(int hour, int minute) {
        return String.format("%02d:%02d %s", hour, minute, hour < 12 ? "AM" : "PM");
    }
*/

    private void onBackButtonPressed() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void searchOrderItem(String orderNo) {
        displayProgress("Searching order");
        String params = String.format("null/%s/null/null", orderNo);
        String url = Constants.endpoint + COVID_INVENTORY_API_SEARCH + params;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Log.e("onResponse: ", "Success ---------------->");
                            if (response.getString("status").equalsIgnoreCase("200")) {
                                Type reviewType = new TypeToken<List<OrderListModel>>() {
                                }.getType();
                                Log.e("onResponse: ", response.toString());
                                orderItemLst = new Gson().fromJson(response.getJSONArray("result").toString(), reviewType);
                                displaySearchedOrder(orderItemLst);
                            } else {
                                displayDialog("Network Error", "Can not connect to server, please try after sometime.");
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
                            ((TextView) findViewById(R.id.search_title)).setText(getString(R.string.orders_not_found));
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

    @SuppressLint("DefaultLocale")
    private void displaySearchedOrder(ArrayList<OrderListModel> list) {
        ((TextView) findViewById(R.id.search_title))
                .setText(String.format("%d Orders found", list.size()));

        if (list.size() > 0) {
            searchOrderList.setAdapter(new Adapter(list, this));
        }
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

    private class Adapter extends BaseAdapter {

        private ArrayList<OrderListModel> arrayList;
        private Context context;

        public Adapter(ArrayList<OrderListModel> list, Context context) {
            arrayList = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"DefaultLocale", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            ViewHolder holder;
            if (v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.search_order_list_item, null, false);
                holder = new ViewHolder(v);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            final OrderListModel model = arrayList.get(i);
            holder.mobile.setText(String.format("Customer Mobile: %s", model.getCustomer().getMobileNumber()));
            holder.name.setText(String.format("Customer Name: %s", model.getCustomer().getFirstName()));
            holder.orderNo.setText(String.format("Order no: %d", model.orderNo));
            holder.status.setText(String.format("Order Status: %s", model.status));
            holder.status.setTextColor(getResources().getColor(R.color.theme_color_orange));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewPendingOrderDetailsActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra(EXTRA_FLOW, FLOW_SEARCH);
                    Constants.getInstance().selectedOrderForDetail = model;
                    startActivity(intent);
                }
            });

            return v;
        }

        class ViewHolder {
            TextView name, mobile, orderNo, status;
            View view;

            ViewHolder(View view) {
                this.view = view;
                orderNo = view.findViewById(R.id.orderid);
                name = view.findViewById(R.id.customername);
                mobile = view.findViewById(R.id.customermobile);
                status = view.findViewById(R.id.status);
            }
        }
    }
}
