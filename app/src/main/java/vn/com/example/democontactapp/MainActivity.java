package vn.com.example.democontactapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.com.example.democontactapp.adapter.ContactAdapter;
import vn.com.example.democontactapp.model.Contact;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewContact;
    private List<Contact> mContacts;
    private ContactAdapter mContactAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkQuyen();
//        getItem();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkQuyen() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            new LoadData().execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new LoadData().execute();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    private void initView() {
        mContacts = new ArrayList<>();
        mContactAdapter = new ContactAdapter(MainActivity.this, mContacts);
        mRecyclerViewContact = findViewById(R.id.recycler_view_contacts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewContact.setLayoutManager(layoutManager);
        mRecyclerViewContact.setHasFixedSize(true);
        mRecyclerViewContact.setAdapter(mContactAdapter);
        mContactAdapter.notifyDataSetChanged();
    }

//    public void getItem() {
//        Uri uri = Uri.parse(getString(R.string.uri_phone));
//        Cursor cursor = getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " DESC");
//        if (cursor != null) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                String avatar = "", phoneNumber = "";
//                String nameColumName = ContactsContract.Contacts.DISPLAY_NAME;
//                int nameIndex = cursor.getColumnIndex(nameColumName);
//                String name = cursor.getString(nameIndex);
//                Cursor cr = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", new String[]{name}, null);
//                while (cr.moveToNext()) {
//                    phoneNumber = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    avatar = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
//                }
//                mContacts.add(new Contact(name, phoneNumber));
//                cursor.moveToNext();
//            }
//            cursor.close();
//            mContactAdapter.notifyDataSetChanged();
//        }
//    }

    public class LoadData extends AsyncTask<Void, Contact, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getItem();
            return null;
        }

        @Override
        protected void onProgressUpdate(Contact... values) {
            super.onProgressUpdate(values);
            mContacts.add(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mContactAdapter.notifyDataSetChanged();
        }

        public void getItem() {
            Uri uri = Uri.parse(getString(R.string.uri_phone));
            Cursor cursor = getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " DESC");
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String phoneNumber = "";
                    String nameColumName = ContactsContract.Contacts.DISPLAY_NAME;
                    int nameIndex = cursor.getColumnIndex(nameColumName);
                    String name = cursor.getString(nameIndex);
                    Cursor cr = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", new String[]{name}, null);
                    while (cr.moveToNext()) {
                        phoneNumber = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    publishProgress(new Contact(name, phoneNumber));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
    }
}
