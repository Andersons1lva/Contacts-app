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

    private ActivityContactDetailBinding binding;
    private String contactName, contactNumber;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactName = getIntent().getStringExtra("name");
        contactNumber = getIntent().getStringExtra("contact");

        nameTV = binding.idTVName;
        contactIV = binding.idIVContact;
        contactTV = binding.idTVPhone;
        nameTV.setText(contactName);
        contactTV.setText(contactNumber);
        callIV = binding.idIVCall;
        messageIV = binding.idIVMessege;

        callIV.setOnClickListener(view -> makeCall(contactNumber));
        messageIV.setOnClickListener(view -> sendMessage(contactNumber));
    }

    private void sendMessage(String contactNumber) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms" + contactNumber));
        intent.putExtra("sms_body", "Enter your messenge");
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