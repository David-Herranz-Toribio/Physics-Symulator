package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {
	protected String id;
	protected String gid;
	protected Vector2D v;
	protected Vector2D f;
	protected Vector2D p;
	protected double m;
	
	
	public Body (String id, String gid, Vector2D p, Vector2D v, double m) throws IllegalArgumentException{
		
		if (m <= 0)
			throw new IllegalArgumentException("Invalid mass");	
		else if(id == null || gid == null || v == null  || p == null)
			throw new IllegalArgumentException("Null parameter");
		else if(id.trim().length() <= 0 || gid.trim().length() <= 0 )
			throw new IllegalArgumentException("Invalid Id");
		else {
			this.id = id;
			this.gid = gid;
			this.v = v;
			this.p = p;
			this.m = m;
			this.f = new Vector2D();
		}
	}
	
	//devuelve el identificador del cuerpo
	public String getId() {
		return id;	
	}
	
	//devuelve el identificador del grupo al que pertenece el cuerpo
	public String getgId() {
		return gid;
	}
	
	//devuelve el vector de velocidad.
	public Vector2D getVelocity() {
		return v;
	}
	
	//devuelve el vector de fuerza
	public Vector2D getForce() {
		return f;
	}
	
	//devuelve el vector de posicion
	public Vector2D getPosition() {
		return p;
	}
	
	//devuelve la masa del cuerpo
	public double getMass() {
		return m;
	}
	
	//a�ade la fuerza f al vector de fuerza
	void addForce(Vector2D f) {
		this.f = this.f.plus(f);
	}
	
	//pone el valor del vector de fuerza a (0, 0)
	void resetForce() {
		this.f = new Vector2D();
	}
	
	//mueve el cuerpo durante dt segundos
	abstract void advance(double dt);
	
	public JSONObject getState(){
		
		JSONObject state = new JSONObject();
		state.put("id", this.id);
		state.put("m", this.m);
		state.put("p", this.p.asJSONArray());
		state.put("v", this.v.asJSONArray());
		state.put("f", this.f.asJSONArray());
		
		return state;		
	}

	public String toString() {
		return getState().toString();
	}
}
