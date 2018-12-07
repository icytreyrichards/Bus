package adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.my.easybus.Details;
import com.my.easybus.DetailsDriver;
import com.my.easybus.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import Entities.Schedule;
import localdata.Data;


public class ScheduleAdapterDrivers extends RecyclerView.Adapter<ScheduleAdapterDrivers.PersonViewHolder> {
    private ArrayList<Schedule>schedulelist=new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private ListView list;
    ProgressDialog dialog;

    public ScheduleAdapterDrivers(Context cxt, ArrayList<Schedule> list) {
        this.context = cxt;
        this.schedulelist = list;
        LayoutInflater inflater;
        dialog=new ProgressDialog(cxt);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView from,to,time,bus,price,type;
        ImageView image;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            from = (TextView) itemView
                    .findViewById(R.id.textViewcustomsource);
            to= (TextView) itemView
                    .findViewById(R.id.textViewcustomdestination);
            time= (TextView) itemView
                    .findViewById(R.id.textViewcustomtime);
            bus= (TextView) itemView
                    .findViewById(R.id.textViewcustomcompany);
            price= (TextView) itemView
                    .findViewById(R.id.textViewcustomprice);
            type= (TextView) itemView
                    .findViewById(R.id.textViewcustomtype);

            image= (ImageView)itemView
                    .findViewById(R.id.imageViewcustomprofilephoto);
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return schedulelist.size();
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder,
                                 final int position) {
        personViewHolder.from.setText("From "+schedulelist.get(position).source);
        personViewHolder.to.setText("To "+schedulelist.get(position).destination);
        personViewHolder.time.setText("Departure Time "+schedulelist.get(position).time);
        personViewHolder.bus.setText(schedulelist.get(position).CompanyName);
        personViewHolder.price.setText(schedulelist.get(position).price+" Ushs");
        personViewHolder.type.setText(schedulelist.get(position).productName);
        String json=schedulelist.get(position).json;
        try {
            JSONObject o=new JSONObject(json);
            String photo=Data.server+"manager/"+o.getString("logo");
           // Utility.shoAlert(photo,context);
            Picasso.with(context).load(photo)
                    .error(R.drawable.logo).fit()
                    .into(personViewHolder.image);
        }
        catch (Exception er)
        {

        }
            personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent map=new Intent(context,DetailsDriver.class);
                    map.putExtra("json",schedulelist.get(position).json);
                    context.startActivity(map);
                }
            });
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.customschedule, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}