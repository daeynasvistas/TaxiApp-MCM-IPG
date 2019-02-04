package pt.ipg.taxiapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.model.Ride;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {

    private Context ctx;
    private List<Ride> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Ride obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView class_name;
        public TextView price;
        public TextView pax;
        public TextView duration;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            class_name = (TextView) v.findViewById(R.id.class_name);
            price = (TextView) v.findViewById(R.id.price);
            pax = (TextView) v.findViewById(R.id.pax);
            duration = (TextView) v.findViewById(R.id.duration);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public RideAdapter(Context ctx, List<Ride> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public RideAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_class, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Ride r = items.get(position);
        Picasso.with(ctx).load(r.image).into(holder.image);
        holder.class_name.setText(r.name);
        holder.price.setText(r.price);
        holder.pax.setText(r.pess);
        holder.duration.setText(r.duration);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, r, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}