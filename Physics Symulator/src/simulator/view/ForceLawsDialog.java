package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ForceLawsDialog extends JDialog implements SimulatorObserver {
	
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	
	// TODO en caso de ser necesario, añadir los atributos aquí…
	private JSONObject info;
	private JSONObject data;
	private int _status;
	private int _selectedLawsIndex;
	
	ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		_selectedLawsIndex = 0;
		initGUI();
		
		// TODO registrar this como observer;
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		
		setTitle("Force Laws Selection");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		setContentPane(mainPanel);
		
		// _forceLawsInfo se usara para establecer la informacion en la tabla
		_forceLawsInfo = _ctrl.getForceLawsInfo();
		
		info = _forceLawsInfo.get(_selectedLawsIndex);
		data = info.getJSONObject("data");
		
		
		// TODO crear un JTable que use _dataTableModel, y añadirla al panel
		_dataTableModel = new DefaultTableModel() {
			
			@Override
			public String getColumnName(int column) {
				// TODO Auto-generated method stub
				if (_headers == null) {
					return "";
				}

				return _headers[column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO hacer editable solo la columna 1
				return column == 1;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		_dataTableModel.setRowCount(data.length());
		_dataTableModel.setColumnCount(_headers.length);
		
		int i = 0;
		for(String a : data.keySet()) {
            _dataTableModel.setValueAt(a, i, 0);
            _dataTableModel.setValueAt(data.getString(a), i, 2);
            i++;
        }
		
		//Texto inicial
		JPanel introPanel = new JPanel(new BorderLayout());
		JLabel intro = new JLabel("<html>Select a force law and provide values for the parameters in the <b> Value column </b>(default values are used for parameters with no value).");
		introPanel.add(intro, BorderLayout.PAGE_START);
		mainPanel.add(introPanel);
		
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		mainPanel.add(s);
		
		//Tablas
		JTable tablita = new JTable(_dataTableModel);
		tablita.setPreferredSize(new Dimension(500, 250));
		mainPanel.add(new JScrollPane(tablita, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		
		_lawsModel = new DefaultComboBoxModel<>();
		// TODO añadir la descripción de todas las leyes de fuerza a _lawsModel
 		for (int iterator = 0; iterator < _forceLawsInfo.size(); iterator++) {
			_lawsModel.addElement(_forceLawsInfo.get(iterator).getString("desc"));
		}
		
 		//Panel inferior
 		JPanel comboPanel = new JPanel();
 		comboPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
 		mainPanel.add(comboPanel);
 		
		// TODO crear un combobox que use _lawsModel y añadirlo al panel
 		JLabel forceLawText = new JLabel("Force Law: ");
 		comboPanel.add(forceLawText);
 		
		JComboBox<String> modeloLeyes = new JComboBox<String>(_lawsModel);
		modeloLeyes.addActionListener(e -> {
			_selectedLawsIndex = modeloLeyes.getSelectedIndex();
			info = _forceLawsInfo.get(_selectedLawsIndex);
			data = info.getJSONObject("data");
			
			_dataTableModel.setRowCount(data.length());
			_dataTableModel.setColumnCount(_headers.length);

			_dataTableModel.setRowCount(data.length());
			
			int j = 0;
			for(String a : data.keySet()) {

	            _dataTableModel.setValueAt(a, j, 0);
	            _dataTableModel.setValueAt(data.getString(a), j, 2);
	            j++;
	        }
			_dataTableModel.fireTableStructureChanged();
		});
		comboPanel.add(modeloLeyes);
		
		
		_groupsModel = new DefaultComboBoxModel<>();
		// TODO crear un combobox que use _groupsModel y añadirlo al panel
		JLabel groupText = new JLabel("Group: ");
 		comboPanel.add(groupText);
 		
		JComboBox<String> modeloGrupos = new JComboBox<String>(_groupsModel);
		comboPanel.add(modeloGrupos);
		
		
		// TODO crear los botones OK y Cancel y añadirlos al panel
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> {
			setVisible(false);	
			this._status = 0;
		});
		buttonsPanel.add(cancel);

		JButton ok = new JButton("OK");
		ok.addActionListener(e -> {			
			
			JSONObject datoTabla = new JSONObject();
			for (int j = 0; j < _dataTableModel.getRowCount(); j++) {	
				Object aux = _dataTableModel.getValueAt(j, 1);
				String f = null;
	
				if(aux != null)
					f =  aux.toString();
				if(f != null && !f.equals("")) {
					
					if(f.charAt(0) == '[' && f.charAt(f.length() - 1) == ']') {
						f = f.substring(1, f.length() - 1);
						String[] ax = f.trim().split(",");
						JSONArray arr = new JSONArray();
						arr.put(ax[0]);
						arr.put(ax[1]);
						datoTabla.put(_dataTableModel.getValueAt(j, 0).toString(),arr);
	
					}else
						datoTabla.put(_dataTableModel.getValueAt(j, 0).toString(),f);

				}
			}
		
			JSONObject flDefinitiva = new JSONObject();

			flDefinitiva.put("type",_forceLawsInfo.get(_selectedLawsIndex).getString("type"));
			flDefinitiva.put("data", datoTabla);
			
			
			try {
				_ctrl.setForceLaws("" + _groupsModel.getSelectedItem(), flDefinitiva);
			}
			catch(Exception error) {
				Utils.showErrorMsg(error.getMessage());
			}
			
			setVisible(false);				
		});
		buttonsPanel.add(ok);
		
		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open() {
		if (_groupsModel.getSize() == 0)
			return _status;
		
		// TODO Establecer la posición de la ventana de diálogo de tal manera que se abra en el centro de la ventana principal
		this._status = 1;
		setLocationRelativeTo(this.getParent());
		pack();
		setVisible(true);
		
		return _status;
	}
	
	
	
	/*LOS IMPORTANTES*/
	
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_groupsModel.removeAllElements();
		
		_dataTableModel.fireTableStructureChanged();
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		for (BodiesGroup grupo : groups.values() ) {
			_groupsModel.addElement(grupo.getId());
		}
		_dataTableModel.fireTableStructureChanged();
	}
	
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		_groupsModel.addElement(g.getId());
		_dataTableModel.fireTableStructureChanged();
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
