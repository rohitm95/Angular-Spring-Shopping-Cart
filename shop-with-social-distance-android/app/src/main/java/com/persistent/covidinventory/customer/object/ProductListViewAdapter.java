package com.persistent.covidinventory.customer.object;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.customer.model.CustomFilter;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.database.CartItem;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductListViewAdapter extends RecyclerView.Adapter<ProductListViewAdapter.MyViewHolder> implements Filterable {


    Context context;
    public ArrayList<ItemDetails> list, filterList;
    CustomFilter filter;
    CartDetails cartDetails;
    private GoldenKatarRepository goldenKatarRepository;

    int productQuantity;

    public ProductListViewAdapter(Context context, ArrayList<ItemDetails> list1, CartDetails cartDetails, GoldenKatarRepository goldenKatarRepository) {
        this.context = context;
        this.filterList = list1;
        this.list = list1;
        this.cartDetails = cartDetails;
        this.goldenKatarRepository = goldenKatarRepository;
    }

    @NonNull
    @Override
    public ProductListViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


//        final ItemDetails itemDetails = cartDetails.getCartItems().get(position);
        final ItemDetails itemDetails = list.get(position);
//        cartDetails.setItemDetails(itemDetails);
        holder.product_name.setText(itemDetails.getItemName());

        holder.product_cost.setText(Double.toString(itemDetails.getPrice()));
        int productStock = itemDetails.getStock();
        if (productStock > 10) {
            holder.product_availability.setTextColor(Color.GREEN);
            holder.product_availability.setText("In Stock");
            holder.addToCart.setVisibility(View.VISIBLE);
        } else if (productStock == 0 || productStock < 0) {
            holder.product_availability.setTextColor(Color.RED);
            holder.product_availability.setText("Out of Stock ");
            holder.addToCart.setVisibility(View.INVISIBLE);
            holder.product_remaining_quantity.setVisibility(View.INVISIBLE);

        } else {
            holder.product_availability.setTextColor(Color.RED);
            holder.product_availability.setText("Only " + productStock + " Items remaining");
            holder.addToCart.setVisibility(View.VISIBLE);
        }

        boolean toBeSoldOneItemPerOrder = itemDetails.getToBeSoldOneItemPerOrder();
        if (toBeSoldOneItemPerOrder) {
            holder.product_remaining_quantity.setText("Only one per Order");
        } else {
            holder.product_remaining_quantity.setVisibility(View.INVISIBLE);
        }


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                productQuantity = Integer.parseInt((String) adapterView.getItemAtPosition(position));
                Toast.makeText(adapterView.getContext(), "Selected: " + productQuantity, Toast.LENGTH_SHORT).show();
                itemDetails.setNoOfItemsOrderded(productQuantity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Spinner Drop down elements
        List<String> quantity = new ArrayList<String>();
        if (itemDetails.getToBeSoldOneItemPerOrder()) {
//            quantity.add("0");
            quantity.add("1");

        }
        else if(productStock < 5)
        {
            for(int k=1; k <=productStock; k++)
            {
                quantity.add(String.valueOf(k));
            }
        }
        else {
//            quantity.add("0");
            quantity.add("1");
            quantity.add("2");
            quantity.add("3");
            quantity.add("4");
            quantity.add("5");

        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, quantity);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        holder.spinner.setAdapter(dataAdapter);


        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                if(cartDetails.getCartItems().contains(cartDetails.getItemDetails())){
//                    Toast.makeText(context,"Already had the same item in cart, Only quantity can be changed",Toast.LENGTH_SHORT).show();
//                }else {
                CartItem cartItem = new CartItem();
                cartItem.itemName = itemDetails.getItemName();
                cartItem.itemNumber = itemDetails.getItemNumber();
                cartItem.itemPrice = itemDetails.getPrice();
                cartItem.itemQuantity = productQuantity;
                cartItem.itemTotalAmount = cartItem.itemQuantity * cartItem.itemPrice;
                cartItem.itemCategory = itemDetails.getCategory();
                cartItem.itemGroup = itemDetails.getGroup();
                cartItem.itemType = itemDetails.getItemType();
                cartItem.itemLowStockIndicator = itemDetails.isLowStockIndicator();
                cartItem.itemToBeSoldPerOrder = itemDetails.getToBeSoldOneItemPerOrder();
                cartItem.itemVolumePerItem = itemDetails.getVolumePerItem();
                cartItem.itemMonthlyQuotaPerUser = itemDetails.getMonthlyQuotaPerUser();
                cartItem.itemYearlyQuotaPerUser = itemDetails.getYearlyQuotaPerUser();
                cartItem.itemId = String.valueOf(itemDetails.getId());
                cartItem.itemStock = itemDetails.getStock();
                goldenKatarRepository.insertCartItem(cartItem);

                if (itemDetails.getNoOfItemsOrderded() == 0) {
                    Toast.makeText(context, "Please Select Quantity", Toast.LENGTH_SHORT).show();
                    holder.addToCart.setClickable(false);
                } else {
                    holder.addToCart.setClickable(true);
                    if (cartDetails.getCartItems().contains(itemDetails)) {
                        holder.addToCart.setClickable(true);
                        cartDetails.getCartItems().remove(itemDetails);
                        cartDetails.getCartItems().add(itemDetails);
//                            cartDetails.setItemDetails(cartDetails.getItemDetails());
                        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                        Log.v("productQuantity", "productQuantity" + productQuantity);

                    } else {
                        holder.addToCart.setClickable(true);
                        cartDetails.getCartItems().add(itemDetails);
//                            cartDetails.setItemDetails(cartDetails.getItemDetails());
                        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                        Log.v("productQuantity", "productQuantity" + productQuantity);
                    }
//                    }
                }
                holder.addToCart.setClickable(true);
                Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                Log.v("productQuantity", "productQuantity" + productQuantity);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
//        return list.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView product_name, product_cost, product_availability, product_remaining_quantity;
        ImageView product_img; //, badge_image2;
        RelativeLayout dropdown_relativeLayout;
        Spinner spinner;
        Button addToCart;


        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_cost = (TextView) itemView.findViewById(R.id.product_price);
            product_availability = (TextView) itemView.findViewById(R.id.stock);
            product_remaining_quantity = (TextView) itemView.findViewById(R.id.remaining_items);
            product_img = (ImageView) itemView.findViewById(R.id.product_img);
            dropdown_relativeLayout = itemView.findViewById(R.id.relative_layout_dropdown);
            spinner = itemView.findViewById(R.id.spinner);
            addToCart = (Button) itemView.findViewById(R.id.addToCart);

//           spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    productQuantity = parent.getItemAtPosition(position).toString();
//
//                }
//            });

        }
    }
}
