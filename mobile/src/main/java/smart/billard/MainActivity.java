package smart.billard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    RemoteController remoteController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteController=new RemoteController();
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
