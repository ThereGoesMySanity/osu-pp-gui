package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import util.OsuUtils;

public class GUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel m_contentPane;
	
	Controller m_controller;
	private JPanel varPanel;
	private JScrollPane varScroll;
	private JPanel varOuterPanel;
	private JPanel varInnerPanel1;
	private JPanel mapPanel;
	private JScrollPane mapScroll;
	private JPanel mapOuterPanel;
	private JPanel mapInnerPanel;
	private JPanel varButtonPanel;
	private JButton btnPPUpdate;
	private JButton btnResetAll;
	private JPanel varInnerPanel2;
	private JLabel lblTheVariablesBelow;
	private JLabel lblPpCalc;
	private JSeparator separator;
	private JPanel mapButtonPanel;
	private JButton btnAddReplay;
	private JButton btnCustomReplay;
	private JButton btnImport;
	private JButton btnClear;

	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		setBounds(100, 100, 900, 650);
		
		m_contentPane = new JPanel();
		m_contentPane.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent e) {
				try {
					e.acceptDrop(DnDConstants.ACTION_LINK);
					List<File> droppedFiles = (List<File>)e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for(File f : droppedFiles) {
						if(f.getName().endsWith(".osr")) {
							m_controller.newScore(OsuUtils.readReplay(f));
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		setContentPane(m_contentPane);
		m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		m_contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		m_contentPane.add(splitPane);
		splitPane.setResizeWeight(0.5);
		
		varPanel = new JPanel();
		splitPane.setRightComponent(varPanel);
		varPanel.setLayout(new BorderLayout(0, 0));
		
		varScroll = new JScrollPane();
		varScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		varPanel.add(varScroll, BorderLayout.CENTER);
		
		varOuterPanel = new JPanel();
		varScroll.setViewportView(varOuterPanel);
		varOuterPanel.setLayout(new BoxLayout(varOuterPanel, BoxLayout.Y_AXIS));
		
		lblPpCalc = new JLabel("PP calc");
		lblPpCalc.setFont(new Font("Tahoma", Font.BOLD, 16));
		varOuterPanel.add(lblPpCalc);
		
		varInnerPanel1 = new JPanel();
		varOuterPanel.add(varInnerPanel1);
		varInnerPanel1.setLayout(new BoxLayout(varInnerPanel1, BoxLayout.Y_AXIS));
		
		separator = new JSeparator();
		varOuterPanel.add(separator);
		
		lblTheVariablesBelow = new JLabel("Diff calc");
		lblTheVariablesBelow.setFont(new Font("Tahoma", Font.BOLD, 16));
		varOuterPanel.add(lblTheVariablesBelow);
		
		varInnerPanel2 = new JPanel();
		varOuterPanel.add(varInnerPanel2);
		varInnerPanel2.setLayout(new BoxLayout(varInnerPanel2, BoxLayout.Y_AXIS));
		
		varButtonPanel = new JPanel();
		varPanel.add(varButtonPanel, BorderLayout.SOUTH);
		varButtonPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		btnPPUpdate = new JButton("Update PP");
		btnPPUpdate.addActionListener(this);
		varButtonPanel.add(btnPPUpdate);
		
		btnResetAll = new JButton("Reset All");
		btnResetAll.addActionListener(this);
		varButtonPanel.add(btnResetAll);
		
		mapPanel = new JPanel();
		splitPane.setLeftComponent(mapPanel);
		mapPanel.setLayout(new BorderLayout(0, 0));
		
		mapScroll = new JScrollPane();
		mapScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mapPanel.add(mapScroll, BorderLayout.CENTER);
		
		mapOuterPanel = new JPanel();
		mapScroll.setViewportView(mapOuterPanel);
		
		mapInnerPanel = new JPanel();
		mapOuterPanel.add(mapInnerPanel);
		mapInnerPanel.setLayout(new BoxLayout(mapInnerPanel, BoxLayout.Y_AXIS));
		
		mapButtonPanel = new JPanel();
		mapPanel.add(mapButtonPanel, BorderLayout.SOUTH);
		
		btnAddReplay = new JButton("Add Replay");
		mapButtonPanel.add(btnAddReplay);
		btnAddReplay.addActionListener(this);
		
		btnCustomReplay = new JButton("Custom Replay");
		mapButtonPanel.add(btnCustomReplay);
		btnCustomReplay.addActionListener(this);
		
		btnImport = new JButton("Import User Top");
		mapButtonPanel.add(btnImport);
		btnImport.addActionListener(this);
		
		btnClear = new JButton("Clear");
		mapButtonPanel.add(btnClear);
		btnClear.addActionListener(this);
		
		
		
		
	}
	public JLabel addScore(Score s) {
		JPanel panel = new JPanel();
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {250, 75, 0};
		gbl_panel.rowHeights = new int[] {26, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblScoreName = new JLabel(s.getScoreName());
		lblScoreName.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblScoreName = new GridBagConstraints();
		gbc_lblScoreName.insets = new Insets(0, 0, 0, 5);
		gbc_lblScoreName.gridx = 0;
		gbc_lblScoreName.gridy = 0;
		panel.add(lblScoreName, gbc_lblScoreName);
		
		JLabel lblpp = new JLabel();
		lblpp.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblpp = new GridBagConstraints();
		gbc_lblpp.gridx = 1;
		gbc_lblpp.gridy = 0;
		panel.add(lblpp, gbc_lblpp);
		
		mapInnerPanel.add(panel);
		
		s.setJPanelVars(lblpp, panel);
		
		return lblpp;
	}
	
	public void removeScore(Score s) {
		mapInnerPanel.remove(s.getPanel());
	}
	
	public Variable addVariable(String name, double min, double max, double def, int res, boolean diff) {
		JPanel var_panel = new JPanel();
		
		GridBagLayout gbl_var_panel_ex = new GridBagLayout();
		gbl_var_panel_ex.columnWidths = new int[] {150, 150, 50, 0};
		gbl_var_panel_ex.rowHeights = new int[]{26, 0};
		gbl_var_panel_ex.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_var_panel_ex.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		var_panel.setLayout(gbl_var_panel_ex);
		
		JLabel label = new JLabel(name);
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		var_panel.add(label, gbc_label);
		
		CustomSlider slider = new CustomSlider(min, max, def, res);
		slider.addMouseListener(new MenuListener());
		
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.BOTH;
		gbc_slider.insets = new Insets(0, 0, 0, 5);
		gbc_slider.gridx = 1;
		gbc_slider.gridy = 0;
		var_panel.add(slider, gbc_slider);
		
		CustomTextField textField = new CustomTextField();
		
		textField.setHorizontalAlignment(SwingConstants.TRAILING);
		textField.setText(Double.toString(def));
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 0;
		var_panel.add(textField, gbc_textField);
		
		Variable v = new Variable(slider, textField, min, max, def, diff);
		slider.setVar(v);
		textField.addActionListener(v);
		
		if(diff) {
			varInnerPanel2.add(var_panel);
		} else {
			varInnerPanel1.add(var_panel);
		}
		return v;
	}
	
	public void setController(Controller c) {
		m_controller = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnPPUpdate)) {
			m_controller.update(true);
		} else if(e.getSource().equals(btnResetAll)){
			m_controller.resetAll();
		} else if(e.getSource().equals(this.btnClear)){
			m_controller.clearScores();
		} else if(e.getSource().equals(this.btnImport)){
			m_controller.importScores();
		} else if(e.getSource().equals(this.btnAddReplay)){
			m_controller.addReplays();
		} else if(e.getSource().equals(this.btnCustomReplay)){
			m_controller.customReplay();
		}
	}
	/*
	 * ----------------------------------------------------
	 *                 Custom Swing classes
	 * ----------------------------------------------------
	 */
	
	class CustomSlider extends JSlider {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int m_res;
		private Variable m_var;
		public CustomSlider(double min, double max, double def, int res) {
			super((int)(min*res), (int)(max*res), (int)(def*res));
			m_res = res;
		}
		public double get() {
			return (double)getValue() / m_res;
		}
		public void set(double f) {
			setValue((int)(f * m_res));
		}
		public int getRes()	 {
			return m_res;
		}
		public Variable getVar() {
			return m_var;
		}
		public void setVar(Variable v) {
			m_var = v;
			this.addChangeListener(v);
		}
	}
	class CustomTextField extends JFormattedTextField {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public CustomTextField() {
			super(new NumberFormatter());
		}
		public double get() {
			return Double.parseDouble(getText());
		}
		public void set(double f) {
			setText(Double.toString(f));
		}
	}
	
	class CustomMenu extends JPopupMenu {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CustomMenu(Variable v) {
			JMenuItem reset = new JMenuItem("Reset");
			reset.addActionListener(new ResetListener(v));
			add(reset);
		}
	}
	class ResetListener implements ActionListener {
		Variable m_var;
		public ResetListener(Variable v) {
			m_var = v;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m_var.reset();
		}
	}
	class MenuListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			popup(e);
		}
		public void mouseReleased(MouseEvent e) {
			popup(e);
		}
		private void popup(MouseEvent e) {
			if(e.isPopupTrigger()) {
				CustomSlider s = (CustomSlider)e.getSource();
				CustomMenu m = new CustomMenu(s.getVar());
				m.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
