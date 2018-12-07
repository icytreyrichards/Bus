package Entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Richard.Ezama on 23/04/2016.
 */
public class Lost {
    public String title,description,category,telephone,date,stamp,json,location,string,id;
    public Lost(String title,String latitude,String longitude, String description, String category, String telephone,String location,
                String date, String stamp, String json,String id)
    {
        this.title=title;
        this.description=description;
        this.telephone=telephone;
        this.telephone=telephone;
        this.json=json;
        this.stamp=stamp;
        this.date=date;
        this.category=category;
        this.location=location;
        this.id=id;
    }
}
