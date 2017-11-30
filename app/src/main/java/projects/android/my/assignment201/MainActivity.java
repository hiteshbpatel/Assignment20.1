package projects.android.my.assignment201;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if((ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED))
        {
            //if permission is not granted ask for permission..
            //RequestCode is used for Callback funtion to check on which permission action was taken
            ActivityCompat.requestPermissions(this,new String[] 
                                              {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},100);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            //If Permission was granted show msg
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
            }

        }
    }

    public void AddContact(View view) {
        try {

            EditText contactName = (EditText) findViewById(R.id.et_name);
            EditText contactNumber = (EditText) findViewById(R.id.et_mobile_phone);
            ContentResolver resolver = getContentResolver();
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,contactName.getText().toString()).build());

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,contactNumber.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,Phone.TYPE_MOBILE).build());

            resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this, "Error inserting Contact"+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
