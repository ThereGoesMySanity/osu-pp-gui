package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import com.github.francesco149.koohii.Koohii;
import com.github.francesco149.koohii.Koohii.PPv2Parameters;

import util.OsuUtils;

public class CustomReplayDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private Controller m_controller;
	private Koohii.Accuracy m_acc;
	private Koohii.Map m_beatmap;

	private JFormattedTextField	n300Field, n100Field, n50Field, missField, accField;
	private JButton btnChooseBeatmap, okButton;
	private JLabel beatmapName;

	/**
	 * Create the dialog.
	 */
	public CustomReplayDialog(Controller c) {
		m_controller = c;

		setBounds(100, 100, 290, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] {0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);

		JLabel lblBeatmap = new JLabel("Beatmap");
		GridBagConstraints gbc_lblBeatmap = new GridBagConstraints();
		gbc_lblBeatmap.insets = new Insets(0, 0, 5, 5);
		gbc_lblBeatmap.gridx = 0;
		gbc_lblBeatmap.gridy = 0;
		contentPanel.add(lblBeatmap, gbc_lblBeatmap);

		btnChooseBeatmap = new JButton("Choose...");
		GridBagConstraints gbc_btnChooseBeatmap = new GridBagConstraints();
		gbc_btnChooseBeatmap.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseBeatmap.gridx = 0;
		gbc_btnChooseBeatmap.gridy = 1;
		btnChooseBeatmap.addActionListener(this);

		JLabel lblSelectedBeatmap = new JLabel("Selected beatmap:");
		GridBagConstraints gbc_lblSelectedBeatmap = new GridBagConstraints();
		gbc_lblSelectedBeatmap.gridwidth = 2;
		gbc_lblSelectedBeatmap.insets = new Insets(0, 0, 5, 0);
		gbc_lblSelectedBeatmap.gridx = 1;
		gbc_lblSelectedBeatmap.gridy = 0;
		contentPanel.add(lblSelectedBeatmap, gbc_lblSelectedBeatmap);
		contentPanel.add(btnChooseBeatmap, gbc_btnChooseBeatmap);

		n300Field = new JFormattedTextField(new NumberFormatter());
		GridBagConstraints gbc_n300Field = new GridBagConstraints();
		gbc_n300Field.insets = new Insets(0, 0, 0, 5);
		gbc_n300Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_n300Field.gridx = 0;
		gbc_n300Field.gridy = 6;
		n300Field.addActionListener(this);

		beatmapName = new JLabel("none");
		GridBagConstraints gbc_beatmapName = new GridBagConstraints();
		gbc_beatmapName.gridwidth = 2;
		gbc_beatmapName.insets = new Insets(0, 0, 5, 5);
		gbc_beatmapName.gridx = 1;
		gbc_beatmapName.gridy = 1;
		contentPanel.add(beatmapName, gbc_beatmapName);

		JLabel lblAccuracy = new JLabel("Accuracy");
		GridBagConstraints gbc_lblAccuracy = new GridBagConstraints();
		gbc_lblAccuracy.insets = new Insets(0, 0, 5, 5);
		gbc_lblAccuracy.gridx = 0;
		gbc_lblAccuracy.gridy = 2;
		contentPanel.add(lblAccuracy, gbc_lblAccuracy);

		accField = new JFormattedTextField(new NumberFormatter());
		GridBagConstraints gbc_accField = new GridBagConstraints();
		gbc_accField.insets = new Insets(0, 0, 5, 5);
		gbc_accField.fill = GridBagConstraints.HORIZONTAL;
		gbc_accField.gridx = 0;
		gbc_accField.gridy = 3;
		accField.addActionListener(this);

		JLabel lblMisses = new JLabel("Misses");
		GridBagConstraints gbc_lblMisses = new GridBagConstraints();
		gbc_lblMisses.insets = new Insets(0, 0, 5, 5);
		gbc_lblMisses.gridx = 1;
		gbc_lblMisses.gridy = 2;
		contentPanel.add(lblMisses, gbc_lblMisses);

		JLabel lblCombo = new JLabel("Combo");
		GridBagConstraints gbc_lblCombo = new GridBagConstraints();
		gbc_lblCombo.insets = new Insets(0, 0, 5, 0);
		gbc_lblCombo.gridx = 2;
		gbc_lblCombo.gridy = 2;
		contentPanel.add(lblCombo, gbc_lblCombo);
		contentPanel.add(accField, gbc_accField);

		missField = new JFormattedTextField(new NumberFormatter());
		GridBagConstraints gbc_missField = new GridBagConstraints();
		gbc_missField.insets = new Insets(0, 0, 5, 5);
		gbc_missField.fill = GridBagConstraints.HORIZONTAL;
		gbc_missField.gridx = 1;
		gbc_missField.gridy = 3;
		missField.addActionListener(this);
		contentPanel.add(missField, gbc_missField);

		JFormattedTextField formattedTextField = new JFormattedTextField();
		GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
		gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
		gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextField.gridx = 2;
		gbc_formattedTextField.gridy = 3;
		contentPanel.add(formattedTextField, gbc_formattedTextField);

		JLabel lblNumberOfs = new JLabel("300s");
		GridBagConstraints gbc_lblNumberOfs = new GridBagConstraints();
		gbc_lblNumberOfs.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfs.gridx = 0;
		gbc_lblNumberOfs.gridy = 5;
		contentPanel.add(lblNumberOfs, gbc_lblNumberOfs);

		JLabel lblNumberOfs_1 = new JLabel("100s");
		GridBagConstraints gbc_lblNumberOfs_1 = new GridBagConstraints();
		gbc_lblNumberOfs_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfs_1.gridx = 1;
		gbc_lblNumberOfs_1.gridy = 5;
		contentPanel.add(lblNumberOfs_1, gbc_lblNumberOfs_1);

		JLabel lblNumberOfs_2 = new JLabel("50s");
		GridBagConstraints gbc_lblNumberOfs_2 = new GridBagConstraints();
		gbc_lblNumberOfs_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNumberOfs_2.gridx = 2;
		gbc_lblNumberOfs_2.gridy = 5;
		contentPanel.add(lblNumberOfs_2, gbc_lblNumberOfs_2);
		contentPanel.add(n300Field, gbc_n300Field);

		n100Field = new JFormattedTextField(new NumberFormatter());
		GridBagConstraints gbc_n100Field = new GridBagConstraints();
		gbc_n100Field.insets = new Insets(0, 0, 0, 5);
		gbc_n100Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_n100Field.gridx = 1;
		gbc_n100Field.gridy = 6;
		n100Field.addActionListener(this);
		contentPanel.add(n100Field, gbc_n100Field);

		n50Field = new JFormattedTextField(new NumberFormatter());
		GridBagConstraints gbc_n50Field = new GridBagConstraints();
		gbc_n50Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_n50Field.gridx = 2;
		gbc_n50Field.gridy = 6;
		n50Field.addActionListener(this);
		contentPanel.add(n50Field, gbc_n50Field);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		okButton.setEnabled(false);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

	private void updateAcc() {
		int n300 = Integer.parseInt(n300Field.getText());
		int n100 = Integer.parseInt(n100Field.getText());
		int n50 = Integer.parseInt(n50Field.getText());
		int nMisses = Integer.parseInt(missField.getText());
		m_acc = new Koohii.Accuracy(n300, n100, n50, nMisses);
		accField.setText(Double.toString(m_acc.value()*100));
	}

	private void updateCounts() {
		if(m_beatmap != null) {
			int nMisses = Integer.parseInt(missField.getText());
			m_acc = new Koohii.Accuracy(Double.parseDouble(accField.getText()), m_beatmap.objects.size(), nMisses);
		}
	}

	public void createScore() {
		PPv2Parameters pp = new PPv2Parameters();
		pp.n300 = m_acc.n300;
		pp.n100 = m_acc.n100;
		pp.n50 = m_acc.n50;
		pp.nmiss = m_acc.nmisses;
		pp.beatmap = m_beatmap;
		m_controller.newScore(new Score(pp, "?"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(btnChooseBeatmap)) {
			JFileChooser jfc = new JFileChooser(OsuUtils.getOsuDir("Songs"));
			jfc.setMultiSelectionEnabled(true);
			int returnVal = jfc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				for(File f : jfc.getSelectedFiles()) {
					if(f.getName().endsWith(".osu")) {
						try {
							m_beatmap = new Koohii.Parser().map(new BufferedReader(new FileReader(f)));
							beatmapName.setText(m_beatmap.title);
							okButton.setEnabled(true);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} else if(arg0.getSource().equals(accField)) {
			updateCounts();
		} else if(arg0.getSource().equals(okButton)) {
			createScore();
		} else /*one of the number boxes*/ {
			updateAcc();
		}
	}
}
