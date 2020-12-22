package com.persistent.covidinventory.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.activities.NewDashboardActivity;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.customer.activities.LandingPageActivity;
import com.persistent.covidinventory.customer.model.Role;
import com.persistent.covidinventory.customer.model.UserDetails;
import com.persistent.covidinventory.database.Customer;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.persistent.covidinventory.common.Constants.REQUEST_TIMEOUT;

public class LoginActivity extends AppCompatActivity {
    private EditText mobileText;
    private EditText passwordText;
    private RequestQueue volley = null;
    private ProgressDialog progressDialog;
    String type,value;
    private GoldenKatarRepository goldenKatarRepository;
    CartDetails cartDetails;

    ArrayList<UserDetails> userList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goldenKatarRepository = GoldenKatarRepository.getRepository(this);

        cartDetails = new CartDetails();

        setContentView(R.layout.login);
        volley = Volley.newRequestQueue(this);
        mobileText = (EditText) findViewById(R.id.mobile_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        Button loginButton = (Button) findViewById(R.id.login);
        Button forgotButton = (Button) findViewById(R.id.forgot);
        Button updateButton = (Button) findViewById(R.id.update);
        loginButton.setEnabled(true);
        forgotButton.setEnabled(true);
        updateButton.setEnabled(true);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                displayDialog("Forgot Password", "Please contact to admin@covidinventoryshop.com.");
                showPopup(v);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UpdatePassword.class);
                startActivity(intent);
            }
        });

    }

    private void validate() {
        if (mobileText.getText().toString().equalsIgnoreCase("") || passwordText.getText().toString().equalsIgnoreCase("") || (mobileText.getText().toString().length() < 10)) {
            displayDialog("Invalid Data", "Please enter the 10 digit Mobile and Password");
        } else {
            login();
        }
    }

    private void login() {

        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("username", mobile);
        jsonParams.put("password", password);
        displayProgress("Logging");
        String url = Constants.endpoint + "/covid-inventory/authenticate";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Log.v("LoginActivityResponse","response $$$ "+response);
                            addToSharedPreferances(response);

                            String token = response.getString("token");
                            int roleId = response.getJSONObject("user").getJSONObject("role").getInt("id");
                            int userId = response.getJSONObject("user").getInt("id");
                            Constants.getInstance().userRoleId = roleId;
                            Constants.getInstance().userId = userId;
                            Constants.token = token;
                            Log.v("Token ","token :"+token);



                            //For Aggregator
                            Intent intent = new Intent(LoginActivity.this, NewDashboardActivity.class);
                            intent.putExtra("token", token);

                            //For Customer
                            if (roleId == Constants.ROLE_ID_CUSTOMER) {
                                JSONObject customer = response.getJSONObject("user");
                                parseJSON(customer);
                                intent = new Intent(LoginActivity.this, LandingPageActivity.class);
                                intent.putExtra("cart", cartDetails);
                            }
                            startActivity(intent);
                            finish();
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
                            displayDialog("Authentication Failed", "Please enter the valid mobile number and password.");

                        } else {
                            displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley.add(jsonObjectRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


    //    ////// for customer
    public void parseJSON(JSONObject customer) throws JSONException {

        UserDetails userDetails = new UserDetails();

        JSONObject jsonObject = customer;

        String dateOfJoin = jsonObject.isNull("dateOfJoin") ? null : jsonObject.getString("dateOfJoin");

        userDetails.setDateOfJoin(dateOfJoin);
        userDetails.setEmailId(jsonObject.getString("emailId"));
        userDetails.setFirstName(jsonObject.getString("firstName"));
        userDetails.setGender(jsonObject.getString("gender"));
        userDetails.setId(jsonObject.getInt("id"));
        userDetails.setActive(jsonObject.getBoolean("isActive"));
        userDetails.setLastName(jsonObject.getString("lastName"));
        userDetails.setMobileNumber(jsonObject.getString("mobileNumber"));
        userDetails.setNewUser(jsonObject.getBoolean("newUser"));
        userDetails.setPassword(jsonObject.getString("password"));
        userDetails.setUsername(jsonObject.getString("username"));

        Role role = new Role();
        JSONObject roleJson = jsonObject.getJSONObject("role");
        role.setId(roleJson.getInt("id"));
        role.setName(roleJson.getString("name"));
        userDetails.setRole(role);

        userList.add(userDetails);

        Log.v("UserList", "userList" + userList);
        addToList();
        addToDB();

    }

    public void addToList() {
        UserDetails userDetails = userList.get(0);
//        cartDetails.getCustomer().add(userDetails);
        cartDetails.setCustomer(userDetails);

    }

    public void addToDB(){

        Customer customer = new Customer();
        UserDetails userDetails =  cartDetails.getCustomer();
        ////DB
        customer.dateOfJoin = userDetails.getDateOfJoin();
        customer.emailId = userDetails.getEmailId();
        customer.firstName = userDetails.getFirstName();
        customer.gender = userDetails.getGender();
        customer.id = userDetails.getId();
//        Log.v("customerID Login","customerID Login"+userDetails.getId());
//        Log.v("customerID Login","customerID Login"+customer.id);
        customer.isActive = userDetails.isActive();
        customer.lastName = userDetails.getLastName();
        customer.mobileNumber = userDetails.getMobileNumber();
        customer.newUser = userDetails.isNewUser();
        customer.password = userDetails.getPassword();
        customer.username = userDetails.getUsername();
        Role role = userDetails.getRole();
        customer.roleId =role.getId();
        customer.roleName = role.getName();
        goldenKatarRepository.insertCustomer(customer);
    }

    public void addToSharedPreferances(JSONObject user) throws JSONException {
        JSONObject aggregator = user.getJSONObject("user");
        Log.v("Aggregator User","user = "+aggregator);
        String emailId = aggregator.getString("emailId");
        int id = aggregator.getInt("id");

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        // Creating an Editor object
        // to edit(write to the file)
        SharedPreferences.Editor myEdit  = sharedPreferences.edit();

        // Storing the key and its value
        // as the data fetched from edittext
        myEdit.putString(Constants.SHARED_PREFERENCE_FIELD_AGGREGATOR_EMAIL_ID,emailId);
        myEdit.putInt(Constants.SHARED_PREFERENCE_FIELD_AGGREGATOR_ID,id);
        //myEdit.putString(Constants.SHARED_PREFERENCE_FIELD_IS_NEW_USER, "1");


        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();


    }

    public void showPopup(View view){
        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.forgot_password_popup,null);

        TextInputEditText mobileNo = mView.findViewById(R.id.etext_mobile_no);
        TextInputEditText emailId = mView.findViewById(R.id.etext_email_id);
        Button submitButton = mView.findViewById(R.id.btn_submit);
        Button cancelButton = mView.findViewById(R.id.btn_cancel);
        RadioGroup radioGroup = mView.findViewById(R.id.radio_group);
        RadioButton radioButtonMobileNo = mView.findViewById(R.id.radio_mobile_no);
        RadioButton radioButtonEmailId = mView.findViewById(R.id.radio_email_id);
        LinearLayout emailLayout = mView.findViewById(R.id.email_id_linear_layout);
        LinearLayout mobileLayout = mView.findViewById(R.id.mobile_no_linear_layout);


        alert.setView(mView);

        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int selectedId) {
               if(selectedId == R.id.radio_email_id)
               {
                   mobileLayout.setVisibility(View.GONE);
                   emailLayout.setVisibility(View.VISIBLE);
               }
               else if(selectedId == R.id.radio_mobile_no)
               {
                   emailLayout.setVisibility(View.GONE);
                   mobileLayout.setVisibility(View.VISIBLE);

               }
           }
       });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.radio_mobile_no)
                {
                    value = mobileNo.getText().toString();
                    type = "mobile";
                }
                else if(selectedId == R.id.radio_email_id)
                {
                    value = emailId.getText().toString();
                    type = "email";
                }

                Log.v("Forgot Password", "Type: " + type);
                Log.v("Forgot Password", "Value: " + value);
                getForgotPasswordApi(type,value);

                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }

    public void getForgotPasswordApi(String type, String value)
    {
        final int[] statusCode = new int[1];
                displayProgress("Resetting password");
        String url = Constants.endpoint + "/covid-inventory/api/users/forgotpassword/" + type + "/" + value;

        Log.v("Forgot Password", "API URL: " + url);

        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.v("Forgot Password", "success! response: " + response);

                            displayDialog("","Your password has been reset successfully!! Please login with new password!! ");

                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("HttpClient", "Error in connection");
                        }
                        Log.v("Forgot Password", "success! response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        int statusCode = error.networkResponse.statusCode;
                        if(statusCode == 400) {
                            displayDialog("Authentication Failed", "Please enter the valid Mobile No.");
                        } else {
                            displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                        }
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
}