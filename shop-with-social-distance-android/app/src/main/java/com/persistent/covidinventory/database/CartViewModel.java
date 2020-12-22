package com.persistent.covidinventory.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private GoldenKatarRepository goldenKatarRepository;

    private LiveData<List<CartItem>> cartItemList;

    public CartViewModel(@NonNull Application application) {
        super(application);
        goldenKatarRepository = new GoldenKatarRepository(application);
    }

    public LiveData<List<CartItem>> getAllItems() {
        cartItemList = goldenKatarRepository.getCartItemsLive();
        Log.e("All Items", "Items =" + cartItemList);

        return cartItemList;
    }
}
