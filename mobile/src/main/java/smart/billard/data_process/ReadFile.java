package smart.billard.data_process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	private String path;
	
	public ReadFile(String file_path) {
		path = file_path;
	}
	
	public String[] OpenFile() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);

		int numberOfLines = readLines(); // get the number of rows

		String[] ans = new String[numberOfLines];   // return ans;
		for (int i = 0; i < numberOfLines; i++) {
			ans[i] = br.readLine();
		}
		
		br.close();
		return ans;
	}
	
	private int readLines() throws IOException {
		int ans = 0;
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		while(br.readLine() != null) {
			ans++;
		}
		br.close();
		return ans;
	}

}
