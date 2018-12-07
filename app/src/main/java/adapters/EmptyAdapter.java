package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.easybus.R;


public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.PersonViewHolder> {
    LayoutInflater inflater;
    Context context;
    public EmptyAdapter(Context cxt) {
        this.context = cxt;
        LayoutInflater inflater;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        PersonViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView
                    .findViewById(R.id.textView2);
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return 1;
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder,
                                 final int position) {
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.emptylayout, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

}