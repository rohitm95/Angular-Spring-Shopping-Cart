package com.persistent.covidinventory.customer.object;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.activities.CustomerOrderDisplayActivity;

import com.persistent.covidinventory.customer.model.CustomerOrders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderListViewAdapter extends RecyclerView.Adapter<OrderListViewAdapter.MyViewHolder>  {


    Context context;
    public ArrayList<CustomerOrders> orderList;



    public OrderListViewAdapter(Context context , ArrayList<CustomerOrders> list ){
        this.context = context;
        this.orderList = list;

    }

    @NonNull
    @Override
    public OrderListViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CustomerOrders customerOrderDetails = orderList.get(position);
        holder.orderNo.setText(Integer.toString(customerOrderDetails.getOrderNo()));

        String dateStr = Long.toString(customerOrderDetails.getDeliveryDate()) ;
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(Long.parseLong(dateStr));

//        holder.orderDate.setText(Long.toString(customerOrderDetails.getDeliveryDate()));
        holder.orderDate.setText(sf.format(date));
        holder.orderStatus.setText(customerOrderDetails.getStatus());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerOrderDisplayActivity.class);
                intent.putExtra("orderNo",customerOrderDetails.getOrderNo());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView orderNo, orderDate, orderStatus;
        LinearLayout parentLayout;



        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            orderNo = (TextView) itemView.findViewById(R.id.customer_order_number);
            orderDate = (TextView) itemView.findViewById(R.id.customer_order_date);
            orderStatus = (TextView) itemView.findViewById(R.id.customer_order_status);
            parentLayout = (LinearLayout)itemView.findViewById(R.id.customer_order_list_row_parent_layout);

        }
    }
}
