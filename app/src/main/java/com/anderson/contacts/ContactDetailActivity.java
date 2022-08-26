package com.anderson.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.anderson.contacts.databinding.ActivityContactDetailBinding;

public class ContactDetailActivity extends AppCompatActivity {

    private String contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.anderson.contacts.databinding.ActivityContactDetailBinding binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String contactName = getIntent().getStringExtra("name");
        contactNumber = getIntent().getStringExtra("contact");

        TextView nameTV = binding.idTVName;
        ImageView contactIV = binding.idIVContact;
        TextView contactTV = binding.idTVPhone;
        nameTV.setText(contactName);
        contactTV.setText(contactNumber);
        ImageView callIV = binding.idIVCall;
        ImageView messageIV = binding.idIVMessege;

        callIV.setOnClickListener(view -> makeCall(contactNumber));
        messageIV.setOnClickListener(view -> sendMessage(contactNumber));
    }

    private void sendMessage(String contactNumber) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms" + contactNumber));
        intent.putExtra("sms_body", "Enter your message");
        startActivity(intent);
    }

    private void makeCall(String contactNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contactNumber));
        if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}