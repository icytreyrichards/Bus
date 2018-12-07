package Entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Richard.Ezama on 23/04/2016.
 */
public class Found {
    public String latitude,longitude,title,description,category,photo,telephone,location,date,stamp,json,id;
    public Found(String title, String latitude, String longitude, String description, String category, String photo, String telephone,
                 String location, String date, String stamp, String json,String id)
    {
this.latitude=latitude;
        this.title=title;
        this.longitude=longitude;
        this.description=description;
        this.telephone=telephone;
        this.telephone=telephone;
        this.json=json;
        this.stamp=stamp;
        this.date=date;
        this.photo=photo;
        this.category=category;
        this.id=id;
    }
}
