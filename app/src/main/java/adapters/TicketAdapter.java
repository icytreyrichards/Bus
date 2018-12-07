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
import com.my.easybus.R;
import com.my.easybus.ViewTicketDetails;

import org.json.JSONObject;

import java.util.ArrayList;

import Entities.Schedule;
import Entities.Ticket;
import utility.Utility;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.PersonViewHolder> {
    private ArrayList<Ticket>schedulelist=new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private ListView list;
    ProgressDialog dialog;

    public TicketAdapter(Context cxt, ArrayList<Ticket> list) {
        this.context = cxt;
        this.schedulelist = list;
        LayoutInflater inflater;
        dialog=new ProgressDialog(cxt);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView from,to,time,bus,price,ticket,status,stamp;
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
            ticket= (TextView) itemView
                    .findViewById(R.id.textViewcustomticket);
            status= (TextView) itemView
                    .findViewById(R.id.textViewcustomstatus);
            stamp= (TextView) itemView
                    .findViewById(R.id.textViewcustomstamp);
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
        final String json=schedulelist.get(position).json;
        final  String ticket=schedulelist.get(position).ticketid;
        String stamp="";
        String ref="";
        try {
            JSONObject o = new JSONObject(json);
            stamp=o.getString("stamp");
            ref=o.getString("BookRef");
        }
        catch (Exception er)
        {

        }

        personViewHolder.from.setText("From "+schedulelist.get(position).source);
        personViewHolder.to.setText("To "+schedulelist.get(position).destination);
        personViewHolder.time.setText("Departure Time "+schedulelist.get(position).time);
        personViewHolder.bus.setText(schedulelist.get(position).CompanyName);
        personViewHolder.price.setText(schedulelist.get(position).price);
        personViewHolder.stamp.setText(stamp);

        personViewHolder.ticket.setText(schedulelist.get(position).ticketid+" Ticket Code "+ref);
        personViewHolder.status.setText(schedulelist.get(position).status);

       // Utility.showAlert(schedulelist.get(position).ticketid,context);
        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utility.showAlert(schedulelist.get(position).ticketid+" "+schedulelist.get(position).status,context);
                //tickets details here
                Intent i=new Intent(context, ViewTicketDetails.class);
                i.putExtra("json",json);
                i.putExtra("ticket",ticket);
                context.startActivity(i);
            }
        });
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.customticket, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }
}//