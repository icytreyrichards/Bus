package Entities;

/**
 * Created by Richard.Ezama on 24/04/2016.
 */
public class Profile {
    public String fullname,telephone,email,photo,json,UserID;
    public Profile(String fullname,String telephone,String email,String photo,String json,String UserID)
    {
       this.email=email;
        this.fullname=fullname;
        this.telephone=telephone;
        this.photo=photo;
        this.json=json;
        this.UserID=UserID;
    }
}
