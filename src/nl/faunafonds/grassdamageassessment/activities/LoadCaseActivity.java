package nl.faunafonds.grassdamageassessment.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.faunafonds.grassdamageassessment.R;
import nl.faunafonds.grassdamageassessment.other.Case;
import nl.faunafonds.grassdamageassessment.other.ParseRelationsXML;
import nl.faunafonds.grassdamageassessment.other.ParseXML;
import nl.faunafonds.grassdamageassessment.other.Relation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class LoadCaseActivity extends Activity {

	List<Case> cases = null;
	List<Relation> relations = null;
	ArrayList<Case> casesAd = new ArrayList<Case>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_case);

		ListView listView = (ListView) findViewById(R.id.list);
		//List<Case> cases = null;
		/*Toast.makeText(getApplicationContext(), downloadsDir + "/employees.xml", 
				Toast.LENGTH_SHORT).show();*/
		//File xmlFile = new File(downloadsDir,"employees.xml");
		try {
			ParseRelationsXML relationsParser = new ParseRelationsXML();			
			
			ParseXML parser = new ParseXML();

			File[] files = getFilesDir().listFiles();
			ArrayList<InputStream> inputStreamList = new ArrayList<InputStream>();
			ArrayList<InputStream> inputStreamListRelations = new ArrayList<InputStream>();
			for (int i = 0; i < files.length; i++){
				String[] file = files[i].toString().split("/");
				String name = file[file.length - 1];
				if (name.startsWith("schade_") && name.endsWith(".xml")){
					inputStreamList.add(new FileInputStream(files[i]));
				}
				else if (name.equals("faunafonds_relaties.xml")) {
					inputStreamListRelations.add(new FileInputStream(files[i]));
				}
			}

			ArrayList<Integer> removeCases = new ArrayList<Integer>();
			relations = relationsParser.parse(inputStreamListRelations);
			//Toast.makeText(this, String.format("%f", relations.size()), Toast.LENGTH_LONG).show();
			cases = parser.parse(inputStreamList);
			
			for (int i=0;i<cases.size();i++){
				String relID = cases.get(i).getRelatieID();
				for (int j=0;j<relations.size();j++){
					if (relations.get(j).getRelatieID().equals(relID)){
						cases.get(i).setRelation(relations.get(j));
					}
				}
			}
			
			for (int i=0;i<cases.size();i++){
				if (cases.get(i).getSavedDate().equals("nvt")){
					for (int j=0;j<cases.size();j++){
						if (i!=j && cases.get(i).getRelatieID().equals(cases.get(j).getRelatieID())){removeCases.add(i);}

					}
				}
			}
			for (int i=cases.size() - 1; i>=0; i--){
				if (!removeCases.contains(i)){
				casesAd.add(cases.get(i));
				}
			}
			
			ArrayAdapter<Case> adapter = 
					new ArrayAdapter<Case>(this,R.layout.list_item, casesAd);
			listView.setAdapter(adapter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
				  /*Toast.makeText(getApplicationContext(),
			      "Click ListItem Number " + position, Toast.LENGTH_LONG)
			      .show();*/
				  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				  intent.putExtra("case", casesAd.get(position));
				  startActivity(intent);
				  finish();
			  }
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	Intent intent = new Intent(this, MainMenuActivity.class);
			startActivity(intent);
			finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
