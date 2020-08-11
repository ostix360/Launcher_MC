package fr.trxyy.alternative.alternative_api.utils.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fr.trxyy.alternative.alternative_api.*;
import fr.trxyy.alternative.alternative_api.utils.Logger;

public class UserConfig {

    public String ram;
    public String windowsSize;
    public String MCVersion;
    public String MCStyle;
    public String autoLogin;
    public File userConfig;

    public UserConfig(GameEngine engine) {
        this.userConfig = new File(engine.getGameFolder().getBinDir(), "user_config.cfg");
        if (!this.userConfig.exists()) {
            try {
                this.userConfig.createNewFile();
                this.writeConfig("1", "854x480", "false", "1.12.2", "VANILLA");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        readConfig();
        engine.reg(convertMemory(getMemory()));
        engine.reg(getWindowSize(getWindowSize()));
        engine.reg(getMCVersion(getMCVersion()));
        engine.reg(getMCStyle(getMCStyle()));
    }

    public void writeConfig(String s, String s1, String s2, String s3, String s4) {
        try {
            FileWriter fw = new FileWriter(this.userConfig);
            fw.write(s + ";");
            fw.write(s1 + ";");
            fw.write(s2 + ";");
            fw.write(s3 + ";");
            fw.write(s4);
            fw.close();
        } catch (IOException e) {
            Logger.log(e.toString());
        }
    }

    public void readConfig() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.userConfig));
            String line = br.readLine();
            String[] result = line.split(";");
            this.ram = result[0];
            this.windowsSize = result[1];
            this.autoLogin = result[2];
            this.MCVersion = result[3];
            this.MCStyle = result[4];

            br.close();
        } catch (IOException e) {
            Logger.log(e.toString());
        }
    }

    public String getRamString() {
        return this.ram;
    }

    public double getRam() {
        if (ram.contentEquals("1.0")) {
            return 1;
        } else if (ram.contentEquals("2.0")) {
            return 2;
        } else if (ram.contentEquals("3.0")) {
            return 3;
        } else if (ram.contentEquals("4.0")) {
            return 4;
        } else if (ram.contentEquals("5.0")) {
            return 5;
        } else if (ram.contentEquals("6.0")) {
            return 6;
        } else if (ram.contentEquals("7.0")) {
            return 7;
        } else if (ram.contentEquals("8.0")) {
            return 8;
        } else if (ram.contentEquals("9.0")) {
            return 9;
        } else if (ram.contentEquals("10.0")) {
            return 10;
        }
        return 1;
    }

    public GameMemory convertMemory(String value) {
        switch (value) {
            case "0.0":
                return GameMemory.DEFAULT;
            case "1.0":
                return GameMemory.DEFAULT;
            case "2.0":
                return GameMemory.RAM_2G;
            case "3.0":
                return GameMemory.RAM_3G;
            case "4.0":
                return GameMemory.RAM_4G;
            case "5.0":
                return GameMemory.RAM_5G;
            case "6.0":
                return GameMemory.RAM_6G;
            case "7.0":
                return GameMemory.RAM_7G;
            case "8.0":
                return GameMemory.RAM_8G;
            case "9.0":
                return GameMemory.RAM_9G;
            case "10.0":
                return GameMemory.RAM_10G;
        }
        return GameMemory.DEFAULT;
    }

    public GameMemory getMemory(double value) {
        if (value == 0) {
            return GameMemory.DEFAULT;
        } else if (value == 1) {
            return GameMemory.DEFAULT;
        } else if (value == 2) {
            return GameMemory.RAM_2G;
        } else if (value == 3) {
            return GameMemory.RAM_3G;
        } else if (value == 4) {
            return GameMemory.RAM_4G;
        } else if (value == 5) {
            return GameMemory.RAM_5G;
        } else if (value == 6) {
            return GameMemory.RAM_6G;
        } else if (value == 7) {
            return GameMemory.RAM_7G;
        } else if (value == 8) {
            return GameMemory.RAM_8G;
        } else if (value == 9) {
            return GameMemory.RAM_9G;
        } else if (value == 10) {
            return GameMemory.RAM_10G;
        }
        return GameMemory.DEFAULT;
    }

    public GameSize getWindowSize(String value) {
        switch (value) {
            case "854x480":
                return GameSize.DEFAULT;
            case "1024x768":
                return GameSize.SIZE_1024x768;
            case "1280x1024":
                return GameSize.SIZE_1280x1024;
            case "1366x768":
                return GameSize.SIZE_1366x768;
            case "1600x900":
                return GameSize.SIZE_1600x900;
            case "1920x1080":
                return GameSize.SIZE_1920x1080;
            case "2560x1440":
                return GameSize.SIZE_2560x1440;
        }
        return GameSize.DEFAULT;
    }

    public String getWindowSize() {
        return this.windowsSize;
    }

    public GameVersion getMCVersion(String value) {
        switch (value) {
            case "1.7.10":
                return GameVersion.V_1_7_10;
            case "1.8":
                return GameVersion.V_1_8;
            case "1.8.1":
                return GameVersion.V_1_8_1;
            case "1.8.2":
                return GameVersion.V_1_8_2;
            case "1.8.3":
                return GameVersion.V_1_8_3;
            case "1.8.4":
                return GameVersion.V_1_8_4;
            case "1.8.5":
                return GameVersion.V_1_8_5;
            case "1.8.6":
                return GameVersion.V_1_8_6;
            case "1.8.7":
                return GameVersion.V_1_8_7;
            case "1.8.8":
                return GameVersion.V_1_8_8;
            case "1.8.9":
                return GameVersion.V_1_8_9;
            case "1.9":
                return GameVersion.V_1_9;
            case "1.9.1":
                return GameVersion.V_1_9_1;
            case "1.9.2":
                return GameVersion.V_1_9_2;
            case "1.9.3":
                return GameVersion.V_1_9_3;
            case "1.9.4":
                return GameVersion.V_1_9_4;
            case "1.10":
                return GameVersion.V_1_10;
            case "1.10.1":
                return GameVersion.V_1_10_1;
            case "1.10.2":
                return GameVersion.V_1_10_2;
            case "1.11":
                return GameVersion.V_1_11;
            case "1.11.1":
                return GameVersion.V_1_11_1;
            case "1.11.2":
                return GameVersion.V_1_11_2;
            case "1.12":
                return GameVersion.V_1_12;
            case "1.12.1":
                return GameVersion.V_1_12_1;
            case "1.12.2":
                return GameVersion.V_1_12_2;
            case "1.13":
                return GameVersion.V_1_13;
            case "1.13.1":
                return GameVersion.V_1_13_1;
            case "1.13.2":
                return GameVersion.V_1_13_2;
            case "1.14":
                return GameVersion.V_1_14;
            case "1.14.1":
                return GameVersion.V_1_14_1;
            case "1.14.2":
                return GameVersion.V_1_14_2;
            case "1.14.3":
                return GameVersion.V_1_14_3;
            case "1.14.4":
                return GameVersion.V_1_14_4;
            case "1.15":
                return GameVersion.V_1_15;
            case "1.15.1":
                return GameVersion.V_1_15_1;
            case "1.15.2":
                return GameVersion.V_1_15_2;
            case "1.16":
                return GameVersion.V_1_16;
            case "1.16.1":
                return GameVersion.V_1_16_1;
            default:return GameVersion.V_1_12_2;
        }


    }

    public String getMCVersion() {
        return this.MCVersion;
    }

    public GameStyle getMCStyle(String value) {
        switch (value) {
            case "VanillaPlus":
                return GameStyle.VANILLA_PLUS;
            case "OptiFine":
                return GameStyle.OPTIFINE;
            case "Forge 1.7.10-":
                return GameStyle.FORGE_1_7_10_OLD;
            case "Forge 1.8 -> 1.12.2":
                return GameStyle.FORGE_1_8_TO_1_12_2;
            case "Forge 1.13+":
                return GameStyle.FORGE_1_13_HIGHER;
            case "OptiFine_Alternat":
                return GameStyle.ALTERNATIVE;
            default:return GameStyle.VANILLA;
        }
    }

    public String getMCStyle() {
        return MCStyle;
    }

    public String getAutoLoginString() {
        return this.autoLogin;
    }

    public boolean getAutoLogin() {
        return !this.autoLogin.equals("false");
    }


    public String getMemory() {
        return ram;
    }

}
