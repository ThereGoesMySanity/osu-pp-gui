package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.GUI.CustomSlider;
import gui.GUI.CustomTextField;

public class Variable implements ChangeListener, ActionListener {
	double m_min, m_max, m_current, m_default;
	CustomSlider m_slider;
	CustomTextField m_text;

	Controller m_controller;
	boolean m_diffCalc;

	public Variable(CustomSlider cs, CustomTextField jftf, double min, double max, double def, boolean diff) {
		m_min = min;
		m_max = max;
		m_current = m_default = def;
		m_slider = cs;
		m_text = jftf;
		m_diffCalc = diff;
	}
	public void reset() {
		m_current = m_default;
		m_slider.set(m_current);
		m_text.set(m_current);
	}

	public double get() {
		return m_current;
	}

	public void setController(Controller c) {
		m_controller = c;
	}

	//slider
	@Override
	public void stateChanged(ChangeEvent arg0) {
		m_current = m_slider.get();
		m_text.set(m_current);
		m_controller.onVarUpdate(m_diffCalc);
	}
	//text
	@Override
	public void actionPerformed(ActionEvent e) {
		m_current = m_text.get();
		if(m_current > (double)m_slider.getMaximum() / m_slider.getRes()) {
			double temp = m_current;
			m_slider.set(m_current);
			m_current = temp;
			m_text.set(m_current);
		} else {
			m_slider.set(m_current);
		}
		m_controller.onVarUpdate(m_diffCalc);
	}

}
