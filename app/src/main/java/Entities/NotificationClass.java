package Entities;

/**
 * Created by Richard.Ezama on 16/05/2016.
 */
public class NotificationClass {
    public String title,stamp,photo,fullname,UserID;
    public NotificationClass(String fullname,String title,String stamp,String photo,String UserID)
    {
        this.fullname=fullname;
        this.title=title;
        this.photo=photo;
        this.stamp=stamp;
        this.UserID=UserID;
    }
}
