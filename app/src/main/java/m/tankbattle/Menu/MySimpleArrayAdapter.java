package m.tankbattle.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m.tankbattle.R;

/**
 * Created by Mato on 05.11.2016.
 */

public class MySimpleArrayAdapter extends BaseAdapter {
    private final ArrayList<UserObj> values;
    private Context context;

    public MySimpleArrayAdapter(Context context, ArrayList<UserObj> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView text = (TextView) rowView.findViewById(R.id.firstLine);
        TextView uid = (TextView)rowView.findViewById(R.id.secondLine);
        text.setText(values.get(position).getS());
        uid.setText(values.get(position).getUserUid());
        return rowView;
    }
}
