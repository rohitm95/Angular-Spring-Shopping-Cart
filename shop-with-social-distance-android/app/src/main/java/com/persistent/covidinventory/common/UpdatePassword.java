package com.persistent.covidinventory.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.database.Customer;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UpdatePassword extends AppCompatActivity {
    private EditText passwordText;
    private EditText newPasswordText;
    private Button updateButton;
    private RequestQueue volley = null;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GoldenKatarRepository goldenKatarRepository;
    private int userID;
    private  int customerId;
    private String updatePasswordURL = Constants.endpoint+"/covid-inventory/api/users/password/change";
    private JSONObject requestBodyObj = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);
        goldenKatarRepository = GoldenKatarRepository.getRepository(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        userID = Integer.parseInt(id);
//        userID = (Integer)intent.getIntExtra("userID",0);
        Log.v("intent userID", "userId"+userID);


        volley = Volley.newRequestQueue(this);
        passwordText = (EditText) findViewById(R.id.password_text);
        newPasswordText = (EditText) findViewById(R.id.new_password_text);
        updateButton = (Button) findViewById(R.id.update);
        updateButton.setEnabled(true);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void validate() throws JSONException {
        if (passwordText.getText().toString().equalsIgnoreCase("") || newPasswordText.getText().toString().equalsIgnoreCase("")) {
            displayDialog("Invalid Data","Please enter the existing and new Password");
        } else {
            //updatePassword();
            getUserId();
        }
    }


//    public void fillData() {
//        compositeDisposable.add(goldenKatarRepository.getCustomer()
//                .doOnSuccess(this::getCustomerId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(items -> {
////                    goldenKatarRepository.getCustomer()
////                            .doOnSuccess(this::fillCustomer)
////                            .subscribeOn(Schedulers.io())
////                            .observeOn(Schedulers.computation())
////
////                            .subscribe();
//                }));
//    }



    public void getUserId() throws JSONException {

        Log.v("CustomerId API ","get customer order API is  ###  "+customerId);
        updatePasswordURL = updatePasswordURL+"/"+userID;
        Log.v("CustomerId API ","get customer order API is  ###  "+updatePasswordURL);

        //Create Json request body
        createJSONRequestObj(userID);

    }

    public void createJSONRequestObj(int customerId) throws JSONException {

        requestBodyObj.put("id",customerId);
        requestBodyObj.put("password",newPasswordText.getText().toString());
        requestBodyObj.put("oldPassword",passwordText.getText().toString());

        updatePassword();

    }


    private void updatePassword() {

//        String password = passwordText.getText().toString();
//        String newpassword = newPasswordText.getText().toString();
//        Map<String, String> jsonParams = new HashMap<String, String>();
//        jsonParams.put("password", password);
        Log.v("Request body object","req body :"+requestBodyObj);
        displayProgress("Updating password..");
        String url = Constants.getInstance().endpoint+"/covid-inventory/authenticate";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, updatePasswordURL, requestBodyObj,
                response -> {
                    try {
                        Log.v("Response","response:"+response);
                        progressDialog.dismiss();
//                        Intent intent = new Intent(UpdatePassword.this, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        killActivity();

                        if(response.getString("statusText").equals("Invalid password, please provide the valid passwords")){
                            displayInfoPopup("Authentication Failed","Invalid password, please provide the valid password!");
                        }else {
                            displayDialog("Authentication Successful", "Your password updated successfully!");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse.statusCode == 401) {
                            displayDialog("Authentication Failed", "Please enter the valid password.");
                        } else {
                            displayDialog("Network Error", "Can not connect to server, please try after sometime.");
                        }
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                Log.v("Token","token :"+Constants.token);
                headers.put("Authorization", "Bearer " + Constants.token);

                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                5,
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

    public void clearText(){
        passwordText.setText("");
        newPasswordText.setText("");
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
                        Intent intent = new Intent(UpdatePassword.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        killActivity();
                    }
                });

        alertDialog = builder1.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void displayInfoPopup(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearText();
                        dialog.cancel();

                    }
                });

        alertDialog = builder1.create();
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

    private void killActivity() {
        this.finishAffinity();
    }
}