package com.example.julliandev.juliantechsolve;

import java.util.Dictionary;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {
    private List<Dictionary<String,String>> values;
    private Context act;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.nombrevuelo);
            txtFooter = (TextView) v.findViewById(R.id.descripcionvuelo);
        }
    }


    public AdapterRecycler(Context ctx, List<Dictionary<String, String>> myDataset) {
        values = myDataset;
        act = ctx;
    }


    @Override
    public AdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.vuelos, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = values.get(position).get("name");
        final String desription = values.get(position).get("description");
        holder.txtHeader.setText(desription);

        holder.txtFooter.setText("Vuelo: " + name);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(values.get(position).get("vueloid"));
    }

}