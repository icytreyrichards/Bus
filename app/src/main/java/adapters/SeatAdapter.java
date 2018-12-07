package adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.my.easybus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Seat;
import utility.Utility;


public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.PersonViewHolder> {
    private ArrayList<Seat>list;
    LayoutInflater inflater;
    Context context;
    private Boolean clicked=false;;
    private EditText edit;
    private String type;
    private String layout;
    public SeatAdapter(Context cxt, ArrayList<Seat>list, EditText edittxt,String type,String layout) {
        this.context = cxt;
        this.list = list;
        this.edit=edittxt;
        this.type=type;
        this.layout=layout;
    }
    public  class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title,subtitle;
        final ImageView image;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView
                    .findViewById(R.id.textViewcustomseatnumber);
            subtitle = (TextView) itemView
                    .findViewById(R.id.textViewcustomseatstatus);

           image= (ImageView)itemView
                    .findViewById(R.id.imageViewcustomseat);
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder,
                                 final int position) {
int actualgrid=position+1;
        personViewHolder.title.setText(list.get(position).title);
        personViewHolder.subtitle.setText(list.get(position).subtitle);
        String status=list.get(position).subtitle;

        if(status.equals("available"))
        {
        Picasso.with(context).load(R.drawable.seat)
                .error(R.drawable.right).fit()
                .into(personViewHolder.image);
        }

         if(list.get(position).subtitle.equals("taken"))
        {
            Picasso.with(context).load(R.drawable.booked)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
        }
         if(list.get(position).subtitle.equals("driver"))
        {
            Picasso.with(context).load(R.drawable.driver)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
        }
         if(list.get(position).subtitle.equals("empty"))
        {
            Picasso.with(context).load(R.drawable.white)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }
         if(list.get(position).subtitle.equals("staff"))
        {
            Picasso.with(context).load(R.drawable.staff)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
           // personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("Staff");
        }
        //ent       //////////door position
         if((list.get(position).subtitle.equals("door")) && (actualgrid==25))
        {
            Picasso.with(context).load(R.drawable.ent)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }
        //rance
         if((list.get(position).subtitle.equals("door")) && (actualgrid==26))
        {
            Picasso.with(context).load(R.drawable.rance)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }




        if((list.get(position).subtitle.equals("door")) && (actualgrid==11))
        {
            Picasso.with(context).load(R.drawable.ent)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }
        //rance
        if((list.get(position).subtitle.equals("door")) && (actualgrid==12))
        {
            Picasso.with(context).load(R.drawable.rance)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }

        ////////////////////clasic ent rance
        if((list.get(position).subtitle.equals("door")) && (actualgrid==1))
        {
            Picasso.with(context).load(R.drawable.ent)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }
        //rance
        if((list.get(position).subtitle.equals("door")) && (actualgrid==2))
        {
            Picasso.with(context).load(R.drawable.rance)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
            personViewHolder.subtitle.setText("");
            personViewHolder.title.setText("");
        }

        personViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seatNumber=1;

                if(type.contains("Ordinary"))
                {
                    seatNumber=LogicClass.getSeatNumberOrdinary(position+1);

                }
                else
                {
                    if ((layout.equals("ordinary")) || (layout.equals("Ordinary"))) {
                        seatNumber = LogicClass.getSeatNumberExecutive(position + 1);
                    }
                    else
                    {
                        seatNumber = LogicClass.getSeatNumberExecutiveClassic(position + 1);
                    }
                }

                if((list.get(position).subtitle.equals("available"))) {
                    if (clicked) {
                        personViewHolder.image.setBackgroundColor(Color.TRANSPARENT);
                        clicked = false;
                    } else if (!clicked) {

                            personViewHolder.subtitle.setText("");
                            //before
                           // personViewHolder.image.setBackgroundColor(Color.GREEN);
                            edit.setText(seatNumber + "");
                            clicked = true;

                        Picasso.with(context).load(R.drawable.selected)
                                .error(R.drawable.right).fit()
                                .into(personViewHolder.image);
                    }
                    Toast.makeText(context, "Seat "+seatNumber+"",Toast.LENGTH_LONG).show();
                }
                else if (list.get(position).subtitle.equals("staff"))
                {
                    Toast.makeText(context, "staff",Toast.LENGTH_LONG).show();
                }
                else if (list.get(position).subtitle.equals("door"))
                {
                    Toast.makeText(context, "Door",Toast.LENGTH_LONG).show();
                }
                else{
                    MediaPlayer mp=MediaPlayer.create(context,R.raw.robot);
                    if(mp.isPlaying())
                    {
                        mp.stop();
                        mp.stop();
                    }
                    else
                    {
                        mp.start();
                    }
                    Toast.makeText(context, "seat "+seatNumber+" Taken",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(context, "seat "+seatNumber+" Taken",Toast.LENGTH_LONG).show();

            }
        });

        personViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.customseat, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}