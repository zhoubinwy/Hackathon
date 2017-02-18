package smart.billard;

import android.util.Log;

/**
 * Created by harry on 2/17/17.
 */
public class RemoteController {

    String TAG="REMOTECONTROLLER";

    public void startCollection(){
        Log.d(TAG,"started pushed");
    }


    public void stopCollection(){
        Log.d(TAG,"stopped pushed");
    }
}
