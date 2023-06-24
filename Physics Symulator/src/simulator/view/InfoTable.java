package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {
	
	private static final long serialVersionUID = 1L;
	String _title;
	TableModel _tableModel;
	
	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
	}
	
	private void initGUI() {
	// TODO cambiar el layout del panel a BorderLayout()
	this.setLayout(new BorderLayout());
	
	// TODO añadir un borde con título al JPanel, con el texto _title
	Border b = BorderFactory.createLineBorder(Color.black, 2);
	this.setBorder(BorderFactory.createTitledBorder (b, _title));

	// TODO añadir un JTable (con barra de desplazamiento vertical) que use _tableModel
	JTable tabla = new JTable(_tableModel);
	this.add(new JScrollPane(tabla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}
}
