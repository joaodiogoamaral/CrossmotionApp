package com.example.joao.crossmotion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deep.videotrimmer.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class SelectFilesActivity extends AppCompatActivity {


    private static String TAG = "SELECT_FILES_ACTIVITY";
    private final int LOGIN_STATUS = 666;

    private static String[] mFileList;
    private List<FileItem> mFileItemList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_files);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.fileList);
        TextView message = findViewById(R.id.selectVideoMsg);
        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setText(R.string.upload_videos);


        mFileList = getListFiles(Constants.externalFilesPath).toArray(new String[0]);



            mFileItemList = new ArrayList<FileItem>();

            if (mFileList != null ) {
                for (int i = 0; i < mFileList.length; i++) {

                    FileItem fI = new FileItem(mFileList[i]);
                    mFileItemList.add(fI);
                }


                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)


                mAdapter = new FileItemAdapter(mFileItemList);

                recyclerView.setAdapter(mAdapter);
            } else {
                message.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                message.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                message.setText("No videos to upload!!!");
                message.setVisibility(View.VISIBLE);

                uploadButton.setVisibility(View.INVISIBLE);
                uploadButton.setText("Back");
            }
        }








    public void backToMainMenu(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    public void uploadVideos(View view)
    {
        List<FileItem> fList = ((FileItemAdapter)mAdapter).getmDataSet();

        List<String> selectedFiles = new ArrayList<String>();

        for(int i = 0; i< fList.size();i++)
        {
            FileItem fI = fList.get(i);

            if(fI.getSelected()==true)
            {
                selectedFiles.add(fI.getFileName());
            }else
            {
                Log.e(TAG,"NOT Selected         "+fI.getFileName());

            }
        }





        Intent checkPassIntent = new Intent(this,LoginActivity.class);
        startActivityForResult(checkPassIntent, LOGIN_STATUS);

        doUpload(selectedFiles);
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }


    public void doUpload(List<String> fileNames)
    {
        for(int i=0;i<fileNames.size();i++)
        {
            UploadVideo(fileNames.get(i));
        }
    }


    public void UploadVideo(final String selectedPath)
    {




        class VideoUp extends AsyncTask<String,Void,String>
        {
            @Override
            protected String doInBackground(String...params)
            {
                upLoad2Server(selectedPath);
                Log.e("UPLOAD: ","Uploading");
                return "Doing in background";



            }

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);
                Log.e("UPLOAD: ","Upload Done");
            }


            public int upLoad2Server(String videoName) {


                String upLoadServerUri = Constants.URLUpload; //Check which django server ip address



                Log.e(TAG,"Uploading"+videoName);

                String fileName = videoName;
                int serverResponseCode = -1;
                HttpsURLConnection conn = null;
                DataOutputStream dos = null;
                DataInputStream inStream = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 100 * 1024 * 1024;
                String responseFromServer = "";

                File sourceFile = new File(Constants.externalFilesPath,videoName);
                if (!sourceFile.isFile()) {
                    Log.e("Huzza", "Source File Does not exist"+fileName);
                    return 0;
                }
                try { // open a URL connection to the Server

                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri);


                    conn = (HttpsURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                    conn.setHostnameVerifier(new HostnameVerifier() {
                             @Override
                             public boolean verify(String hostname, SSLSession session) {
                                 return hostname.equals(Constants.serverIP);
                             }
                         }

                    );
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);


            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
//            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
            Log.e("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)

            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("Upload file to server", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            // close streams
            Log.i("Upload file to server", fileName + " File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//this block will give the response of upload link


        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                Log.i("Huzza", "RES Message: " + line);
            }
            rd.close();
        } catch (IOException ioex) {
            Log.e("Huzza", "error: " + ioex.getMessage(), ioex);
        }

                Log.e("HTTP_RESPONSE:",Integer.toString( serverResponseCode));
                return serverResponseCode;  // like 200 (Ok)


            } // end upLoad2Server
        }

        VideoUp vu=new VideoUp();
        vu.execute(selectedPath);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //Handle login result
        if(requestCode == LOGIN_STATUS && resultCode == RESULT_OK)
        {

            Log.e(TAG,"Successfully Login!!!");
        }
    }



    public List<String>  getListFiles(String directoryName) {
        File directory = new File(directoryName);

        // get all the files from a directory

        File[] fList = directory.listFiles();
        ArrayList<String> files = new ArrayList<String>();

        for (int i = 0; i < fList.length ; i++) {
            if (fList[i].isFile()) {

                files.add(fList[i].getName());
                //Log.e(TAG,"ADDED:"+fList[i].getName());
            }
            Log.e(TAG,fList[i].getAbsolutePath());

        }

        return files;
    }
}
