package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body {
	private Vector2D a;
	
	public MovingBody(String id, String gid,Vector2D p, Vector2D v,  double m){
		super(id, gid, p, v, m);
		a = new Vector2D();
	}

	public void advance(double dt) {
		if (m == 0)
			a = new Vector2D();
		else 
 			a = f.scale(1/m);
		
		//p = p + v*t + 1/2 * a * t^2
		p = p.plus(v.scale(dt).plus(a.scale(dt * dt).scale(0.5)));
		//v = v + a*t
		v = v.plus(a.scale(dt));
	}
}
