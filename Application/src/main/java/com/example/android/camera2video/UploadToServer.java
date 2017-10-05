package com.example.android.camera2video;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadToServer extends Activity {

    int serverResponseCode = 0;
    String upLoadServerUri ="http://172.16.124.171/UploadToServer.php";
    // guest 172.16.82.130
    //lan 172.16.124.171

    /**********  File Path *************/
    String mNextVideoAbsolutePath1;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera2_video);
        context = this;

    }
    public UploadToServer(final String mNextVideoAbsolutePath) {
        mNextVideoAbsolutePath1 = mNextVideoAbsolutePath;

        new Uploading().doInBackground(mNextVideoAbsolutePath1);

    }




//    private class Uploading extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            try {
//                uploadFile(mNextVideoAbsolutePath1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    private class Uploading extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... voids) {
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("4446");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            System.out.println("uploading started....");
                        }
                    });

                    try {
                        uploadFile(mNextVideoAbsolutePath1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            return null;
        }
    }


    public int uploadFile(String sourceFileUri) throws IOException {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {


            Log.e("uploadFile", "Source File not exist :"
                    +mNextVideoAbsolutePath1);

            runOnUiThread(new Runnable() {
                public void run() {
                    System.out.println("999");
                }
            });

            return 0;

        }
        else
        {
            FileInputStream fileInputStream = null;
           // ParcelFileDescriptor fileDescriptor = null;
            try {

                // open a URL connection to the Servlet
                fileInputStream = new FileInputStream(sourceFile);
                //

               // fileDescriptor.getFileDescriptor();

                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
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
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

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

                Log.e("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

//                    runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
//                                    +" /home/sravani/uploads"
//                                    +mNextVideoAbsolutePath1;
//
//                            System.out.println(msg);
//                        }
//                    });
                    Log.i("uploadFile", "File Upload Completed with code " + serverResponseCode +
                            ".\n\n See uploaded file here : \n\n"
                            +" /home/sravani/uploads"
                            +mNextVideoAbsolutePath1);


                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sourceFile.delete();
                if(null != fileInputStream ){
                    fileInputStream.close();
                    fileInputStream = null;
                }
                if(null != conn ){
                    conn.disconnect();
                    conn = null;
                }
            } catch (Exception ex) { /* ignore this */ }
            finally {

            }
            return serverResponseCode;


        } // End else block
    }
}