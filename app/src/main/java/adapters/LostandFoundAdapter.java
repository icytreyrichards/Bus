package adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.my.easybus.Details;
import com.my.easybus.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entities.Found;
import localdata.Data;
import utility.Utility;


public class LostandFoundAdapter extends RecyclerView.Adapter<LostandFoundAdapter.PersonViewHolder> {
    private ArrayList<Found>troublelist=new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private ListView list;
    private Boolean  click;
    ProgressDialog dialog;

    public LostandFoundAdapter(Context cxt, ArrayList<Found> troublelist,Boolean click) {
        this.context = cxt;
        this.troublelist = troublelist;
        LayoutInflater inflater;
        this.click=click;
        dialog=new ProgressDialog(cxt);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title,stamp;
        ImageView image;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView
                    .findViewById(R.id.textViewcustomtitle);
            stamp= (TextView) itemView
                    .findViewById(R.id.textViewstamp);
            image= (ImageView)itemView
                    .findViewById(R.id.imageViewcustomprofilephoto);
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return troublelist.size();
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder,
                                 final int position) {

        personViewHolder.title.setText(troublelist.get(position).title);
        personViewHolder.stamp.setText(troublelist.get(position).stamp);
        Picasso.with(context).load(Data.server+troublelist.get(position).photo)
                .error(R.drawable.logo).fit()
                .into(personViewHolder.image);


        personViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map=new Intent(context,Details.class);
                map.putExtra("json",troublelist.get(position).json);
                context.startActivity(map);
            }
        });
        if(click)
        {
            personViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            AlertDialog.Builder alert=new AlertDialog.Builder(context);
            alert.setTitle("Take Action");
            alert.setMessage("");
            alert.setPositiveButton("View", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent map=new Intent(context,Details.class);
                    map.putExtra("json",troublelist.get(position).json);
                    context.startActivity(map);
                }
            });
            alert.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //String res=LogicClass.delete(context,troublelist.get(position).id);
                    RequestTast task= new RequestTast();
                   task.execute(troublelist.get(position).id);

                }
            });
            alert.show();
                }
            });
        }
        else
        {
            personViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent map=new Intent(context,Details.class);
                    map.putExtra("json",troublelist.get(position).json);
                    context.startActivity(map);
                }
            });
        }
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.customschedule, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    public class RequestTast extends AsyncTask<String, Integer, String> {
        final String  url = Data.server + "delete.php";
        UrlEncodedFormEntity uefa;
        String result="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Deleting");
            dialog.setMessage("please wait");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            final HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setEntity(uefa);
        HttpResponse response;
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        Log.e("id",Data.UserID(context));
        param.add(new BasicNameValuePair("delete","" ));
        param.add(new BasicNameValuePair("id",params[0]));
        try {
            uefa = new UrlEncodedFormEntity(param);
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("delete response", result);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

        @Override
        protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //result=s;
       // Log.e("on post", s);
            Utility.shoAlert(s,context);
            dialog.dismiss();
    }
    }
}