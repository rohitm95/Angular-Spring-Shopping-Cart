package com.persistent.covidinventory.customer.object;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.model.TimeSlotData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

     TimeSlotData listData;
     Context context;
    JSONArray jsonArrayTime;
    private String selectedValue;
    private String selectedDate;

    public String getSelectedDate(){
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate){
        this.selectedDate = selectedDate;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue){
        this.selectedValue = selectedValue;
    }


    public TimeSlotAdapter( TimeSlotData listData , Context context ){

        this.listData = listData;
        this.context = context;

    }



    @NonNull
    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.timeslot_list_row,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotAdapter.ViewHolder holder, final int position) {

        final ArrayList<String> timeData = new ArrayList<String>();

        jsonArrayTime = listData.getTimeSlotTimes();
        Log.v("Timeslot data","TimeslotData"+ jsonArrayTime);

        for (int i = 0; i< jsonArrayTime.length();i++){
            try {
                String time = (String) jsonArrayTime.get(i);
                timeData.add(i,time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.v("Timeslot data","timeData"+ timeData);
        Log.v("Timeslot data","timeData"+ listData.getTimeSlotDate());



        holder.textViewTime.setText((CharSequence) timeData.get(position));


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //onTimeSlotClickListener.onTimeSlotClick((String) timeData.get(position));
                setSelectedValue(timeData.get(position));
                setSelectedDate(listData.getTimeSlotDate());
                Toast.makeText(context, "Timeslot selected !! " , Toast.LENGTH_SHORT).show();
            }

    });


    }

    @Override
    public int getItemCount() {
        return listData.getTimeSlotTimes().length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTime;
        Context context;
        LinearLayout parentLayout;

    public ViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();



        textViewTime = itemView.findViewById(R.id.timeData_tv);
        parentLayout = itemView.findViewById(R.id.parentLayout);
    }
}
}
