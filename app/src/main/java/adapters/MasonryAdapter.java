package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.easybus.R;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {
    private Context context;
    int[] imgList = {R.drawable.logo, R.drawable.logo, R.drawable.logo};
    String[] nameList = {"One", "Two", "Three"};
    public MasonryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mansonarycustom, parent, false);
        MasonryView masonryView = new MasonryView(layoutView);
        return masonryView;
    }
    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        holder.imageView.setImageResource(imgList[position]);
        holder.textView.setText(nameList[position]);
    }
    @Override
    public int getItemCount() {
        return nameList.length;
    }
    public class MasonryView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        MasonryView(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            textView = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}