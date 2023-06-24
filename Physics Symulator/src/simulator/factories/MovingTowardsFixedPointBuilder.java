package simulator.factories;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingBody;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {
	private final static String Tag = "mtfp";
	private final static String Desc = "Moving Towards Fixed Point";
	
	public MovingTowardsFixedPointBuilder() {
		super(Tag, Desc);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected ForceLaws createInstance(JSONObject data) throws IllegalArgumentException{
		// TODO Auto-generated method stub
		JSONArray arr1;
		ForceLaws f;
		if(data.has("c")) {
			arr1 = data.getJSONArray("c");
			if(arr1.length() != 2 ) 
				throw new IllegalArgumentException("Vector must be 2D");
			
			if(!data.has("g")) {
				f = new MovingTowardsFixedPoint(new Vector2D(arr1.getDouble(0),arr1.getDouble(1)),  9.81 );
			}else
				f = new MovingTowardsFixedPoint(new Vector2D(arr1.getDouble(0),arr1.getDouble(1)), data.getDouble("g"));
		}else{
			if(!data.has("g")) {
				f = new MovingTowardsFixedPoint(new Vector2D(),  9.81 );
			}else
				f = new MovingTowardsFixedPoint(new Vector2D(), data.getDouble("g"));
		}
		
		return f;
	}
	
	public JSONObject getInfo() {
		JSONObject info = new JSONObject();
		info.put("type", Tag);
		info.put("desc", Desc);
		JSONObject data = new JSONObject();
		data.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		data.put("g", "the length of the acceleration vector (a number)");
		info.put("data", data);
		return info;
	}
}
