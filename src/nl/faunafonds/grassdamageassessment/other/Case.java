package nl.faunafonds.grassdamageassessment.other;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlSerializer;

import com.google.android.gms.drive.internal.x;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;

public class Case implements Parcelable{
	private String relatieID;
	private String address;
	private String crop;
	private String email;
	private String dierSoort1;
	private String dierSoort2;
	private String dierSoort3;
	private String datum;
	private String startArea;
	private String latitude;
	private String longitude;
	private String savedDate;
	private String schadeID;
	private String farmerComments;
	private String barrier;
	private String scareCrows;
	private String sticks;
	private String noiseMachines;
	private String chased;
	private String hunting;
	private String damageContinues;
	private String generalComments;
	private ArrayList<DamageArea> damageAreas = new ArrayList<DamageArea>();
	private ArrayList<DamageArea> initialAreas = new ArrayList<DamageArea>();
	private ArrayList<ReferencePoint> referencePoints = new ArrayList<ReferencePoint>();
	private Relation relation = new Relation();
	

	public Case(){
		this.relatieID= null;
		this.address= null;
		this.crop= null;
		this.email= null;
		this.dierSoort1= null;
		this.dierSoort2= null;
		this.dierSoort3= null;
		this.datum= null;
		this.startArea = null;
		this.latitude = null;
		this.longitude = null;
		this.savedDate = "nvt";
		this.schadeID = null;
		this.farmerComments = null;
		this.barrier = null;
		this.scareCrows = null;
		this.sticks = null;
		this.noiseMachines = null;
		this.chased = null;
		this.hunting = null;
		this.damageContinues = null;
		this.generalComments = "";
	}
	
	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public String getRelatieID() {
		return relatieID;
	}

	public void setRelatieID(String name) {
		this.relatieID = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String department) {
		this.address = department;
	}

	public String getCrop() {
		return crop;
	}

	public void setCrop(String type) {
		this.crop = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDierSoort1() {
		return dierSoort1;
	}

	public void setDierSoort1(String dierSoort1) {
		this.dierSoort1 = dierSoort1;
	}

	public String getDierSoort2() {
		return dierSoort2;
	}

	public void setDierSoort2(String dierSoort2) {
		this.dierSoort2 = dierSoort2;
	}

	public String getDierSoort3() {
		return dierSoort3;
	}

	public void setDierSoort3(String dierSoort3) {
		this.dierSoort3 = dierSoort3;
	}
	
	public String getDierSoorten() {
		String result = "";
		if (!this.dierSoort1.equals("0")){result += this.dierSoort1;}
		if (!this.dierSoort2.equals("0")){result += " / " + this.dierSoort2;}
		if (!this.dierSoort3.equals("0")){result += " / " + this.dierSoort3;}
		return result;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getStartArea() {
		return startArea;
	}

	public void setStartArea(String startArea) {
		this.startArea = startArea;
		setInitialArea(startArea);
	}
	
	private void setInitialArea(String startArea) {
		DamageArea nwInitialArea = new DamageArea("Aanvrager veld " + initialAreas.size());
		String text;
		if (startArea.startsWith("POLYGON((")){
		text = startArea.substring(9,startArea.length() - 2);
		}
		else {
			text = startArea.substring(10,startArea.length() - 2);
		}
		String[] pointsText = text.split(",");
		ArrayList<LatLng> result = new ArrayList<LatLng>();
		for (int i = 0; i < pointsText.length; i++){
			String[] xy = pointsText[i].trim().split(" ");
			double lon;
			double lat;
			if (xy[0].contains("e")){
			String[] x = xy[0].split("e");
			String[] y = xy[1].split("e");
			lon = Double.parseDouble(x[0]) * Math.pow(10, Integer.parseInt(x[1]));
			lat = Double.parseDouble(y[0]) * Math.pow(10, Integer.parseInt(y[1]));
			}
			else {
				lon = Double.parseDouble(xy[0]);
				lat = Double.parseDouble(xy[1]);
			}
			
			double[] latlon = ConvertCoordinates.RDtoLatLon(lon, lat);
			result.add(new LatLng(latlon[0], latlon[1]));
		}
		
		
		nwInitialArea.setCoords(result);
		this.initialAreas.add(nwInitialArea);
	}
	
	public ArrayList<DamageArea> getInitialAreas() {
		return this.initialAreas;
	}

	public void setLatitude(String lat) {
		this.latitude = lat;
	}

	public void setLongitude(String lon) {
		this.longitude = lon;
	}

	public ArrayList<DamageArea> getDamageAreas() {
		return damageAreas;
	}
	
	public void addDamageArea(DamageArea area) {
		this.damageAreas.add(area);
	}
	
	public ArrayList<ReferencePoint> getReferencePoints() {
		return referencePoints;
	}
	
	public void addReferencePoint(ReferencePoint point) {
		this.referencePoints.add(point);
	}

	public ArrayList<LatLng[]> getPolygons() {
		ArrayList<LatLng[]> polygonsLatLng = new ArrayList<LatLng[]>();

		for (int i=0; i<this.damageAreas.size(); i++){
			
			LatLng[] add = damageAreas.get(i).getCoords();
			polygonsLatLng.add(add);
		}
		return polygonsLatLng;
	}
	
	public ArrayList<LatLng> getPoints() {
		ArrayList<LatLng> pointsLatLng = new ArrayList<LatLng>();

		for (int i=0; i<this.referencePoints.size(); i++){
			
			LatLng add = referencePoints.get(i).getCoords();
			pointsLatLng.add(add);
		}
		return pointsLatLng;
	}

	public void addPolygon(ArrayList<LatLng> polygon) {
		DamageArea nwDamageArea = new DamageArea("Taxateur veld " + (damageAreas.size() + 1));
		nwDamageArea.setCoords(polygon);
		this.damageAreas.add(nwDamageArea);
	}
	
	public void addPolygon(String polygonString) {
		DamageArea nwDamageArea = new DamageArea("Taxateur veld" + (damageAreas.size() + 1));
		nwDamageArea.setCoords(polygonString);
		this.damageAreas.add(nwDamageArea);
	}
	
	public void addPoint(LatLng point) {
		ReferencePoint nwReferencePoint = new ReferencePoint("Referentiepunt " + (referencePoints.size() + 1));
		nwReferencePoint.setCoords(point);
		this.referencePoints.add(nwReferencePoint);
	}
	
	public void addPoint(String pointString) {
		ReferencePoint nwReferencePoint = new ReferencePoint("Taxateur veld" + (referencePoints.size() + 1));
		nwReferencePoint.setCoords(pointString);
		this.referencePoints.add(nwReferencePoint);
	}
	
	public void removeDamageArea(int i) {
		this.damageAreas.remove(i);
		
	}
	
	public void removeReferencePoint(int i) {
		this.referencePoints.remove(i);
		
	}

	public LatLng getLatLng(){
		return new LatLng(Double.parseDouble(this.latitude), Double.parseDouble(this.longitude));
	}

	public LatLng[] getStartAreaLatLng(){
		return initialAreas.get(0).getCoords();
		
	}

	private String writeXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "FaunafondsSchade");
			serializer.startTag("", "Schade");
			serializer.startTag("", "relatieID");
			serializer.text(this.relatieID);
			serializer.endTag("", "relatieID");
			serializer.startTag("", "datum_melding");
			serializer.text(this.datum);
			serializer.endTag("", "datum_melding");
			serializer.startTag("", "diersoort1");
			serializer.text(this.dierSoort1);
			serializer.endTag("", "diersoort1");
			serializer.startTag("", "diersoort2");
			serializer.text(this.dierSoort2);
			serializer.endTag("", "diersoort2");
			serializer.startTag("", "diersoort3");
			serializer.text(this.dierSoort3);
			serializer.endTag("", "diersoort3");
			serializer.startTag("", "perceelgeowkt");
			serializer.text(this.startArea);
			serializer.endTag("", "perceelgeowkt");
			serializer.startTag("", "perceellat");
			serializer.text(this.latitude);
			serializer.endTag("", "perceellat");
			serializer.startTag("", "perceellon");
			serializer.text(this.longitude);
			serializer.endTag("", "perceellon");
			serializer.startTag("", "gewas");
			serializer.text(this.crop);
			serializer.endTag("", "gewas");
			
			serializer.startTag("", "schadeID");
			serializer.text(this.schadeID);
			serializer.endTag("", "schadeID");
			serializer.startTag("", "opmerkingen_aanvrager");
			serializer.text(this.farmerComments);
			serializer.endTag("", "opmerkingen_aanvrager");
			serializer.startTag("", "opmerkingen_taxateur");
			serializer.text(this.generalComments);
			serializer.endTag("", "opmerkingen_taxateur");
			serializer.startTag("", "schade_zetvoort");
			serializer.text(this.damageContinues);
			serializer.endTag("", "schade_zetvoort");
			serializer.startTag("", "preventie_afrasteringdatum");
			serializer.text(this.barrier);
			serializer.endTag("", "preventie_afrasteringdatum");
			serializer.startTag("", "preventie_vogelverschrikkeraantal");
			serializer.text(this.scareCrows);
			serializer.endTag("", "preventie_vogelverschrikkeraantal");
			serializer.startTag("", "preventie_stokkenaantal");
			serializer.text(this.sticks);
			serializer.endTag("", "preventie_stokkenaantal");
			serializer.startTag("", "preventie_knalapparaataantal");
			serializer.text(this.noiseMachines);
			serializer.endTag("", "preventie_knalapparaataantal");
			serializer.startTag("", "preventie_verjaagdaantal");
			serializer.text(this.chased);
			serializer.endTag("", "preventie_verjaagdaantal");
			serializer.startTag("", "preventie_bejaagdaantal");
			serializer.text(this.hunting);
			serializer.endTag("", "preventie_bejaagdaantal");			
			serializer.startTag("", "savedatum");
			serializer.text(this.savedDate);
			serializer.endTag("", "savedatum");
			
			for (int i=0;i<this.damageAreas.size();i++){
				serializer.startTag("", "damageArea");
				serializer.startTag("", "name");
				serializer.text(this.damageAreas.get(i).getName());
				serializer.endTag("", "name");
				serializer.startTag("", "coords");
				serializer.text(this.damageAreas.get(i).getCoordsString());
				serializer.endTag("", "coords");
				serializer.startTag("", "grassHeight");
				serializer.text(String.format("%.0f", this.damageAreas.get(i).getGrassHeight()));
				serializer.endTag("", "grassHeight");
				serializer.startTag("", "gemaaktop");
				serializer.text(this.damageAreas.get(i).getDate());
				serializer.endTag("", "gemaaktop");
				serializer.endTag("", "damageArea");
			}	
			for (int i=0;i<this.referencePoints.size();i++){
				serializer.startTag("", "referencePoint");
				serializer.startTag("", "refname");
				serializer.text(this.referencePoints.get(i).getName());
				serializer.endTag("", "refname");
				serializer.startTag("", "refcoord");
				serializer.text(this.referencePoints.get(i).getCoordsString());
				serializer.endTag("", "refcoord");
				serializer.startTag("", "refgrassHeight");
				serializer.text(String.format("%.0f", this.referencePoints.get(i).getGrassHeight()));
				serializer.endTag("", "refgrassHeight");
				serializer.startTag("", "refgemaaktop");
				serializer.text(this.referencePoints.get(i).getDate());
				serializer.endTag("", "refgemaaktop");
				serializer.endTag("", "referencePoint");
			}
			serializer.endTag("", "Schade");
			serializer.endTag("", "FaunafondsSchade");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public void save(OutputStream out){
		try {
			Time now = new Time();
			now.setToNow();
			this.savedDate = Integer.toString(now.monthDay) + "-" + Integer.toString(now.month + 1) + "-" + Integer.toString(now.year) + " " + 
					Integer.toString(now.hour) + ":" + Integer.toString(now.minute) + ":" + Integer.toString(now.second);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
	        outputStreamWriter.write(writeXml());
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
		
	}

	@Override
	public String toString() {
		return "naam: " + this.relation.getName() + "\nAdres: " + this.relation.getAddress() + ", " + this.relation.getCity() + "\nGewas: " + crop
				+ "\nDatum: " + datum + "\nOpgeslagen: " + savedDate;
	}

	//parcel part
	public Case(Parcel in){
		String[] data= new String[22];

		in.readStringArray(data);
		this.relatieID= data[0];
		this.address= data[1];
		this.crop= data[2];
		this.email= data[3];
		this.dierSoort1= data[4];
		this.dierSoort2= data[5];
		this.dierSoort3= data[6];
		this.datum= data[7];
		this.startArea = data[8];
		this.latitude = data[9];
		this.longitude = data[10];
		this.savedDate = data[11];
		this.schadeID = data[12];
		this.farmerComments = data[13];
		this.barrier = data[14];
		this.scareCrows = data[15];
		this.sticks = data[16];
		this.noiseMachines = data[17];
		this.chased = data[18];
		this.hunting = data[19];
		this.damageContinues = data[20];
		this.generalComments = data[21];
		damageAreas = new ArrayList<DamageArea>();
		in.readTypedList(damageAreas, DamageArea.CREATOR);
		initialAreas = new ArrayList<DamageArea>();
		in.readTypedList(initialAreas, DamageArea.CREATOR);
		referencePoints = new ArrayList<ReferencePoint>();
		in.readTypedList(referencePoints, ReferencePoint.CREATOR);
		this.relation = in.readParcelable(Relation.class.getClassLoader());

	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		
		dest.writeStringArray(new String[]{this.relatieID,this.address,this.crop,this.email,this.dierSoort1,this.dierSoort2,this.dierSoort3,this.datum,this.startArea,this.latitude,this.longitude,this.savedDate,this.schadeID,this.farmerComments,this.barrier,this.scareCrows,this.sticks,this.noiseMachines,this.chased,this.hunting,this.damageContinues,this.generalComments});
		dest.writeTypedList(this.damageAreas);
		dest.writeTypedList(this.initialAreas);
		dest.writeTypedList(this.referencePoints);
		dest.writeParcelable(this.relation, flags);
	}


	
	public String getSavedDate() {
		return savedDate;
	}

	public void setSavedDate(String savedDate) {
		this.savedDate = savedDate;
	}

	public String getSchadeID() {
		return schadeID;
	}

	public void setSchadeID(String schadeID) {
		this.schadeID = schadeID;
	}

	public String getFarmerComments() {
		return farmerComments;
	}

	public void setFarmerComments(String farmerComments) {
		this.farmerComments = farmerComments;
	}

	public String getBarrier() {
		return barrier;
	}

	public void setBarrier(String barrier) {
		this.barrier = barrier;
	}

	public String getScareCrows() {
		return scareCrows;
	}

	public void setScareCrows(String scareCrows) {
		this.scareCrows = scareCrows;
	}

	public String getSticks() {
		return sticks;
	}

	public void setSticks(String sticks) {
		this.sticks = sticks;
	}

	public String getNoiseMachines() {
		return noiseMachines;
	}

	public void setNoiseMachines(String noiseMachines) {
		this.noiseMachines = noiseMachines;
	}

	public String getChased() {
		return chased;
	}

	public void setChased(String chased) {
		this.chased = chased;
	}

	public String getHunting() {
		return hunting;
	}

	public void setHunting(String hunting) {
		this.hunting = hunting;
	}

	public String getDamageContinues() {
		return damageContinues;
	}

	public void setDamageContinues(String schade_zetvoort) {
		this.damageContinues = schade_zetvoort;
	}

	public static final Parcelable.Creator<Case> CREATOR= new Parcelable.Creator<Case>() {

		@Override
		public Case createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Case(source);  //using parcelable constructor
		}

		@Override
		public Case[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Case[size];
		}
	};


	public boolean damageContinues() {
		return (this.damageContinues.equals("true"));
		
	}

	public String getGeneralComments() {
		return generalComments;
	}

	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	
}
