package com.persistent.covidinventory.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"mobile_number"},
        unique = true)})

public class Customer {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "customer_id")
    public int customerId = 0;


    @ColumnInfo(name = "date_of_join")
    public String dateOfJoin = "";

    @ColumnInfo(name = "email_id")
    public String emailId = "";

    @ColumnInfo(name = "first_name")
    public String firstName = "";

    @ColumnInfo(name = "gender")
    public String gender = "";

    @ColumnInfo(name = "id")
    public int id = 0;

    @ColumnInfo(name = "is_active")
    public Boolean isActive = false;

    @ColumnInfo(name = "last_name")
    public String lastName = "";

    @ColumnInfo(name = "mobile_number")
    public String mobileNumber = "";

    @ColumnInfo(name = "new_user")
    public Boolean newUser = false;

    @ColumnInfo(name = "password")
    public String password = "";

    @ColumnInfo(name = "username")
    public String username = "";

    @ColumnInfo(name = "role_id")
    public int roleId = 0;

    @ColumnInfo(name = "role_name")
    public String roleName = "";



}
