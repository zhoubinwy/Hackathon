package smart.billard;

import android.app.Fragment;
import android.app.FragmentManager;


import android.app.FragmentTransaction;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements ControlFragment.OnFragmentInteractionListener ,ResultFragment.OnResultFragmentListener{

    String TAG="MainActivity";
    RemoteController remoteController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);

        remoteController=new RemoteController();

        Log.d(TAG, "Start the day!");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ControlFragment fragment=new ControlFragment();
        fragmentTransaction.add(R.id.main_container,(Fragment)fragment);


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

    public void show_analyze_fragment(View v){
        ResultFragment newFragment = new ResultFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_container, (Fragment)newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void show_control_fragment(View v){
        ControlFragment newFragment = new ControlFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onControlSelected(String command) {
        if(!is_remote_on){
            remoteController.startCollection();
            is_remote_on=true;
        }
        else{
            remoteController.stopCollection();
            is_remote_on=false;
        }
    }

    public void onSwitchFragment(View v){
        Log.d(TAG,"public void onSwitchFragment(View v) clicked");
    }

    @Override
    public void OnResultFragmentListener(Uri uri) {

        Log.d(TAG,"result fragment interaction");

    }
}
