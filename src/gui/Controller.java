package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import util.OsuUtils;

public class Controller {
	HashMap<String, Variable> variables;
	ArrayList<Score> scores;
	GUI m_gui;
	public Controller(GUI gui) {
		m_gui = gui;
		variables = new HashMap<String, Variable>();
		scores = new ArrayList<Score>();
		newVariable("length bonus 1", 0, 1, 0.4, 100);
		newVariable("length bonus 2", 0, 1, 0.5, 100);
		newVariable("AR bonus", 0, 1, 0.45, 100);
		newVariable("low AR bonus", 0, 0.1, 0.01, 1000);
		newVariable("HD bonus", 1, 2, 1.18, 100);
		newVariable("FL bonus", 1, 2, 1.45, 100);
		newVariable("OD acc mod", 1, 2, 1.52163, 100000);
		newVariable("acc weight", 0, 5, 2.83, 100);
		newVariable("speed weight", 0, 5, 1, 100);
		newVariable("aim weight", 0, 5, 1, 100);
		/* diffCalc =  true*/
		newVariable("high cs buff", 0, 0.1, 0.02, 100, true);
		newVariable("decay weight", 0, 1, 0.9, 100, true);
		newVariable("aim decay", 0, 1, 0.15, 100, true);
		newVariable("speed decay", 0, 1, 0.3, 100, true);
		newVariable("aim star weight", 10, 50, 26.25, 100, true);
		newVariable("speed star weight", 1000, 2000, 1400, 1, true);
		newVariable("singletap spacing weight", 1, 5, 2.5, 100, true);
		newVariable("stream spacing weight", 0, 2, 0.9, 100, true);
		newVariable("spacing bonus 1", 0, 1, 0.4, 100, true);
		newVariable("spacing bonus 2", 0, 1, 0.25, 100, true);

		File r = new File("replays");
		if(r.exists() && r.isDirectory()) {
			for(File f : r.listFiles()){
				if(f.getName().startsWith("replay") && f.getName().endsWith(".osr")) {
					try {
						newScore(OsuUtils.readReplay(f));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public double getVariable(String var, double def) {
		if(!variables.containsKey(var)) {
			return def;
		}
		return variables.get(var).get();
	}

	public void newVariable(String s, double min, double max, double value, int res) {
		newVariable(s, min, max, value, res, false);
	}
	public void newVariable(String s, double min, double max, double value, int res, boolean diff) {
		Variable v = m_gui.addVariable(s, min, max, value, res, diff);
		variables.put(s, v);
		v.setController(this);
	}

	public void newScore(Score s) {
		m_gui.addScore(s);
		s.setController(this);
		s.updateDiffCalc();
		scores.add(s);
	}

	public void update(boolean diffCalc) {
		for(Score s : scores) {
			if(diffCalc) s.updateDiffCalc();
			else s.updatePPCalc();
		}
	}
	public void resetAll() {
		for(Variable v : variables.values()) {
			v.reset();
		}
		update(true);
	}
	public void clearScores() {
		for(Score s : scores) {
			m_gui.removeScore(s);
		}
		scores.clear();
		m_gui.repaint();
	}
	public void importScores() {
		
	}
	public void addReplays() {
		
	}
	public void customReplay() {
		CustomReplayDialog c = new CustomReplayDialog(this);
		c.setVisible(true);
	}
}
