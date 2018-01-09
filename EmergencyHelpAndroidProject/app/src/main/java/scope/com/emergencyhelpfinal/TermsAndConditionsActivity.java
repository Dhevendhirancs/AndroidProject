
package scope.com.emergencyhelpfinal;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


public class TermsAndConditionsActivity extends AppCompatActivity {

    private Menu menu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_terms);

        final TextView tvbutton=(TextView)findViewById(R.id.textviewbutton);

        tvbutton.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.activity_tc_popup, null);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(width-50);
                popupWindow.setHeight(height-100);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(popupView,Gravity.CENTER, 0, 0);
                Button btnDismiss = (Button) popupView.findViewById(R.id.closebutton);

                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        popupWindow.dismiss();

                    }
                });
                popupWindow.showAsDropDown(tvbutton, 30,-39 );
            }
        });

        Button btn_continue = (Button) findViewById(R.id.buttoncontiniue);

        btn_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent MYintent = new Intent(TermsAndConditionsActivity.this,MobileVerifyActivity.class);
                startActivity(MYintent);

            }

        });

    }

}


