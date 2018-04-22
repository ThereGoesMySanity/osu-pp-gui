package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Paths;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.github.francesco149.koohii.Koohii;
import com.github.francesco149.koohii.Koohii.PPv2Parameters;
import com.zerocooldown.libosu.util.Uleb128;

import gui.Score;

public class OsuUtils {
	static final String API = "https://osu.ppy.sh/api/";
	static String key;
	static String osuDir;
	public static void setKey(String s) {
		key = s;
	}
	public static void setOsuDir(String s) {
		osuDir = s;
	}
	public static Koohii.Map getBeatmapFromID(String beatmapID) throws IOException {
		URL url = new URL(API+"get_beatmaps?k="+key+"&b="+beatmapID);
		return getBeatmapFromURL(url);
	}
	public static Koohii.Map getBeatmapFromHash(String beatmapHash) throws IOException {
		URL url = new URL(API+"get_beatmaps?k="+key+"&h="+beatmapHash);
		return getBeatmapFromURL(url);
	}

	public static Koohii.Map getBeatmapFromURL(URL url) throws IOException {
		BufferedReader res = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = res.readLine();
		JsonObject j = Json.parse(result).asArray().get(0).asObject();
		String version = j.getString("version", "");
		String setID = j.getString("beatmapset_id", "");
		if(version.isEmpty() || setID.isEmpty()) {
			throw new IOException("Failed to connect to osu! api");
		}
		for(File f : Paths.get(osuDir, "Songs").toFile().listFiles()) {
			if(f.isDirectory() && f.getName().startsWith(setID)) {
				for(File f1 : f.listFiles()) {
					if(f1.getName().endsWith(".osu") && f1.getName().contains("["+version+"]")) {
						return new Koohii.Parser().map(new BufferedReader(new InputStreamReader(new FileInputStream(f1))));
					}
				}
				return null;
			}
		}
		return null;
	}

	public static Score[] getTopScores(String player, int n) throws IOException {
		Score[] scores = new Score[n];
		URL url = new URL(API + "get_user_best?k=" + key + "&u=" + player + "&limit=" + n);
		BufferedReader res = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = res.readLine();
		JsonArray arr = Json.parse(result).asArray();
		int i = 0;
		for(JsonValue v : arr) {
			PPv2Parameters pp = new PPv2Parameters();
			JsonObject j = v.asObject();
			String id = j.getString("beatmap_id", "");
			if(id.isEmpty()) {
				throw new IOException("Failed to connect to osu! api");
			}
			pp.beatmap = getBeatmapFromID(id);
			pp.n300 = Integer.parseInt(j.getString("count300", "0"));
			pp.n100 = Integer.parseInt(j.getString("count100", "0"));
			pp.n50 = Integer.parseInt(j.getString("count50", "0"));
			pp.nmiss = Integer.parseInt(j.getString("countmiss", "0"));
			pp.combo = Integer.parseInt(j.getString("maxcombo", "0"));
			pp.mods = Integer.parseInt(j.getString("enabled_mods", "0"));
			
			scores[i] = new Score(pp, player);
			i++;
		}
		return scores;
	}
	public static Score readReplay(File replayFile) throws IOException {
		Koohii.PPv2Parameters pp = new Koohii.PPv2Parameters();

		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(replayFile));

		stream.skip(5);
		String hash = readULEBString(stream);
		pp.beatmap = OsuUtils.getBeatmapFromHash(hash);

		String player = readULEBString(stream);

		skipULEBString(stream);

		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		byte[] arr = new byte[2];

		stream.read(arr);
		bb.put(arr);
		bb.position(0);
		pp.n300 = bb.getShort();

		bb.clear();
		stream.read(arr);
		bb.put(arr);
		bb.position(0);
		pp.n100 = bb.getShort();

		bb.clear();
		stream.read(arr);
		bb.put(arr);
		bb.position(0);
		pp.n50 = bb.getShort();

		stream.skip(4);

		bb.clear();
		stream.read(arr);
		bb.put(arr);
		bb.position(0);
		pp.nmiss = bb.getShort();

		stream.skip(4);

		bb.clear();
		stream.read(arr);
		bb.put(arr);
		bb.position(0);
		pp.combo = bb.getShort();

		stream.skip(1);

		bb.clear();
		arr = new byte[4];
		stream.read(arr);
		bb.put(arr);
		bb.position(0);
		pp.mods = bb.getInt();

		stream.close();

		return new Score(pp, player);
	}
	public static void skipULEBString(BufferedInputStream stream) throws IOException {
		if(stream.read() == 0)return;
		
		long l = Uleb128.fromByteStream(stream).asLong();
		stream.skip(l);
	}
	public static String readULEBString(BufferedInputStream stream) throws IOException {
		if(stream.read() == 0)return "";
		
		long l = Uleb128.fromByteStream(stream).asLong();
		byte[] bh = new byte[(int) l];
		stream.read(bh);
		return new String(bh, "UTF-8");
	}
}
