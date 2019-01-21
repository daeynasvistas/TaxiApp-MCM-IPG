package pt.ipg.taxiapp.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.model.Taxi;

public class TaxiAdapter extends RecyclerView.Adapter<TaxiAdapter.UserHolder> {
    private List<Taxi> taxis = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        Taxi currentTaxi = taxis.get(position);
        holder.textViewNome.setText(currentTaxi.getNome());
        holder.textViewEmail.setText(currentTaxi.getEmail());
        holder.textViewStar.setText(String.valueOf(currentTaxi.getStar()));
    }

    @Override
    public int getItemCount() {
        return taxis.size();
    }

    public void setTaxis(List<Taxi> taxis) {
        this.taxis = taxis;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView textViewNome;
        private TextView textViewEmail;
        private TextView textViewStar;

        public UserHolder(View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.text_view_nome);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            textViewStar = itemView.findViewById(R.id.text_view_star);
        }
    }
}
