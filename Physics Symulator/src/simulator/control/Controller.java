package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.Observable;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	private PhysicsSimulator ps;
	private Factory<ForceLaws> fl;
	private Factory<Body> b;
	public Controller (PhysicsSimulator ps, Factory<ForceLaws> fl, Factory<Body> b) {
		this.ps = ps;
		this.fl = fl;
		this.b = b;
	}
	
	public void loadData(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));

		//Llama a addGroup para cada grupo
		JSONArray jsonGroups = jsonInput.getJSONArray("groups");
		for (int i = 0; i < jsonGroups.length(); i++) {
			ps.addGroup(jsonGroups.getString(i));
		}
		
		//Llama a setForceLaws para cada ley
		if (jsonInput.has("laws")) {
			JSONArray jsonLaws = jsonInput.getJSONArray("laws");
			for (int i = 0; i < jsonLaws.length(); i++) {
				
				ps.setForceLaws(jsonLaws.getJSONObject(i).getString("id"), fl.createInstance(jsonLaws.getJSONObject(i).getJSONObject("laws")));
			}
		}
		
		//Llamar a addBody para cada bb
		
		JSONArray jsonBodies = jsonInput.getJSONArray("bodies");
		for (int i = 0; i < jsonBodies.length(); i++) {
			ps.addBody(b.createInstance(jsonBodies.getJSONObject(i)));
		}
	
	}
	
	public void run(int n) {
		for(int i = 0; i < n; i++)
			ps.advance();
	}
	

	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		
		p.println("{");
		p.println("\"states\": [");
		p.println(ps.toString());
		
		for (int i = 1; i <= n; i++) {
			ps.advance();
			p.print(",");
			p.println(ps.toString());
			
		}
		
		p.println("]");
		p.println("}");
	}

	public void reset() {
		ps.reset();
	}
	
	public void setDeltaTime(double dt) {
		ps.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		ps.addObserver(o);
	}

	public void removeObserver(SimulatorObserver o) {
		ps.removeObserver(o);
	}
	
	public int getGroupSize() {
		return ps.getGroupSize();
	}
	
	public void setForceLaws(String gid, JSONObject f) {
		ps.setForceLaws(gid, fl.createInstance(f));
	}
	
	public List<JSONObject> getForceLawsInfo(){
		List<JSONObject> _lista = new ArrayList<>();
		_lista = fl.getInfo();
		
		return _lista;
	}
}
