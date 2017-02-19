package smart.billard;


import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.SparseLongArray;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import smart.shared.DataMapKeys;


public class DeviceClient {
    private static final String TAG = "DeviceClient";
    private static final int CLIENT_CONNECTION_TIMEOUT = 15000;

    private boolean is_filter_on=true;

    public static DeviceClient instance;



    public static DeviceClient getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceClient(context.getApplicationContext());
        }

        return instance;
    }

    public static void destroy(){
        instance=null;
    }

    private Context context;
    private GoogleApiClient googleApiClient;
    private ExecutorService executorService;
    private int filterId=1;

    private int sequenceNum=0;

    private SparseLongArray lastSensorData;

    private DeviceClient(Context context) {
        this.context = context;

        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();

        executorService = Executors.newCachedThreadPool();
        lastSensorData = new SparseLongArray();

    }








    public void transfer_all_files (File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    transfer_all_files(file);
                } else {
                    // do something here with the file
//                    Log.d(TAG,file.getAbsolutePath()+" has length: "+file.length()/1024);

                    if((file.length()/1024)>10000){
                        file.delete();
                        Log.d(TAG, file.getAbsolutePath() + " is deleted ");
                        continue;
                    }
                    StringBuilder file_text= new StringBuilder();
                    String file_name=file.getName();

                    String folder_name=file.getParentFile().getName();
                    String folder_name_up=file.getParentFile().getParentFile().getName();
//                    file_text.append(file.getAbsolutePath()+"\n");
                    try {

                        if(file_name.contains(".csv")){

                            ParcelFileDescriptor pfd= ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                            Asset data_asset=Asset.createFromFd(pfd);

                            Log.d(TAG, "sending file " + file_name);
                            PutDataMapRequest dataMap = PutDataMapRequest.create("/data/");
                            dataMap.getDataMap().putAsset(DataMapKeys.DATAFILE, data_asset);
                            dataMap.getDataMap().putString(DataMapKeys.DATAFILENAME, file_name);
                            dataMap.getDataMap().putString(DataMapKeys.DATAFOLDERNAME, folder_name_up + "/" + folder_name);
//                            dataMap.getDataMap().putLong("timestemp",System.currentTimeMillis());

                            PutDataRequest putDataRequest = dataMap.asPutDataRequest();
//                            send(putDataRequest,file_name,file);
                            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                                    .putDataItem(googleApiClient, putDataRequest);

                            DataApi.DataItemResult result = pendingResult.await(2000, TimeUnit.MILLISECONDS);
                            if(result.getStatus().isSuccess()) {
                                Log.d(TAG, "Data item set: " + result.getDataItem().getUri());

                            }
                            else if(result.getStatus().isCanceled()){
                                Log.d(TAG, "Data item canceled: " + result.getDataItem().getUri());
                            }
                            else if(result.getStatus().isInterrupted()){
                                Log.d(TAG, "Data item interrupted: " + result.getDataItem().getUri());
                            }



                            System.gc();
                            dataMap.getDataMap().clear();

                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public int file_count=0;
    public void count_file_number (File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if(files==null){
                return;
            }
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    count_file_number(file);
                } else {
                    String file_name=file.getName();
                    if(file_name.contains(".pcm")||file_name.contains(".csv")){
                       file_count++;
                    }
                }
            }
        }
    }
    private int get_file_count(){
        return file_count;
    }


    public void delete_file_with_name (File dir,String filename_delete) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    delete_file_with_name(file, filename_delete);
                } else {
                    if(file.getAbsolutePath().contains(filename_delete)) {
                            file.delete();

                        Log.d(TAG, file.getAbsolutePath() + " is deleted ");
                    }
                }
            }
        }
    }


    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath +"/"+ inputFile);
            out = new FileOutputStream(outputPath +"/"+ inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file

            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


//    private void sendMessageInBackground(String path,String message) {
//
//        PutDataMapRequest dataMap = PutDataMapRequest.create("/message/");
//        dataMap.getDataMap().putString(DataMapKeys.IDLENESS,message);
//
//        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
//        send(putDataRequest);
//    }



    private void sendSensorDataInBackground(int sensorType, int accuracy, long timestamp,  float[] values) {
        if (sensorType == filterId) {
            Log.v(TAG, "Sensor " + sensorType + " = " + Arrays.toString(values));
        } else {
            Log.v(TAG, "Sensor " + sensorType + " = " + Arrays.toString(values));
        }


        PutDataMapRequest dataMap = PutDataMapRequest.create("/sensors/" + sensorType);


     //   dataMap.getDataMap().putInt(DataMapKeys.ACCURACY, accuracy);
        dataMap.getDataMap().putLong(DataMapKeys.TIMESTAMP, timestamp);
        dataMap.getDataMap().putFloatArray(DataMapKeys.VALUES, values);
//        dataMap.getDataMap().putInt(DataMapKeys.SEQUENCE_NUMBER, sequenceNum++);
//
//
//
//
//        long data_size=4+8+4*4+4+4;
//
//        data_size=data_size+16;
//        if(sensorType==4){
//            data_size=data_size+4;
//        }
//
//
//        float data_rate=data_size*8000/timeAgo;
  //      Log.v(TAG, "data size "+Long.toString(data_size)+" interval "+Long.toString(timeAgo)+" data_rate "+ Double.toString(data_rate));
//        long data_size=size_calculate.sizeOf(dataMap);
//
//        long data_size=org.jh.Sizer.sizeof(dataMap);





    //    dataMap.getDataMap().putFloat(DataMapKeys.Data_RATE, data_rate);


        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
        send(putDataRequest);
    }


    public void sendFileCount() {
        final int file_count=get_file_count();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendFileCountinBackground(file_count);
            }
        });
    }
    private void sendFileCountinBackground(int file_number) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/file_count/" );
        dataMap.getDataMap().putInt(DataMapKeys.FILE_COUNT,file_number );
        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
        send(putDataRequest);
    }

    public void sendDetection(final int[] gestureID) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendDetectioninBackground(gestureID);
            }
        });
    }
    private void sendDetectioninBackground(int[] gestureID) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/detection/" );
        dataMap.getDataMap().putInt(DataMapKeys.DETECTION, gestureID[0]);
        dataMap.getDataMap().putInt(DataMapKeys.CORRECTNESS, gestureID[1]);
        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
        send(putDataRequest);
    }






    private boolean validateConnection() {
        if (googleApiClient.isConnected()) {
            return true;
        }

        ConnectionResult result = googleApiClient.blockingConnect(CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }

    private boolean send_result;


    private boolean send(PutDataRequest putDataRequest) {
        send_result=false;
        if (validateConnection()) {
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {

            Log.d(TAG, "send is success?: " + dataItemResult.getStatus().isSuccess());
                }
            });

        }

        else{
            Log.v(TAG, "connection error ");
        }

        return send_result;
    }

    private void send(final PutDataRequest putDataRequest, final String filename, final File file) {
        if (validateConnection()) {
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {


                    if(dataItemResult.getStatus().isSuccess()){
                        Log.d(TAG, filename + " transmitted");

                    }
                    else {
                        Log.d(TAG, filename + " transmission failed");
                    }
                }
            });

        }

        else{
            Log.v(TAG, "connection error ");
        }

    }


    private void on_data_transfer_complete(){

    }

    public void set_filter_switch(boolean input_value){
        is_filter_on=input_value;
    }

//    public void create_recorder(String file_name){
//        if(watch_data_record==null){
//            String timeStamp = new SimpleDateFormat("MMdd_HHmm").format(Calendar.getInstance().getTime());
//            watch_data_record=new DataRecord(file_name+timeStamp);
//
//        }
//    }
}
