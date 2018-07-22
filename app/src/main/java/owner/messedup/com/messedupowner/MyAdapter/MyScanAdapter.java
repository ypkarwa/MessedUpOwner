package owner.messedup.com.messedupowner.MyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import owner.messedup.com.messedupowner.R;
import owner.messedup.com.messedupowner.data.model.Lastscan;

public class MyScanAdapter extends ArrayAdapter<Lastscan> {

    List<Lastscan> lastscanList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MyScanAdapter(Context context, List<Lastscan> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        lastscanList = objects;
    }

    @Override
    public Lastscan getItem(int position) {
        return lastscanList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_scans, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Lastscan item = getItem(position);

        vh.textViewName.setText(item.getName());
        vh.textViewPlate.setText(item.getPlate());
        vh.textViewTime.setText(item.getTimestamp());

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView textViewName;
        public final TextView textViewPlate;
        public final TextView textViewTime;


        private ViewHolder(LinearLayout rootView, TextView textViewName, TextView textViewPlate, TextView textViewTime) {
            this.rootView = rootView;
            this.textViewName = textViewName;
            this.textViewPlate = textViewPlate;
            this.textViewTime = textViewTime;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewPlate = (TextView) rootView.findViewById(R.id.textViewPlate);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textViewTime);

            return new ViewHolder(rootView, textViewName, textViewPlate, textViewTime);
        }
    }
}

