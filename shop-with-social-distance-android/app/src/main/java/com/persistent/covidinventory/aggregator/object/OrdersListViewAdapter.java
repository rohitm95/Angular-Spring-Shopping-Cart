package com.persistent.covidinventory.aggregator.object;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.activities.NewPendingOrderDetailsActivity;
import com.persistent.covidinventory.aggregator.model.OrdersList;
import com.persistent.covidinventory.customer.activities.CustomerOrderDisplayActivity;
import com.persistent.covidinventory.customer.object.TimeSlotAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrdersListViewAdapter extends RecyclerView.Adapter<OrdersListViewAdapter.ViewHolder>
{

    Context context;
    ArrayList<OrdersList> ordersList;

    public OrdersListViewAdapter(Context context,  ArrayList<OrdersList> ordersList)
    {
        this.context = context;
        this.ordersList = ordersList;
    }

    public OrdersListViewAdapter() {

    }

    @NonNull
    @Override
    public OrdersListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.placed_order_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrdersList orderList = ordersList.get(position);

        if(!orderList.getDeliveryDate().equals("null")){
            holder.orderId.setText(Integer.toString(orderList.getOrderNo()));

            Log.v("timestampDate","orders"+orderList.getDeliveryDate());
            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(Long.parseLong(orderList.getDeliveryDate()));
            Log.v("timestampDate","orders sf.date"+sf.format(date));
            holder.deliveryDate.setText(sf.format(date));

            String timeslot = orderList.getSlotFrom() + "-" + orderList.getSlotTo();
            holder.timeslot.setText(timeslot);
            holder.parentLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, NewPendingOrderDetailsActivity.class);
                intent.putExtra("orderNo",orderList.getOrderNo());
//                    intent.putExtra("orderObj",ordersList.get(position));
                context.startActivity(intent);

            }) ;
        }else{
            holder.orderId.setText(Integer.toString(orderList.getOrderNo()));

            Log.v("timestampDate","orders"+orderList.getDeliveryDate());
//            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
//            Date date = new Date(Long.parseLong(orderList.getDeliveryDate()));
//            Log.v("timestampDate","orders sf.date"+sf.format(date));
            holder.deliveryDate.setText("Not available");

            String timeslot = orderList.getSlotFrom() + "-" + orderList.getSlotTo();
            holder.timeslot.setText(timeslot);
            holder.parentLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, NewPendingOrderDetailsActivity.class);
                intent.putExtra("orderNo",orderList.getOrderNo());
//                    intent.putExtra("orderObj",ordersList.get(position));
                context.startActivity(intent);

            }) ;
        }



    }



    @Override
    public int getItemCount() {
        return ordersList == null ? 0 : ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, deliveryDate, timeslot;
        LinearLayout parentLayout;

        public ViewHolder(View view) {
            super(view);

            orderId = (TextView) view.findViewById(R.id.orderid);
            deliveryDate = (TextView) view.findViewById(R.id.deliveryDate);
            timeslot = (TextView) view.findViewById(R.id.timeSlot);
            parentLayout = itemView.findViewById(R.id.product_image_layout);
        }
    }
}
