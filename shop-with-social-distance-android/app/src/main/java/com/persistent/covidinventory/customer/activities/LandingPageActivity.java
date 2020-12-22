package com.persistent.covidinventory.customer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.common.UpdatePassword;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.common.LoginActivity;
import com.persistent.covidinventory.customer.object.ProductListViewAdapter;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.database.CartItem;
import com.persistent.covidinventory.database.CartViewModel;
import com.persistent.covidinventory.database.Customer;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ProductListViewAdapter productListViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    TextView textCartItemCount;
    CartDetails cartDetails;

    ProgressDialog progressDialog;

    Button filterButton;

    int mCartItemCount = 10;


    ArrayList<ItemDetails> productList = new ArrayList<>();
    ArrayList<ItemDetails> categoryList = new ArrayList<>();


    //Pagination related variables
    boolean isLoading =false;
    int TOTAL_ITEM_COUNT=0;
    int PAGE_NUMBER = 0;
    int PAGE_SIZE = 10 ;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GoldenKatarRepository goldenKatarRepository;
    int customerId;

    String getItemAPI =  Constants.endpoint + "/covid-inventory/api/items/category?page="+PAGE_NUMBER+"&size="+PAGE_SIZE;

//    String getItemAPI = Constants.endpoint + "/covid-inventory/api/items";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        goldenKatarRepository = GoldenKatarRepository.getRepository(this);

        Intent intent = getIntent();
        cartDetails = (CartDetails) intent.getSerializableExtra("cart");
        Log.v("CartDetail", "CartDetail" + cartDetails.getCustomer());
        Log.v("intentcart =", "cartDetails.getCartItems().size()" + cartDetails.getCartItems().size());
        Log.v("selectedCategories =", "selectedCategories" + cartDetails.categoriesSelected);


        filterButton = findViewById(R.id.filter_button);

        filterButton.setOnClickListener(this);
        fillData();

        try {
            setNavigationView();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        changeAPIURL(PAGE_NUMBER, PAGE_SIZE);
        loadAPIgetItem(PAGE_NUMBER,PAGE_SIZE);
        setUIRecyclerView(productList,cartDetails);
//
//        if (cartDetails.getCategoryList().size() == 0) {
//            Log.v("getCartItemsLanding1 =", "cartDetails.getCartItems().size()" + cartDetails.getCartItems().size());
//            loadAPIgetItem(PAGE_NUMBER,PAGE_SIZE);
//            setUIRecyclerView(productList,cartDetails);
//        } else {
//            categoryList = cartDetails.getCategoryList();
//            Log.v("getCartItems()Landing =", "cartDetails.getCartItems().size()" + categoryList.size());
//            setUIRecyclerView(categoryList, cartDetails);
//
//        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        loadAPIgetItem();
//        drawerLayout.closeDrawer(0);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void initialization() {
        //productList = new ArrayList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);

        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                productListViewAdapter.getFilter().filter(query);
                return false;
            }
        });


        final MenuItem menuItem = menu.findItem(R.id.shoppingbag);

        FrameLayout actionView = (FrameLayout)menu.findItem(R.id.shoppingbag).getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_count_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shoppingbag:

                Intent i = new Intent(getApplicationContext(), CartDisplayActivity.class);
                Log.v("Cart Details", "Cart Details" + cartDetails);
                i.putExtra("cart", cartDetails);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeAPIURL(int pageNumber , int pageSize){
        if(cartDetails.categoriesSelected.size()!= 0){
            getItemAPI = Constants.endpoint + "/covid-inventory/api/items/category?page="+pageNumber+"&size="+pageSize;
            String APIURL = getItemAPI+"&categories=";
            for(int i= 0;i<cartDetails.categoriesSelected.size();i++){
                getItemAPI = APIURL+cartDetails.categoriesSelected.get(i)+",";
            }

            if (getItemAPI.endsWith(",")) {
                getItemAPI = getItemAPI.substring(0, getItemAPI.length()-1);
            }

            Log.v("API categories","API category "+getItemAPI);
        }
        else {
            Log.v("API categories","API category "+getItemAPI);
            getItemAPI = Constants.endpoint + "/covid-inventory/api/items/category?page="+pageNumber+"&size="+pageSize;
        }
    }


    public void setNavigationView() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        String buildVersionString = packageInfo.versionName;

        //<************************Navigation Drawer set up****************************************>//
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting profile icon and username below that icon
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        //View navView =  navigationView.inflateHeaderView(R.layout);
        //TextView username = navView.findViewById(R.id.username_on_navigation);
        //username.setText("User Name");

        TextView buildVersion = findViewById(R.id.build_version);
        buildVersion.setText("Build Version " + buildVersionString);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerclose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }


    public void setUIRecyclerView(ArrayList<ItemDetails> productList, CartDetails cartDetails) {

        //Recycler view set up
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(LandingPageActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Adding the CustomAdapter Class
        GoldenKatarRepository goldenKatarRepository = GoldenKatarRepository.getRepository(this);
        productListViewAdapter = new ProductListViewAdapter(LandingPageActivity.this, productList, cartDetails, goldenKatarRepository);
        recyclerView.setAdapter(productListViewAdapter);

        //Adding Divider in recycler view
//        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(decoration);


//        <<----------------------To set AddToCart Count on AddToCart Icon on toolbar ---------------->>
        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getAllItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemList) {

                Log.v("Product list Adapter :", " total cart items :" + cartItemList.size());

                if (textCartItemCount != null) {
                    textCartItemCount.setText("" + cartItemList.size());
                } else {
                    /// Do Nothing
                }
            }
        });

        pagination();
    }


    public void pagination()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = linearLayoutManager.getChildCount();
                int totalItem = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading ){    ////  if (!isLoading && TOTAL_ITEM_COUNT >= productList.size())

                    if ((visibleItem + firstVisibleItemPosition) >= totalItem &&
                            firstVisibleItemPosition >= 0 && totalItem >= PAGE_SIZE){

                        PAGE_NUMBER++;
                        getNextPage(PAGE_NUMBER,PAGE_SIZE);
                        isLoading = true;

                    }
                }
//                else {
//                    Log.v("LandingPage :", "end of list, productlist size"+productList.size());
//                }
                Log.v("LandingPage :", "product list size  :"+productList.size() );
                Log.v("LandingPage :", "total items  :"+totalItem );
                Log.v("Page Number :","Page number ="+PAGE_NUMBER);
            }
        });
    }


    public void loadAPIgetItem(int pageNumber,int pageSize) {


//    http://localhost:8080/covid-inventory/api/items/category?categories=Electronics&page=0&size=2
        // getItemAPI = Constants.endpoint + "/covid-inventory/api/items/category?page="+pageNumber+"&size="+pageSize;

        displayProgress("Loading products...");
        isLoading = true;
        StringRequest sr = new StringRequest(Request.Method.GET, getItemAPI,
                response -> {
                    try {
                        isLoading = false;
                        JSONObject getItem = new JSONObject(response);

                        TOTAL_ITEM_COUNT = getItem.getInt("totalElements");
                        Log.v("LoadAPI","Total Item Count"+TOTAL_ITEM_COUNT);

                        parseJSONResponse(getItem);
                        Log.v("HttpClient", "success! response: " + response);
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HttpClient", "Error in connection");
                    }
                    Log.v("HttpClient", "success! response: " + response);
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
                30000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void  getNextPage(int pageNumber,int pageSize){



        isLoading = true;
        StringRequest sr = new StringRequest(Request.Method.GET, getItemAPI,
                response -> {
                    try {

                        isLoading = false;
                        JSONObject getItem = new JSONObject(response);


                        Log.v("getNextPage","Total Item Count"+getItem.getInt("totalElements"));
                        parseJSONResponse(getItem);
                        productListViewAdapter.notifyDataSetChanged();

//                          Toast.makeText(LandingPageActivity.this, "No more products in the store !!", Toast.LENGTH_SHORT).show();

                        Log.v("HttpClient", "success! response: " + response);
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HttpClient", "Error in connection");
                    }
                    Log.v("HttpClient", "success! response: " + response);
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
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);

    }

    public void parseJSONResponse(JSONObject getItems) {

        try {
            JSONObject getList = getItems;
            JSONArray jsonArray = getList.getJSONArray("result");
            Log.v("Item Array", "Item Array" + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ItemDetails itemDetails = new ItemDetails();

                itemDetails.setId(jsonObject.getInt("id"));
                itemDetails.setItemNumber(jsonObject.getString("itemNumber"));
                itemDetails.setGroup(jsonObject.getString("group"));
                itemDetails.setItemName(jsonObject.getString("itemName"));
                itemDetails.setPrice(jsonObject.getDouble("price"));
                itemDetails.setStock(jsonObject.getInt("stock"));
                itemDetails.setCategory(jsonObject.getString("category"));
                itemDetails.setLowStockIndicator(jsonObject.getBoolean("lowStockIndicator"));
                itemDetails.setToBeSoldOneItemPerOrder(jsonObject.getBoolean("toBeSoldOneItemPerOrder"));

                String volumePerLiter = jsonObject.isNull("volumePerItem") ? null : jsonObject.getString("volumePerItem");

                itemDetails.setVolumePerItem(volumePerLiter);
                itemDetails.setMonthlyQuotaPerUser(jsonObject.getString("monthlyQuotaPerUser"));
                itemDetails.setItemType(jsonObject.getString("itemType"));
                itemDetails.setYearlyQuotaPerUser(jsonObject.getString("yearlyQuotaPerUser"));

                String noOfItemsOrderedStr = jsonObject.getString("noOfItemsOrderded");
                int noOfItemsOrdered = 0;
                if (noOfItemsOrderedStr != null && noOfItemsOrderedStr != "null") {
                    noOfItemsOrdered = Integer.parseInt(noOfItemsOrderedStr);
                }
                itemDetails.setNoOfItemsOrderded(noOfItemsOrdered);
                productList.add(itemDetails);
//                cartDetails.setCartItems(productList);
//                setUIRecyclerView(productList, cartDetails);
            }
            productListViewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void killActivity() {
        this.finishAffinity();
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

        if (v.getId() == filterButton.getId()) {

            Intent intent1 = new Intent(LandingPageActivity.this, CategoryFilterActivity.class);
            intent1.putExtra("cart", cartDetails);
            startActivity(intent1);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.myCart :
                Intent i = new Intent(getApplicationContext(), CartDisplayActivity.class);
                i.putExtra("cart",cartDetails);
                startActivity(i);
                break;

            case R.id.myOrders :
                Intent intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
                startActivity(intent);

                break;

            case R.id.logout :
                Log.v("sign out ","sign out is clicked");
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logout);
                killActivity();
                break;

            case R.id.update_password :
                Log.v("userID Landing page","user id "+customerId);
                String id = Integer.toString(customerId);
                Intent updatePasswordIntent = new Intent(LandingPageActivity.this, UpdatePassword.class);
//                updatePasswordIntent.putExtra("userId",customerId);
                updatePasswordIntent.putExtra("id",id);
                startActivity(updatePasswordIntent);
                break;

        }
        return true;
    }



    public void fillData() {
        compositeDisposable.add(goldenKatarRepository.getCustomer()
                .doOnSuccess(this::getCustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
//                    goldenKatarRepository.getCustomer()
//                            .doOnSuccess(this::fillCustomer)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(Schedulers.computation())
//
//                            .subscribe();
                }));
    }



    public void getCustomerId(Customer customer) {
        customerId = customer.id;
    }



}
