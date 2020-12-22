package com.persistent.covidinventory.customer.object;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.model.OrdersList;
import com.persistent.covidinventory.customer.activities.CustomerOrderDisplayActivity;
import com.persistent.covidinventory.customer.model.CustomerOrderDetails;
import com.persistent.covidinventory.customer.model.CustomerOrders;

import java.util.ArrayList;

public class CustomerOrderDetailsViewAdapter extends RecyclerView.Adapter<CustomerOrderDetailsViewAdapter.MyViewHolder>{


    Context context;
    public ArrayList<CustomerOrderDetails> orderList;
    OrdersList ordersListObj;

    public CustomerOrderDetailsViewAdapter(Context context , OrdersList ordersListObj ){ //ArrayList<CustomerOrderDetails> list
        this.context = context;
//        this.orderList = list;
        this.ordersListObj = ordersListObj;
    }


    @NonNull
    @Override
    public CustomerOrderDetailsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_details_list_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        CustomerOrderDetails customerOrderDetails = orderList.get(position);
//        holder.itemName.setText(customerOrderDetails.getItemName());
//        holder.itemQuantity.setText(Integer.toString(customerOrderDetails.getItemQuantity()));
//        holder.itemPrice.setText(Double.toString(customerOrderDetails.getItemPrice()));

//        ((CustomerOrderDisplayActivity)context).findViewById(R.id.txt_order_no);

//        customerOrderDetails.getOrderTotalAmount();

        holder.itemName.setText(ordersListObj.getInventoryData().get(position).getItemName());
        holder.itemQuantity.setText(Integer.toString(ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded()));
        holder.itemPrice.setText(Double.toString(ordersListObj.getInventoryData().get(position).getPrice()));
//        ((CustomerOrderDisplayActivity)context).findViewById(R.id.txt_order_no);
//        ordersListObj.getAmountPayable();
    }

    @Override
    public int getItemCount() {
//        return orderList == null ? 0 : orderList.size();
        return ordersListObj.getInventoryData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView itemName, itemQuantity, itemPrice;
        LinearLayout parentLayout;



        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemName = (TextView) itemView.findViewById(R.id.customer_order_item_name);
            itemQuantity = (TextView) itemView.findViewById(R.id.customer_order_quantity);
            itemPrice = (TextView) itemView.findViewById(R.id.customer_order_price);
            parentLayout = (LinearLayout)itemView.findViewById(R.id.customer_order_list_row_parent_layout);

        }
    }
}
