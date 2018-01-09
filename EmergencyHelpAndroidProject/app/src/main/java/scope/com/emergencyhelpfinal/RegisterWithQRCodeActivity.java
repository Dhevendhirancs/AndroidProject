package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
/**
 * Created by Administrator on 3/26/2017.
 */

public class RegisterWithQRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_qrcode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button btn_scan=(Button)findViewById(R.id.btn_sacn);

        final Activity activity=this;

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplication(),"Scan Your Aadhar QR code here",Toast.LENGTH_LONG).show();
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        Button btn_skip = (Button)findViewById(R.id.btn_skip);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterWithQRCodeActivity.this,ProfileActivity.class);
                intent.putExtra("jsonObject","");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result!=null)
            if(result.getContents()==null){
                Toast.makeText(this,"You cancelled the scanning",Toast.LENGTH_LONG).show();
            }
            else{
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterWithQRCodeActivity.this,ProfileActivity.class);
                intent.putExtra("jsonObject", ParseXml(result.getContents()).toString());
                startActivity(intent);
            }
        else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public JSONObject ParseXml(String xmlString){
        JSONObject jsonObj = null;
        JSONObject json_data = null;
        try {
            jsonObj = XML.toJSONObject(xmlString);
            json_data = jsonObj.getJSONObject("PrintLetterBarcodeData");
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }
        return json_data;
    }
}
