package gui;

import javax.swing.JLabel;

import com.github.francesco149.koohii.Koohii;

public class Score {
	Koohii.DiffCalc m_diff;
	Koohii.PPv2 m_pp;
	Koohii.PPv2Parameters m_ppParams;
	
	Controller m_controller;
	
	JLabel m_ppLabel;
	public Score(Koohii.PPv2Parameters pp, Controller c) {
		m_ppParams = pp;
		m_controller = c;
	}
	public void setPPLabel(JLabel pp) {
		m_ppLabel = pp;
	}
	public double getPP() {
		return m_pp.total;
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
}