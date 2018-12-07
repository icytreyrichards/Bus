package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.my.easybus.R;
import com.my.easybus.SearchRoutes;

import java.util.ArrayList;

import Entities.Account;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.PersonViewHolder> {
    private ArrayList<Account>profilelist=new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private ListView list;

    public DetailsAdapter(Context cxt, ArrayList<Account> profilelist) {
        this.context = cxt;
        this.profilelist = profilelist;
        LayoutInflater inflater;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title,text;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView
                    .findViewById(R.id.textViewtitle);
            text= (TextView) itemView
                    .findViewById(R.id.textViewdata);
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return profilelist.size();
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder,
                                 final int position) {
        personViewHolder.title.setText(profilelist.get(position).title);
        personViewHolder.text.setText(profilelist.get(position).text);
        personViewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=new Intent(context,SearchRoutes.class);
                s.putExtra("search",profilelist.get(position).text);
                if(position==1) {
                    context.startActivity(s);
                }
            }
        });
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.customdetails, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}