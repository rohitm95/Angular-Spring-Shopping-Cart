package com.persistent.covidinventory.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CartItem.class , Customer.class}, version = 1, exportSchema = false)
public abstract class GoldenKatarDatabase extends RoomDatabase {

    public abstract CartItemDao getCartItemDao();

    public abstract CustomerDao getCustomerDao();

    private static volatile GoldenKatarDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static GoldenKatarDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoldenKatarDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GoldenKatarDatabase.class, "golden_katar_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}