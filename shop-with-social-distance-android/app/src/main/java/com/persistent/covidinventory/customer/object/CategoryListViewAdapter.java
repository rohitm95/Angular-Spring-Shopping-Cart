package com.persistent.covidinventory.customer.object;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.persistent.covidinventory.R;
import com.persistent.covidinventory.customer.activities.CategoryFilterActivity;
import com.persistent.covidinventory.customer.model.CartDetails;

import java.util.ArrayList;

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<String> categoryList;
    CartDetails cartDetails;


    private ArrayList<String> selectedCategoryList = new ArrayList<>();
    public ArrayList<String> getSelectedCategoryList() {
        return selectedCategoryList;
    }
    public void setSelectedCategoryList(ArrayList<String> selectedCategoryList) {
        this.selectedCategoryList = selectedCategoryList;
    }


    public CategoryListViewAdapter(Context context, ArrayList<String>categoryList, CartDetails cartDetails){
        this.context = context;
        this.categoryList = categoryList;
        this.cartDetails = cartDetails;
    }

    @NonNull
    @Override
    public CategoryListViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categoryName.setText(categoryList.get(position));
//        if(holder.categoryCheckbox.isChecked()){
//            selectedCategoryList.add(categoryList.get(position));
//            setSelectedCategoryList(selectedCategoryList);
//            Log.v("categoryAdapter","CategoryAdapterSelectedValues ArrayList "+selectedCategoryList);
//            Log.v("categoryAdapter","CategoryAdapterSelectedValues ArrayList "+getSelectedCategoryList());
//
//        }

        if(cartDetails.categoriesSelected.size()!=0){
            for(int i =0; i<cartDetails.categoriesSelected.size();i++){
                if(categoryList.get(position).equals(cartDetails.categoriesSelected.get(i))){
                    Log.v("selectedCategoryList","if(categoryList.get(position).equals(cartDetails.categoriesSelected.get(i))){"+ cartDetails.categoriesSelected.get(i));
                    holder.categoryCheckbox.setChecked(true);

                    break;
                }
            }
        }else{
            holder.cardView.setOnClickListener(v -> {
                holder.categoryCheckbox.setChecked(true);
                selectedCategoryList.add(categoryList.get(position));
                cartDetails.categoriesSelected.add(categoryList.get(position));
                setSelectedCategoryList(selectedCategoryList);
                Log.v("categoryAdapter","CategoryAdapterSelectedValues ArrayList "+selectedCategoryList);
                Log.v("categoryAdapter","CategoryAdapterSelectedValues ArrayList "+getSelectedCategoryList());
                Log.v("categoryAdapter"," cartDetails.categoriesSelected.add(categoryList.get(position)) ArrayList "+ cartDetails.categoriesSelected);

            });
        }

    }


    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView categoryName ;
        CheckBox categoryCheckbox ;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            categoryCheckbox = (CheckBox) itemView.findViewById(R.id.checkbox_category);
            cardView = (CardView)itemView.findViewById(R.id.category_name_cardview);
        }
    }
}
