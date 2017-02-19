package smart.billard;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import smart.billard.analyze_view.CSVFile;

/**
 * Created by harry on 2/18/17.
 */
public class DisplayView extends View {

    String TAG="DisplayView";

    CSVFile csvFile;

    List<Float> time;
    List<Float> acc;
    public DisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String baseDir= Environment.getExternalStorageDirectory().getAbsolutePath();

        String file_name = "sdcard/billiard_data/curveData.csv";

//        AssetManager assetManager = context.getAssets();

        List curve_data=new ArrayList();
        try {
            InputStream csvStream = new FileInputStream(file_name);
            csvFile=new CSVFile(csvStream);
            curve_data=csvFile.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        time=new ArrayList<Float>(curve_data.size());
        acc=new ArrayList<Float>(curve_data.size());


        for(int i=0;i<curve_data.size();i++){
//            Log.d(TAG,curve_data.get(i).toString());
            time.add(Float.parseFloat(((String[])curve_data.get(i))[0]));
            acc.add(Float.parseFloat(((String[]) curve_data.get(i))[1]));
//            Log.d(TAG, ((String[]) curve_data.get(i))[1]);
//            Log.d(TAG,((String)curve_data.get(i)).split(",")[0]);
        }


    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20.f);
        paint.setTextAlign(Paint.Align.CENTER);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        // Draw background
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(0, 0, width -1, height -1), paint);

        // Draw x label grids
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(1);
        canvas.drawLine(0, 0, width - 1, 0, paint);
        canvas.drawLine(0, height /4, width - 1, height /4, paint);
        canvas.drawLine(0, height /2, width - 1, height /2, paint);
        canvas.drawLine(0, 3* height /4, width - 1,  3* height /4, paint);
        canvas.drawLine(0, height -1, width - 1,  height -1, paint);

        canvas.drawLine(0, 0, 0, height - 1, paint);
        canvas.drawLine(width/4, 0, width/4, height - 1, paint);
        canvas.drawLine(width/2, 0, width/2, height - 1, paint);
        canvas.drawLine(3*width/4, 0, 3*width/4, height - 1, paint);
        canvas.drawLine(width - 1, 0, width - 1, height - 1, paint);


        float time_span=time.get(time.size() - 1)-time.get(0);
//        float height_span=find_max(acc)-find_min(acc);
        float height_span=Math.max(Math.abs(find_max(acc)),Math.abs(find_min(acc)));
        height_span=height_span*(float)2.1;


        paint.setStrokeWidth(4);
        for(int i=1;i<time.size();i++){
            float prev_x=(time.get(i-1)-time.get(0))/time_span*width;
            float prev_y=-(acc.get(i-1))/height_span*height+height/2;

            float cur_x=(time.get(i)-time.get(0))/time_span*width;
            float cur_y=-(acc.get(i))/height_span*height+height/2;

            paint.setColor(Color.RED);
            canvas.drawLine(prev_x,prev_y,cur_x,cur_y, paint);
        }

    }

    float find_min(List<Float> in){
        float min=100000;

        for(int i=0;i<in.size();i++){
            if(in.get(i)<min){
                min=in.get(i);
            }
        }
        return  min;
    }

    float find_max(List<Float> in){
        float max=-100000;

        for(int i=0;i<in.size();i++){
            if(in.get(i)>max){
                max=in.get(i);
            }
        }
        return  max;
    }




}
