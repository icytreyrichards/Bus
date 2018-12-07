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

import java.util.ArrayList;

import Entities.Price;
import Entities.Schedule;


public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PersonViewHolder> {
    private ArrayList<Price>schedulelist=new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private ListView list;
    ProgressDialog dialog;

    public PriceAdapter(Context cxt, ArrayList<Price> list) {
        this.context = cxt;
        this.schedulelist = list;
        LayoutInflater inflater;
        dialog=new ProgressDialog(cxt);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView from,to,bus,price,type;
        ImageView image;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            from = (TextView) itemView
                    .findViewById(R.id.textViewcustomsource);
            to= (TextView) itemView
                    .findViewById(R.id.textViewcustomdestination);

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
        personViewHolder.bus.setText(schedulelist.get(position).CompanyName);
        personViewHolder.price.setText(schedulelist.get(position).price+" Ush");
        personViewHolder.type.setText(schedulelist.get(position).productName);

    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.customprice, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}