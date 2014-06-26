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


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class GeneralInfo extends Fragment implements FragmentsInterface, OnClickListener{
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View generalInfo = inflater.inflate(R.layout.general_frag, container, false);


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

		btnSaveComments = (Button) generalInfo.findViewById(R.id.btnSaveComments);
		btnSaveComments.setOnClickListener(this);

		btnExport = (Button) generalInfo.findViewById(R.id.btnExport);
		btnExport.setOnClickListener(this);


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
			txtsticks.setTextColor(Color.rgb(0,0,0));
			sticks.setTextColor(Color.rgb(0,0,0));
			checkBoxSticks.setEnabled(true);
		}
		if (MainActivity.theCase.getChased().equals("0")){
			txtchased.setTextColor(Color.rgb(122,122,122));
			chased.setTextColor(Color.rgb(122,122,122));
			checkBoxChased.setEnabled(false);
		}
		else {
			txtsticks.setTextColor(Color.rgb(0,0,0));
			sticks.setTextColor(Color.rgb(0,0,0));
			checkBoxSticks.setEnabled(true);
		}
		if (MainActivity.theCase.getHunting().equals("0")){
			txthunting.setTextColor(Color.rgb(122,122,122));
			hunting.setTextColor(Color.rgb(122,122,122));
			checkBoxHunting.setEnabled(false);
		}
		else {
			txtsticks.setTextColor(Color.rgb(0,0,0));
			sticks.setTextColor(Color.rgb(0,0,0));
			checkBoxSticks.setEnabled(true);
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

			
			String subject = "Aanvraag van " + MainActivity.theCase.getRelation().getName() + " getaxeerd op " + MainActivity.theCase.getSavedDate().split(" ")[0];
			String message = MainActivity.theCase.getGeneralComments();

			Intent emailIntent = new Intent(Intent.ACTION_SEND);

			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(Intent.EXTRA_TEXT, message);
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
			//emailIntent.putExtra(Intent.EXTRA_STREAM, "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/schade_0_saved.xml");
			emailIntent.setType("plain/text");

			startActivity(Intent.createChooser(emailIntent, "Emailfile"));

			break;
		}
	}

	private void saveCase() {
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
}
