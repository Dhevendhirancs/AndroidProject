package scope.com.emergencyhelpfinal;

import android.content.pm.ActivityInfo;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by Administrator on 3/25/2017.
 */

public class CameraFragment extends Fragment implements View.OnClickListener,SurfaceHolder.Callback {

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;

    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile("/sdcard/videocapture_example.mp4");
        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            getActivity().finish();
        } catch (IOException e) {
            e.printStackTrace();
            getActivity().finish();
        }
    }

    public void onClick(View v) {
        if (recording) {
            recorder.stop();
            recording = false;

            initRecorder();
            prepareRecorder();
        } else {
            recording = true;
            recorder.start();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.camera_activity, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // setContentView(R.layout.activity_terms);

        recorder = new MediaRecorder();
        initRecorder();

        SurfaceView cameraView = (SurfaceView) getActivity().findViewById(R.id.surfaceView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);

        super.onCreate(savedInstanceState);

    }


}
