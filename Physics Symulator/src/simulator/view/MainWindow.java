package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		// TODO crear ControlPanel y añadirlo en PAGE_START de mainPanel
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		
		// TODO crear StatusBar y añadirlo en PAGE_END de mainPanel
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		
		// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		// TODO crear la tabla de grupos y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño

		InfoTable tablaGrupos = new InfoTable("Groups", new GroupsTableModel(_ctrl));
		tablaGrupos.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(tablaGrupos);
		// TODO crear la tabla de cuerpos y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño

		InfoTable tablaCuerpos = new InfoTable("Bodies", new BodiesTableModel(_ctrl));
		tablaCuerpos.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(tablaCuerpos);
		
		// TODO llama a Utils.quit(MainWindow.this) en el método windowClosing
//		addWindowListener();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}	
}