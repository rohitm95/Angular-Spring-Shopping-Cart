package com.persistent.covidinventory.common;

import com.persistent.covidinventory.aggregator.OrderListModel;
import com.persistent.covidinventory.aggregator.model.OrdersList;

import java.util.ArrayList;

public class Constants {

    public static final String ORDER_STATUS_LAPSED = "LAPSED";
    public static final String ORDER_STATUS_READY_TO_DELIVER = "READY_TO_DELIVER";
    public static final String ORDER_STATUS_INITIATED = "INITIATED";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";

    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_CUSTOMER = "Customer";
    public static final String ROLE_AGGREGATOR = "Aggregator";

    public static final int ROLE_ID_ADMIN = 1;
    public static final int ROLE_ID_CUSTOMER = 2;
    public static final int ROLE_ID_AGGREGATOR = 3;

    public static final Integer REQUEST_TIMEOUT = 30000;



    //// customer app

    public static ArrayList<String> dates;
    public static String day1="" ;
    public static String day2="";
    public static String day3="";

    public static final String EXTRA_FLOW = "Flow";
    public static final String FLOW_DASHBOARD = "Dashboard";
    public static final String FLOW_SEARCH = "Search";

    private static Constants self = null;
    public OrderListModel selectedOrderForDetail = null;
    public int userRoleId = 0;
    public int userId = 0;
    //    public String endpoint = "http://10.0.2.2:8080";
    public static String endpoint = "http://ec2-13-233-160-172.ap-south-1.compute.amazonaws.com:8080";
    //Ankita - local ip
//    public static String endpoint = "http://192.168.43.68:8080";

    //Shalvi - local ip
    //public static String endpoint = "http://192.168.43.161:8080";
//    public static String endpoint = "http://192.168.0.3:8080";
    public static final String COVID_INVENTORY_API_ORDERS = "/covid-inventory/api/orders";

    private Constants() {
    }

    public static ArrayList<OrdersList> globalOrderList;

    public static Constants getInstance() {
        if (self == null) {
            self = new Constants();
            globalOrderList = new ArrayList<>();
        }
        return self;
    }


    public static String token = "";
    public static String SHARED_PREFERENCE_NAME = "AGGREGATOR_DATA";
    public static final String SHARED_PREFERENCE_FIELD_AGGREGATOR_EMAIL_ID = "AGGREGATOR_EMAIL_ID";
    public static final String SHARED_PREFERENCE_FIELD_AGGREGATOR_ID = "AGGREGATOR_ID";
}
