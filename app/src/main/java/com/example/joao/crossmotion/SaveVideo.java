package com.example.joao.crossmotion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class SaveVideo extends AppCompatActivity {

    //static Context mCtx;
    static Uri mVideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mVideoUri = getIntent().getData();

        //mCtx = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_video);
    }


    public void onVideoSaveClicked(View view)
    {
        EditText saveVideoPrompt = findViewById(R.id.saveVideoPrompt);
        TextView errorMsg = findViewById(R.id.errorMsg);
        String videoName = saveVideoPrompt.getText().toString();

        if(checkNameOk(videoName))
        {
            saveVideoPrompt.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            if(errorMsg.getVisibility() == View.VISIBLE)
            {
                errorMsg.setVisibility(View.INVISIBLE);
            }



            boolean result = saveVideo(videoName);

            //errorMsg.setVisibility(View.VISIBLE);
            if(result)
            {
                Intent intent = new Intent(this, SelectFilesActivity.class);
                startActivity(intent);
            }else
            {
                Log.e("rename","false!");
                finish();
            }



        }else
        {

            errorMsg.setVisibility(View.VISIBLE);
        }



    }


    public void backToMainMenu(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }


    private boolean saveVideo(String videoName)
    {

        boolean result = false;
        String sourceUri = mVideoUri.getPath();

        File newFile = new File(Constants.externalFilesPath,videoName);

        File origFile = new File(sourceUri);
        if(!origFile.exists())
        {

            Log.e("MSG:","OrigFile does not exist!!!!");
        }


        if(origFile.exists()) {
            Log.e("MSG:","File exists!!! --" + sourceUri);

//            try {
//                InputStream inStream = new FileInputStream(origFile);
//                OutputStream outStream = new FileOutputStream(newFile);
//
//                byte[] buffer = new byte[1024];
//
//                int length;
//
//
//                while ((length = inStream.read(buffer))>0)
//                {
//                    outStream.write(buffer,0,length);
//                }
//
//                inStream.close();
//                outStream.close();
//                origFile.delete();
            origFile.renameTo(newFile);
            Log.e("SIZE:", Long.toString(newFile.length()));
            return true;

//            } catch (FileNotFoundException e)
//            {
//                e.printStackTrace();
//            }
        }

        return result;
    }

    private boolean checkNameOk(String name)
    {
        // Context context = getApplicationContext();

        File video = new File(Constants.externalFilesPath,name);


        //if the file does not exist, it can have this name!
        return video == null || !video.exists();
    }
}
