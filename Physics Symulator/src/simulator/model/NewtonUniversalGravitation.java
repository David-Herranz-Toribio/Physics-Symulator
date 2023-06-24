package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
	
	private double G;
	
	public NewtonUniversalGravitation(double G) throws IllegalArgumentException {
		if(G <= 0 )
			throw new IllegalArgumentException("Invalid Id");
		this.G = G;
	}
	
	@Override
	public void apply(List<Body> bs) {
		
		double f;
		Vector2D dir;
		
		for (int i = 0; i < bs.size(); i++) {
			// reinicio fuerza
			Body body1 = bs.get(i);
			f = 0;
			dir = new Vector2D();
			
			for (int j = i + 1; j < bs.size(); j++) {
				Body body2 = bs.get(j);
				
				if (body1 != body2) {
					if (!body1.getPosition().equals(body2.getPosition())) {
					
						//distancia
						double dist = body1.getPosition().distanceTo(body2.getPosition());
						
						//G * (masas)/(distancia)^2
						f = (G * body1.getMass() * body2.getMass())/(dist * dist);
						
						//actualizar el vector direccion 
						dir = body2.getPosition().minus(body1.getPosition()).direction();
						
						// Fuerza vectorial acumulada q es igual a vector director por fuerza
						body1.addForce(dir.scale(f));
						body2.addForce(dir.scale(-f));
					}
				}
			}
		}
	}
	
	public String toString() {
		return "Newton's Universal Gravitation with G="+ G;
	}
}
