package smart.billard;

import android.app.Fragment;
import android.app.FragmentManager;


import android.app.FragmentTransaction;
import android.net.Uri;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import smart.billard.data_process.StrokeAnalysis;

public class MainActivity extends AppCompatActivity
        implements ControlFragment.OnFragmentInteractionListener ,ResultFragment.OnResultFragmentListener,DisplayFragment.OnDisplayFragmentListener{

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

        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.VIBRATE"};

        int permsRequestCode = 200;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }


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


    public  void onDataProcess(View v){
        String path = "sdcard/billiard_data/";
        StrokeAnalysis dp=new StrokeAnalysis(path);
        dp.process();
        Log.d(TAG,"begin to process data");
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

    public void show_display_fragment(View v){
        DisplayFragment newFragment = new DisplayFragment();
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

    @Override
    public void onDisplayFragmentInteraction(Uri uri) {
        Log.d(TAG,"Display fragment interaction");
    }
}
