package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.my.easybus.R;

public class SpinnerAdapter extends BaseAdapter {

	ArrayList<String> text = new ArrayList<String>();
	LayoutInflater inflater;
	Context context;

	public SpinnerAdapter(Context context, ArrayList<String> text) {
		this.text = text;
		this.context = context;
		inflater = LayoutInflater.from(this.context); // only context can also
														// be used
	}

	@Override
	public int getCount() {
		return text.size();
	}

	@Override
	public Object getItem(int position) {
		return text.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder mViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.customspinner, null);
			mViewHolder = new MyViewHolder();
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (MyViewHolder) convertView.getTag();
		}
		mViewHolder.text = (TextView) convertView
				.findViewById(R.id.textViewaccount);
		mViewHolder.imageview = (ImageView) convertView
				.findViewById(R.id.imageViewaccount);
		mViewHolder.text.setText(text.get(position));
		Picasso.with(this.context).load(R.drawable.city)
				.error(R.drawable.error).into(mViewHolder.imageview);
		return convertView;
	}

	private class MyViewHolder {
		TextView text;
		ImageView imageview;
	}

}