import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import gui.Controller;
import gui.GUI;
import util.OsuUtils;

public class Main {
	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(new File("key.dat"));
			OsuUtils.setKey(s.nextLine());
			s.close();
			OsuUtils.setOsuDir("A:\\osu!");
			GUI g = new GUI();
			Controller c = new Controller(g);
			g.setController(c);
			g.setVisible(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
