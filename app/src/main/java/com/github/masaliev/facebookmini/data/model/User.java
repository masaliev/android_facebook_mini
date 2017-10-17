package com.github.masaliev.facebookmini.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mbt on 10/16/17.
 */

public class User implements Serializable{

    @Expose
    public int id;

    @Expose
    @SerializedName("full_name")
    public String fullName;

    @Expose
    public String picture;

    @Expose
    public String phone;

    @Expose
    public String token;
}
