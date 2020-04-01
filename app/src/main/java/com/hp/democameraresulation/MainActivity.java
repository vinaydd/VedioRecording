package com.hp.democameraresulation;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaScannerConnection;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView sampleGLView;
    private CameraRecorder cameraRecorder;
    private  String filepath;
    File  file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seocnd_activity);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  filepath = getVideoFilePath();
                file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/" + System.currentTimeMillis()+"video_record" + ".MP4");
                cameraRecorder.start(file.toString());

            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRecorder.stop();
                scanFile();
            }
        });
    }


    public void scanFile() {
        MediaScannerConnection.scanFile(this, new String[]{filepath}, new String[]{file.getName()}, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sampleGLView = new GLSurfaceView(getApplicationContext());
        FrameLayout frameLayout = findViewById(R.id.wrap_view);
        frameLayout.addView(sampleGLView);

        cameraRecorder = new CameraRecorderBuilder(this, sampleGLView)
                .lensFacing(LensFacing.FRONT)
                .build();
    }


    public static String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4";
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sampleGLView.onPause();
        cameraRecorder.stop();
        cameraRecorder.release();
        cameraRecorder = null;
        ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
        sampleGLView = null;
    }
}
