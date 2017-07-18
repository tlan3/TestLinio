package test.com.mx.testfernandopiedra.entities;


import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;

/**
 * Created by fernandostone on 17/07/17.
 *
 */

public class FavoritoItem {

    @Expose
    public String image;
    @Expose
    public boolean freeShipping;
    @Expose
    public boolean imported;
    @Expose
    public String name;
    @Expose
    public boolean active;
    @Expose
    public int wishListPrice;
    @Expose
    public String conditionType;
    @Expose
    public int id;
    @Expose
    public int linioPlusLevel;
    @Expose
    public String slug;
    @Expose
    public String url;
    @Expose(serialize = false)
    public Drawable linioPlusDrawable;

    @Expose(serialize = false)
    public Drawable conditionTypeDrawable;

    @Expose(serialize = false)
    public Drawable freeShippingDrawable;



}
