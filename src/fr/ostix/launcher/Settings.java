package fr.ostix.launcher;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameSize;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.GameVersion;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.Logger;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherRectangle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Settings extends IScreen {

    private final LauncherLabel memorySliderLabel;
    private final Slider memorySlider;
    private final ComboBox<String> windowsSizeList;
    private final ComboBox<String> versionsList;
    private final ComboBox<String> stylesList;
    private final CheckBox autoLogin;

    public Settings(final Pane root, final GameEngine engine, final Panel pane) {
        this.drawBackgroundImage(engine, root, "background.png");
        pane.userConfig.readConfig();
        engine.reg(pane.userConfig.convertMemory(pane.userConfig.getMemory()));
        engine.reg(pane.userConfig.getWindowSize(pane.userConfig.getWindowSize()));
        /* ===================== RECTANGLE NOIR EN HAUT ===================== */
        LauncherRectangle topRectangle = new LauncherRectangle(root, 0, 0, 500, 15);
        topRectangle.setOpacity(0.7);
        /* ===================== LABEL TITRE ===================== */
        LauncherLabel titleLabel = new LauncherLabel(root);
        titleLabel.setText("PARAMETRES");
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 28F));
        titleLabel.setPosition(150, 20);
        titleLabel.setSize(230, 35);
        /* ===================== MC SIZE LABEL ===================== */
        LauncherLabel windowsSizeLabel = new LauncherLabel(root);
        windowsSizeLabel.setText("Taille de la fenetre:");
        windowsSizeLabel.setOpacity(1.0);
        windowsSizeLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
        windowsSizeLabel.setStyle("-fx-text-fill: white;");
        windowsSizeLabel.setSize(370, 30);
        windowsSizeLabel.setPosition(50, 60);
        /* ===================== MC SIZE LIST ===================== */
        this.windowsSizeList = new ComboBox<>();
        this.populateSizeList();
        if (pane.userConfig.getWindowSize() != null) {
            this.windowsSizeList.setValue(pane.userConfig.getWindowSize());
        }
        this.windowsSizeList.setPrefSize(100, 20);
        this.windowsSizeList.setLayoutX(340);
        this.windowsSizeList.setLayoutY(65);
        this.windowsSizeList.setVisibleRowCount(5);
        root.getChildren().add(this.windowsSizeList);
        /* ===================== MC VERSION LABEL ===================== */
        LauncherLabel versionsLabel = new LauncherLabel(root);
        versionsLabel.setText("Version du jeu:");
        versionsLabel.setOpacity(1.0);
        versionsLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
        versionsLabel.setStyle("-fx-text-fill: white;");
        versionsLabel.setSize(370, 30);
        versionsLabel.setPosition(50, 100);
        /* ===================== MC VERSION LIST ===================== */
        this.versionsList = new ComboBox<>();
        this.populateVersionList();
        if (pane.userConfig.getMCVersion() != null) {
            this.versionsList.setValue(pane.userConfig.getMCVersion());
        }
        this.versionsList.setPrefSize(100, 20);
        this.versionsList.setLayoutX(340);
        this.versionsList.setLayoutY(105);
        this.versionsList.setVisibleRowCount(10);
        root.getChildren().add(this.versionsList);
        /* ===================== MC STYLE LABEL ===================== */
        LauncherLabel styleLabel = new LauncherLabel(root);
        styleLabel.setText("Mode de Minecraft:");
        styleLabel.setOpacity(1.0);
        styleLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
        styleLabel.setStyle("-fx-text-fill: white;");
        styleLabel.setSize(370, 30);
        styleLabel.setPosition(50, 140);
        /* ===================== MC STYLE LIST ===================== */
        this.stylesList = new ComboBox<>();
        this.populateStyleList();
        if (pane.userConfig.getMCStyle() != null) {
            this.stylesList.setValue(pane.userConfig.getMCStyle());
        }
        this.stylesList.setPrefSize(100, 20);
        this.stylesList.setLayoutX(340);
        this.stylesList.setLayoutY(145);
        this.stylesList.setVisibleRowCount(5);
        root.getChildren().add(this.stylesList);
        /* ===================== SLIDER RAM LABEL ===================== */
        LauncherLabel sliderLabel = new LauncherLabel(root);
        sliderLabel.setText("RAM Allouee:");
        sliderLabel.setOpacity(1.0);
        sliderLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
        sliderLabel.setStyle("-fx-text-fill: white;");
        sliderLabel.setSize(370, 30);
        sliderLabel.setPosition(50, 210);
        /* ===================== SLIDER RAM LABEL SELECTIONNED ===================== */
        this.memorySliderLabel = new LauncherLabel(root);
        this.memorySliderLabel.setOpacity(1.0);
        this.memorySliderLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
        this.memorySliderLabel.setStyle("-fx-text-fill: white;");
        this.memorySliderLabel.setSize(370, 30);
        this.memorySliderLabel.setPosition(380, 210);
        /* ===================== SLIDER RAM ===================== */
        this.memorySlider = new Slider();
        this.memorySlider.setStyle("-fx-control-inner-background: rgba(46, 47, 48, 0.5);");
        this.memorySlider.setMin(1);
        this.memorySlider.setMax(10);
        if (pane.userConfig.getRamString() != null) {
            this.memorySlider.setValue(pane.userConfig.getRam());
        }
        this.memorySlider.setLayoutX(50);
        this.memorySlider.setLayoutY(240);
        this.memorySlider.setPrefWidth(395);
        this.memorySlider.setBlockIncrement(1);
        memorySlider.valueProperty().addListener((ov, old_val, new_val) -> memorySlider.setValue(Math.round(new_val.doubleValue())));
        this.memorySlider.valueProperty().addListener((observable, oldValue, newValue) -> memorySliderLabel.setText(newValue + "Gb"));
        Platform.runLater(() -> root.getChildren().add(memorySlider));

        this.memorySliderLabel.setText(this.memorySlider.getValue() + "Gb");


        this.autoLogin = new CheckBox();
        this.autoLogin.setText("Connexion auto (crack)");
        this.autoLogin.setSelected(pane.userConfig.getAutoLogin());
        this.autoLogin.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
        this.autoLogin.setStyle("-fx-text-fill: white;");
        this.autoLogin.setLayoutX(50);
        this.autoLogin.setLayoutY(180);
        root.getChildren().add(autoLogin);

        /* ===================== BOUTON DE VALIDATION ===================== */
        LauncherButton saveButton = new LauncherButton(root);
        saveButton.setText("Valider");
        saveButton.setStyle("-fx-background-color: rgba(53, 89, 119, 0.4); -fx-text-fill: white;");
        saveButton.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 16F));
        saveButton.setPosition(310, 270);
        saveButton.setSize(130, 35);
        saveButton.setOnAction(event -> {
            String style = "";
            if (stylesList.getValue().equals("FORGE")) {

                new LauncherAlert("ALERTE!!!!","Attention toutes les versions de forge ne sont pas encor pris en compte par notre launcher \n " +
                        "si vous voullez avoire une version different de celle qui crash contacter ostix360@gmail.com pour le moment seul la version 1.12.2 de forge fonction", Alert.AlertType.CONFIRMATION);
                switch (versionsList.getValue()) {
                    case "1.8.1":
                    case "1.8.2":
                    case "1.8.3":
                    case "1.8.4":
                        style = GameStyle.FORGE_1_8_TO_1_12_2.getTweakName();
                        versionsList.setValue("1.8");
                        break;
                    case "1.8.5":
                    case "1.8.6":
                    case "1.8.7":
                        style = GameStyle.FORGE_1_8_TO_1_12_2.getTweakName();
                        versionsList.setValue("1.8.8");
                        break;
                    case "1.9.1":
                    case "1.9.2":
                        style = GameStyle.FORGE_1_8_TO_1_12_2.getTweakName();
                        versionsList.setValue("1.9");
                        break;
                    case "1.9.3":
                        style = GameStyle.FORGE_1_8_TO_1_12_2.getTweakName();
                        versionsList.setValue("1.9.4");
                        break;
                    case "1.10.1":
                        style = GameStyle.FORGE_1_8_TO_1_12_2.getTweakName();
                        versionsList.setValue("1.10.2");
                        break;
                    case "1.11.1":
                        style = GameStyle.FORGE_1_8_TO_1_12_2.getTweakName();
                        versionsList.setValue("1.11");
                        break;
                    case "1.13":
                    case "1.13.1":
                        versionsList.setValue("1.13.2");
                        break;
                    case "1.14":
                    case "1.14.1":
                        versionsList.setValue("1.14.2");
                        break;
                    case "1.15":
                        versionsList.setValue("1.15.1");
                        break;
                    case "1.16":
                        versionsList.setValue("1.16.1");
                        break;
                }
            }
            pane.userConfig.writeConfig("" + memorySlider.getValue(), windowsSizeList.getValue(), isAutoLogin(), versionsList.getValue(), style);
            Logger.log("Memory Usage " + memorySlider.getValue() + " GByte(s) || Window size " + windowsSizeList.getValue()
                    + " || MC Version " + versionsList.getValue()
                    + " || MC Mod " + stylesList.getValue());
//            engine.reg(pane.userConfig.getMemory(memorySlider.getValue()));
//            engine.reg(pane.userConfig.getWindowSize(windowsSizeList.getValue()));
//            engine.reg(pane.userConfig.getMCStyle(stylesList.getValue()));
//            engine.reg(pane.userConfig.getMCVersion(versionsList.getValue()));
            Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    private void populateSizeList() {
        for (GameSize size : GameSize.values()) {
            this.windowsSizeList.getItems().add(size.getDesc());
        }
    }

    private void populateVersionList() {
        for (GameVersion version : GameVersion.values()) {
            this.versionsList.getItems().add(version.getVersion());
        }
    }

    private void populateStyleList() {

            this.stylesList.getItems().add(GameStyle.VANILLA.getTweakName());
            this.stylesList.getItems().add("FORGE");

    }

    private String isAutoLogin() {
        if (autoLogin.isSelected()) {
            return "true";
        } else {
            return "false";
        }
    }
}
