package com.example.joao.crossmotion;

/**
 * Created by Deep Patel
 * (Sr. Android Developer)
 * on 6/4/2018
 */
public class Constants {

    private static String serverURL = "https://192.168.0.101:8000/";
    public static String URLUpload = serverURL+"upload/upload_video/";
    public static String URLCheckUsers = serverURL + "upload/uploadlogin/";

    public static String serverIP = "192.168.0.101";

    public static volatile boolean loggedIn=false;

    //public static String URLUpload = "https://www.google.com";

    public static String externalFilesPath;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    public static String croppedVideoURI;

    public static String userName;
    public static String password;
}
