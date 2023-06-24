package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws>{
	private final static String Tag = "nf";
	private final static String Desc = "No force";
	
	public NoForceBuilder() {		
		super(Tag, Desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		// TODO Auto-generated method stub
		return new NoForce();
	}
	
	public JSONObject getInfo() {
		JSONObject info = new JSONObject();
		info.put("type", Tag);
		info.put("desc", Desc);
		JSONObject data = new JSONObject();
		info.put("data", data);
		return info;
	}
}
