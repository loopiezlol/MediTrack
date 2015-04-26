package ro.laflamme.meditrack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class PharmsAdapter extends RecyclerView.Adapter<PharmsAdapter.ViewHolder> {

    OnItemClickListener mItemClickListener;
    private Context context;
    private List<Item> items;

    public PharmsAdapter(Context context){
        this.context=context;
    }
    public PharmsAdapter(Context context, List<Item> items){
        this.context=context;
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemLayout = R.layout.pharm_list_item;
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ItemInterface item = items.get(position);

        holder.pharmName.setText(item.getTitle());
        holder.pharmDesc.setText(item.getSubtitle());

    }

    public void setData(List<Item> items){
        this.items=items;
    }
    @Override
    public int getItemCount() {
        if(items!= null)
            return items.size();
        return 0;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout pharm;
        public TextView pharmName;
        public TextView pharmDesc;

        public ViewHolder(View itemView){
            super(itemView);
            pharm = (RelativeLayout) itemView.findViewById(R.id.pharm_layout);
            pharmName = (TextView) itemView.findViewById(R.id.pharm_name);
            pharmDesc = (TextView) itemView.findViewById(R.id.pharm_desc);
        }


        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }



}
