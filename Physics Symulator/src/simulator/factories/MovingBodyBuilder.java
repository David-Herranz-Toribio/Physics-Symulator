package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {
	private final static String Tag = "mv_body";
	
	public MovingBodyBuilder() {
		super(Tag, "Moving Body");
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected Body createInstance(JSONObject data) throws IllegalArgumentException{
		// TODO Auto-generated method stub
		if(!data.has("gid") || !data.has("id")||!data.has("v")||
				!data.has("p")||!data.has("m")) {
			throw new IllegalArgumentException("Missing data");
		}
		JSONArray arr1 = data.getJSONArray("p");
		JSONArray arr2 = data.getJSONArray("v");
		if(arr1.length() != 2 || arr2.length() != 2) {
			throw new IllegalArgumentException("Vector must be 2D");
		}
		return new MovingBody(data.getString("id"),data.getString("gid"),new Vector2D(arr1.getDouble(0),arr1.getDouble(1)),
					new Vector2D(arr2.getDouble(0),arr2.getDouble(1)),data.getDouble("m"));
	}
}
