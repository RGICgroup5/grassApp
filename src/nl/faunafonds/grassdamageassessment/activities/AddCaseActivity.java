package nl.faunafonds.grassdamageassessment.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.faunafonds.grassdamageassessment.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class AddCaseActivity extends Activity {
	File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	EditText fileNameInput;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_case);
		
		fileNameInput = (EditText) findViewById(R.id.inputFilename);
		textView = (TextView) findViewById(R.id.txtDetails);
		
		/*File file = new File(downloadsDir,"out-1.txt");
		StringBuilder text = new StringBuilder();

		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		TextView textView = (TextView)findViewById(R.id.textLoadCase);
		textView.setText(text);
		
		String FILENAME = "hello_file";
		String string = "hello world!";

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(string.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String testText = null;
		FileInputStream fis;
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		TextView textView2 = (TextView)findViewById(R.id.textView1);
		textView2.setText(text);
	*/
		
	}
	
	public void loadCase(View view){
		final String FILENAME = fileNameInput.getText().toString();
		add(FILENAME);
		
		
		File[] files = getFilesDir().listFiles();
				
		String filesString = "";
		for (int i = 0; i< files.length; i++){
			filesString += files[i] + "\n Next:";
		}
		textView.setText(filesString);
	    
	}
	
	private void add(String name){
		InputStream in;
		OutputStream out;
		try {
			in = new FileInputStream(downloadsDir + "/" + name);
			out = openFileOutput(name, Context.MODE_PRIVATE);
			
			// Transfer bytes from in to out
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addAll(View view){
		File[] files = downloadsDir.listFiles();
		String filesString = "";
		for (int i = 0; i< files.length; i++){
			String[] file = files[i].toString().split("/");
			String name = file[file.length - 1];
			if (name.startsWith("schade_") && name.endsWith(".xml")){
				filesString += name + "\n Next: ";
				add(name);
			}
			
		}
		textView.setText(filesString);
	}

	/* Checks if external storage is available for read and write */
	/*public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}*/

	
}
