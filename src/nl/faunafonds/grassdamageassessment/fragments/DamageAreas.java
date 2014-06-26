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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class DamageAreas extends Fragment implements OnClickListener, FragmentsInterface{
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


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		damageAreasFrag = inflater.inflate(R.layout.damage_frag, container, false);

		// TextViews
		//((TextView)damageAreasFrag.findViewById(R.id.textView)).setText("Damage Areas");
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
		//btnDone.setVisibility(View.INVISIBLE);

		btnDelete = (Button) damageAreasFrag.findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		btnDelete.setEnabled(false);

		btnReCalculate = (Button) damageAreasFrag.findViewById(R.id.btnReCalculate);
		btnReCalculate.setOnClickListener(this);

		np = (NumberPicker) damageAreasFrag.findViewById(R.id.np);
		np.setOnValueChangedListener(new OnValueChangeListener()
        {
            public void onValueChange(NumberPicker picker, int oldVal, 
                int newVal)
            {
                //tv.setText(String.valueOf(newVal)); 
            }        
        });

        np.setMaxValue(100);
        np.setMinValue(0);

		init();

		return damageAreasFrag;
	}

	public void init(){
		

		averageHeightAreas = -999;
		averageHeightPoints = -999;
		areas = new ArrayList<DamageArea>();
		areas.addAll(MainActivity.theCase.getInitialAreas());
		areas.addAll(MainActivity.theCase.getDamageAreas());

		listView.setAdapter(new ArrayAdapter<DamageArea>(getActivity(),R.layout.list_item, areas));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (editing == false){
					if (position >= MainActivity.theCase.getInitialAreas().size()){
						btnEdit.setEnabled(true);
						btnDelete.setEnabled(true);
					}
					else {
						btnEdit.setEnabled(false);
						btnDelete.setEnabled(false);
					}
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

		points = new ArrayList<ReferencePoint>();
		points.addAll(MainActivity.theCase.getReferencePoints());
		listView2.setAdapter(new ArrayAdapter<ReferencePoint>(getActivity(),R.layout.list_item, points));
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

		if (areas.size() > MainActivity.theCase.getInitialAreas().size()){
			double totalHeightAreas = 0;
			int number = 0;
			for (int i=MainActivity.theCase.getInitialAreas().size() - 1;i<areas.size();i++){
				if (areas.get(i).getGrassHeight() != -999){
					totalHeightAreas += areas.get(i).getGrassHeight();
					number++;
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
		if (areas.size() > MainActivity.theCase.getInitialAreas().size()){
			totalAreaHa = 0;
			for (int i=0;i<areas.size();i++){
				totalAreaHa += areas.get(i).getSize();
			}
			int kgDryMatterPer = Integer.parseInt(inputKgDryMatterPer.getText().toString());
			double euroPerKg = Double.parseDouble(inputEuroPerKg.getText().toString());
			totalArea.setText(String.format("%.2f", totalAreaHa / 10000) + " ha");
			double dryMatterKg = totalAreaHa / 10000 * differenceHeight * kgDryMatterPer;
			dryMatter.setText(String.format("%.2f", dryMatterKg) + " kg");
			compensation.setText(String.format("€ %.2f", dryMatterKg * euroPerKg));
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEdit:
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
					MainActivity.theCase.getDamageAreas().get(selectedAreaPosition - MainActivity.theCase.getInitialAreas().size()).setName(inputName.getText().toString());
					name.setText(inputName.getText().toString());
				}
				if(!inputGrassHeight.getText().toString().equals("")){
					MainActivity.theCase.getDamageAreas().get(selectedAreaPosition - MainActivity.theCase.getInitialAreas().size()).setGrassHeight(Double.parseDouble(inputGrassHeight.getText().toString()));
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
				MainActivity.theCase.removeDamageArea(selectedAreaPosition - MainActivity.theCase.getInitialAreas().size());

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
			firstSelected();

			break;
		}
	}

	private void firstSelected() {
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		selectedArea = areas.get(0);
		selectedAreaPosition = 0;
		name.setText(selectedArea.getName());
		area.setText(String.format("%.2f", selectedArea.getSize() / 10000) + " ha");
		grassHeight.setText(String.format("%.0f", selectedArea.getGrassHeight()) + " cm");
	}

	private void saveCase() {
		OutputStream out;
		try {
			/*File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(downloadsDir, "schade_test_saved.xml");
			if(file.exists())
	            file.delete();
	        file.createNewFile();
			out = new FileOutputStream(file);*/
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


	@Override
	public void fragmentBecameVisible() {
		init();

	}

}
