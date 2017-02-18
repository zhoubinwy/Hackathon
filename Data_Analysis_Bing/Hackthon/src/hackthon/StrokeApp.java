package hackthon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StrokeApp {
	public static void main(String[] args) throws IOException {
		// path for files
		String path = "C:/Users/zhoub/Documents/Hackathon/Data_Analysis_Bing/hackathon_files/data0/";
		
		// construct Stroke object
		Stroke stroke = new Stroke(path);

		// Plot curves for acc_x
		PlotCurve plotAccx = new PlotCurve("Acc X_axis", stroke.getAccX());
		plotAccx.setVisible(true);
		PlotCurve plotVelocity = new PlotCurve("Velocity", stroke.getVelocity());
		plotVelocity.setVisible(true);

		// Save results into file
		writeFile(path, stroke);
	}
	
	
	
	private static void writeFile(String path, Stroke stroke) {
		File file = new File(path + "result.txt");
		FileWriter fstream;
		try {
			fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("shootNumber: " + 1);
			out.newLine();
			out.write("mainAccAverage: " + stroke.getMainAccAverage());
			out.newLine();
			out.write("mainAccPeak: " + stroke.getMaxAcc());
			out.newLine();
			out.write("lateralSwayAverage: "+ stroke.getSideSway());
			out.newLine();
			out.write("verticalSwayAverage: " + stroke.getUpDownSway());
			out.newLine();
			out.write("mainAccRatio: " + stroke.getMajorAccRatio());
			out.newLine();
			out.write("forwardStartTime: " + stroke.getStartTime());
			out.newLine();
			out.write("hitBallTime: " + stroke.getHitTime());
			out.newLine();
			out.write("stopTime: " + stroke.getStopTime());
			out.newLine();
			out.write("forwardDuration: " + (stroke.getHitTime() - stroke.getStartTime()));
			out.newLine();
			out.write("deaccelerationDuration: " + (stroke.getStopTime() - stroke.getHitTime()));
			out.newLine();
			out.write("strokeScore: " + stroke.getScore());
			out.newLine();
			out.write("rollAngle: " + stroke.getStartRollAngle() + " ~ " + stroke.getHitRollAngle());
			out.newLine();
			out.write("lateralAngle: " + stroke.getStartLateralAngle() + " ~ " + stroke.getHitLateralAngle());
			out.newLine();
			out.write("vertialAngle: " + stroke.getStartVerticalAngle() + " ~ " + stroke.getHitVertialAngle());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
