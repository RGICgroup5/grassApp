package nl.faunafonds.grassdamageassessment.other;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import com.google.android.gms.maps.model.LatLng;

public class Relation implements Parcelable {
		private String relatieID;
		private String name;
		private String address;
		private String zipCode;
		private String city;
		private String province;
		
		public Relation(){
			this.relatieID ="unknown";
			this.name ="unknown";
			this.address ="unknown";
			this.zipCode ="unknown";
			this.city ="unknown";
			this.province ="unknown";
		}

		public String getRelatieID() {
			return relatieID;
		}

		public void setRelatieID(String kind) {
			this.relatieID = kind;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String kind) {
			this.name = kind;
		}
		
		public String getAddress() {
			return address;
		}

		public void setAddress(String kind) {
			this.address = kind;
		}
		
		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String kind) {
			this.zipCode = kind;
		}
		
		public String getCity() {
			return city;
		}

		public void setCity(String kind) {
			this.city = kind;
		}
		
		public String getProvince() {
			return province;
		}

		public void setProvince(String kind) {
			this.province = kind;
		}

		

		@Override
		public String toString() {
			return name;
		}

		//parcel part
		public Relation(Parcel in){
			String[] data= new String[6];
			in.readStringArray(data);
			this.relatieID= data[0];
			this.name = data[1];
			this.address = data[2];
			this.zipCode = data[3];
			this.city = data[4];
			this.province = data[5];
			


		}
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub


			dest.writeStringArray(new String[]{this.relatieID,this.name, this.address, this.zipCode,this.city,this.province});
			
		}

		public static final Parcelable.Creator<Relation> CREATOR= new Parcelable.Creator<Relation>() {

			@Override
			public Relation createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				return new Relation(source);  //using parcelable constructor
			}

			@Override
			public Relation[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Relation[size];
			}
		};

	}
