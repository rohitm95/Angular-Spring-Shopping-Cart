package com.persistent.covidinventory.customer.object;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.CartDetails;
import com.persistent.covidinventory.customer.activities.CartDisplayActivity;
import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.database.CartItem;
import com.persistent.covidinventory.database.GoldenKatarRepository;

import java.util.ArrayList;
import java.util.List;

public class CartListViewAdapter  extends RecyclerView.Adapter<CartListViewAdapter.MyViewHolder> {


    Context context;
    ArrayList<ItemDetails> list , filterList;
    CartDetails cartDetails;

    List<CartItem> cartItemList ;

    private GoldenKatarRepository goldenKatarRepository;



    public CartListViewAdapter(Context context ,CartDetails cartDetails ,GoldenKatarRepository goldenKatarRepository){
        this.context = context;
        this.filterList = list;
        this.cartDetails = cartDetails;
        this.goldenKatarRepository = goldenKatarRepository;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
       MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.product_name.setText(cartItemList.get(position).itemName);
        holder.product_cost.setText(Double.toString(cartItemList.get(position).itemPrice));
        holder.quantity.setText(Integer.toString(cartItemList.get(position).itemQuantity));
        holder.amount.setText(Double.toString(cartItemList.get(position).itemTotalAmount));
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goldenKatarRepository.deleteCartItem(cartItemList.get(position).itemNumber);

                ((CartDisplayActivity)context).totalAmount = 0;
                
                ((CartDisplayActivity)context).totalAmountPayable(cartItemList);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItemList == null ? 0 : cartItemList.size();

//        return cartDetails.getCartItems().size();
    }



    public void setProductList(List<CartItem> cartItems) {
        cartItemList = cartItems;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView product_name ,product_cost, quantity ,amount ;

        Button deleteItem;


        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            product_name = (TextView) itemView.findViewById(R.id.product_name_cart);
            product_cost = (TextView)itemView.findViewById(R.id.product_price_cart);
            quantity = (TextView)itemView.findViewById(R.id.quantity_cart);
            amount = (TextView)itemView.findViewById(R.id.amount);
            deleteItem = (Button) itemView.findViewById(R.id.deleteItem_btn);

        }
    }
}
