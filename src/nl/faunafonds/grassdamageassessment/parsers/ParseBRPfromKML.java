package nl.faunafonds.grassdamageassessment.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.gms.maps.model.LatLng;

public class ParseBRPfromKML {
	ArrayList<ArrayList<LatLng>> polygons;
	private ArrayList<LatLng> polygon;
	private String text;

	public ParseBRPfromKML() {
		polygons = new ArrayList<ArrayList<LatLng>>();
	}

	public ArrayList<ArrayList<LatLng>> getEmployees() {
		return polygons;
	}

	public ArrayList<ArrayList<LatLng>> parse(InputStream is) {
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try {
			
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				parser = factory.newPullParser();

				parser.setInput(is, null);

				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String tagname = parser.getName();
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if (tagname.equalsIgnoreCase("coordinates")) {
							// create a new instance of case
							polygon = new ArrayList<LatLng>();
						} 
						break;

					case XmlPullParser.TEXT:
						text = parser.getText();
						break;

					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase("coordinates")) {
							String string = text.trim();
							string = text.trim().substring(0, string.length() - 4);
							String coords[] = string.split(",0 ");
							for (int i=0;i<coords.length;i++){
								String[] xy = coords[i].split(",");
								LatLng newLatLng = new LatLng(Double.parseDouble(xy[1]), Double.parseDouble(xy[0]));
								polygon.add(newLatLng);
							}
							polygons.add(polygon);
						} 
						break;

					default:
						break;
					}
					eventType = parser.next();
				
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return polygons;
	}
}