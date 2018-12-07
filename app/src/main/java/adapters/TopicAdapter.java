package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.easybus.R;
import com.my.easybus.SearchRoutes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Entities.Topics;


public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.PersonViewHolder> {
    private ArrayList<Topics>list;
    LayoutInflater inflater;
    Context context;
    private Boolean origin;

    public TopicAdapter(Context cxt, ArrayList<Topics>list) {
        this.context = cxt;
        this.list = list;
        LayoutInflater inflater;
        this.origin=origin;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title,subtitle;
        ImageView image,icon;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView
                    .findViewById(R.id.textViewtitle);
            subtitle = (TextView) itemView
                    .findViewById(R.id.textViewsubtitle);
            icon= (ImageView)itemView
                    .findViewById(R.id.imageViewtopiclogo);
           image= (ImageView)itemView
                    .findViewById(R.id.imageViewthumbnail);
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder,
                                 final int position) {
        personViewHolder.title.setText(list.get(position).title);
        personViewHolder.subtitle.setText(list.get(position).subtitle);
        Picasso.with(context).load(list.get(position).photo)
                .error(R.drawable.right).fit()
                .into(personViewHolder.icon);

        personViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        personViewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=new Intent(context,SearchRoutes.class);
                s.putExtra("search",list.get(position).title);
                context.startActivity(s);

            }
        });
        personViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=new Intent(context,SearchRoutes.class);
                s.putExtra("search",list.get(position).title);
                context.startActivity(s);

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
                R.layout.topiccard, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}