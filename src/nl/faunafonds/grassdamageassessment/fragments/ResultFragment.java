package nl.faunafonds.grassdamageassessment.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;







import nl.faunafonds.grassdamageassessment.R;
import nl.faunafonds.grassdamageassessment.R.id;
import nl.faunafonds.grassdamageassessment.R.layout;
import nl.faunafonds.grassdamageassessment.activities.MainActivity;
import nl.faunafonds.grassdamageassessment.other.DamageArea;
import nl.faunafonds.grassdamageassessment.other.ReferencePoint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ResultFragment extends Fragment implements OnClickListener, FragmentsInterface{
	List<DamageArea> areas;
	List<ReferencePoint> points;
	View damageAreasFrag;
	DamageArea selectedArea = null;
	ReferencePoint selectedPoint = null;
	private int selectedAreaPosition;
	private int selectedPointPosition;
	private ListView listView;
	private ListView listView2;
	private TextView name;
	private TextView area;
	private TextView grassHeight;
	private TextView date;

	private TextView averageRef;
	private TextView averageDam;
	private TextView difference;
	private TextView totalArea;
	private TextView dryMatter;
	private TextView compensation;

	private Button btnEdit;
	private Button btnDone;
	private Button btnDelete;
	private Button btnReCalculate;
	private EditText inputName;
	private EditText inputGrassHeight;

	private EditText inputKgDryMatterPer;
	private EditText inputEuroPerKg;

	private NumberPicker np;

	private boolean selectingArea = false;
	private boolean selectingPoint = false;
	private boolean editing = false;

	private double averageHeightAreas;
	private double averageHeightPoints;
	private double differenceHeight;
	private double totalAreaHa;
	static Dialog d ;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		damageAreasFrag = inflater.inflate(R.layout.fragment_result, container, false);
		setupUI(damageAreasFrag);

		// Get vies as objects
		listView = (ListView) damageAreasFrag.findViewById(R.id.listDamage);
		listView2 = (ListView) damageAreasFrag.findViewById(R.id.listDamage2);
		name = (TextView) damageAreasFrag.findViewById(R.id.txtNameContent);
		area = (TextView) damageAreasFrag.findViewById(R.id.txtAreaContent);
		grassHeight = (TextView) damageAreasFrag.findViewById(R.id.txtGrassHeightContent);
		date = (TextView) damageAreasFrag.findViewById(R.id.txtDateContent);

		inputName = (EditText) damageAreasFrag.findViewById(R.id.inputName);
		inputGrassHeight = (EditText) damageAreasFrag.findViewById(R.id.inputGrassHeight);

		inputKgDryMatterPer = (EditText) damageAreasFrag.findViewById(R.id.inputKgDryMatterPer);
		inputEuroPerKg = (EditText) damageAreasFrag.findViewById(R.id.inputEuroPerKg);
		
		
		averageRef = (TextView) damageAreasFrag.findViewById(R.id.txtAverageRef);
		averageDam = (TextView) damageAreasFrag.findViewById(R.id.txtAverageDam);
		difference = (TextView) damageAreasFrag.findViewById(R.id.txtDifference);
		totalArea = (TextView) damageAreasFrag.findViewById(R.id.txtTotalArea);
		dryMatter = (TextView) damageAreasFrag.findViewById(R.id.txtDryMatter);
		compensation = (TextView) damageAreasFrag.findViewById(R.id.txtCompensation);

		inputName.setVisibility(EditText.INVISIBLE);
		inputGrassHeight.setVisibility(EditText.INVISIBLE);

		// Buttons
		btnEdit = (Button) damageAreasFrag.findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(this);
		btnEdit.setEnabled(false);

		btnDone = (Button) damageAreasFrag.findViewById(R.id.btnDoneDamage);
		btnDone.setOnClickListener(this);
		btnDone.setEnabled(false);

		btnDelete = (Button) damageAreasFrag.findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		btnDelete.setEnabled(false);

		btnReCalculate = (Button) damageAreasFrag.findViewById(R.id.btnReCalculate);
		btnReCalculate.setOnClickListener(this);
		

		init();

		return damageAreasFrag;
	}

	public void init(){
		
		// initialize calc values
		averageHeightAreas = -999;
		averageHeightPoints = -999;
		
		// add damageareas to listview
		areas = new ArrayList<DamageArea>();
		areas.addAll(MainActivity.theCase.getDamageAreas());
		listView.setAdapter(new ArrayAdapter<DamageArea>(getActivity(),R.layout.list_item, areas));
		// clicklistener if a damagearea is selected
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (editing == false){
						btnEdit.setEnabled(true);
						btnDelete.setEnabled(true);
					
					selectedArea = areas.get(position);
					selectedAreaPosition = position;
					name.setText(selectedArea.getName());
					area.setText(String.format("%.2f", selectedArea.getSize() / 10000) + " ha");
					grassHeight.setText(String.format("%.0f", selectedArea.getGrassHeight()) + " cm");
					date.setText(selectedArea.getDate());
					selectingArea = true;
					selectingPoint = false;
				}}
		});

		// add reference points to listview
		points = new ArrayList<ReferencePoint>();
		points.addAll(MainActivity.theCase.getReferencePoints());
		listView2.setAdapter(new ArrayAdapter<ReferencePoint>(getActivity(),R.layout.list_item, points));
		// clicklistener if a reference point is selected
		listView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (editing == false){
					btnEdit.setEnabled(true);
					btnDelete.setEnabled(true);

					selectedPoint = points.get(position);
					selectedPointPosition = position;
					name.setText(selectedPoint.getName());
					area.setText("nvt");
					grassHeight.setText(String.format("%.0f", selectedPoint.getGrassHeight()) + " cm");
					date.setText(selectedPoint.getDate());
					selectingArea = false;
					selectingPoint = true;
				}
			}
		});


		// Calculations
		averageRef.setText("");
		averageDam.setText("");
		difference.setText("");
		dryMatter.setText("");
		compensation.setText("");
		totalArea.setText("");
		if (points.size() > 0){
			double totalHeightPoints = 0;
			int number = 0;
			for (int i=0;i<points.size();i++){
				if (points.get(i).getGrassHeight() != -999){
					totalHeightPoints += points.get(i).getGrassHeight();
					number++;
				}
			}
			if (number > 0){
				averageHeightPoints = totalHeightPoints / number;
				averageRef.setText(String.format("%.1f", averageHeightPoints) + " cm");
			}
		}

		if (areas.size() > 0){
			double totalHeightAreas = 0;
			double number = 0;
			for (int i=0;i<areas.size();i++){
				if (areas.get(i).getGrassHeight() != -999){
					totalHeightAreas += areas.get(i).getGrassHeight() * areas.get(i).getSize();
					number+= areas.get(i).getSize();
				}
			}
			if (number > 0){
				averageHeightAreas = totalHeightAreas / number;
				averageDam.setText(String.format("%.1f", averageHeightAreas) + " cm");
			}
		}
		if (averageHeightAreas != -999 && averageHeightPoints != -999){
			differenceHeight = averageHeightPoints - averageHeightAreas;
			difference.setText(String.format("%.1f", differenceHeight) + " cm");
		}
		if (areas.size() > 0){
			totalAreaHa = 0;
			for (int i=0;i<areas.size();i++){
				totalAreaHa += areas.get(i).getSize();
			}
			int kgDryMatterPer = Integer.parseInt(inputKgDryMatterPer.getText().toString());
			double euroPerKg = Double.parseDouble(inputEuroPerKg.getText().toString());
			totalArea.setText(String.format("%.2f", totalAreaHa / 10000) + " ha");
			double dryMatterKg = totalAreaHa / 10000 * differenceHeight * kgDryMatterPer;
			dryMatter.setText(String.format("%.2f", dryMatterKg) + " kg");
			compensation.setText("€ " + String.format("%.2f", dryMatterKg * euroPerKg));
		}
	}
	
	 
	 
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEdit:
			// For editing grassheight and/or name of reference point or damagearea
			btnDone.setEnabled(true);
			btnEdit.setEnabled(false);
			name.setVisibility(TextView.INVISIBLE);
			grassHeight.setVisibility(TextView.INVISIBLE);
			inputName.setVisibility(EditText.VISIBLE);
			inputGrassHeight.setVisibility(EditText.VISIBLE);
			if (selectingArea){
				inputName.setHint(selectedArea.getName());
				inputGrassHeight.setHint(String.format("%.0f", selectedArea.getGrassHeight()));
			}
			if (selectingPoint){
				inputName.setHint(selectedPoint.getName());
				inputGrassHeight.setHint(String.format("%.0f", selectedPoint.getGrassHeight()));
			}
			editing = true;
			break;
		case R.id.btnReCalculate:
			init();
			break;
		case R.id.btnDoneDamage:
			if (selectingArea){
				if(!inputName.getText().toString().equals("")){
					MainActivity.theCase.getDamageAreas().get(selectedAreaPosition).setName(inputName.getText().toString());
					name.setText(inputName.getText().toString());
				}
				if(!inputGrassHeight.getText().toString().equals("")){
					MainActivity.theCase.getDamageAreas().get(selectedAreaPosition).setGrassHeight(Double.parseDouble(inputGrassHeight.getText().toString()));
					grassHeight.setText(inputGrassHeight.getText().toString() + " cm");
				}
			}
			if (selectingPoint){
				if(!inputName.getText().toString().equals("")){
					MainActivity.theCase.getReferencePoints().get(selectedPointPosition).setName(inputName.getText().toString());
					name.setText(inputName.getText().toString());
				}
				if(!inputGrassHeight.getText().toString().equals("")){
					MainActivity.theCase.getReferencePoints().get(selectedPointPosition).setGrassHeight(Double.parseDouble(inputGrassHeight.getText().toString()));
					grassHeight.setText(inputGrassHeight.getText().toString() + " cm");
				}
			}
			inputName.setText("");
			inputGrassHeight.setText("");
			saveCase();
			init();

			btnDone.setEnabled(false);
			btnEdit.setEnabled(true);
			name.setVisibility(TextView.VISIBLE);
			grassHeight.setVisibility(TextView.VISIBLE);
			inputName.setVisibility(EditText.INVISIBLE);
			inputGrassHeight.setVisibility(EditText.INVISIBLE);
			editing = false;

			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inputGrassHeight.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);
			break;
		case R.id.btnDelete:
			if (selectingArea){
				MainActivity.theCase.removeDamageArea(selectedAreaPosition);

				Toast.makeText(getActivity().getApplicationContext(), "Area deleted", 
						Toast.LENGTH_SHORT).show();
			}
			if (selectingPoint){
				MainActivity.theCase.removeReferencePoint(selectedPointPosition);

				Toast.makeText(getActivity().getApplicationContext(), "Point deleted", 
						Toast.LENGTH_SHORT).show();
			}
			saveCase();
			init();
			noneSelected();

			break;
		}
	}

	private void noneSelected() {
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		name.setText("");
		area.setText("");
		grassHeight.setText("");
		date.setText("");
	}

	private void saveCase() {
		OutputStream out;
		try {
			out = getActivity().openFileOutput("schade_" + MainActivity.theCase.getRelatieID() + "_saved.xml", Context.MODE_PRIVATE);
			MainActivity.theCase.save(out);
			Toast.makeText(getActivity().getApplicationContext(), "Edit saved", 
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

	// For if something else than a editview is tapped
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

	@Override
	public void fragmentBecameVisible() {
		init();

	}

}
