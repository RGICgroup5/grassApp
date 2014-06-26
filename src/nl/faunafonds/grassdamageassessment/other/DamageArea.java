package nl.faunafonds.grassdamageassessment.other;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class DamageArea implements Parcelable {
	private String name;
	private String coords;
	private String date;
	private double size;
	private double grassHeight;

	public DamageArea(String k, double s, double gh){
		this.setName(k);
		this.setSize(s);
		this.setGrassHeight(gh);
		initDate();
		this.coords = "";
	}

	public DamageArea(String k, double s){
		this.setName(k);
		this.setSize(s);
		this.setGrassHeight(-999);
		initDate();
		this.coords = "";
	}

	public DamageArea(String k){
		this.setName(k);
		this.setSize(-999);
		this.setGrassHeight(-999);
		initDate();
		this.coords = "";
	}

	public DamageArea(){
		this.setName("");
		this.setSize(-999);
		this.setGrassHeight(-999);
		initDate();
		this.coords = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String kind) {
		this.name = kind;
	}

	public void setCoords(ArrayList<LatLng> polygon) {
		String polygonString = "";
		for (int i = 0; i<polygon.size(); i++) {
			if (i > 0){
				polygonString += ",";
			}
			polygonString += polygon.get(i).latitude + " " + polygon.get(i).longitude;
		}
		this.coords = polygonString;
		setSize(CalculatePolygonArea.calculateAreaOfGPSPolygonOnEarthInSquareMeters(Arrays.asList(getCoords())));
	}

	public void setCoords(String polygonString) {

		this.coords = polygonString;
		setSize(CalculatePolygonArea.calculateAreaOfGPSPolygonOnEarthInSquareMeters(Arrays.asList(getCoords())));
	}

	public LatLng[] getCoords() {

		ArrayList <LatLng> polygonPoints = new ArrayList<LatLng>();
		String[] verticesText = this.coords.split(",");
		for (int j=0; j<verticesText.length;j++){
			String[] ll = verticesText[j].split(" ");
			polygonPoints.add(new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1])));
		}
		LatLng[] add = new LatLng[polygonPoints.size()];
		add = polygonPoints.toArray(add);
		return add;
	}

	public String getCoordsString() {
		return this.coords;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	private void initDate(){
		Time now = new Time();
		now.setToNow();
		this.date = Integer.toString(now.monthDay) + "-" + Integer.toString(now.month + 1) + "-" + Integer.toString(now.year) + " " + 
				Integer.toString(now.hour) + ":" + Integer.toString(now.minute) + ":" + Integer.toString(now.second);
		
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getGrassHeight() {
		return grassHeight;
	}

	public void setGrassHeight(double grassHeight) {
		this.grassHeight = grassHeight;
	}

	@Override
	public String toString() {
		return name + "\n" + String.format("%.2f", size / 10000) + " ha";
	}

	//parcel part
	public DamageArea(Parcel in){
		String[] data= new String[3];
		double[] doubles= new double[2];

		in.readStringArray(data);
		this.name= data[0];
		this.coords = data[1];
		this.date = data[2];
		in.readDoubleArray(doubles);
		this.size = doubles[0];
		this.grassHeight = doubles[1];


	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub


		dest.writeStringArray(new String[]{this.name,this.coords, this.date});
		dest.writeDoubleArray(new double[]{this.size,this.grassHeight});

	}

	public static final Parcelable.Creator<DamageArea> CREATOR= new Parcelable.Creator<DamageArea>() {

		@Override
		public DamageArea createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new DamageArea(source);  //using parcelable constructor
		}

		@Override
		public DamageArea[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DamageArea[size];
		}
	};

}
