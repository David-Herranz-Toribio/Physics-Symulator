package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {

	private Vector2D c;
	private double g;
	
	public MovingTowardsFixedPoint(Vector2D c, double g) throws IllegalArgumentException{
		if (c == null)
			throw new IllegalArgumentException("Null vector");
		else if (g <= 0)
			throw new IllegalArgumentException("Invalid gravity");
		else {
			this.c = c;
			this.g = g;
		}
	}

	@Override
	public void apply(List<Body> bs) {
		double f;
		Vector2D dir;
		
		for (Body body : bs){
			
			if (!body.getPosition().equals(c)) {
			
				//g * masa
				f = (g * body.getMass());
				
				//actualizar el vector direccion 
				dir = c.minus(body.getPosition()).direction();
				
				//Fuerza vectorial que es igual a vector director por fuerza
				body.addForce(dir.scale(f));
			}
		}
	}
	
	public String toString() {
		return "Moving towards "+ c +" with constant acceleration "+ g;
	}
}

