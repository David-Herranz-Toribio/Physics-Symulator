package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
	List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		// TODO registrar this como observer
		ctrl.addObserver(this);
	}
	
	// TODO el resto de métodos van aquí…

	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		if (_header == null) {
			return "";
		}

		return _header[column];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return _bodies.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return _header.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Object s = null;

		switch(columnIndex) {
		case 0:
			s = _bodies.get(rowIndex).getId();
			break;
		case 1:
			s = _bodies.get(rowIndex).getgId();
			break;
		case 2:
			s = _bodies.get(rowIndex).getMass();
			break;
		case 3:
			s = _bodies.get(rowIndex).getVelocity();
			break;
		case 4:
			s = _bodies.get(rowIndex).getPosition();
			break;
		case 5:
			s = _bodies.get(rowIndex).getForce();
			break;
		}

		return s;
	}


	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		this._bodies.clear();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		for (BodiesGroup grupo : groups.values() ) {
			for (Body body : grupo) {
				this._bodies.add(body);
			}
		}
		this.fireTableStructureChanged();
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		for (Body body : g ) {
			this._bodies.add(body);
		}
		this.fireTableStructureChanged();
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		this._bodies.add(b);
		this.fireTableStructureChanged();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		this.fireTableStructureChanged();
	}
}