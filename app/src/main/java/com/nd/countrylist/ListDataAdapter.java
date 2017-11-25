package com.nd.countrylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListDataAdapter extends RecyclerView.Adapter<ListDataAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<CountryList> mArrayList;
    private List<CountryList> mFilteredList;
    private ListDataAdapterListener listener;

    public interface ListDataAdapterListener {
        void onItemSelected(CountryList country);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView thumbnail;


        public ViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.name);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Log.e("Object", "Name : " + mFilteredList.get(getAdapterPosition()).getName());

                    listener.onItemSelected(mFilteredList.get(getAdapterPosition()));
                }
            });
        }
    }

    public ListDataAdapter(Context context, List<CountryList> contactList) {
        this.context = context;
        this.mArrayList = contactList;
        this.mFilteredList = contactList;
    }

    public ListDataAdapter(Context context, List<CountryList> contactList, ListDataAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.mArrayList = contactList;
        this.mFilteredList = contactList;
    }

    @Override
    public ListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListDataAdapter.ViewHolder viewHolder, final int position) {
        final CountryList country = mFilteredList.get(position);

        viewHolder.name.setText(country.getName());

        /*
        GlideApp
                .with(this.context)
                .load(country.getFlag())
                .into(viewHolder.thumbnail);
        */
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    List<CountryList> filteredList = new ArrayList<>();

                    for (CountryList row : mArrayList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<CountryList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
