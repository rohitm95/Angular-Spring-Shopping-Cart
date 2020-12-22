package com.persistent.covidinventory.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"cart_item_number"},
        unique = true)})
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "cart_item_id")
    public long id = 0L;

    @ColumnInfo(name = "cart_item_number")
    public String itemNumber = "";

    @ColumnInfo(name = "cart_item_name")
    public String itemName = "";

    @ColumnInfo(name = "cart_item_quantity")
    public int itemQuantity = 0;

    @ColumnInfo(name = "cart_per_item_price")
    public double itemPrice = 0f;

    @ColumnInfo(name = "cart_item_total_amount")
    public double itemTotalAmount = 0f;

    @ColumnInfo(name = "item_category")       ////
    public String itemCategory = "";

    @ColumnInfo(name = "item_group")
    public String itemGroup = "";

    @ColumnInfo(name = "item_id")
    public String itemId = "";

    @ColumnInfo(name = "item_type")
    public String itemType = "";

    @ColumnInfo(name = "item_low_stock_indicator")
    public boolean itemLowStockIndicator = false;

    @ColumnInfo(name = "item_to_be_sold_per_order")
    public boolean itemToBeSoldPerOrder = false;

    @ColumnInfo(name = "item_volume_per_item")
    public String itemVolumePerItem = "";

    @ColumnInfo(name = "item_monthly_quota_per_user")
    public String itemMonthlyQuotaPerUser = "";

    @ColumnInfo(name = "item_yearly_quota_per_user")
    public String itemYearlyQuotaPerUser = "";

    @ColumnInfo(name =  "item_stock")
    public int itemStock = 0;

}
