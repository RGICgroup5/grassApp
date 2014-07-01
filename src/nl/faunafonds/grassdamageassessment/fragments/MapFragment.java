package nl.faunafonds.grassdamageassessment.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import nl.faunafonds.grassdamageassessment.activities.MainActivity;
import nl.faunafonds.grassdamageassessment.R;
import nl.faunafonds.grassdamageassessment.R.id;
import nl.faunafonds.grassdamageassessment.R.layout;
import nl.faunafonds.grassdamageassessment.other.CalculatePolygonArea;
import nl.faunafonds.grassdamageassessment.other.ConvertCoordinates;
import nl.faunafonds.grassdamageassessment.parsers.ParseBRPfromKML;
import nl.faunafonds.grassdamageassessment.parsers.ParseCaseXML;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends Fragment implements OnClickListener, OnMapClickListener, FragmentsInterface{
	private GoogleMap googleMap;
	private ImageView crossHair;
	private ImageView legendWDVI;
	private ImageView legendGroenmonitor;
	private TextView legendTitle;
	private TextView legendLow;
	private TextView legendHigh;
	private ImageButton btnAddPolygonX;
	private ImageButton btnUndoX;
	private ImageButton btnCancelX;
	private ImageButton btnDoneX;
	private ImageButton btnAddReferenceX;
	private ImageButton btnCancelRefX;
	private ImageButton btnDoneRefX;
	private RadioGroup radioBasemap;
	private RadioGroup radioOverlay;
	private RadioGroup radioBoundaries;
	private boolean addingPolygon = false;
	private ArrayList<LatLng> newPolygon = new ArrayList<LatLng>();
	private ArrayList<Marker> vertices = new ArrayList<Marker>();
	private ArrayList<Marker> references = new ArrayList<Marker>();
	private ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	private Polygon startPolygon;
	private ArrayList<Polyline> lines = new ArrayList<Polyline>();
	private enum UndoAction {
		DELVERTEX, DELPOLYGON, DELREFERENCE, DONOTHING 
	}
	private ArrayList<UndoAction> undoAction = new ArrayList<UndoAction>(); 
	private LatLngBounds overlayBounds = null;
	private GroundOverlay satelliteOverlay = null;
	BitmapDescriptor imageGroenmonitor;
	BitmapDescriptor imageSpotRGB;
	BitmapDescriptor imageSpotWDVI;
	private boolean groenmonitor = false;
	private boolean spotrgb = false;
	private boolean spotwdvi = false;
	private boolean brp = false;
	private int polygonWidth = 5;
	private boolean overlayInitiated = false;
	private String imageGroenmonitorString;
	private String imageSpotRGBString;
	private String imageSpotWDVIString;
	private String textLegendWDVI;
	private String textLegendGroenmonitor;
	private ArrayList<Polygon> brpPols = new ArrayList<Polygon>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

		View map = inflater.inflate(R.layout.fragment_map, container, false);

		// Get gui objects and handle initial visibility
		crossHair = (ImageView) map.findViewById(R.id.crossHair);
		crossHair.setVisibility(View.INVISIBLE);
		
		legendWDVI = (ImageView) map.findViewById(R.id.legendWDVI);
		legendWDVI.setVisibility(View.INVISIBLE);
		
		legendTitle = (TextView) map.findViewById(R.id.textViewLegend); 
		legendLow = (TextView) map.findViewById(R.id.textViewLow); 
		legendHigh = (TextView) map.findViewById(R.id.textViewHigh); 
		
		legendTitle.setVisibility(View.INVISIBLE);
		legendLow.setVisibility(View.INVISIBLE);
		legendHigh.setVisibility(View.INVISIBLE);
		
		textLegendWDVI = getResources().getString(R.string.map_legend_WDVI);
		textLegendGroenmonitor = getResources().getString(R.string.map_legend_Groenmonitor);
		
		legendGroenmonitor = (ImageView) map.findViewById(R.id.legendGroenmonitor);
		legendGroenmonitor.setVisibility(View.INVISIBLE);

		// OnClickListeners and visibility for the buttons
		btnAddPolygonX = (ImageButton) map.findViewById(R.id.btnAddPolygonX);
		btnAddPolygonX.setOnClickListener(this);

		btnAddReferenceX = (ImageButton) map.findViewById(R.id.btnAddReferenceX);
		btnAddReferenceX.setOnClickListener(this);

		btnUndoX = (ImageButton) map.findViewById(R.id.btnUndoX);
		btnUndoX.setEnabled(false);
		btnUndoX.setVisibility(View.INVISIBLE);
		btnUndoX.setOnClickListener(this);

		btnCancelX = (ImageButton) map.findViewById(R.id.btnCancelX);
		btnCancelX.setEnabled(false);
		btnCancelX.setVisibility(View.INVISIBLE);
		btnCancelX.setOnClickListener(this);

		btnDoneX = (ImageButton) map.findViewById(R.id.btnDoneX);
		btnDoneX.setEnabled(false);
		btnDoneX.setVisibility(View.INVISIBLE);	
		btnDoneX.setOnClickListener(this);		

		btnCancelRefX = (ImageButton) map.findViewById(R.id.btnCancelRefX);
		btnCancelRefX.setEnabled(false);
		btnCancelRefX.setVisibility(View.INVISIBLE);
		btnCancelRefX.setOnClickListener(this);

		btnDoneRefX = (ImageButton) map.findViewById(R.id.btnDoneRefX);
		btnDoneRefX.setEnabled(false);
		btnDoneRefX.setVisibility(View.INVISIBLE);	
		btnDoneRefX.setOnClickListener(this);	

		radioBasemap = (RadioGroup) map.findViewById(R.id.radioBasemap);
		radioBasemap.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.radioHybrid:
					changeBasemap("radioHybrid");
					break;
				case R.id.radioTopographic:
					changeBasemap("radioTopographic");
					break;
				case R.id.radioNoBasemap:
					changeBasemap("radioNoBasemap");
					break;
				}
			}

		});
		
		radioOverlay = (RadioGroup) map.findViewById(R.id.radioOverlay);
		radioOverlay.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.radioGroenmonitor:
					changeMapOverlay("radioGroenmonitor");
					break;
				case R.id.radioSpotRGB:
					changeMapOverlay("radioSpotRGB");
					break;
				case R.id.radioSpotWDVI:
					changeMapOverlay("radioSpotWDVI");
					break;
				case R.id.radioNoOverlay:
					changeMapOverlay("radioNoOverlay");
					break;
				}
			}

		});
		
		radioBoundaries = (RadioGroup) map.findViewById(R.id.radioBoundaries);
		radioBoundaries.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.radioOn:
					changeBoundary("radioOn");
					break;
				case R.id.radioOff:
					changeBoundary("radioOff");
					break;
				}
			}

		});

		// Initial view for googleMap
		setUpMapIfNeeded();
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);

		googleMap.setOnMapClickListener(this);
		init();

		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				MainActivity.theCase.getLatLng(), 15));

		// Check if there are pictures for overlay and get the string for it.
		File[] files = getActivity().getFilesDir().listFiles();
		for (int i=0;i<files.length; i++){
			String[] file = files[i].toString().split("/");
			String name = file[file.length - 1];
			// initiate overlay if there is any
			if (name.startsWith("schade_" + MainActivity.theCase.getSchadeID()) && name.endsWith(".png") && overlayInitiated == false){
				String[] frags = name.split("_");
				double[] sw = ConvertCoordinates.RDtoLatLon(Double.parseDouble(frags[3]), Double.parseDouble(frags[4]));
				double[] ne = ConvertCoordinates.RDtoLatLon(Double.parseDouble(frags[5]), Double.parseDouble(frags[6].substring(0,6)));
				overlayBounds = new LatLngBounds(new LatLng(sw[0], sw[1]), new LatLng(ne[0], ne[1]));
				BitmapDescriptor image = BitmapDescriptorFactory.fromPath(getActivity().getFilesDir().getAbsolutePath() + "/" + name);
				satelliteOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions().image(image).positionFromBounds(overlayBounds));
				satelliteOverlay.setVisible(false);
				overlayInitiated  = true;
				// save strings to images for the overlays
			} if (name.startsWith("schade_" + MainActivity.theCase.getSchadeID() + "_groenmonitor") && name.endsWith(".png")){
				imageGroenmonitorString = getActivity().getFilesDir().getAbsolutePath() + "/" + name;
				groenmonitor = true;
			} if (name.startsWith("schade_" + MainActivity.theCase.getSchadeID() + "_spotrgb") && name.endsWith(".png")){
				imageSpotRGBString = getActivity().getFilesDir().getAbsolutePath() + "/" + name;
				spotrgb = true;
			} if (name.startsWith("schade_" + MainActivity.theCase.getSchadeID() + "_spotwdvi") && name.endsWith(".png")){
				imageSpotWDVIString = getActivity().getFilesDir().getAbsolutePath() + "/" + name;
				spotwdvi = true;
			} if ((name.startsWith("schade_" + MainActivity.theCase.getSchadeID() + "_BRP") || name.startsWith("schade_" + MainActivity.theCase.getSchadeID() + "_brp"))
					&& name.endsWith(".kml")){
				// If there is a brp kml file, parse it and draw polygons
				InputStream is;
				try {
					// Parse kml file
					is = new FileInputStream(new File(getActivity().getFilesDir().getAbsolutePath() + "/" + name));
					ParseBRPfromKML parser = new ParseBRPfromKML();
					ArrayList<ArrayList<LatLng>> boundaries = parser.parse(is);
					for (ArrayList<LatLng> pol : boundaries){
						// Add polygons to map
						PolygonOptions newPolygonOptions = new PolygonOptions();
						for (int j=0;j<pol.size();j++){
							newPolygonOptions.add(pol.get(j));
						}
						newPolygonOptions.strokeWidth(2);
						Polygon nwpolygon = googleMap.addPolygon(newPolygonOptions);
						nwpolygon.setVisible(false);
						brpPols.add(nwpolygon);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				brp = true;
			}
			
		}
		return map;
	}
	
	private void init() {
		// damage polygons and reference points removed
		for (int i=0; i<polygons.size();i++){
			polygons.get(i).remove();
		}
		polygons.clear();

		for (int i=0; i<references.size();i++){
			references.get(i).remove();
		}
		references.clear();
		
		// Adding damage polygons drawn by farmer
		
		PolygonOptions newPolygonOptions = new PolygonOptions();
		LatLng[] points = MainActivity.theCase.getStartAreaLatLng();
		for (int i=0;i<points.length;i++){
			newPolygonOptions.add(points[i]);
		}
		newPolygonOptions.fillColor(0x330012ff);
		newPolygonOptions.strokeWidth(polygonWidth);
		startPolygon = googleMap.addPolygon(newPolygonOptions);
		
		// Adding damage polygons drawn by assessor
		
		ArrayList<LatLng[]> savedPolygons = MainActivity.theCase.getPolygons();
		for (int i = 0; i < savedPolygons.size(); i++){
			newPolygonOptions = new PolygonOptions();
			for (int j=0;j<savedPolygons.get(i).length;j++){
				newPolygonOptions.add(savedPolygons.get(i)[j]);
			}
			newPolygonOptions.fillColor(0x7F00EF00);
			newPolygonOptions.strokeWidth(polygonWidth);
			Polygon nwpolygon = googleMap.addPolygon(newPolygonOptions);
			polygons.add(nwpolygon);
		}
		
		// Adding reference points added by assessor

		ArrayList<LatLng> savedReferences = MainActivity.theCase.getPoints();
		for (int i = 0; i < savedReferences.size(); i++){
			Marker newMarker = googleMap.addMarker(new MarkerOptions()
			.position(savedReferences.get(i))
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_reference))
			.title(MainActivity.theCase.getReferencePoints().get(i).getName()));
			references.add(newMarker);
		}
		
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddPolygonX:
			// Start adding polygon
			if (addingPolygon == false){
				Toast topToastPol = Toast.makeText(getActivity().getApplicationContext(), "Adding Polygon", 
						Toast.LENGTH_SHORT);
				topToastPol.setGravity(Gravity.TOP, 0, -75);
				topToastPol.show();
				crossHair.setVisibility(View.VISIBLE);

				addingPolygon = true;

				btnAddPolygonX.setBackgroundResource(R.drawable.button_add_vertex);
				btnUndoX.setEnabled(true);
				btnUndoX.setVisibility(View.VISIBLE);
				btnCancelX.setEnabled(true);
				btnCancelX.setVisibility(View.VISIBLE);
				btnDoneX.setEnabled(true);
				btnDoneX.setVisibility(View.VISIBLE);
				btnAddReferenceX.setEnabled(false);
			}
			else{
				// add vertex
				LatLng center = googleMap.getCameraPosition().target;
				Marker newMarker = googleMap.addMarker(new MarkerOptions()
				.position(center)
				.title("Vertex")
				.anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.polygon_vertex)));
				vertices.add(newMarker);
				newPolygon.add(center);
				undoAction.add(UndoAction.DELVERTEX); 

				if (newPolygon.size()>1){
					PolylineOptions lineOptions = new PolylineOptions()
					.add(newPolygon.get(newPolygon.size()-2))
					.add(newPolygon.get(newPolygon.size()-1)).width(polygonWidth);

					// Get back the mutable Polyline
					Polyline polyline = googleMap.addPolyline(lineOptions);
					lines.add(polyline);

				}

			}

			break;
		case R.id.btnAddReferenceX:
			// Start adding reference point

			Toast topToastRef = Toast.makeText(getActivity().getApplicationContext(), "Adding Reference", 
					Toast.LENGTH_SHORT);
			topToastRef.setGravity(Gravity.TOP, 0, -75);
			topToastRef.show();
			crossHair.setVisibility(View.VISIBLE);
			btnDoneRefX.setEnabled(true);
			btnDoneRefX.setVisibility(View.VISIBLE);
			btnCancelRefX.setEnabled(true);
			btnCancelRefX.setVisibility(View.VISIBLE);
			btnAddPolygonX.setEnabled(false);
			btnAddReferenceX.setEnabled(false);
			btnAddReferenceX.setVisibility(View.INVISIBLE);

			break;
		case R.id.btnDoneRefX:
			//Add reference point

			LatLng center = googleMap.getCameraPosition().target;
			MainActivity.theCase.addPoint(center);
			Marker newMarker = googleMap.addMarker(new MarkerOptions()
			.position(center)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_reference))
			.title(MainActivity.theCase.getReferencePoints().get(MainActivity.theCase.getReferencePoints().size() - 1).getName()));
			references.add(newMarker);

			// Save the case
			try {
				OutputStream out = getActivity().openFileOutput("schade_" + MainActivity.theCase.getRelatieID() + "_saved.xml", Context.MODE_PRIVATE);
				MainActivity.theCase.save(out);
				Toast.makeText(getActivity().getApplicationContext(), "Saved", 
						Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getActivity().getApplicationContext(), "Error", 
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			crossHair.setVisibility(View.INVISIBLE);
			btnDoneRefX.setEnabled(false);
			btnDoneRefX.setVisibility(View.INVISIBLE);
			btnCancelRefX.setEnabled(false);
			btnCancelRefX.setVisibility(View.INVISIBLE);
			btnAddReferenceX.setEnabled(true);
			btnAddReferenceX.setVisibility(View.VISIBLE);
			btnAddPolygonX.setEnabled(true);
			break;
		case R.id.btnCancelRefX:
			crossHair.setVisibility(View.INVISIBLE);
			btnDoneRefX.setEnabled(false);
			btnDoneRefX.setVisibility(View.INVISIBLE);
			btnCancelRefX.setEnabled(false);
			btnCancelRefX.setVisibility(View.INVISIBLE);
			btnAddReferenceX.setEnabled(true);
			btnAddReferenceX.setVisibility(View.VISIBLE);
			btnAddPolygonX.setEnabled(true);
			break;
		case R.id.btnDoneX:
			// Add polygon
			if (newPolygon.size() > 2){
				PolygonOptions newPolygonOptions = new PolygonOptions();
				for (int i=0;i<newPolygon.size();i++){
					newPolygonOptions.add(newPolygon.get(i));
				}
				newPolygonOptions.fillColor(0x7F00FF00);
				newPolygonOptions.strokeWidth(polygonWidth);
				Polygon nwpolygon = googleMap.addPolygon(newPolygonOptions);
				polygons.add(nwpolygon);
				MainActivity.theCase.addPolygon(newPolygon);
				undoAction.add(UndoAction.DELPOLYGON);
			}
			else{
				Toast.makeText(getActivity().getApplicationContext(), "Not enough vertices to make a polygon", 
						Toast.LENGTH_SHORT).show();
			}
			
			// Save case
			OutputStream out;
			try {
				out = getActivity().openFileOutput("schade_" + MainActivity.theCase.getRelatieID() + "_saved.xml", Context.MODE_PRIVATE);
				MainActivity.theCase.save(out);
				Toast.makeText(getActivity().getApplicationContext(), "Saved", 
						Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getActivity().getApplicationContext(), "Error", 
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			// Empty arraylists and removing markers and lines
			newPolygon.clear();
			for (int i=0;i<vertices.size();i++){
				vertices.get(i).remove();
			}
			for (int i=0;i<lines.size();i++){
				lines.get(i).remove();
			}
			vertices.clear();
			lines.clear();

			// Visibilities
			crossHair.setVisibility(View.INVISIBLE);
			btnUndoX.setEnabled(false);
			btnUndoX.setVisibility(View.INVISIBLE);
			btnDoneX.setEnabled(false);
			btnDoneX.setVisibility(View.INVISIBLE);
			btnCancelX.setEnabled(false);
			btnCancelX.setVisibility(View.INVISIBLE);
			addingPolygon = false;
			btnAddPolygonX.setBackgroundResource(R.drawable.button_add_polygon);
			btnAddReferenceX.setEnabled(true);
			break;
		case R.id.btnCancelX:
			Toast.makeText(getActivity().getApplicationContext(), "Adding canceled", 
					Toast.LENGTH_SHORT).show();
			// Empty arraylists and removing markers and lines
			newPolygon.clear();
			for (int i=0;i<vertices.size();i++){
				vertices.get(i).remove();
			}
			for (int i=0;i<lines.size();i++){
				lines.get(i).remove();
			}
			vertices.clear();
			lines.clear();

			// Visibilities
			crossHair.setVisibility(View.INVISIBLE);
			btnUndoX.setEnabled(false);
			btnUndoX.setVisibility(View.INVISIBLE);
			btnDoneX.setEnabled(false);
			btnDoneX.setVisibility(View.INVISIBLE);
			btnCancelX.setEnabled(false);
			btnCancelX.setVisibility(View.INVISIBLE);
			btnAddReferenceX.setEnabled(true);
			addingPolygon = false;
			btnAddPolygonX.setBackgroundResource(R.drawable.button_add_polygon);
			break;
		case R.id.btnUndoX:
			switch (undoAction.get(undoAction.size() - 1)){

			// Delete last vertex
			case DELVERTEX:
				if (vertices.size() == 1){
					vertices.get(0).remove();
					vertices.clear();
				}
				else{
					vertices.get(vertices.size() - 1).remove();
					vertices.remove(vertices.size() - 1);
					lines.get(lines.size() - 1).remove();
					lines.remove(lines.size() - 1);
				}
				newPolygon.remove(newPolygon.size() - 1);
				undoAction.remove(undoAction.size() - 1);
				break;
			default:
				Toast.makeText(getActivity().getApplicationContext(), "Nothing to undo", 
						Toast.LENGTH_SHORT).show();
				break;


			}
			break;

		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap(); // Check if we were
			// successful in
			// obtaining the
			// map.
			if (googleMap != null) { // The Map is verified. It is now safe to
				// manipulate the map.
			}
		}
	}

	private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
		// Check if point is inside polygon
		int intersectCount = 0;
		for(int j=0; j<vertices.size()-1; j++) {
			if( rayCastIntersect(tap, vertices.get(j), vertices.get(j+1)) ) {
				intersectCount++;
			}
		}

		return (intersectCount%2) == 1; // odd = inside, even = outside;
	}

	private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {
		double aY = vertA.latitude;
		double bY = vertB.latitude;
		double aX = vertA.longitude;
		double bX = vertB.longitude;
		double pY = tap.latitude;
		double pX = tap.longitude;

		if ( (aY>pY && bY>pY) || (aY<pY && bY<pY) || (aX<pX && bX<pX) ) {
			return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
		}

		double m = (aY-bY) / (aX-bX);               // Rise over run
		double bee = (-aX) * m + aY;                // y = mx + b
		double x = (pY - bee) / m;                  // algebra is neat!

		return x > pX;
	}

	@Override
	public void onMapClick(LatLng tap) {
		// Check if polygon is tapped
		int nrtouched = 0;
		boolean hit = false;
		ArrayList<LatLng> vertices = new ArrayList<LatLng>(Arrays.asList(MainActivity.theCase.getInitialAreas().get(0).getCoords()));
		if (isPointInPolygon(tap, vertices)){
			hit = true;
			Toast.makeText(getActivity().getApplicationContext(), MainActivity.theCase.getInitialAreas().get(0).getName() 
					+ String.format(" (%.2f ha)", MainActivity.theCase.getInitialAreas().get(0).getSize() / 10000), 
					Toast.LENGTH_SHORT).show();
		}
		else{
			if (polygons.size() > 0){
				for (int i = 0; i < polygons.size(); i++){
					vertices = new ArrayList<LatLng>(Arrays.asList(MainActivity.theCase.getDamageAreas().get(i).getCoords()));
					if (isPointInPolygon(tap, vertices)){
						nrtouched = i;
						hit = true;
					}
				}
			}
			if (hit){
				Toast.makeText(getActivity().getApplicationContext(), MainActivity.theCase.getDamageAreas().get(nrtouched).getName() 
						+ String.format(" (%.2f ha)", MainActivity.theCase.getDamageAreas().get(nrtouched).getSize() / 10000), 
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void fragmentBecameVisible() {
		init();

	}

	private void changeBasemap(String basemap) {
		// Change basemap
		if (basemap == "radioHybrid") {
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
		else if (basemap == "radioTopographic") {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}
		else if (basemap == "radioNoBasemap") {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		}

	}
	
	private void changeMapOverlay(String overlay) {
		// change overlay
		if (overlayInitiated) {
			satelliteOverlay.setVisible(true);
		}
		if (overlay == "radioGroenmonitor") {
			hideLegend();
			if (groenmonitor) {
				satelliteOverlay.setImage(BitmapDescriptorFactory.fromPath(imageGroenmonitorString));
				legendGroenmonitor.setVisibility(View.VISIBLE);
				legendTitle.setText(textLegendGroenmonitor);
				legendTitle.setVisibility(View.VISIBLE);
				legendLow.setVisibility(View.VISIBLE);
				legendHigh.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No groenmonitor picture available", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (overlay == "radioSpotRGB") {
			hideLegend();
			if (spotrgb) {
				satelliteOverlay.setImage(BitmapDescriptorFactory
						.fromPath(imageSpotRGBString));
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No Spot RGB picture available", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (overlay == "radioSpotWDVI") {
			hideLegend();
			if (spotwdvi) {
				satelliteOverlay.setImage(BitmapDescriptorFactory
						.fromPath(imageSpotWDVIString));
				legendWDVI.setVisibility(View.VISIBLE);
				legendTitle.setText(textLegendWDVI);
				legendTitle.setVisibility(View.VISIBLE);
				legendLow.setVisibility(View.VISIBLE);
				legendHigh.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No Spot WDVI picture available", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (overlay == "radioNoOverlay") {
			hideLegend();
			satelliteOverlay.setVisible(false);
		}
	}

	private void hideLegend() {
		legendWDVI.setVisibility(View.INVISIBLE);
		legendGroenmonitor.setVisibility(View.INVISIBLE);
		legendTitle.setVisibility(View.INVISIBLE);
		legendLow.setVisibility(View.INVISIBLE);
		legendHigh.setVisibility(View.INVISIBLE);
	}
	
	private void changeBoundary(String switchBoundary) {
		// Change boundary polygons
		if (switchBoundary == "radioOn") {
			if (brp){
				for (Polygon pol : brpPols){
					pol.setVisible(true);
				}	
			}
			else{
				Toast.makeText(getActivity().getApplicationContext(), "No boundaries available", Toast.LENGTH_SHORT).show();	
			}
			
		}
		else if (switchBoundary == "radioOff") {
			if (brp){
				for (Polygon pol : brpPols){
					pol.setVisible(false);
				}				
			}
		}
	}
}
