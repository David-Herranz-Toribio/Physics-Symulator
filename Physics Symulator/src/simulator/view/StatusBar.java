package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class StatusBar extends JPanel implements SimulatorObserver {
	
	private Controller _ctrl;
	private double dt;
	private JLabel tiempo;
	private JLabel grupos;
	
	StatusBar(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		// TODO registrar this como observador
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		// TODO Crear una etiqueta de tiempo y añadirla al panel
		tiempo = new JLabel("Tiempo: " + dt);
		this.add(tiempo);
		
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		 
		// TODO Crear la etiqueta de numero de grupos y a�adirla al panel
		 
		grupos = new JLabel("Grupos: " + _ctrl.getGroupSize());
		this.add(grupos);
		 
	}
	// TODO el resto de métodos van aquí…
	
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		this.dt += time;
		tiempo.setText("Tiempo: " + dt);
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		this.dt = 0;
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		dt = 0;
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		grupos.setText("Grupos: " + _ctrl.getGroupSize());
		
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}
}
