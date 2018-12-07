package Entities;

/**
 * Created by Richard.Ezama on 23/04/2016.
 */
public class Price {
    public String source,destination,CompanyName,price,productName;
    public Price(String source, String destination,String name, String price, String productName)
    {
        this.source=source;
        this.destination=destination;
        this.CompanyName=name;
        this.price=price;
        this.productName=productName;
    }
}
