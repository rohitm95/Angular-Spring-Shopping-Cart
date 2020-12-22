package com.persistent.covidinventory.customer.model;

import java.io.Serializable;

public class Role implements Serializable {

    int id ;
    String name ;

    public  Role(){

    }

   public Role(int id , String role ){

        this.id = id ;
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
