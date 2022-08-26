package com.anderson.contacts;

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

    private Context context;
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


    public void filterList(ArrayList<ContactsModal> filterlist){
        contactsModalArrayList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRVAdapter.ViewHolder holder, int position) {

        ContactsModal modal = contactsModalArrayList.get(position);
        holder.contactTV.setText(modal.getUserName());
        int color= ((int)(Math.random()*16777215)) | (0xFF << 24);

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

        private ImageView contactIV;
        private TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactIV = itemView.findViewById(R.id.idIVContact);
            contactTV = itemView.findViewById(R.id.idTVContactsName);
        }
    }
}
