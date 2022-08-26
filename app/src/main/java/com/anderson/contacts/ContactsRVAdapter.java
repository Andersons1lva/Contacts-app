package com.anderson.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<ContactsModal> contactsModalArrayList;

    public ContactsRVAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList){
        this.context = context;
        this.contactsModalArrayList = contactsModalArrayList;
    }

    @NonNull
    @Override
    public ContactsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false));
    }


    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ContactsModal> filters){
        contactsModalArrayList = filters;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRVAdapter.ViewHolder holder, int position) {

        ContactsModal modal = contactsModalArrayList.get(position);
        holder.contactTV.setText(modal.getUserName());

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, ContactDetailActivity.class);
            i.putExtra("name", modal.getUserName());
            i.putExtra("contact", modal.getContactNumber());

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return contactsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ImageView contactIV = itemView.findViewById(R.id.idIVContact);
            contactTV = itemView.findViewById(R.id.idTVContactsName);
        }
    }
}
