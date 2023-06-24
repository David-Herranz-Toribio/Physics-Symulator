package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver>{
	private double dTime;
	private double actualTime;
	private ForceLaws fl;
	private Map<String,BodiesGroup> _groups;
	private List<String> list;
	private List<SimulatorObserver> obs;
	private Map<String, BodiesGroup> _groupsRO;
	
	public PhysicsSimulator(ForceLaws fl, double dt) throws IllegalArgumentException {
		if (dt <= 0)
			throw new IllegalArgumentException("Invalid time");
		else if (fl == null)
			throw new IllegalArgumentException("Invalid law");
		else {
			this.dTime = dt;
			this.actualTime = 0;
			this.fl = fl;
			this._groups = new HashMap<>();
			this.list = new ArrayList<>();
			this._groupsRO = Collections.unmodifiableMap(_groups);
			this.obs = new ArrayList<>();
		}
	}
	
	public void addGroup(String gid) throws IllegalArgumentException{
		if (!_groups.containsKey(gid)) {
			BodiesGroup bg = new BodiesGroup(gid, fl);
			_groups.put(gid, bg);
			list.add(gid);
			
			for(SimulatorObserver so: obs) {
				so.onGroupAdded(_groupsRO, bg);
			}
		}else
			throw new IllegalArgumentException("Invalid id");
	}
	
	public void addBody(Body b) throws IllegalArgumentException {

		BodiesGroup bg = _groups.get(b.getgId());
		if (bg != null) {
			bg.addBody(b);
			_groups.put(b.getgId(), bg);
			
			for(SimulatorObserver so: obs) {
				so.onBodyAdded(_groupsRO, b);
			}
			
		}else
			throw new IllegalArgumentException("Invalid body");	
	}
	
	public void advance() {
		
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String gidAux = iterator.next();
			_groups.get(gidAux).advance(dTime);
		}
		this.actualTime += dTime;
		
		for(SimulatorObserver so: obs) {
			so.onAdvance(_groupsRO, dTime);
		}
	}

	public void setForceLaws(String gid, ForceLaws f) {
		if(f == null)
			throw new IllegalArgumentException("Null force");
		
		else if (!_groups.containsKey(gid))
			throw new IllegalArgumentException("Invalid id");	
		
		else {
			BodiesGroup bg = _groups.get(gid);
			bg.setForceLaws(f);
			for(SimulatorObserver so: obs) {
				so.onForceLawsChanged(bg);
			}
		}
	}
	
	public void reset() {
		_groups.clear();
		list.clear();
		actualTime = 0;
		
		for(SimulatorObserver so: obs) {
			so.onReset(_groupsRO, actualTime, dTime);
		}
	}
	
	public void setDeltaTime(double dt) {
		if (dt > 0) {
			dTime = dt;
			
			for(SimulatorObserver so: obs) {
				so.onDeltaTimeChanged(dt);
			}
		}
		else
			throw new IllegalArgumentException("dt no valido");
	}
	
	
	public JSONObject getState() {
		
		JSONObject state = new JSONObject();
		state.put("time", this.actualTime);
		JSONArray arr = new JSONArray();
		
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String gidAux = iterator.next();
			arr.put(_groups.get(gidAux).getState());
		}
		
		state.put("groups", arr);
		return state;		
	}
	
	public String toString() {
		return getState().toString();
	}
	
	public int getGroupSize() {
		return _groups.size();
	}

	@Override
	public void addObserver(SimulatorObserver o) {
		// TODO Auto-generated method stub
		if (!obs.contains(o)) {
			obs.add(o);
			o.onRegister(_groupsRO, dTime, actualTime);
		}
	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		// TODO Auto-generated method stub
		obs.remove(o);
	}
}
