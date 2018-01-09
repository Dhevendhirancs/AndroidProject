package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 3/26/2017.
 */

public class ProfileActivity extends Activity {

    JSONObject jObj=null;

    EditText edAdhaar;
    EditText edName;
    EditText edFname;
    EditText edDob;
    EditText doornumer;
    EditText Street;
    EditText city;
    EditText state;
    EditText pincode;
    RadioButton male;
    RadioButton female;

    SQLiteDatabase db;
    String MobileNo="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = openOrCreateDatabase( "Temp.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.profile_fragment);

        Button btn_continue = (Button) findViewById(R.id.BtnContinue);

        btn_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Cursor c = db.rawQuery("SELECT * FROM tbl_user", null);
                if(c.getCount()>0){

                    c.moveToFirst();
                    MobileNo = c.getString(c.getColumnIndex("MOBILE"));
                }
                c.close();



                try {
                    db.delete("tbl_user_info", null, null);

                    String sql =
                            "INSERT or replace INTO tbl_user_info (edAdhaar,mobile,edName,edFname,edDob,doornumer,street,city,state,pincode) " +
                                    "VALUES('"+edAdhaar.getText().toString()+"'" +
                                    ",'"+MobileNo.toString()+"'" +
                                    ",'"+edName.getText().toString()+"'" +
                                    ",'"+edFname.getText().toString()+"'" +
                                    ",'"+edDob.getText().toString()+"'" +
                                    ",'"+doornumer.getText().toString()+"'" +
                                    ",'"+Street.getText().toString()+"'" +
                                    ",'"+city.getText().toString()+"'" +
                                    ",'"+state.getText().toString()+"'" +
                                    ",'"+pincode.getText().toString()+"')" ;
                    db.execSQL(sql);
                }
                catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(ProfileActivity.this,ContactsActivity.class);
                startActivity(intent);

            }

        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edAdhaar = (EditText)findViewById(R.id.Aadharnum);
        edName = (EditText)findViewById(R.id.myname);
        edFname = (EditText)findViewById(R.id.fname);
        edDob= (EditText)findViewById(R.id.DOB);
        doornumer = (EditText)findViewById(R.id.doornum);
        Street = (EditText)findViewById(R.id.street);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        pincode = (EditText)findViewById(R.id.pincode);
        male=(RadioButton)findViewById(R.id.Male);
        female=(RadioButton)findViewById(R.id.Female);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonObject");

        if(!jsonString.isEmpty()) {

            try {
                jObj = new JSONObject(jsonString);
                edAdhaar.setText(jObj.getString("uid").toString());
                edName.setText(jObj.getString("name").toString());
                edFname.setText(jObj.getString("gname").toString());
                edDob.setText(jObj.getString("dob").toString());
                doornumer.setText(jObj.getString("house").toString());
                Street.setText(jObj.getString("street").toString());
                city.setText(jObj.getString("dist").toString());
                state.setText(jObj.getString("state").toString());
                pincode.setText(jObj.getString("pc").toString());

                if(jObj.getString("gender").toString().equals("M")){
                    male.setChecked(true);
                    this.setVisible(true);
                }
                else{
                    female.setChecked(true);
                    this.setVisible(true);
                }
            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
