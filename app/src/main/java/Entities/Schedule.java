package Entities;

/**
 * Created by Richard.Ezama on 23/04/2016.
 */
public class Schedule {
    public String source,destination,time,RouteID,CompanyID,id,CompanyName,json,price,productName;
    public Schedule(String source, String destination, String time,String routeID, String id,String name,String json,String price,String productName)
    {
        this.source=source;
        this.destination=destination;
        this.time=time;
        this.RouteID=routeID;
        this.id=id;
        this.CompanyName=name;
        this.json=json;
        this.price=price;
        this.productName=productName;

    }
}
