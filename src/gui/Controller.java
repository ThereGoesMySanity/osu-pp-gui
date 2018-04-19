package gui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.francesco149.koohii.Koohii;
import com.zerocooldown.libosu.util.Uleb128;

import util.OsuAPI;

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
					newScore(f);
				}
			}
		}
	}
	public void onVarUpdate(boolean diffCalc) {
		update(diffCalc);
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

	public void newScore(File replayFile) {
		try {
			Koohii.PPv2Parameters pp = new Koohii.PPv2Parameters();

			BufferedInputStream br = new BufferedInputStream(new FileInputStream(replayFile));

			br.skip(6);
			long l = Uleb128.fromByteStream(br).asLong();
			byte[] bh = new byte[(int) l];
			br.read(bh);
			String hash = new String(bh, "UTF-8");

			br.skip(1);
			bh = new byte[(int) Uleb128.fromByteStream(br).asLong()];
			br.read(bh);
			String player = new String(bh, "UTF-8");

			File beatmap = OsuAPI.getBeatmap(hash);
			Koohii.Map map = new Koohii.Parser().map(new BufferedReader(new InputStreamReader(new FileInputStream(beatmap))));
			pp.beatmap = map;

			br.skip(1);
			bh = new byte[(int) Uleb128.fromByteStream(br).asLong()];
			br.skip(bh.length);

			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bh = new byte[2];

			br.read(bh);
			bb.put(bh);
			bb.position(0);
			pp.n300 = bb.getShort();

			bb.clear();
			br.read(bh);
			bb.put(bh);
			bb.position(0);
			pp.n100 = bb.getShort();

			bb.clear();
			br.read(bh);
			bb.put(bh);
			bb.position(0);
			pp.n50 = bb.getShort();

			br.skip(4);

			bb.clear();
			br.read(bh);
			bb.put(bh);
			bb.position(0);
			pp.nmiss = bb.getShort();

			br.skip(4);

			bb.clear();
			br.read(bh);
			bb.put(bh);
			bb.position(0);
			pp.combo = bb.getShort();

			br.skip(1);

			bb.clear();
			bh = new byte[4];
			br.read(bh);
			bb.put(bh);
			bb.position(0);
			pp.mods = bb.getInt();

			br.close();

			Score s = new Score(pp, this);
			s.setPPLabel(m_gui.addScore(player + " - " + map.artist + " - " + map.title));

			scores.add(s);
			s.updateDiffCalc();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
