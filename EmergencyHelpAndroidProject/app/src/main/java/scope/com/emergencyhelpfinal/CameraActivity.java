package scope.com.emergencyhelpfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Administrator on 3/25/2017.
 */

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, Camera2VideoFragment.newInstance())
                    .commit();
        }
    }

}

