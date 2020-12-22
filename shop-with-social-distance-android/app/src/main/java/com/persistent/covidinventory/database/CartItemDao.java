package com.persistent.covidinventory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CartItem cartItem);

    @Query("DELETE FROM CartItem")
    void deleteAllCartItems();

    @Query("DELETE FROM CartItem WHERE cart_item_number = :itemNumber")
    void deleteItem(String itemNumber);

    @Query("SELECT * from CartItem")
    Single<List<CartItem>> getCartItems();

    @Query("SELECT * from CartItem")
    LiveData<List<CartItem>> getCartItemsLiveData();
}