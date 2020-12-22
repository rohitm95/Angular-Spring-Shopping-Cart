package com.persistent.covidinventory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Customer customer);

    @Query("DELETE FROM Customer")
    void deleteAllCustomers();

    @Query("SELECT * from Customer")
    Single<Customer> getCustomerDetails();
}
