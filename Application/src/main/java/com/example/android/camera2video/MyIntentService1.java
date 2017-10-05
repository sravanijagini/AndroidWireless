package com.example.android.camera2video;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService1 extends IntentService {

    private static final String TAG = "ok";

    public MyIntentService1() {
        super("MyIntentService1");
    }




    public MyIntentService1(String mNextVideoAbsolutePath) {
        super(mNextVideoAbsolutePath);

      //  mNextVideoAbsolutePath1 = mNextVideoAbsolutePath;
       // System.out.println(mNextVideoAbsolutePath1+"yaaasss");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //int upload = intent.getIntExtra("upload",1);
       // Log.i(TAG,"atlast");
      //  mNextVideoAbsolutePath1 = intent.getStringExtra("path");
        String mNextVideoAbsolutePath1 = intent.getStringExtra("path");

       // System.out.println(mNextVideoAbsolutePath1+"nooooo");
        try {
            uploadFile(mNextVideoAbsolutePath1);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    int serverResponseCode = 0;
    String upLoadServerUri ="http://172.16.124.249/UploadToServer.php";


    public int uploadFile(String sourceFileUri) throws IOException {

       // String sourceFileUri= mNextVideoAbsolutePath1;
        String mNextVideoAbsolutePath1 = sourceFileUri;
       // System.out.println(mNextVideoAbsolutePath1);

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


            return 0;

        }
        else
        {
           // FileInputStream fileInputStream = null;
            // ParcelFileDescriptor fileDescriptor = null;
            try (FileInputStream fileInputStream = new FileInputStream(sourceFile)){

                // open a URL connection to the Servlet

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
            }finally {
                try {
                    dos.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            try {
                sourceFile.delete();
                //if(null != fileInputStream ){
                //    fileInputStream.close();
                //    fileInputStream = null;
                //}
                if(null != conn ){
                    conn.disconnect();
                    conn = null;
                }
            } catch (Exception ex) { /* ignore this */ }
            finally {
                try {
                    dos.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return serverResponseCode;


        } // End else block
    }



}
