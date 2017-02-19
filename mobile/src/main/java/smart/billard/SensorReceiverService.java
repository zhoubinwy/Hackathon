package smart.billard;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;


import smart.shared.DataMapKeys;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class SensorReceiverService extends WearableListenerService {
    private static final String TAG = "SensorDashboard/SensorReceiverService";

    private int sequence_num_prev=0;
    private int recieved_count=0;
    private int file_count=0;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "sensor receiver service started");
//        sensorManager = RemoteSensorManager.getInstance(this);
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        Log.d(TAG, "Connected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);

        Log.d(TAG, "Disconnected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged() g");

        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();



                Log.d(TAG,"file received");
                if(path.startsWith("/data/")){


                    String file_name=DataMapItem.fromDataItem(dataItem).getDataMap().getString(DataMapKeys.DATAFILENAME);
                    String folder_name=DataMapItem.fromDataItem(dataItem).getDataMap().getString(DataMapKeys.DATAFOLDERNAME);
//                    File temp_file=new File(file_name);
//                    File folder_creater=new File(folder_name);
                    File storage_media = Environment.getExternalStorageDirectory();
                    File folder_creater = new File(storage_media.getAbsolutePath() + "/mag_uncali/"+folder_name);
                    String folder_creater_string=folder_creater.getAbsolutePath();


//                    recieved_count++;
////                    Intent i = new Intent(DataMapKeys.FILE_RECEIVE_UPDATE);
////                    i.putExtra("receiving_state", Integer.toString(recieved_count)+"/"+ Integer.toString(file_count)+" files are received");
////                    sendBroadcast(i);
//                    Log.d(TAG, recieved_count + "/" + file_count + " is received");


//                    Log.d(TAG,"sound file received ");
                    Asset sound_asset=DataMapItem.fromDataItem(dataItem).getDataMap().getAsset(DataMapKeys.DATAFILE);
//                    Log.d(TAG, "Asset is " + sound_asset.toString());

                    mGoogleApiClient = new GoogleApiClient.Builder(this.getApplicationContext()).addApi(Wearable.API).build();

                    ConnectionResult result =mGoogleApiClient.blockingConnect(4000, TimeUnit.MILLISECONDS);
                    if (!result.isSuccess()) {
                        return;
                    }

                    InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, sound_asset).await().getInputStream();
                    mGoogleApiClient.disconnect();

                    if (assetInputStream == null) {
                        Log.w(TAG, "Requested an unknown Asset.");
                        return;
                    }



                    try {

                        if(!folder_creater.exists()){
                            folder_creater.mkdirs();
                        }
                        Log.d(TAG, "file name : " + folder_creater_string + file_name);
                        File myFile = new File(folder_creater_string+"/"+file_name);
                        if(myFile.isFile()){
                            return;
                        }else{
                            String path_sound=folder_creater_string+"/"+file_name;
                            FileOutputStream output = new FileOutputStream(path_sound);
                            int bufferSize = 1024;
                            byte[] buffer = new byte[bufferSize];
                            int len = 0;
                            while ((len = assetInputStream.read(buffer)) != -1) {
                                output.write(buffer, 0, len);
                            }


                        }





                    } catch (Exception e) {
                        e.printStackTrace();
//                        System.exit(0);
                    }

                }
            }
        }
    }




}
