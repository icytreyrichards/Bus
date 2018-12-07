package adapters;

import android.content.Context;
import android.content.Intent;
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

import com.my.easybus.MainActivity;
import com.my.easybus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Menu;
import Entities.Seat;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.PersonViewHolder> {
    private ArrayList<Menu>list;
    LayoutInflater inflater;
    Context context;
    private Boolean clicked=false;;
    private EditText edit;
    private String type;
    private String layout;
    public MenuAdapter(Context cxt, ArrayList<Menu>list) {
        this.context = cxt;
        this.list = list;
        this.type=type;
        this.layout=layout;
    }
    public  class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        final ImageView image;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView
                    .findViewById(R.id.textViewmenutitle);

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

            Picasso.with(context).load(list.get(position).photo)
                    .error(R.drawable.right).fit()
                    .into(personViewHolder.image);
        personViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(position==0)
{
    Intent i=new Intent(context, MainActivity.class);
    context.startActivity(i);
}

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
                R.layout.custommenu, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}