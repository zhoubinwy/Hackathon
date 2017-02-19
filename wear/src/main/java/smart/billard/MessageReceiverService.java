package smart.billard;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;

public class MessageReceiverService extends WearableListenerService {
    private static final String TAG = "SensorDashboard/MessageReceiverService";

    private DeviceClient deviceClient;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"receiver started");

        deviceClient = DeviceClient.getInstance(this);

        //   Toast.makeText(this, "Message receiver service Started\n", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d(TAG,"message received");


        if (messageEvent.getPath().startsWith("StartMeasurement")) {

            String[] parts = messageEvent.getPath().split("/");
            Log.d(TAG, "received start message: ");

//            startService(new Intent(SensorService.START_MONITOR, Uri.EMPTY, this, SensorService.class));
//            startService(new Intent(this, SensorService.class));
            Toast.makeText(this, "Monitoring Started\n", Toast.LENGTH_SHORT).show();

            Intent in = new Intent("SOMEACTION");
            sendBroadcast(in);
        }


        if (messageEvent.getPath().equals("StopMeasurement")) {
//            stopService(new Intent(this, SensorService.class));

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                //Handle exception
                e.printStackTrace();
            }

            Intent in = new Intent("SOMEACTION");
            sendBroadcast(in);


            File file_temp = new File("/sdcard/mag_uncali");
            deviceClient = DeviceClient.getInstance(getApplicationContext());
            //////////////////////////////////////////This parameter determines whether or not to upload data to the cloud/////////////////////
            boolean should_upload_data = true;

            Log.d(TAG,"try to transfer");
            deviceClient.transfer_all_files(file_temp);



        }


    }

}







