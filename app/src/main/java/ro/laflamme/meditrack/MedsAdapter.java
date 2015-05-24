package ro.laflamme.meditrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loopiezlol on 13.05.2015.
 */
public class MedsAdapter extends BaseAdapter implements Filterable  {

    Context context;
    private List<Med> originalMeds = null;
    private List<Med> filteredMeds =null;
    private MedFilter mFilter = new MedFilter();
    LayoutInflater inflater;

    public MedsAdapter (Context context, List<Med> meds){
        this.originalMeds = meds;
        this.filteredMeds = meds;
        this.inflater=LayoutInflater.from(context);
        this.context=context;
    }


    @Override
    public int getCount() {
        return filteredMeds.size();
    }

    @Override
    public Med getItem(int position) {
        return filteredMeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredMeds.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.item_list_med, parent,false);

            holder.title=(TextView) convertView.findViewById(R.id.med_item);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Med med = filteredMeds.get(position);
        holder.title.setText(med.getName());

        return convertView;
    }

    private class ViewHolder {
        TextView title;
    }

    public Filter getFilter(){
        return mFilter;
    }

    private class MedFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Med> list = originalMeds;

            int count = list.size();
            final ArrayList<Med> nlist = new ArrayList<Med>(count);

            String filterableString;
            for(int i=0;i<count;i++){

                filterableString = list.get(i).getName();
                if(filterableString.toLowerCase().contains(filterString)){
                    nlist.add(list.get(i));
                }
            }

            results.values=nlist;
            results.count = nlist.size();

            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            filteredMeds =(ArrayList<Med>) results.values;
            notifyDataSetChanged();
        }
    }
}


