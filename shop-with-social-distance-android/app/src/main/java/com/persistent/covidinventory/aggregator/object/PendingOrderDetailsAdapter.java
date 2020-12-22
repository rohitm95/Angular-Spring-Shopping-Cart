package com.persistent.covidinventory.aggregator.object;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.activities.NewPendingOrderDetailsActivity;
import com.persistent.covidinventory.aggregator.model.OrdersList;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.customer.model.CustomerOrderDetails;

import java.util.ArrayList;

public class PendingOrderDetailsAdapter extends RecyclerView.Adapter<PendingOrderDetailsAdapter.MyViewHolder> {
    Context context;
    public ArrayList<CustomerOrderDetails> orderList;
    public OrdersList ordersListObj;
    public int maximumQuantityLimit;
    private int stock;
    private boolean lowStockIndicator;
    private boolean toBeSoldOneItemPerOrder;

    public PendingOrderDetailsAdapter(Context context, OrdersList ordersListObj){
        this.context = context;
        this.ordersListObj = ordersListObj;

    }




    @NonNull
    @Override
    public PendingOrderDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.itemName.setText(ordersListObj.getInventoryData().get(position).getItemName());
        holder.itemQuantity.setText(Integer.toString(ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded()));
        holder.itemPrice.setText(Double.toString(ordersListObj.getInventoryData().get(position).getPrice()));

        stock = ordersListObj.getInventoryData().get(position).getStock();
        lowStockIndicator = ordersListObj.getInventoryData().get(position).isLowStockIndicator();
        toBeSoldOneItemPerOrder = ordersListObj.getInventoryData().get(position).getToBeSoldOneItemPerOrder();

        if (toBeSoldOneItemPerOrder)
        {
            maximumQuantityLimit = 1;
        }
        else if (lowStockIndicator)
        {
            maximumQuantityLimit = stock;
        }
        else {
            maximumQuantityLimit = 5;
        }


//        CustomerOrderDetails customerOrderDetails = orderList.get(position);
//        holder.itemName.setText(customerOrderDetails.getItemName());
//        holder.itemQuantity.setText(Integer.toString(customerOrderDetails.getItemQuantity()));
//        holder.itemPrice.setText(Double.toString(customerOrderDetails.getItemPrice()));

        if(ordersListObj.getStatus().equals(Constants.ORDER_STATUS_COMPLETED) || ordersListObj.getStatus().equals(Constants.ORDER_STATUS_READY_TO_DELIVER)){
//            holder.quantityIncrease.setVisibility(View.INVISIBLE);
//            holder.quantityIncrease.setVisibility(View.INVISIBLE);
            holder.productQuantityLayout.setVisibility(View.INVISIBLE);
        }else {
            holder.productQuantityLayout.setVisibility(View.VISIBLE);
            holder.quantityIncrease.setVisibility(View.VISIBLE);
            holder.quantityIncrease.setVisibility(View.VISIBLE);
            int quentityBeforeUpdate =ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded();

            Log.v("Before quantity upate","Quantity = "+quentityBeforeUpdate);
            Log.v("Before quantity upate","Amount = "+ordersListObj.getAmountPayable());
            holder.quantityDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded() > 1) {
                        ordersListObj.getInventoryData().get(position).setNoOfItemsOrderded(ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded() - 1);
//                        double priceCalculate = 0;
                        double price = ordersListObj.getInventoryData().get(position).getPrice();//*quentityBeforeUpdate;

                        double priceCalculate = ordersListObj.getAmountPayable() - price;

//                         priceCalculate = (ordersListObj.getInventoryData().get(position).getPrice() * ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded()) -  priceCalculate;
                        ordersListObj.setAmountPayable(priceCalculate);

                        Log.v("After quantity decrease","Quantity = "+ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded());
                        Log.v("After quantity decrease","Amount = "+ordersListObj.getAmountPayable());

                        ((NewPendingOrderDetailsActivity) context).setValuesToUI(ordersListObj);

                        notifyDataSetChanged();
                        ;
                    }

                }
            });

            holder.quantityIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded() < maximumQuantityLimit) {
                        ordersListObj.getInventoryData().get(position).setNoOfItemsOrderded(ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded() + 1);
//                        double priceCalculate = 0;
                        double price = ordersListObj.getInventoryData().get(position).getPrice();//*quentityBeforeUpdate;

                        double priceCalculate = ordersListObj.getAmountPayable() + price;
//                        priceCalculate = priceCalculate + (ordersListObj.getInventoryData().get(position).getPrice() * ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded());
                        ordersListObj.setAmountPayable(priceCalculate);

                        Log.v("After quantity increase","Quantity = "+ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded());
                        Log.v("After quantity increase","Amount = "+ordersListObj.getAmountPayable());

                        ((NewPendingOrderDetailsActivity) context).setValuesToUI(ordersListObj);
                        notifyDataSetChanged();
                    }
                }
            });

        }

        holder.deliveredQuantity.setText(Integer.toString(ordersListObj.getInventoryData().get(position).getNoOfItemsOrderded()));
    }

    @Override
    public int getItemCount() {
        return  ordersListObj.getInventoryData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView itemName, itemQuantity, itemPrice, quantityIncrease, quantityDecrease, deliveredQuantity;
        LinearLayout productQuantityLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            itemName = (TextView) itemView.findViewById(R.id.aggregator_pending_order_item_name);
            itemQuantity = (TextView) itemView.findViewById(R.id.aggregator_pending_order_quantity);
            itemPrice = (TextView) itemView.findViewById(R.id.aggregator_pending_order_price);
            productQuantityLayout = (LinearLayout)itemView.findViewById(R.id.product_quantity_layout);
            quantityDecrease = itemView.findViewById(R.id.minus);
            quantityIncrease = itemView.findViewById(R.id.plus);
            deliveredQuantity = itemView.findViewById(R.id.delivered_quantity);
        }
    }
}
