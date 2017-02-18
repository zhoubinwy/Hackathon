package smart.billard.data_process;

/**
 * Created by harry on 2/18/17.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class StrokeAnalysis {
    private List<Float> acc_t, acc_x, acc_y, acc_z, gyro_t, gyro_x, gyro_y, gyro_z, velocity;   // raw data
    private int startTime, hitTime, stopTime, targetBallTime;   // important time stamps
    private float startLocation, hitLocation, stopLocation, targetBallLocation; // locations
    private float peakVelocity, peakAcc;   // peak velocity and acceleration of cue
    private String strokeType, hitPoint;   // classify stroke types, hitting points
    private float sideSway, upDownSway, qualityScore;  // score your stroke
    private float fs = 50f;

    // Constructor function
    public StrokeAnalysis(String file_path) {
        // Initialize acceleration data
        initializeAcc(file_path);

        // Initialize gyroscope data
        initializeGyro(file_path);
    }

    public void process() {
        // Integrate velocity
        integrateVelocity();

        // Compute time indexes for peaks
        setTimes();

        // Write files
        String outPath = "sdcard/billiard_data/";
        writeFile(outPath);
    }

    // Score the stroke
    public int getScore() {
        return 100;
    }

    // Integrate velocity
    private void integrateVelocity() {
        velocity = new ArrayList<>();
        velocity.add(0f);
        for (int i = 0; i < acc_x.size(); i++) {
            velocity.add(velocity.get(i) + acc_x.get(i) / fs);
        }
    }

    // Get velocity
    public List<Float> getVelocity() {
        return velocity;
    }

    // Get important time stamps
    public void setTimes() {
        // Find peak
        hitTime = findPeak(acc_x, 0, acc_x.size());
        startTime = findValley(acc_x, 0, hitTime);
        stopTime = findValley(acc_x, hitTime, acc_x.size());
    }

    // Peak finding
    private int findPeak(List<Float> nums, int start, int end) {
        float max = Float.MIN_VALUE;
        int index = 0;
        for (int i = start; i < end; i++) {
            if (nums.get(i) > max) {
                max = nums.get(i);
                index = i;
            }
        }
        return index;
    }

    // Valley finding
    private int findValley(List<Float> nums, int start, int end) {
        float min = Float.MAX_VALUE;
        int index = 0;
        for (int i = start; i < end; i++) {
            if (nums.get(i) < min) {
                min = nums.get(i);
                index = i;
            }
        }
        return index;
    }

    // Get major direction acc ratio
    public float getMajorAccRatio() {
        float x = 0, total = 0;
        for (int i = startTime; i < hitTime; i++) {
            x += Math.pow(acc_x.get(i), 2);
            total += Math.pow(acc_x.get(i), 2) + Math.pow(acc_y.get(i), 2) + Math.pow(acc_z.get(i), 2);
        }
        return x / total;
    }

    // Get side sway acc
    public float getMainAccAverage() {
        float ans = 0f;
        for (int i = startTime; i < hitTime; i++)
            ans += Math.abs(acc_x.get(i));
        return ans / (hitTime - startTime);
    }

    // Get side sway acc
    public float getSideSway() {
        float ans = 0f;
        for (int i = startTime; i < hitTime; i++)
            ans += Math.abs(acc_y.get(i));
        return ans / (hitTime - startTime);
    }

    // Get upDownSway acc
    public float getUpDownSway() {
        float ans = 0f;
        for (int i = startTime; i < hitTime; i++)
            ans += Math.abs(acc_z.get(i));
        return ans / (hitTime - startTime);
    }

    // Get max acc_x
    public float getMaxAcc() {
        return acc_x.get(hitTime);
    }

    // Get functions
    public List<Float> getAccT() {
        return acc_t;
    }

    public List<Float> getAccX() {
        return acc_x;
    }

    public List<Float> getAccY() {
        return acc_y;
    }

    public List<Float> getAccZ() {
        return acc_z;
    }

    public List<Float> getGryoT() {
        return gyro_t;
    }

    public List<Float> getGyroX() {
        return gyro_x;
    }

    public List<Float> getGyroY() {
        return gyro_y;
    }

    public List<Float> getGyroZ() {
        return gyro_z;
    }

    public float getStartTime() {
        return startTime / fs;
    }

    public float getHitTime() {
        return hitTime / fs;
    }

    public float getStopTime() {
        return stopTime / fs;
    }

    public float getStartRollAngle() {
        return gyro_x.get(startTime);
    }

    public float getHitRollAngle() {
        return gyro_x.get(hitTime);
    }

    public float getStartLateralAngle() {
        return gyro_y.get(startTime);
    }

    public float getHitLateralAngle() {
        return gyro_y.get(hitTime);
    }

    public float getStartVerticalAngle() {
        return gyro_z.get(startTime);
    }

    public float getHitVertialAngle() {
        return gyro_z.get(hitTime);
    }

    // Read acc data from files
    private void initializeAcc(String file_path) {
        // path for files
        String path = file_path + "acc_null.csv";
        // arrayLists to store acceleration data
        acc_t = new ArrayList<>();
        acc_x = new ArrayList<>();
        acc_y = new ArrayList<>();
        acc_z = new ArrayList<>();
        // read files
        ReadFile rf = new ReadFile(path);
        String[] lines = null;
        try {
            lines = rf.OpenFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get the acceleration data
        for (int i = 0; i < lines.length; i++) {
            String[] oneLine = lines[i].split(",");
            acc_t.add(Float.parseFloat(oneLine[0]));
            acc_x.add(Float.parseFloat(oneLine[1]));
            acc_y.add(Float.parseFloat(oneLine[2]));
            acc_z.add(Float.parseFloat(oneLine[3]));
        }
    }

    // Read gyro data from files
    private void initializeGyro(String file_path) {
        // path for files
        String path = file_path + "gyro_null.csv";
        // arrayLists to store acceleration data
        gyro_t = new ArrayList<>();
        gyro_x = new ArrayList<>();
        gyro_y = new ArrayList<>();
        gyro_z = new ArrayList<>();
        // read files
        ReadFile rf = new ReadFile(path);
        String[] lines = null;
        try {
            lines = rf.OpenFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get the acceleration data
        for (int i = 0; i < lines.length; i++) {
            String[] oneLine = lines[i].split(",");
            gyro_t.add(Float.parseFloat(oneLine[0]));
            gyro_x.add(Float.parseFloat(oneLine[1]));
            gyro_y.add(Float.parseFloat(oneLine[2]));
            gyro_z.add(Float.parseFloat(oneLine[3]));
        }
    }

    private void writeFile(String path) {
        File file = new File(path + "result.csv");
        FileWriter fstream;
        try {
            fstream = new FileWriter(file);
            StringBuilder sb = new StringBuilder();
            sb.append("shootNumber" + "," + "1\n");
            sb.append("mainAccAverage" + "," + this.getMainAccAverage() + "\n");
            sb.append("mainAccPeak" + "," + this.getMaxAcc() + "\n");
            sb.append("lateralSwayAverage" + "," + this.getSideSway() + "\n");
            sb.append("verticalSwayAverage" + "," + this.getUpDownSway() + "\n");
            sb.append("mainAccRatio" + "," + this.getMajorAccRatio() + "\n");
            sb.append("forwardStartTime" + "," + this.getStartTime() + "\n");
            sb.append("hitBallTime" + "," + this.getHitTime() + "\n");
            sb.append("forwardDuration" + "," + (this.getHitTime() - this.getStartTime()) + "\n");
            sb.append("deaccelerationDuration" + "," + (this.getStopTime() - this.getHitTime()) + "\n");
            sb.append("strokeScore" + "," + this.getScore() + "\n");
            sb.append("rollAngle" + "," + this.getStartRollAngle() + " ~ " + this.getHitRollAngle() + "\n");
            sb.append("lateralAngle" + "," + this.getStartLateralAngle() + " ~ " + this.getHitLateralAngle() + "\n");
            sb.append("verticalAngle" + "," + this.getStartVerticalAngle() + " ~ " + this.getHitVertialAngle() + "\n");

            BufferedWriter out = new BufferedWriter(fstream);
            out.write(sb.toString());
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
