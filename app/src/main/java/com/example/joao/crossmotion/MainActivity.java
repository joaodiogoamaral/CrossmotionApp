package com.example.joao.crossmotion;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.databinding.DataBinderMapper;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.security.NetworkSecurityPolicy;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.deep.videotrimmer.utils.FileUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_MESSAGE = "com.example.crossmotion.MESSAGE";
    private static String TAG = "MainActivity:";
    public static final int PERMISSION_STORAGE = 100;
    private static TextView errorMsg;
    private boolean isLoggedIn=false;
    //variables
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private final int REQUEST_VIDEO_TRIMMER = 0x12;
    private final int REQUEST_VIDEO_TRIMMER_RESULT = 342;
    private final int LOGIN_STATUS = 666;

    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_PASSWORD = "PASS";



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorMsg = findViewById(R.id.PermissionMsg);
        checkForPermission();






        Constants.externalFilesPath = Environment.getExternalStorageDirectory().toString()+ File.separator + "Crossmotion/";
        File baseDir = new File(Constants.externalFilesPath);

        if(baseDir==null)
        {
            Log.e("MAIN ACTIVITY","Dir null file");
        }

        if(baseDir.isDirectory())
        {
            Log.e("MAIN ACTIVITY", "Is DIR");
        }
        if(!baseDir.exists()) {
            if (!baseDir.mkdirs()) {
                Log.e("MAIN ACTIVITY", "Dir not created");

            } else {
                Log.e("MAIN ACTIVITY", "Dir created");

            }
        }else
        {
            Log.e("MAIN ACTIVITY", "Dir already exists");
        }



        //Check for user login
        if(!isLoggedIn) {
            Intent checkPassIntent = new Intent(this,LoginActivity.class);
            startActivityForResult(checkPassIntent, LOGIN_STATUS);
            isLoggedIn=true;
        }
    }

    public void selectMovement(View view)
    {

        openCamera();

    }

    public void openCamera()
    {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);



        if (takeVideoIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {

        if ( requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK )
        {

            Uri videoUri = intent.getData();
            Intent newIntent = new Intent(this,CutVideoActivity.class);
            newIntent.putExtra(EXTRA_VIDEO_PATH,FileUtils.getPath(this,videoUri));

            if(videoUri!=null)
            {
                startActivityForResult(newIntent,REQUEST_VIDEO_TRIMMER_RESULT);
            }


        }

        //Handle login result
        if(requestCode == LOGIN_STATUS && resultCode == RESULT_OK)
        {
            isLoggedIn = true;
            Log.e(TAG,"Successfully Login!!!");
        }
    }
    public void startSelectVideosActivity(View view)
    {
        Intent intent = new Intent(this,SelectFilesActivity.class);
        startActivity(intent);
    }

    private void checkForPermission() {
        requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_STORAGE, new BaseActivity.setPermissionListener() {
                    @Override
                    public void onPermissionGranted(int requestCode){
                    }

                    @Override
                    public void onPermissionDenied(int requestCode) {

                        errorMsg.setVisibility(View.VISIBLE);
                        checkForPermission();

                    }

                    @Override
                    public void onPermissionNeverAsk(int requestCode) {
                        showPermissionSettingDialog(getString(R.string.permission_gallery_camera));
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}
