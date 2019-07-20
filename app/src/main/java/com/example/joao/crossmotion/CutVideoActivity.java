package com.example.joao.crossmotion;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.deep.videotrimmer.DeepVideoTrimmer;
import com.deep.videotrimmer.interfaces.OnTrimVideoListener;
import com.deep.videotrimmer.view.RangeSeekBarView;
import com.example.joao.crossmotion.databinding.ActivityCutVideoBinding;

import org.w3c.dom.Text;

import java.io.File;

import static com.example.joao.crossmotion.Constants.EXTRA_VIDEO_PATH;


public class CutVideoActivity extends AppCompatActivity implements OnTrimVideoListener {

    ActivityCutVideoBinding mBinder;
    private DeepVideoTrimmer mVideoTrimmer;
    public static String croppedVideoURI;
    TextView textSize,tvCroppingMessage;
    RangeSeekBarView timeLineBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Uri videoUri;
        super.onCreate(savedInstanceState);


        Intent extraIntent = getIntent();
        String videoPath ="";


        if(extraIntent!=null)
        {
            videoPath = extraIntent.getStringExtra(EXTRA_VIDEO_PATH);
            Log.e("VIDEO_PATH:",videoPath);
        }



        mBinder = DataBindingUtil.setContentView(this,R.layout.activity_cut_video);

        mVideoTrimmer = findViewById(R.id.timeLine);
        timeLineBar = findViewById(R.id.timeLineBar);
        textSize = findViewById(R.id.textSize);
        tvCroppingMessage = findViewById(R.id.tvCroppingMessage);


        if(mVideoTrimmer!=null)
        {

            videoUri = Uri.parse(videoPath);

            mVideoTrimmer.setMaxDuration(100);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setVideoURI(videoUri);
            mVideoTrimmer.setDestinationPath(Constants.externalFilesPath);

        }


    }



    @Override
    public void getResult(final Uri uri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCroppingMessage.setVisibility(View.GONE);



            }
        });

        //mVideoTrimmer.setVisibility(View.GONE);
        Intent intent = new Intent( this,SaveVideo.class);
        intent.setData(uri);
        //setResult(RESULT_OK, intent);

        startActivity(intent);
        //croppedVideoURI = uri.toString();






    }

    @Override
    public void cancelAction() {
        mVideoTrimmer.destroy();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCroppingMessage.setVisibility(View.GONE);
            }
        });
        finish();
    }


}







