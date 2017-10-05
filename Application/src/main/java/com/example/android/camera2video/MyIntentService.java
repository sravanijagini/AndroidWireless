package com.example.android.camera2video;

import android.app.IntentService;
import android.content.Intent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.android.camera2video.action.FOO";
    private static final String ACTION_BAZ = "com.example.android.camera2video.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.android.camera2video.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.android.camera2video.extra.PARAM2";
    private static String mNextAbsolutePath1;
    private static String upLoadServerUri ="http://172.16.124.171/UploadToServer.php";
    private static int serverResponseCode= 0;

//    int serverResponseCode = 0;




    public MyIntentService() {
        super("MyIntentService");
    }

    public MyIntentService(String mNextVideoAbsolutePath) {
        super(mNextVideoAbsolutePath);
        mNextAbsolutePath1 = mNextVideoAbsolutePath;
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MyIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MyIntentService.class);
//        intent.setAction(ACTION_BAZ);
////        intent.putExtra(EXTRA_PARAM1, param1);
////        intent.putExtra(EXTRA_PARAM2, param2);
//      //  Log.e(TAG, "startActionBaz: working");
//
//
//       context.startService(intent);
//
//
//
//
//    }

    @Override
    protected void onHandleIntent(Intent intent) {


        //Log.i(TAG, "onHandleIntent: working" );
        if (intent != null && intent.getAction() != null) {
            method(null, null);
        }
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//               // final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//             //   final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(null, null);
//            }
//         else {
////            startActionBaz(this,null,null);
////        }
//
//
//
//    }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
//    private void handleActionFoo(String param1, String param2) {
//        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void method(String param1, String param2) {
        String fileName = mNextAbsolutePath1;
        String sourceFileUri = mNextAbsolutePath1;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if(sourceFile == null){
          //  Log.e(TAG, "onHandleIntent: no sourcefile found" );
        }

        if (!sourceFile.isFile()) {
//            Log.e("uploadFile", "Source File not exist :");

        }
        else
        {
            FileInputStream fileInputStream = null;
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

//                Log.e("uploadFile", "HTTP Response is : "
  //                      + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

    //                Log.i("uploadFile", "File Upload Completed with code " + serverResponseCode +
      //                      ".\n\n See uploaded file here : \n\n"
         //                   +" /home/sravani/uploads"

           //         );


                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
        //        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
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

            //  return serverResponseCode;


        } // End else block




    }
}
