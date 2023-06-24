package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup implements Iterable<Body> {
	private String gid;
	private ForceLaws laws;
	private List<Body> bs;
	private List<Body> _bodiesRO;
	
	public BodiesGroup(String gid, ForceLaws laws) throws IllegalArgumentException{
		if(gid == null || laws == null) {
			throw new IllegalArgumentException("Null parameter");
		}else if(gid.trim().length() <= 0 )
			throw new IllegalArgumentException("Invalid gid");
		else
		{
			this.gid = gid;
			this.laws = laws;
			this.bs = new ArrayList<>();
			this._bodiesRO = Collections.unmodifiableList(bs);
		}
	}
	
	public String getId() {
		return gid;
	}
	
	public void addBody(Body b) throws IllegalArgumentException {
		if(b == null)
			throw new IllegalArgumentException("Null parameter");
		else {
			int i = 0;
			boolean ok = true;
			
			while(i < bs.size() && ok) {
				if (b.id == bs.get(i).getId())
					ok = false;
				else
					i++;
			}
			if (ok)
				bs.add(b);
			else
				throw new IllegalArgumentException("Body already in group");
		}
	}
	
	public void setForceLaws(ForceLaws fl) throws IllegalArgumentException{
		if(fl == null)
			throw new IllegalArgumentException("Null parameter");
		else
			laws = fl;
	}
	
	public void advance(double dt) throws IllegalArgumentException {
		if (dt <= 0)
			throw new IllegalArgumentException("Invalid time");
		else {
			for(Body b : bs) {
				b.resetForce();
			}
			
			laws.apply(bs);
			
			for(Body b : bs) {
				b.advance(dt);
			}
		}
	}
	
	public JSONObject getState(){
		
		JSONObject state = new JSONObject();
		state.put("id", this.gid);
		JSONArray arr = new JSONArray();
		
		for(Body b : bs) {
			arr.put(b.getState());
		}
		
		state.put("bodies", arr);
		return state;		
	}
	
	public String getForceLawsInfo() {
		return laws.toString();
	}
	
	public String toString() {
		return getState().toString();
	}

	@Override
	public Iterator<Body> iterator() {
		// TODO Auto-generated method stub
		return _bodiesRO.iterator();
	}
}