package smart.billard;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String TAG="ResultFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnResultFragmentListener mListener;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        TextView tv=(TextView)container.findViewById(R.id.results);
//        tv.setText("asdfasdfl;kajsdf");
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();


        String baseDir= Environment.getExternalStorageDirectory().getAbsolutePath();

        String file_name=baseDir+"/billiard_data/result.csv";

        Log.d(TAG,"file name "+file_name);

        File file=new File(file_name);

        List<String> text = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.add(line.split(",")[1]);
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

//Set the text
        TextView tv=(TextView) getActivity().findViewById(R.id.result0);
        tv.setText(text.get(0));
        tv=(TextView) getActivity().findViewById(R.id.result1);
        tv.setText(text.get(1));
        tv=(TextView) getActivity().findViewById(R.id.result2);
        tv.setText(text.get(2));
        tv=(TextView) getActivity().findViewById(R.id.result3);
        tv.setText(text.get(3));
        tv=(TextView) getActivity().findViewById(R.id.result4);
        tv.setText(text.get(4));
        tv=(TextView) getActivity().findViewById(R.id.result5);
        tv.setText(text.get(5));
        tv=(TextView) getActivity().findViewById(R.id.result6);
        tv.setText(text.get(6));
        tv=(TextView) getActivity().findViewById(R.id.result7);
        tv.setText(text.get(7));
        tv=(TextView) getActivity().findViewById(R.id.result8);
        tv.setText(text.get(8));
        tv=(TextView) getActivity().findViewById(R.id.result9);
        tv.setText(text.get(9));
        tv=(TextView) getActivity().findViewById(R.id.result10);
        tv.setText(text.get(10));
        tv=(TextView) getActivity().findViewById(R.id.result11);
        tv.setText(text.get(11));
        tv=(TextView) getActivity().findViewById(R.id.result12);
        tv.setText(text.get(12));
        tv=(TextView) getActivity().findViewById(R.id.result13);
        tv.setText(text.get(13));
        tv=(TextView) getActivity().findViewById(R.id.result14);
        tv.setText(text.get(14));


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnResultFragmentListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResultFragmentListener) {
            mListener = (OnResultFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnResultFragmentListener {
        // TODO: Update argument type and name
        void OnResultFragmentListener(Uri uri);
    }
}
