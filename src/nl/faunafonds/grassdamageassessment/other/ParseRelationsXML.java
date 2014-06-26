package nl.faunafonds.grassdamageassessment.other;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ParseRelationsXML {
	List<Relation> relations;
	private Relation newRelation;
	private String text;

	public ParseRelationsXML() {
		relations = new ArrayList<Relation>();
	}

	public List<Relation> getEmployees() {
		return relations;
	}

	public List<Relation> parse(ArrayList<InputStream> is) {
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try {
			for (int i = 0; i < is.size(); i++) {
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				parser = factory.newPullParser();

				parser.setInput(is.get(i), null);

				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String tagname = parser.getName();
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if (tagname.equalsIgnoreCase("relatie")) {
							// create a new instance of case
							newRelation = new Relation();
						} 
						break;

					case XmlPullParser.TEXT:
						text = parser.getText();
						break;

					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase("relatie")) {
							relations.add(newRelation);
						} else if (tagname.equalsIgnoreCase("relatieID")) {
							newRelation.setRelatieID(text);
						} else if (tagname.equalsIgnoreCase("naam")) {
							newRelation.setName(text);
						} else if (tagname.equalsIgnoreCase("adres")) {
							newRelation.setAddress(text);
						} else if (tagname.equalsIgnoreCase("postcode")) {
							newRelation.setZipCode(text);
						} else if (tagname.equalsIgnoreCase("plaats")) {
							newRelation.setCity(text);
						} else if (tagname.equalsIgnoreCase("provincie")) {
							newRelation.setProvince(text);
						} 
						break;

					default:
						break;
					}
					eventType = parser.next();
				}
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return relations;
	}
}