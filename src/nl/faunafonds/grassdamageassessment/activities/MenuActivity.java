package nl.faunafonds.grassdamageassessment.activities;

import nl.faunafonds.grassdamageassessment.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * This is the first activity of the app, it continues to the LoadCaseActivity and implements the function to add cases. 
 * @author Group 5, RGIC 2014, WUR
 * @version 0.1
 */

public class MenuActivity extends Activity {
	
	File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	Locale myLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	/**
	 * This method executes when the button load case is clicked, it starts the LoadCaseActivity
	 * @author Group 5, RGIC 2014, WUR
	 * @version 0.1
	 * @param View view
	 * @return void
	 */
	
	public void loadCase(View view){
		Intent intent = new Intent(this, LoadCaseActivity.class);
		startActivity(intent);
	}
	
	private void add(String name){
		// Add the file 'name' to the internal storage
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
	
	/**
	 * This method executes when the button download cases is clicked, it downloads the cases from the download folder
	 * @author Group 5, RGIC 2014, WUR
	 * @version 0.1
	 * @param View view
	 * @return void
	 */
	
	public void addCase(View view){
		// get full paths of files in the download folder
		File[] files = downloadsDir.listFiles();
		for (int i = 0; i< files.length; i++){
			String[] file = files[i].toString().split("/");
			String name = file[file.length - 1];
			// check if the file should be copied to internal storage
			if (name.startsWith("schade_") && name.endsWith(".xml")){
				add(name);
			} else if (name.equals("faunafonds_relaties.xml")) {
				add(name);
			} else if (name.startsWith("schade_") && name.endsWith(".png")){
				add(name);
			} else if (name.startsWith("schade_") && name.endsWith(".kml")){
				add(name);
			}
		}
		Toast.makeText(view.getContext(), "Downloading complete, all files have been added to the system!", Toast.LENGTH_LONG).show();
	}

	/**
	 * This method executes when the Dutch flag button is clicked, it switches the language to Dutch
	 * @author Group 5, RGIC 2014, WUR
	 * @version 0.1
	 * @param View view
	 * @return void
	 */
	
	public void changeToDutch(View view){
		String lang = "";
		setLocale(lang);
	}

	/**
	 * This method executes when the English flag button is clicked, it switches the language to English
	 * @author Group 5, RGIC 2014, WUR
	 * @version 0.1
	 * @param View view
	 * @return void
	 */
	
	public void changeToEnglish(View view){
		String lang = "en";
		setLocale(lang);
	}
	
	private void setLocale(String lang) { 
		myLocale = new Locale(lang); 
		Resources res = getResources(); 
		DisplayMetrics dm = res.getDisplayMetrics(); 
		Configuration conf = res.getConfiguration(); 
		conf.locale = myLocale; 
		res.updateConfiguration(conf, dm); 
		Intent refresh = new Intent(this, MenuActivity.class); 
		startActivity(refresh); 
		} 

	/**
	 * This method executes when the back button is clicked, it closes the app.
	 * @author Group 5, RGIC 2014, WUR
	 * @version 0.1
	 * @param int keyCode, KeyEvent event
	 * @return boolean 
	 */
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	finish();
			System.exit(0);	
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	public void appInfo(View view) {
	AlertDialog.Builder aboutAlert = new AlertDialog.Builder(this);
	String m1 = "Grass Damage (version 0.1)";
	String m2 = "Release date: June 30, 2014";
	String m3 = "Wageningen University";
	String m4 = "Commissioners: Faunafonds & Alterra";
	String m5 = "";
	String m6 = "Course: GRS60312";
	String m7 = "Authors:";
	String m8 = "Bouke Pieter Ottow";
	String m9 = "Sander van der Drift";
	String m10 = "Kristin Abraham";
	String m11 = "Sabina Rosca";
	String m12 = "Bastiaen Boekelo";
	aboutAlert.setTitle(m1);
	aboutAlert.setMessage(m2 +"\n"+ m3 + "\n"+ m4 + "\n"+ m5 +"\n"+ m6 +"\n"+"\n"+ m7 +"\n"+ m8 +"\n"+ m9 +"\n"+ m10 +"\n"+ m11 +"\n"+ m12)
	.setCancelable(false)
	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
			//do things
		}
	});
	aboutAlert.create().show();
	
	}
}
