package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.francesco149.koohii.Koohii;

public class Score {
	Koohii.DiffCalc m_diff;
	Koohii.PPv2 m_pp;
	Koohii.PPv2Parameters m_ppParams;
	String m_playerName;
	
	Controller m_controller;
	
	JLabel m_ppLabel;
	JPanel m_panel;
	public Score(Koohii.PPv2Parameters pp, String player) {
		m_ppParams = pp;
		m_playerName = player;
	}
	public void setJPanelVars(JLabel pp, JPanel panel) {
		m_ppLabel = pp;
		m_panel = panel;
	}
	public void setController(Controller c) {
		m_controller = c;
	}
	public double getPP() {
		return m_pp.total;
	}
	public JPanel getPanel() {
		return m_panel;
	}
	public void updateDiffCalc() {
		m_diff = new Koohii.DiffCalc().calc(m_ppParams.beatmap, m_ppParams.mods, m_controller);
		m_ppParams.aim_stars = m_diff.aim;
		m_ppParams.speed_stars = m_diff.speed;
		updatePPCalc();
	}
	public void updatePPCalc() {
		m_pp = new Koohii.PPv2(m_ppParams, m_controller);
		m_ppLabel.setText(String.format("%.2fpp", m_pp.total));
	}
	
	public String getScoreName() {
		return m_playerName +" - "+ m_ppParams.beatmap.artist + " - " + m_ppParams.beatmap.title;
	}
}
