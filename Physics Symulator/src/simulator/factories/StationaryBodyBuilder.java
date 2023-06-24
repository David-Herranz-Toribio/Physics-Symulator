package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {
	private final static String Tag = "st_body";
	
	public StationaryBodyBuilder() {
		super(Tag, "Stacionary Body");
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected Body createInstance(JSONObject data) throws IllegalArgumentException{
		// TODO Auto-generated method stub
		if(!data.has("gid") || !data.has("id")||
				!data.has("p")||!data.has("m")) {
			throw new IllegalArgumentException("Missing data");
		}
		JSONArray arr1 = data.getJSONArray("p");
		
		if(arr1.length() != 2 ) {
			throw new IllegalArgumentException("Vector must be 2D");
		}
		return new StationaryBody(data.getString("id"),data.getString("gid"),new Vector2D(arr1.getDouble(0),arr1.getDouble(1)),
					data.getDouble("m"));
	}
}
