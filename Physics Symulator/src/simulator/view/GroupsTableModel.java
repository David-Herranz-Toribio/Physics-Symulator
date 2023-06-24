package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver {

	String[] _header = { "Id", "Force Laws", "Bodies" };
	List<BodiesGroup> _groups;
	
	GroupsTableModel(Controller ctrl) {
		_groups = new ArrayList<>();
		// TODO registrar this como observador;
		ctrl.addObserver(this);
	}
	
	// TODO el resto de métodos van aquí …

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
		return _groups.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return _header.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Object s = "";

		switch(columnIndex) {
		case 0:
			s = _groups.get(rowIndex).getId();
			break;
		case 1:
			s = _groups.get(rowIndex).getForceLawsInfo();
			break;
		case 2:
			for (Body body : _groups.get(rowIndex)) {
				s +=  " " + body.getId() ;
			}
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
		_groups.clear();
	}
	
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		for (BodiesGroup grupo : groups.values() ) {
			_groups.add(grupo);
		}
		this.fireTableStructureChanged();
	}
	
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		this._groups.add(g);
		this.fireTableStructureChanged();
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
		this.fireTableStructureChanged();
	}
}