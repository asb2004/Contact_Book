package com.example.contactbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactbook.model.Contact;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> contactList;
    private SelectListener selectListener;

    public RecyclerViewAdapter(Context context, ArrayList<Contact> contactList, SelectListener selectListener) {
        this.context = context;
        this.contactList = contactList;
        this.selectListener = selectListener;
    }

    public void setFilterdList(ArrayList<Contact> filterdList) {
        this.contactList = filterdList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.contactName.setText(contactList.get(position).getName());

        holder.eachCard.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.contact_list_anim));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView contactName;
        private ImageView contactimg;
        private CardView eachCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.contactname);
            eachCard = itemView.findViewById(R.id.eachCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.call_button));
                    if(selectListener != null) {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION) {
                            selectListener.onItemClicked(contactList.get(position));
                        }
                    }
                }
            });

        }
    }
}
