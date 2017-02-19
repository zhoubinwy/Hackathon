package smart.billard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {


    String TAG="wearable/mainActivity";
    SensorManager sensorManager;
    //	Sensor mag_sensor;
    Sensor acc_uncalib_sensor;
    Sensor game_uncalib_sensor;
    Sensor grav_uncalib_sensor;

    File acc_file;
    File game_file;
    File grav_file;

    public String folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("wearable", "oncreated");

        startService(new Intent(this, MessageReceiverService.class));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        acc_uncalib_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        game_uncalib_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        grav_uncalib_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);



        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.VIBRATE"};

        int permsRequestCode = 200;

        requestPermissions(perms, permsRequestCode);

        IntentFilter filter = new IntentFilter("SOMEACTION");

        SwitchReceiver broadcast_receiver=new SwitchReceiver();
        this.registerReceiver(broadcast_receiver, filter);

    }

    boolean is_collecting=false;

    class SwitchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            // TODO Auto-generated method stub
            Log.d(TAG,"intent received");
            switch_once();
        }
    }

    public void switch_once(){


        if(!is_collect){


            String baseDir= Environment.getExternalStorageDirectory().getAbsolutePath();
            Time now = new Time();
            now.setToNow();
            folder_name=baseDir+"/mag_uncali/";

            long[] pattern = {0, 1000};
            Log.d(TAG,"event triggered");
            run_vibration(pattern);


            sensorManager.unregisterListener(acc_uncali_Listener);
            sensorManager.unregisterListener(game_uncali_Listener);
            sensorManager.unregisterListener(grav_uncali_Listener);
            if(acc_file!=null && acc_file.exists()){
                acc_file=null;
            }
            if(game_file!=null && game_file.exists()){
                game_file=null;
            }
            if(grav_file!=null && grav_file.exists()){
                grav_file=null;
            }

            acc_file=new File(folder_name+"acc"+".csv");
            if(acc_file.exists()){
                acc_file.delete();
            }
            acc_file=new File(folder_name+"acc"+".csv");
            grav_file=new File(folder_name+"grav"+".csv");
            if(grav_file.exists()){
                grav_file.delete();
            }
            grav_file=new File(folder_name+"grav"+".csv");
            game_file=new File(folder_name+"game"+".csv");

            if(game_file.exists()){
                game_file.delete();
            }
            game_file=new File(folder_name+"game"+".csv");

            sensorManager.registerListener(acc_uncali_Listener, acc_uncalib_sensor,
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(game_uncali_Listener, game_uncalib_sensor,
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(grav_uncali_Listener, grav_uncalib_sensor,
                    SensorManager.SENSOR_DELAY_GAME);

            is_collect=true;

        }
        else{
            sensorManager.unregisterListener(acc_uncali_Listener);
            sensorManager.unregisterListener(game_uncali_Listener);
            sensorManager.unregisterListener(grav_uncali_Listener);
            if(acc_file!=null && acc_file.exists()){
                acc_file=null;
            }
            if(game_file!=null && game_file.exists()){
                game_file=null;
            }
            if(grav_file!=null && grav_file.exists()){
                grav_file=null;
            }

            is_collect=false;
        }


    }

    private void run_vibration(long[] pattern){
        Log.d(TAG,"try to vibrate");
        Vibrator vibrator= (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        if(vibrator.hasVibrator()){
            vibrator.vibrate(pattern,-1);
        }
        else{
            Log.d(TAG, "Do not have vibration service ");
        }
    }



    SensorEventListener acc_uncali_Listener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            write_file(acc_file,event);
        }
    };
    SensorEventListener grav_uncali_Listener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            write_file(grav_file,event);
        }
    };
    SensorEventListener game_uncali_Listener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            write_file(game_file,event);
        }
    };

    private void write_file(File f,SensorEvent event){
        long time=event.timestamp;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];


        StringBuilder content=new StringBuilder();
        content.append(time);
        content.append(",");

        content.append(x);
        content.append(",");

        content.append(y);
        content.append(",");

        content.append(z);
        content.append("\n");

        try {
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(content.toString());
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    boolean is_collect=false;
}
