package Entities;

/**
 * Created by Richard.Ezama on 23/04/2016.
 */
public class Ticket {
    public String source,destination,time,RouteID,CompanyID,id,CompanyName,json,price,ticketid,status;
    public Ticket(String source, String destination, String time, String routeID, String id, String name, String json, String price,String ticketid,String status)
    {
        this.source=source;
        this.destination=destination;
        this.time=time;
        this.RouteID=routeID;
        this.id=id;
        this.CompanyName=name;
        this.json=json;
        this.price=price;
        this.ticketid=ticketid;
        this.status=status;
    }
}
