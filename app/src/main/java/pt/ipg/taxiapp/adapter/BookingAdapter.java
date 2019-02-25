package pt.ipg.taxiapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.model.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context ctx;
    private List<Booking> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Booking obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView status;
        public TextView date;
        public TextView pickup;
        public TextView destination;
        public TextView time;
        public TextView rider_class;
        public TextView payment;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            status = (TextView) v.findViewById(R.id.status);
            date = (TextView) v.findViewById(R.id.date);
            pickup = (TextView) v.findViewById(R.id.pickup);
            destination = (TextView) v.findViewById(R.id.destination);
            time = (TextView) v.findViewById(R.id.time);
            rider_class = (TextView) v.findViewById(R.id.ride_class);
            payment = (TextView) v.findViewById(R.id.payment);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public BookingAdapter(Context ctx, List<Booking> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Booking b = items.get(position);

        holder.date.setText(b.date);
        holder.pickup.setText(b.pickup);
        holder.destination.setText(b.destination);
        holder.time.setText(b.time);
        holder.rider_class.setText(b.ride_class);
        holder.payment.setText(b.payment);

        holder.status.setText(b.status);
        if (b.status.equals("ATIVO")) {
            holder.status.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.shape_rectangle_ongoing));
        } else if (b.status.equals("FINALIZADO")) {
            holder.status.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.shape_rectangle_finished));
        } else if (b.status.equals("CANCELADO")) {
            holder.status.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.shape_rectangle_canceled));
        }

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, b, position);
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