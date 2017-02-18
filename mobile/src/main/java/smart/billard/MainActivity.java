package smart.billard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String TAG="MainActivity";
    RemoteController remoteController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteController=new RemoteController();

        Log.d(TAG, "Start the day!");
    }

    boolean is_remote_on=false;

    public void onSwitch(View v){
        if(!is_remote_on){
            remoteController.startCollection();
            is_remote_on=true;
        }
        else{
            remoteController.stopCollection();
            is_remote_on=false;
        }
    }


}
