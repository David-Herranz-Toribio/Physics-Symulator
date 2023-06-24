package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import java.util.regex.*;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ControlPanel extends JPanel implements SimulatorObserver{

	private Controller _ctrl;
	private JToolBar _toolaBar;
	private boolean _stopped;
	private JButton _quitButton;
	private JButton _openButton;
	private JButton _physicsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JButton _viewerButton;
	private JTextField dTime;
	private JSpinner steps;

	private JFileChooser _fc;
	private ForceLawsDialog fld;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUIS();

		_ctrl.addObserver(this);
	}

	private void initGUIS() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);

		steps = new JSpinner(new SpinnerNumberModel(10000, 1, 10000, 100));
		dTime = new JTextField("2500.0");

		// Open Button
		_openButton = new JButton();
		_openButton.setToolTipText("Load an input file into the simulator");
		_openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		_openButton.addActionListener(e -> {
			int selection = _fc.showOpenDialog(Utils.getWindow(this));

			if (selection == JFileChooser.APPROVE_OPTION){
				File file = _fc.getSelectedFile();

				try {
					_ctrl.reset();
					InputStream i = new FileInputStream(file);
					_ctrl.loadData(i);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		_toolaBar.add(_openButton);

		// Physics Button
		_toolaBar.addSeparator();
		_physicsButton = new JButton();
		_physicsButton.setToolTipText("Set force laws for groups");
		_physicsButton.setIcon(new ImageIcon("resources/icons/physics.png"));
		_physicsButton.addActionListener(e -> {
			if (fld == null) {

				fld = new ForceLawsDialog(Utils.getWindow(this), _ctrl);
				fld.open();
			}
			else {
				if(fld.open() == 0)
					fld.setVisible(false);				
			}
		});

		_toolaBar.add(_physicsButton);


		// Viewer Button
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("Open viewer window");
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_viewerButton.addActionListener(e -> {
			ViewerWindow viewer = new ViewerWindow( (JFrame) Utils.getWindow(this), _ctrl);
		});
		_toolaBar.add(_viewerButton);


		// Run Button
		_toolaBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("Run the simulator");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_runButton.addActionListener(e -> {
			_stopped = false;
			//Desactivar los botones
			toggleButtons(false);
			_ctrl.setDeltaTime(Double.parseDouble(dTime.getText()));

			int n = Integer.parseInt(steps.getValue().toString());
			run_sim(n);
		});
		_toolaBar.add(_runButton);


		// Stop Button
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stop the simulator");
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButton.addActionListener(e -> {_stopped = true;});
		_toolaBar.add(_stopButton);


		// Steps Spinner
		JLabel sText = new JLabel(" Steps: ");
		_toolaBar.add(sText);
		steps.setToolTipText("Simulation steps to run: 1-10000");
		steps.setMaximumSize(new Dimension(70, 50));
		_toolaBar.add(steps);


		// Delta-time Text
		JLabel dText = new JLabel(" Delta-Time: ");
		_toolaBar.add(dText);
		dTime.setToolTipText("Real time (seconds) corresponding to a step");
		dTime.setPreferredSize(new Dimension(70, 50));
		dTime.setMaximumSize(new Dimension(70, 50));
		dTime.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE) ||
						(c == KeyEvent.VK_PERIOD))) {
					dTime.setEditable(false);
				}else
					dTime.setEditable(true);
			}
		});
		_toolaBar.add(dTime);


		// Quit Button
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Exit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> Utils.quit(this));
		_toolaBar.add(_quitButton);
		// TODO crear el selector de ficheros
		_fc = new JFileChooser("resources/examples/input");
	}

	private void toggleButtons(boolean toggle) {
		_openButton.setEnabled(toggle);
		_physicsButton.setEnabled(toggle);
		_viewerButton.setEnabled(toggle);
		_runButton.setEnabled(toggle);
		_quitButton.setEnabled(toggle);
	}

	private void run_sim(int n) {

		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				Utils.showErrorMsg(e.getMessage());
				toggleButtons(true);
				_stopped = true;

				return;
			}
			SwingUtilities.invokeLater(() -> run_sim(n - 1));
		} else {
			toggleButtons(true);
			_stopped = true;
		}
	}


	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		this.dTime.setText("" + dt); 
	}


	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
	}
}
