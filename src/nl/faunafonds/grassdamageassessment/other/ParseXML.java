package nl.faunafonds.grassdamageassessment.other;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ParseXML {
	List<Case> cases;
	private Case newCase;
	private DamageArea newDamageArea;
	private ReferencePoint newReferencePoint;
	private String text;

	public ParseXML() {
		cases = new ArrayList<Case>();
	}

	public List<Case> getEmployees() {
		return cases;
	}

	public List<Case> parse(ArrayList<InputStream> is) {
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
						if (tagname.equalsIgnoreCase("Schade")) {
							// create a new instance of case
							newCase = new Case();
						} else if (tagname.equalsIgnoreCase("damageArea")) {
							// create a new instance of case
							newDamageArea = new DamageArea();
						} else if (tagname.equalsIgnoreCase("referencePoint")) {
							// create a new instance of case
							newReferencePoint = new ReferencePoint();
						}
						break;

					case XmlPullParser.TEXT:
						text = parser.getText();
						break;

					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase("Schade")) {
							// add employee object to list
							cases.add(newCase);
						} else if (tagname.equalsIgnoreCase("relatieID")) {
							newCase.setRelatieID(text);
						} else if (tagname.equalsIgnoreCase("diersoort1")) {
							newCase.setDierSoort1(text);
						} else if (tagname.equalsIgnoreCase("diersoort2")) {
							newCase.setDierSoort2(text);
						} else if (tagname.equalsIgnoreCase("diersoort3")) {
							newCase.setDierSoort3(text);
						} else if (tagname.equalsIgnoreCase("datum_melding")) {
							newCase.setDatum(text.substring(0,10));
						} else if (tagname.equalsIgnoreCase("gewas")) {
							newCase.setCrop(text);
						} else if (tagname.equalsIgnoreCase("perceelgeowkt")) {
							newCase.setStartArea(text);
						} else if (tagname.equalsIgnoreCase("perceellat")) {
							newCase.setLatitude(text);
						} else if (tagname.equalsIgnoreCase("perceellon")) {
							newCase.setLongitude(text);
						} else if (tagname.equalsIgnoreCase("damageArea")) {
							newCase.addDamageArea(newDamageArea);
						} else if (tagname.equalsIgnoreCase("name")) {
							newDamageArea.setName(text);
						} else if (tagname.equalsIgnoreCase("coords")) {
							newDamageArea.setCoords(text);
						} else if (tagname.equalsIgnoreCase("gemaaktop")) {
							newDamageArea.setDate(text);
						} else if (tagname.equalsIgnoreCase("grassHeight")) {
							newDamageArea.setGrassHeight(Double.parseDouble(text));
						} else if (tagname.equalsIgnoreCase("referencePoint")) {
							newCase.addReferencePoint(newReferencePoint);
						} else if (tagname.equalsIgnoreCase("refname")) {
							newReferencePoint.setName(text);
						} else if (tagname.equalsIgnoreCase("refcoord")) {
							newReferencePoint.setCoords(text);
						} else if (tagname.equalsIgnoreCase("refgemaaktop")) {
							newReferencePoint.setDate(text);
						} else if (tagname.equalsIgnoreCase("refgrassHeight")) {
							newReferencePoint.setGrassHeight(Double.parseDouble(text));
						} else if (tagname.equalsIgnoreCase("savedatum")) {
							newCase.setSavedDate(text);
						} else if (tagname.equalsIgnoreCase("schadeID")) {
							newCase.setSchadeID(text);
						} else if (tagname.equalsIgnoreCase("opmerkingen_aanvrager")) {
							newCase.setFarmerComments(text);
						} else if (tagname.equalsIgnoreCase("opmerkingen_taxateur")) {
							newCase.setGeneralComments(text);
						} else if (tagname.equalsIgnoreCase("schade_zetvoort")) {
							newCase.setDamageContinues(text);
						} else if (tagname.equalsIgnoreCase("preventie_afrasteringdatum")) {
							newCase.setBarrier(text);
						} else if (tagname.equalsIgnoreCase("preventie_vogelverschrikkeraantal")) {
							newCase.setScareCrows(text);
						} else if (tagname.equalsIgnoreCase("preventie_stokkenaantal")) {
							newCase.setSticks(text);
						} else if (tagname.equalsIgnoreCase("preventie_knalapparaataantal")) {
							newCase.setNoiseMachines(text);
						} else if (tagname.equalsIgnoreCase("preventie_verjaagdaantal")) {
							newCase.setChased(text);
						} else if (tagname.equalsIgnoreCase("preventie_bejaagdaantal")) {
							newCase.setHunting(text);
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

		return cases;
	}
}