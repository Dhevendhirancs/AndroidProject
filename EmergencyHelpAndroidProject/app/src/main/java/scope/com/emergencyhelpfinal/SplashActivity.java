package scope.com.emergencyhelpfinal;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SQLiteDatabase db;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        db = openOrCreateDatabase( "Temp.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS tbl_user_info ("
                + "ID INTEGER primary key AUTOINCREMENT,"
                + "edAdhaar TEXT,"
                + "mobile TEXT,"
                + "edName TEXT,"
                + "edFname TEXT,"
                + "edDob TEXT,"
                + "doornumer TEXT,"
                + "Street TEXT,"
                + "city TEXT,"
                + "state TEXT,"
                + "pincode TEXT);";
        db.execSQL(CREATE_TABLE_CONTAIN);

        final String CREATE_TABLE_CONTAIN1 = "CREATE TABLE IF NOT EXISTS tbl_user ("
                + "ID INTEGER primary key AUTOINCREMENT,"
                + "MOBILE TEXT);";
        db.execSQL(CREATE_TABLE_CONTAIN1);

        final String CREATE_TABLE_CONTAIN2 = "CREATE TABLE IF NOT EXISTS tbl_contact_info ("
                + "ID INTEGER primary key AUTOINCREMENT,"
                + "ContactNo TEXT,"
                + "Name TEXT);";
        db.execSQL(CREATE_TABLE_CONTAIN2);

        Cursor c = db.rawQuery("SELECT * FROM tbl_user", null);
        count=c.getCount();
        c.close();

        Intent mServiceIntent = new Intent(getBaseContext(), MyService.class);

        startService(mServiceIntent);

       // startService(new Intent(getBaseContext(), MyService.class));

        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3*1000);

                    if(count>0) {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        Intent i = new Intent(getBaseContext(), TermsAndConditionsActivity.class);
                        startActivity(i);
                    }

                    finish();

                } catch (Exception e) {

                }
            }
        };


        background.start();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
