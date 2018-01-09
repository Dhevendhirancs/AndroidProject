package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static scope.com.emergencyhelpfinal.R.id.mlistView;



public class ContactsActivity extends Activity {

    private ListView list;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    SQLiteDatabase db;
    String MobileNo="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fragment);

        list = (ListView) findViewById(mlistView);
        arrayList = new ArrayList<String>();

        db = openOrCreateDatabase( "Temp.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                arrayList.remove(item);
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), item +" is removed.", Toast.LENGTH_SHORT).show();

            }
        });



        Button btn_finish = (Button) findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Cursor c = db.rawQuery("SELECT * FROM tbl_user", null);
                if(c.getCount()>=0){

                    c.moveToFirst();
                    MobileNo = c.getString(c.getColumnIndex("MOBILE"));
                }
                c.close();



                try {

                    db.delete("tbl_contact_info", null, null);

                    for (String data : arrayList)
                    {
                         String[] tokens =data.split(Pattern.quote("-"));
                        String sql =
                                "INSERT or replace INTO tbl_contact_info (ContactNo,Name) " +
                                        "VALUES('"+tokens[1]+"'" +
                                        ",'"+tokens[0]+"')" ;
                        db.execSQL(sql);
                    }



                }
                catch (Exception e) {
                    Toast.makeText(ContactsActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(ContactsActivity.this,MainActivity.class);
                startActivity(intent);

            }

        });

        ImageButton mButton = (ImageButton)findViewById(R.id.imgbtn_addCon);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent,1);


            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    //  if(!contactData.equals(arrayList)){
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI};
                    Cursor c = this.getContentResolver().query(contactData, projection, null, null, null);
                    c.moveToFirst();
                    int nameIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int phoneNumberIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int photoIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI);
                    String name = c.getString(nameIdx);
                    String phoneNumber = c.getString(phoneNumberIdx);
                    if (name == null) {
                        name = "No Name";
                    }

                    if(arrayList.contains(name + "-" + phoneNumber)){
                        Toast.makeText(this, "Already Added", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (arrayList.size() >= 5) {
                            Toast.makeText(this, "You already added 5 contacts", Toast.LENGTH_SHORT).show();
                        } else {
                            arrayList.add(name + "-" + phoneNumber);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    c.close();
                }
        }
    }
}
