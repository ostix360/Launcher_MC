package fr.ostix.launcher;

import fr.trxyy.alternative.alternative_api.*;
import fr.trxyy.alternative.alternative_api.maintenance.GameMaintenance;
import fr.trxyy.alternative.alternative_api.maintenance.Maintenance;
import fr.trxyy.alternative.alternative_api_ui.LauncherBackground;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.AlternativeBase;
import fr.trxyy.alternative.alternative_api_ui.base.LauncherBase;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends AlternativeBase {


    private final GameFolder gameFolder = new GameFolder("ostix");
    private final LauncherPreferences launcherPreferences = new LauncherPreferences("Ostix Launcher",1250,750,true);
    private final GameEngine gameEngine = new GameEngine(gameFolder,launcherPreferences,GameVersion.V_1_12_2,GameStyle.FORGE_1_8_TO_1_12_2);
    private final GameMaintenance maintenance = new GameMaintenance(Maintenance.USE,gameEngine);
    private final GameLinks links = new GameLinks("http://25.86.120.161:80/launcher_MC/", gameEngine.getGameVersion().getVersion()+".json",gameEngine);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        gameEngine.reg(links);
        gameEngine.reg(primaryStage);
        gameEngine.reg(maintenance);
        LauncherBase base = new LauncherBase(primaryStage,scene, StageStyle.UNDECORATED, gameEngine);
        base.setIconImage(primaryStage,getResourceLocation().loadImage(gameEngine, "logo.png"));
    }

    private Parent createContent() {
        LauncherPane pane = new LauncherPane(gameEngine);
        new Panel(pane,gameEngine);
        return pane;
    }
}
