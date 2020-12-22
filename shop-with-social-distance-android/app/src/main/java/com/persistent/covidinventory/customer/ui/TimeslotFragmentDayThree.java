package com.persistent.covidinventory.customer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.activities.TimeslotActivity;
import com.persistent.covidinventory.customer.model.TimeSlotData;
import com.persistent.covidinventory.customer.object.TimeSlotAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;


public class TimeslotFragmentDayThree extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressDialog progressDialog;

    RecyclerView recyclerView;

    TimeSlotAdapter timeSlotAdapter;

    ArrayList<TimeSlotData> listData;

    public TimeslotFragmentDayThree() {
        // Required empty public constructor
    }


    public static TimeslotFragmentDayThree newInstance(String param1, String param2) {
        TimeslotFragmentDayThree fragment = new TimeslotFragmentDayThree();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeslot_day3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewTimeSlotDay3);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listData = new ArrayList<>();

        try {
            getTimeSlotData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getTimeSlotData() throws JSONException {
        displayProgress("Loading...");

        double amountPayable = 0.0;
        if (getActivity() != null && getActivity() instanceof TimeslotActivity) {
            amountPayable = ((TimeslotActivity) getActivity()).amount;
        }
        String getTimeSlot = String.format(Constants.endpoint + "/covid-inventory/api/availableSlots?userId=%1$s&amountPayable=%2$s",
                Constants.getInstance().userId,
                amountPayable);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, getTimeSlot, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            JSONObject getTimeSlot = new JSONObject(response);
                            parseJSONResponse(response);
                            Log.v("HttpClient", "success! response: " + response);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("HttpClient", "Error in connection");
                        }
                        Log.v("HttpClient", "success! response: " + response);
                        setupData(listData);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(sr);
    }


    public void parseJSONResponse(JSONObject getTimeSlot1) {

        try {
            JSONObject getTimeSlot = getTimeSlot1;
            JSONObject jObject = getTimeSlot.getJSONObject("result");
            Iterator<String> keys = jObject.keys();
            while (keys.hasNext()) {

                TimeSlotData timeSlotData = new TimeSlotData();
                String key = keys.next();
                timeSlotData.setTimeSlotDate(key);

                JSONArray jsonArray = jObject.getJSONArray(key);
                timeSlotData.setTimeSlotTimes(jsonArray);

                listData.add(timeSlotData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupData(ArrayList<TimeSlotData> list_data) {
        if (list_data != null && list_data.size() > 2) {
            timeSlotAdapter = new TimeSlotAdapter(list_data.get(2), getContext());
            recyclerView.setAdapter(timeSlotAdapter);
        }

    }


    public String getSelectedValue() {
        String returnData = null;
        if (timeSlotAdapter == null) {
            //null check
        } else {
            returnData = timeSlotAdapter.getSelectedValue();
        }

        return returnData;

    }

    public String getSelectedDate() {
        String returnDate = null;
        if (timeSlotAdapter == null) {
            //null check
        } else {
            returnDate = timeSlotAdapter.getSelectedDate();
        }
        return returnDate;
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
}
