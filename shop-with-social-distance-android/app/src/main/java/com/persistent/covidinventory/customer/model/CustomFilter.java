package com.persistent.covidinventory.customer.model;

import android.widget.Filter;

import com.persistent.covidinventory.customer.object.ProductListViewAdapter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    ProductListViewAdapter productListViewAdapter;
    ArrayList<ItemDetails> filterList;

    public CustomFilter(ArrayList<ItemDetails> filterList, ProductListViewAdapter productListViewAdapter)
    {
        this.productListViewAdapter=productListViewAdapter;
        this.filterList=filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<ItemDetails> filteredItems=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getItemName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredItems.add(filterList.get(i));
                }
            }
            results.count=filteredItems.size();
            results.values=filteredItems;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }


    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        productListViewAdapter.list= (ArrayList<ItemDetails>) results.values;
        //REFRESH
        productListViewAdapter.notifyDataSetChanged();

    }
}
