package ro.laflamme.meditrack.fragment;


import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ro.laflamme.meditrack.MainActivity;
import ro.laflamme.meditrack.MediLocation;
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
            holder.isOpen = (ImageView) convertView.findViewById(R.id.isopen_indicator);
            holder.navHolder = (LinearLayout) convertView.findViewById(R.id.holder_nav);
            holder.navDistance = (TextView) convertView.findViewById(R.id.nav_distance);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Pharm pharm = mPharms.get(position);
        holder.name.setText(pharm.getName());
        holder.desc.setText(pharm.getDesc());
        //holder.navDistance.setText();





        if(pharm.isOpenNow())
        {
            holder.isOpen.setColorFilter(Color.parseColor("#4CAF50"));
        } else
        {
            holder.isOpen.setColorFilter(Color.parseColor("#FF5722"));
        }


        holder.navHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).goOnMap(pharm.getLatitude(),pharm.getLongitude());
                }
            }
        });





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
        ImageView isOpen;
        LinearLayout navHolder;
        TextView navDistance;
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
