package com.example.medical.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.medical.R;
import com.example.medical.activity.ChattingActivity;
import com.example.medical.model.User;
import com.example.medical.session.MyPreferences;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterPasien extends RecyclerView.Adapter<UserAdapterPasien.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapterPasien(Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listpasien, parent, false);
        return new UserAdapterPasien.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = userList.get(position);

        holder.txtPasien.setText(MyPreferences.getSharedPreferences()
                .getString(MyPreferences.STATUS, "status"));
        String dokter = holder.txtPasien.getText().toString();

        if (user.getStatus().equals("Pasien")) {
            holder.username.setText(user.getNamaLengkap());
            if (user.getImageURL().equals("default")) {
                holder.imageDoctor.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Glide.with(context).load(user.getImageURL()).into(holder.imageDoctor);
            }
            holder.btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ChattingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("userid", user.getId());
                    context.startActivity(i);
                }
            });
        } else {
            holder.cardView1.setVisibility(View.GONE);
            holder.btnChat.setVisibility(View.GONE);
            holder.cardView3.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username, txtPasien;
        public CircleImageView imageDoctor;
        public CardView btnChat, cardView1, cardView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.nameDoctor);
            txtPasien = itemView.findViewById(R.id.txtPasien);
            imageDoctor = itemView.findViewById(R.id.imageDoctor);
            btnChat = itemView.findViewById(R.id.cardView2);
            cardView1 = itemView.findViewById(R.id.cardView1);
            cardView3 = itemView.findViewById(R.id.cardView3);
        }
    }
}