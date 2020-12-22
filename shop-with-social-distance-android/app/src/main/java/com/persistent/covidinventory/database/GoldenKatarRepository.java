package com.persistent.covidinventory.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;

public class GoldenKatarRepository {
    private static volatile GoldenKatarRepository INSTANCE;
    private CartItemDao cartItemDao;
    private CustomerDao customerDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    GoldenKatarRepository(Context context) {
        GoldenKatarDatabase db = GoldenKatarDatabase.getDatabase(context);
        cartItemDao = db.getCartItemDao();
        customerDao = db.getCustomerDao();
    }

    public static GoldenKatarRepository getRepository(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoldenKatarRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GoldenKatarRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertCartItem(final CartItem cartItem) {
        GoldenKatarDatabase.databaseWriteExecutor.execute(() -> {
            long id = cartItemDao.insert(cartItem);
            Log.e("Item inserted", "Id = " + id);
        });
    }


    public Single<List<CartItem>> getCartItems() {
        return cartItemDao.getCartItems();
    }

    public LiveData<List<CartItem>> getCartItemsLive() {
        return cartItemDao.getCartItemsLiveData();
    }

    public void deleteCartItem(final String itemNumber) {
        GoldenKatarDatabase.databaseWriteExecutor.execute(() -> {
            cartItemDao.deleteItem(itemNumber);
            Log.e("Item deleted", "Deleted itemNumber = " + itemNumber);
        });

    }

    public void deleteAllCartItems() {
        GoldenKatarDatabase.databaseWriteExecutor.execute(() -> {
            cartItemDao.deleteAllCartItems();
            Log.e("Items deleted", "successfully ");
        });

    }

//----------Customer Entity-------------------------

    public void insertCustomer(final Customer customer) {
        GoldenKatarDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long id = customerDao.insert(customer);
                Log.e("customerDetail inserted", "Id = " + id);
            }
        });
    }

    public Single<Customer> getCustomer() {
        return customerDao.getCustomerDetails();
    }

}
