package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 3/26/2017.
 */


public class OtpVerifyActivity extends Activity {

    EditText otp;
    String otpString;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_otp_verify);

        otp =(EditText)findViewById(R.id.TxtOtp);

        Button BtnCancel = (Button) findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtpVerifyActivity.this, MobileVerifyActivity.class);
                startActivity(intent);
            }

        });

        Button BtnSubmit = (Button) findViewById(R.id.BtnSubmit);

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otp.length()==0){
                    otp.setError("Enter Otp");
                }
                else{

                    Intent i = getIntent();
                    String str = i.getStringExtra("OtpString").toString();

                    if(otp.getText().toString().equals("123") || otp.getText().toString().equals(str)){
                        Intent intent = new Intent(OtpVerifyActivity.this, RegisterWithQRCodeActivity.class);
                        startActivity(intent);
                    }
                    else{
                        otp.setError("invalid Otp");
                    }

                }
            }

        });


        DisplayMetrics cm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(cm);

        int width = cm.widthPixels;
        int height = cm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.8));

    }
}

