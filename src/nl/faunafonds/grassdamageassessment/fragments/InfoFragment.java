package nl.faunafonds.grassdamageassessment.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import nl.faunafonds.grassdamageassessment.R;
import nl.faunafonds.grassdamageassessment.R.id;
import nl.faunafonds.grassdamageassessment.R.layout;
import nl.faunafonds.grassdamageassessment.activities.MainActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment implements FragmentsInterface, OnClickListener{
	TextView relID;
	TextView name;
	TextView address;
	TextView zipCode;
	TextView city;
	TextView province;
	TextView caseID;
	TextView startDate;
	TextView animal;
	TextView crop;
	TextView savedDate;
	TextView farmerComments;
	Switch damageContinues;
	TextView barrier;
	TextView scareCrows;
	TextView sticks;
	TextView noiseMachines;
	TextView chased;
	TextView hunting;
	TextView txtbarrier;
	TextView txtscareCrows;
	TextView txtsticks;
	TextView txtnoiseMachines;
	TextView txtchased;
	TextView txthunting;
	EditText generalComments;
	CheckBox checkBoxBarrier;
	CheckBox checkBoxScareCrows;
	CheckBox checkBoxSticks;
	CheckBox checkBoxNoiseMachines;
	CheckBox checkBoxChased;
	CheckBox checkBoxHunting;
	private Button btnSaveComments;
	private Button btnExport;
	
	View generalInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		generalInfo = inflater.inflate(R.layout.fragment_info, container, false);
		setupUI(generalInfo);

		// Get objects of all the views
		relID = (TextView) generalInfo.findViewById(R.id.inputGIRelID);
		name = (TextView) generalInfo.findViewById(R.id.inputGIName);
		address = (TextView) generalInfo.findViewById(R.id.inputGIAddress);
		zipCode = (TextView) generalInfo.findViewById(R.id.inputGIZipCode);
		city = (TextView) generalInfo.findViewById(R.id.inputGICity);
		province = (TextView) generalInfo.findViewById(R.id.inputGIProvince);
		caseID = (TextView) generalInfo.findViewById(R.id.inputGICaseID);
		startDate = (TextView) generalInfo.findViewById(R.id.inputGIStartDate);
		animal = (TextView) generalInfo.findViewById(R.id.inputGIAnimal);
		crop = (TextView) generalInfo.findViewById(R.id.inputGICrop);
		savedDate = (TextView) generalInfo.findViewById(R.id.inputGISaved);
		farmerComments = (TextView) generalInfo.findViewById(R.id.inputGIFarmerComments);
		damageContinues = (Switch) generalInfo.findViewById(R.id.switchDamageContinues);
		barrier = (TextView) generalInfo.findViewById(R.id.inputGIBarrier);
		scareCrows = (TextView) generalInfo.findViewById(R.id.inputGIScareCrows);
		sticks = (TextView) generalInfo.findViewById(R.id.inputGISticks);
		noiseMachines = (TextView) generalInfo.findViewById(R.id.inputGINoiseMachines);
		chased = (TextView) generalInfo.findViewById(R.id.inputGIChased);
		hunting = (TextView) generalInfo.findViewById(R.id.inputGIHunting);
		txtbarrier = (TextView) generalInfo.findViewById(R.id.txtGIBarrier);
		txtscareCrows = (TextView) generalInfo.findViewById(R.id.txtGIScareCrows);
		txtsticks = (TextView) generalInfo.findViewById(R.id.txtGISticks);
		txtnoiseMachines = (TextView) generalInfo.findViewById(R.id.txtGINoiseMachines);
		txtchased = (TextView) generalInfo.findViewById(R.id.txtGIChased);
		txthunting = (TextView) generalInfo.findViewById(R.id.txtGIHunting);
		checkBoxBarrier = (CheckBox) generalInfo.findViewById(R.id.checkBoxBarrier);
		checkBoxScareCrows = (CheckBox) generalInfo.findViewById(R.id.checkBoxScareCrows);
		checkBoxSticks = (CheckBox) generalInfo.findViewById(R.id.checkBoxSticks);
		checkBoxNoiseMachines = (CheckBox) generalInfo.findViewById(R.id.checkBoxNoiseMachines);
		checkBoxChased = (CheckBox) generalInfo.findViewById(R.id.checkBoxChased);
		checkBoxHunting = (CheckBox) generalInfo.findViewById(R.id.checkBoxHunting);
		generalComments = (EditText) generalInfo.findViewById(R.id.inputGIGeneralComments);

		// Populates all the views with case info
		relID.setText(MainActivity.theCase.getRelatieID());
		name.setText(MainActivity.theCase.getRelation().getName());
		address.setText(MainActivity.theCase.getRelation().getAddress());
		zipCode.setText(MainActivity.theCase.getRelation().getZipCode());
		city.setText(MainActivity.theCase.getRelation().getCity());
		province.setText(MainActivity.theCase.getRelation().getProvince());
		caseID.setText(MainActivity.theCase.getSchadeID());
		startDate.setText(MainActivity.theCase.getDatum());
		animal.setText(MainActivity.theCase.getDierSoorten());
		crop.setText(MainActivity.theCase.getCrop());
		savedDate.setText(MainActivity.theCase.getSavedDate().split(" ")[0]);
		farmerComments.setText(MainActivity.theCase.getFarmerComments());
		damageContinues.setChecked(MainActivity.theCase.damageContinues());
		barrier.setText(MainActivity.theCase.getBarrier().split("T")[0]);
		scareCrows.setText(MainActivity.theCase.getScareCrows() + " stuks");
		sticks.setText(MainActivity.theCase.getSticks() + " stuks");
		noiseMachines.setText(MainActivity.theCase.getNoiseMachines() + " stuks");
		chased.setText(MainActivity.theCase.getChased() + " keer");
		hunting.setText(MainActivity.theCase.getHunting() + " keer");
		generalComments.setText(MainActivity.theCase.getGeneralComments());

		// Get button objects and add them to the onclicklistener
		btnSaveComments = (Button) generalInfo.findViewById(R.id.btnSaveComments);
		btnSaveComments.setOnClickListener(this);

		btnExport = (Button) generalInfo.findViewById(R.id.btnExport);
		btnExport.setOnClickListener(this);

		// Handle grey color of the measures against geese
		if (MainActivity.theCase.getScareCrows().equals("0")){
			txtscareCrows.setTextColor(Color.rgb(122,122,122));
			scareCrows.setTextColor(Color.rgb(122,122,122));
			checkBoxScareCrows.setEnabled(false);
		}
		else {
			txtscareCrows.setTextColor(Color.rgb(0,0,0));
			scareCrows.setTextColor(Color.rgb(0,0,0));
			checkBoxScareCrows.setEnabled(true);
		}
		if (MainActivity.theCase.getSticks().equals("0")){
			txtsticks.setTextColor(Color.rgb(122,122,122));
			sticks.setTextColor(Color.rgb(122,122,122));
			checkBoxSticks.setEnabled(false);
		}
		else {
			txtsticks.setTextColor(Color.rgb(0,0,0));
			sticks.setTextColor(Color.rgb(0,0,0));
			checkBoxSticks.setEnabled(true);
		}
		if (MainActivity.theCase.getNoiseMachines().equals("0")){
			txtnoiseMachines.setTextColor(Color.rgb(122,122,122));
			noiseMachines.setTextColor(Color.rgb(122,122,122));
			checkBoxNoiseMachines.setEnabled(false);
		}
		else {
			txtnoiseMachines.setTextColor(Color.rgb(0,0,0));
			noiseMachines.setTextColor(Color.rgb(0,0,0));
			checkBoxNoiseMachines.setEnabled(true);
		}
		if (MainActivity.theCase.getChased().equals("0")){
			txtchased.setTextColor(Color.rgb(122,122,122));
			chased.setTextColor(Color.rgb(122,122,122));
			checkBoxChased.setEnabled(false);
		}
		else {
			txtchased.setTextColor(Color.rgb(0,0,0));
			chased.setTextColor(Color.rgb(0,0,0));
			checkBoxChased.setEnabled(true);
		}
		if (MainActivity.theCase.getHunting().equals("0")){
			txthunting.setTextColor(Color.rgb(122,122,122));
			hunting.setTextColor(Color.rgb(122,122,122));
			checkBoxHunting.setEnabled(false);
		}
		else {
			txthunting.setTextColor(Color.rgb(0,0,0));
			hunting.setTextColor(Color.rgb(0,0,0));
			checkBoxHunting.setEnabled(true);
		}

		return generalInfo;
	}
	@Override
	public void fragmentBecameVisible() {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSaveComments:
			MainActivity.theCase.setGeneralComments(generalComments.getText().toString());
			saveCase();
			break;
		case R.id.btnExport:
			OutputStream out;
			File file = null;
			// export saved case file to download folder
			try {
				File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				file = new File(downloadsDir, "schade_" + MainActivity.theCase.getRelatieID() + "_saved.xml");
				if(file.exists())
					file.delete();
				file.createNewFile();
				out = new FileOutputStream(file);
				MainActivity.theCase.save(out);
				Toast.makeText(getActivity().getApplicationContext(), "File exported", 
						Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getActivity().getApplicationContext(), "Error", 
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// make email with attachment
			String subject = "Aanvraag van " + MainActivity.theCase.getRelation().getName() + " getaxeerd op " + MainActivity.theCase.getSavedDate().split(" ")[0];
			String message = MainActivity.theCase.getGeneralComments();

			Intent emailIntent = new Intent(Intent.ACTION_SEND);

			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(Intent.EXTRA_TEXT, message);
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
			//emailIntent.putExtra(Intent.EXTRA_STREAM, "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/schade_0_saved.xml");
			emailIntent.setType("plain/text");

			startActivity(Intent.createChooser(emailIntent, getString(R.string.case_popup_email)));

			break;
		}
	}

	private void saveCase() {
		// Save case to internal storage
		OutputStream out;
		try {
			out = getActivity().openFileOutput("schade_" + MainActivity.theCase.getRelatieID() + "_saved.xml", Context.MODE_PRIVATE);
			MainActivity.theCase.save(out);
			Toast.makeText(getActivity().getApplicationContext(), "Comments saved", 
					Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getActivity().getApplicationContext(), "Error", 
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Make the soft keyboard close when somewhere else is tapped.
	private void setupUI(View view) {

	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {

	        view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(getActivity());
					return false;
				}

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView);
	        }
	    }
	}
	
	private static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}
