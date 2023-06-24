package simulator.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


public class BuilderBasedFactory<T> implements Factory<T>{
	
	private Map<String,Builder<T>> _builders;
	private List<JSONObject> _buildersInfo;
	
	public BuilderBasedFactory() {
	// Create a HashMap for _builders, a LinkedList _buildersInfo
	// ...
		this._builders = new HashMap<>();
		this._buildersInfo = new LinkedList<>();
	}
	public BuilderBasedFactory(List<Builder<T>> builders) {
		this();
	
		// call addBuilder(b) for each builder b in builder
		// ...
		for (Builder<T> b: builders) {
			this.addBuilder(b);
		}
	}
	
	public void addBuilder(Builder<T> b) {
	// add and entry b.getTag() b to _builders.
	// ...
		this._builders.put(b.getTypeTag(), b);
	// add b.getInfo () to _buildersInfo
	// ...
		this._buildersInfo.add(b.getInfo());
	}
	
	@Override
	public T createInstance(JSONObject info) {
	
	if (info == null) {
		throw new IllegalArgumentException("Invalid value for createInstance:null");
	}else if(!info.has("type")) {
		throw new IllegalArgumentException("Invalid value for createInstance:not type found");
	}else {
		String s = info.getString("type");
		if(_builders.containsKey(s)) {
			Builder<T> b = _builders.get(s);
			JSONObject js = new JSONObject();
			if(info.has("data"))
				js = info.getJSONObject("data");
			return b.createInstance(js);
		}else
			throw new IllegalArgumentException("Invalid value for createInstance: " +
					info.toString());	
	
	}
	// Search for a builder with a tag equals to info . getString ("type"), call its
	// createInstance method and return the result if it is not null . The value you
	// pass to createInstance is :
	//
	// info . has("data") ? info . getJSONObject("data") : new getJSONObject()
	// If no builder is found or th result is null ...
	
	}
	@Override
	public List<JSONObject> getInfo() {
		return Collections.unmodifiableList(_buildersInfo);
	}
}
