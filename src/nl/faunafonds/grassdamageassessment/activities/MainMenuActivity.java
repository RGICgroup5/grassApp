package nl.faunafonds.grassdamageassessment.activities;

import nl.faunafonds.grassdamageassessment.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Locale; 

import android.content.res.Configuration; 
import android.content.res.Resources; 
import android.util.DisplayMetrics; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainMenuActivity extends Activity {
	
	File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	Locale myLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	public void loadCase(View view){
		Intent intent = new Intent(this, LoadCaseActivity.class);
		startActivity(intent);
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
	
	public void addCase(View view){
		File[] files = downloadsDir.listFiles();
		for (int i = 0; i< files.length; i++){
			String[] file = files[i].toString().split("/");
			String name = file[file.length - 1];
			if (name.startsWith("schade_") && name.endsWith(".xml")){
				add(name);
			} else if (name.equals("faunafonds_relaties.xml")) {
				add(name);
				//Toast.makeText(view.getContext(), name, Toast.LENGTH_LONG).show();
			}
		}
		Toast.makeText(view.getContext(), "Downloading complete, all files have been added to the system!", Toast.LENGTH_LONG).show();
	}
	
	public void changeToDutch(View view){
		String lang = "";
		setLocale(lang);
	}
	
	public void changeToEnglish(View view){
		String lang = "en";
		setLocale(lang);
	}
	
	public void setLocale(String lang) { 
		myLocale = new Locale(lang); 
		Resources res = getResources(); 
		DisplayMetrics dm = res.getDisplayMetrics(); 
		Configuration conf = res.getConfiguration(); 
		conf.locale = myLocale; 
		res.updateConfiguration(conf, dm); 
		Intent refresh = new Intent(this, MainMenuActivity.class); 
		startActivity(refresh); 
		} 
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	Intent intent = new Intent(this, MainMenuActivity.class);
			startActivity(intent);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
}
