package fr.trxyy.alternative.alternative_api.build;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.text.StrSubstitutor;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameForge;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.account.Session;
import fr.trxyy.alternative.alternative_api.minecraft.json.Argument;
import fr.trxyy.alternative.alternative_api.minecraft.json.ArgumentType;
import fr.trxyy.alternative.alternative_api.utils.HastebinSender;
import fr.trxyy.alternative.alternative_api.utils.Logger;
import fr.trxyy.alternative.alternative_api.utils.OperatingSystem;
import fr.trxyy.alternative.alternative_api.utils.file.FileUtil;
import fr.trxyy.alternative.alternative_api.utils.file.GameUtils;
import javafx.application.Platform;

public class GameRunner {

	private GameEngine engine;
	private Session session;
	
	public GameRunner(GameEngine gameEngine, Session account) {
		this.engine = gameEngine;
		this.session = account;
		Logger.log("========================================");
		Logger.log("Unpacking natives             [Step 5/7]");
		Logger.log("========================================");
		this.unpackNatives();
		Logger.log("Deleting unrequired Natives   [Step 6/7]");
		Logger.log("========================================");
		this.deleteFakeNatives();
		if (engine.getStage() != null) {
			Platform.runLater(new Runnable() {
				public void run() {
					engine.getStage().hide();
				}
			});
		}
	}
	  
    public void launch() throws Exception
    {
    	ArrayList<String> commands = getLaunchCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectInput(Redirect.INHERIT);
        processBuilder.redirectOutput(Redirect.INHERIT);
        processBuilder.redirectError(Redirect.INHERIT);
		processBuilder.directory(engine.getGameFolder().getGameDir());
		processBuilder.redirectErrorStream(true);
		String cmds = "";
		for (String command : commands) {
			cmds += command + " ";
		}
		String[] ary = cmds.split(" ");
		Logger.log("Lancement: " + processBuilder.command());
		try {
			Process process = processBuilder.start();
			process.waitFor();
			int exitVal = process.exitValue();
			if (exitVal != 0) {
				engine.getStage().showAndWait();
		        HastebinSender hastebin = new HastebinSender();
		        String crashUrl = "";
				try {
					crashUrl = hastebin.postError(Logger.getLines(), false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Logger.log("\n\n");
				Logger.log("========================================");
				Logger.log("|         Minecraft has crashed.       |");
				Logger.log("|  Your crash report is available at:  |");
				Logger.log("|  " + crashUrl + "  |");
				Logger.log("========================================");
				this.openLink(crashUrl);
			}
		} catch (IOException e) {
			throw new Exception("Cannot launch !", e);
		}
	}
    
	public void openLink(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	private ArrayList<String> getLaunchCommand() {
		ArrayList<String> commands = new ArrayList<String>();
		OperatingSystem os = OperatingSystem.getCurrentPlatform();
        commands.add(OperatingSystem.getJavaPath());
        commands.add("-XX:-UseAdaptiveSizePolicy");
        commands.add("-XX:+UseConcMarkSweepGC");
		
		if (os.equals(OperatingSystem.OSX)) {
			commands.add("-Xdock:name=Minecraft");
			commands.add("-Xdock:icon=" + engine.getGameFolder().getAssetsDir() + "icons/minecraft.icns");
			commands.add("-XX:+CMSIncrementalMode");
		} else if (os.equals(OperatingSystem.WINDOWS)) {
			commands.add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
		}
		commands.add("-Djava.library.path=" + engine.getGameFolder().getNativesDir().getAbsolutePath());
		commands.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
		commands.add("-Dfml.ignorePatchDiscrepancies=true");
		
		boolean is32Bit = "32".equals(System.getProperty("sun.arch.data.model"));
		String defaultArgument = is32Bit ? "-Xmx512M -Xmn128M" : "-Xmx1G -Xmn128M";
		if (engine.getGameMemory() != null) {
			defaultArgument = is32Bit ? "-Xmx512M -Xmn128M" : "-Xmx" + engine.getGameMemory().getCount() + " -Xmn128M";
		}
		String str[] = defaultArgument.split(" ");
		List<String> args = Arrays.asList(str);
		commands.addAll(args);
		
		commands.add("-cp");
		commands.add("\"" + GameUtils.constructClasspath(engine) + "\"");
		commands.add(engine.getGameStyle().getMainClass());
		
		/** ----- Minecraft Arguments ----- */
		if (engine.getMinecraftVersion().getMinecraftArguments() != null) {
	        final String[] argsD = getArgumentsOlder();
	        List<String> arguments = Arrays.asList(argsD);
	        commands.addAll(arguments);
		}
		/** ----- Minecraft Arguments 1.13+ ----- */
		if (engine.getMinecraftVersion().getArguments() != null) {
			List<Argument> argsNewer = engine.getMinecraftVersion().getArguments().get(ArgumentType.GAME);
			final String[] newerArgumentsString = getArgumentsNewer(argsNewer);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < newerArgumentsString.length; i++) {
				sb.append(newerArgumentsString[i] + " ");
			}
			String sub = sb.toString().replace("--demo", "").replace("--width", "").replace("--height", "");
			String strcs[] = sub.split(" ");
			List<String> newerList = Arrays.asList(strcs);
			commands.addAll(newerList);
		}
		
		/** ----- Addons arguments ----- */
		if (engine.getGameArguments() != null) {
			commands.addAll(engine.getGameArguments().getArguments());
		}
		
		/** ----- Size of window ----- */
		if (engine.getGameSize() != null) {
			commands.add("--width=" + engine.getGameSize().getWidth());
			commands.add("--height=" + engine.getGameSize().getHeight());
		}
		
		/** ----- Change properties of Forge (1.13+) ----- */
		if (engine.getGameStyle().getSpecificsArguments() != null) {
			commands.addAll(getForgeArguments());
		}
		
		/** ----- Direct connect to a server if required. ----- */
		if (engine.getGameConnect() != null) {
			commands.add("--server=" + engine.getGameConnect().getIp());
			commands.add("--port=" + engine.getGameConnect().getPort());
		}
		
		/** ----- Tweak Class if required ----- */
		if (engine.getGameStyle().equals(GameStyle.FORGE_1_7_10_OLD) || engine.getGameStyle().equals(GameStyle.FORGE_1_8_TO_1_12_2) || engine.getGameStyle().equals(GameStyle.OPTIFINE) || engine.getGameStyle().equals(GameStyle.ALTERNATIVE)) {
			commands.add("--tweakClass");
			commands.add(engine.getGameStyle().getTweakArgument());
		}
		return commands;
	}
	
	private List<String> getForgeArguments() {
		String specfs = engine.getGameStyle().getSpecificsArguments();
		specfs = specfs.replace("${launch_target_fml}", GameForge.getLaunchTarget())
		.replace("${forge_version_fml}", GameForge.getForgeVersion())
		.replace("${mc_version_fml}", GameForge.getMcVersion())
		.replace("${forge_group_fml}", GameForge.getForgeGroup())
		.replace("${mcp_version_fml}", GameForge.getMcpVersion());
		String[] ficelle = specfs.split(" ");
		List<String> newerList = Arrays.asList(ficelle);
		return newerList;
	}

	private String[] getArgumentsOlder() {
		final Map<String, String> map = new HashMap<String, String>();
		final StrSubstitutor substitutor = new StrSubstitutor(map);
		final String[] split = engine.getMinecraftVersion().getMinecraftArguments().split(" ");
		map.put("auth_player_name", this.session.getUsername());
		map.put("auth_uuid", this.session.getUuid());
		map.put("auth_access_token", this.session.getToken());
		map.put("user_type", "legacy");
		map.put("version_name", this.engine.getGameVersion().getVersion());
		map.put("version_type", "release");
		map.put("game_directory", this.engine.getGameFolder().getPlayDir().getAbsolutePath());
		map.put("assets_root", this.engine.getGameFolder().getAssetsDir().getAbsolutePath());
		map.put("assets_index_name", this.engine.getGameVersion().getAssetIndex());
		map.put("user_properties", "{}");

		for (int i = 0; i < split.length; i++)
			split[i] = substitutor.replace(split[i]);

		return split;
	}
	
	private String[] getArgumentsNewer(List<Argument> args) {
		final Map<String, String> map = new HashMap<String, String>();
		final StrSubstitutor substitutor = new StrSubstitutor(map);
		final String[] split = new String[args.size()];
		for (int i = 0; i < args.size(); i++) {
				split[i] = args.get(i).getArguments();
		}
		map.put("auth_player_name", this.session.getUsername());
		map.put("auth_uuid", this.session.getUuid());
		map.put("auth_access_token", this.session.getToken());
		map.put("user_type", "legacy");
		map.put("version_name", this.engine.getGameVersion().getVersion());
		map.put("version_type", "release");
		map.put("game_directory", this.engine.getGameFolder().getPlayDir().getAbsolutePath());
		map.put("assets_root", this.engine.getGameFolder().getAssetsDir().getAbsolutePath());
		map.put("assets_index_name", this.engine.getGameVersion().getAssetIndex());
		map.put("user_properties", "{}");

		for (int i = 0; i < split.length; i++)
			split[i] = substitutor.replace(split[i]);

		return split;
	}
	
	private void unpackNatives() {
		try {
			FileUtil.unpackNatives(engine.getGameFolder().getNativesDir(), engine);
		} catch (IOException e) {
			Logger.log("Couldn't unpack natives!");
			e.printStackTrace();
			return;
		}
	}
	
	private void deleteFakeNatives() {
		try {
			FileUtil.deleteFakeNatives(engine.getGameFolder().getNativesDir(), engine);
		} catch (IOException e) {
			Logger.log("Couldn't unpack natives!");
			e.printStackTrace();
			return;
		}
	}
	
	public static List<String> hideAccessToken(String[] arguments) {
        final ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < arguments.length; i++) {
            if (i > 0 && Objects.equals(arguments[i-1], "--accessToken")) {
                output.add("????????");
            } else {
                output.add(arguments[i]);
            }
        }
        return output;
    }
}
