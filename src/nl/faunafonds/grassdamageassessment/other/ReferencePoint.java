package nl.faunafonds.grassdamageassessment.other;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class ReferencePoint implements Parcelable {
	private String name;
	private String coords;
	private String date;
	private double grassHeight;

	public ReferencePoint(String k, double gh){
		this.setName(k);
		this.setGrassHeight(gh);
		initDate();
		this.coords = "";
	}

	public ReferencePoint(String k){
		this.setName(k);
		this.setGrassHeight(-999);
		initDate();
		this.coords = "";
	}

	public ReferencePoint(){
		this.setName("");
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

	public void setCoords(LatLng coord) {
		String coordString = "";
		
			coordString += coord.latitude + " " + coord.longitude;
		
		this.coords = coordString;
	}

	public void setCoords(String coordString) {

		this.coords = coordString;
	}

	public LatLng getCoords() {

		LatLng point;
		
		
			String[] ll = this.coords.split(" ");
			point = new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]));
		
		return point;
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

	public double getGrassHeight() {
		return grassHeight;
	}

	public void setGrassHeight(double grassHeight) {
		this.grassHeight = grassHeight;
	}

	@Override
	public String toString() {
		return name;
	}

	//parcel part
	public ReferencePoint(Parcel in){
		String[] data= new String[3];
		double[] doubles= new double[1];

		in.readStringArray(data);
		this.name= data[0];
		this.coords = data[1];
		this.date = data[2];
		in.readDoubleArray(doubles);
		this.grassHeight = doubles[0];


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
		dest.writeDoubleArray(new double[]{this.grassHeight});

	}

	public static final Parcelable.Creator<ReferencePoint> CREATOR= new Parcelable.Creator<ReferencePoint>() {

		@Override
		public ReferencePoint createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ReferencePoint(source);  //using parcelable constructor
		}

		@Override
		public ReferencePoint[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ReferencePoint[size];
		}
	};

}
