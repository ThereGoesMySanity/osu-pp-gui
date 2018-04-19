package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class OsuAPI {
	static String key;
	static File osuDir;
	public static void setKey(String s) {
		key = s;
	}
	public static void setOsuDir(String s) {
		osuDir = new File(s);
	}
	public static File getBeatmap(String beatmapHash) throws IOException, NoSuchAlgorithmException {
		URL url = new URL("https://osu.ppy.sh/api/get_beatmaps?k="+key+"&h="+beatmapHash);
		BufferedReader res = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = res.readLine();
		JsonObject j = Json.parse(result).asArray().get(0).asObject();
		String version = j.getString("version", "");
		String setID = j.getString("beatmapset_id", "");
		if(version.isEmpty() || setID.isEmpty()) {
			throw new IOException("Failed to connect to osu! api");
		}
		for(File f : osuDir.listFiles()) {
			if(f.isDirectory() && f.getName().startsWith(setID)) {
				for(File f1 : f.listFiles()) {
					if(f1.getName().endsWith(".osu") && f1.getName().contains("["+version+"]")) {
						return f1;
					}
				}
				return null;
			}
		}
		return null;
	}
}
