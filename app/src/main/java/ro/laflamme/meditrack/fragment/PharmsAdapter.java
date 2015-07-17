package ro.laflamme.meditrack.fragment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.laflamme.meditrack.R;
import ro.laflamme.meditrack.domain.Pharm;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class PharmsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Pharm> mPharms;
    private LayoutInflater inflater;

    public PharmsAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.mPharms = new ArrayList<>();
    }

    public PharmsAdapter(Context mContext, List<Pharm> mPharms) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.mPharms = mPharms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_pharm, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.pharm_name);
            holder.desc = (TextView) convertView.findViewById(R.id.pharm_desc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Pharm pharm = mPharms.get(position);
        holder.name.setText(pharm.getName());
        holder.desc.setText(pharm.getDesc() + pharm.isOpenNow());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Pharm getItem(int position) {
        return mPharms.get(position);
    }

    @Override
    public int getCount() {
        return mPharms.size();
    }

    private class ViewHolder {
        TextView name;
        TextView desc;
    }


    /*

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemLayout = R.layout.item_list_pharm;
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemInterface item = mPharms.get(position);
        holder.pharmName.setText(item.getTitle());
        holder.pharmDesc.setText(item.getSubtitle());
    }
*/

    public void setData(List<Pharm> items) {
        this.mPharms = items;
    }

    public void clear() {
        this.mPharms = null;
    }





}
