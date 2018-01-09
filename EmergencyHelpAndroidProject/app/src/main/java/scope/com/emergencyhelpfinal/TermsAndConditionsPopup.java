package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TermsAndConditionsPopup extends Activity {


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tc_popup);
        TextView TCtxt=(TextView)findViewById(R.id.tctxctview);
        TCtxt.setMovementMethod(new ScrollingMovementMethod());

        Button closebtn = (Button)findViewById(R.id.closebutton);

        closebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TermsAndConditionsPopup.this,TermsAndConditionsActivity.class);
                startActivity(intent);

            }

        });

        DisplayMetrics cm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(cm);

        int width = cm.widthPixels;
        int height = cm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.8));
    }
}
