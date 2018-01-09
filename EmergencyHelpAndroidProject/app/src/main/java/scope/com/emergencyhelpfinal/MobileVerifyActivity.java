package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MobileVerifyActivity extends Activity {

    String TAG="E-HELP";
    String otp;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button submit = (Button) findViewById(R.id.BtnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mobile_no = (EditText) findViewById(R.id.mobileNum);

                String number = mobile_no.getText().toString();

                if (number.length() != 0){

                    if (number.length() == 10) {


                        SQLiteDatabase db;
                        db = openOrCreateDatabase( "Temp.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                        if(db.isOpen()){
                            Log.d("TEST","OPEN");
                        }
                        else{
                            Log.d("TEST","CLOSE");
                        }
                        try {
                            db.delete("tbl_user", null, null);

                            String sql =
                                    "INSERT or replace INTO tbl_user (MOBILE) VALUES('"+number+"')" ;
                            db.execSQL(sql);
                            db.close();
                        }
                        catch (Exception e) {
                            Toast.makeText(MobileVerifyActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                        }

                         otp = ""+((int)(Math.random()*9000)+1000);
                        String url="http://buysms.net/api/sendtrnsms.php?loginid=Dhiraj&password=$m$dgct17&sender_id=DHIRAJ&to="+number+"&message=Your OTP code is:"+otp;


                        post(url, "", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG,"Error");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String responseStr = response.body().string();
                                    Intent intent = new Intent(MobileVerifyActivity.this, OtpVerifyActivity.class);
                                    intent.putExtra("OtpString",otp.toString());
                                    startActivity(intent);

                                } else {
                                    Log.d(TAG,"Error");
                                }
                            }
                        });



/*                        String url_to_save="http://buytheway.org.in/rest/api/register/save/";
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("Mobile",number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String str = obj.toString();
                        post(url_to_save,str, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG,"Error");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String responseStr = response.body().string();


                                } else {
                                    Log.d(TAG,"Error");
                                }
                            }
                        });*/



                    } else {
                        mobile_no.setError("Invalid Number");
                    }
                }
                else
                {
                    mobile_no.setError("Enter Mobile Number");
                }

            }
        });
    }
}
