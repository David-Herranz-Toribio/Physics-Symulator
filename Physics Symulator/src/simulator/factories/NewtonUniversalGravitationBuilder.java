package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
	private final static String Tag = "nlug";
	private final static String Desc = "Newton's law of universal gravitation";

	public NewtonUniversalGravitationBuilder() {
		super(Tag, Desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) throws IllegalArgumentException{
		// TODO Auto-generated method stub
		ForceLaws f;
		if(!data.has("G")) {			
			f = new NewtonUniversalGravitation(6.67E-11);

		}else{
			f = new NewtonUniversalGravitation(data.getDouble("G"));
		}

		return f;
	}

	public JSONObject getInfo() {
		JSONObject info = new JSONObject();
		info.put("type", Tag);
		info.put("desc", Desc);
		JSONObject data = new JSONObject();
		data.put("G", "The gravitational constant (e.g., 9.81)");
		info.put("data", data);
		return info;
	}
}
