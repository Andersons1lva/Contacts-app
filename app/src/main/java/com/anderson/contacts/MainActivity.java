package com.anderson.contacts;

import static com.karumi.dexter.Dexter.withActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anderson.contacts.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<ContactsModal> contactsModalArrayList;
    private RecyclerView contactRV;
    private ContactsRVAdapter contactsRVAdapter;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactsModalArrayList = new ArrayList<>();
        contactRV = binding.idRVContacts;
        FloatingActionButton addNewContactFAB = binding.idFABadd;
        loadingPB = binding.idPBLoading;

        prepareContactRV();
        exibirProgress();
        requestPermissions();

        addNewContactFAB.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateNewContactActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toLowerCase());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {
        ArrayList<ContactsModal> filteredlist = new ArrayList<>();
        for (ContactsModal item : contactsModalArrayList){
            if (item.getUserName().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()){
            Toast.makeText(this, "No Contact found", Toast.LENGTH_SHORT).show();
        }else{
            contactsRVAdapter.filterList(filteredlist);
        }
    }

    private void prepareContactRV() {
        contactsRVAdapter = new ContactsRVAdapter(this, contactsModalArrayList);
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        contactRV.setAdapter(contactsRVAdapter);
    }

    private void requestPermissions() {

        withActivity(this)
                .withPermissions(Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CONTACTS)
                .withListener(new MultiplePermissionsListener(){

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        if (multiplePermissionsReport.areAllPermissionsGranted()){
                            getContacts();
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                            showSettingDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).withErrorListener(dexterError -> Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_SHORT).show()).onSameThread().check();

    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can them in app settings. ");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();

            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @SuppressLint({"Range", "NotifyDataSetChanged"})
    private void getContacts() {

        String contactId;
        String displayName;

        Cursor cursor = getContentResolver()
                .query(ContactsContract.Contacts
                .CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "ASC");
        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){
                @SuppressLint("Range") int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0){
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ",
                            new String[]{contactId}, null);
                    if (phoneCursor.moveToNext()){
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsModalArrayList.add(new ContactsModal(displayName, phoneNumber));
                    }
                    phoneCursor.close();
                }
            }
        }
        cursor.close();
        loadingPB.setVisibility(View.GONE);
        contactsRVAdapter.notifyDataSetChanged();
    }
    public void exibirProgress(){
       // loadingPB.setVisibility(true ? View.VISIBLE : View.GONE);
    }
}