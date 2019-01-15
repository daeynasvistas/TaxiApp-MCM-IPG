package pt.ipg.taxiapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> users = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = users.get(position);
        holder.textViewNome.setText(currentUser.getNome());
        holder.textViewEmail.setText(currentUser.getEmail());
        holder.textViewStar.setText(String.valueOf(currentUser.getStar()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
