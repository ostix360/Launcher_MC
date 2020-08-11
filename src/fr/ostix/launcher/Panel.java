package fr.ostix.launcher;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameLinks;
import fr.trxyy.alternative.alternative_api.account.AccountType;
import fr.trxyy.alternative.alternative_api.auth.GameAuth;
import fr.trxyy.alternative.alternative_api.updater.GameUpdater;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.Mover;
import fr.trxyy.alternative.alternative_api.utils.config.UserConfig;
import fr.trxyy.alternative.alternative_api.utils.config.UsernameSaver;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Panel extends IScreen {
    Font fontComfortaa = FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa-Regular", 18);

    private final LauncherButton settings;
    private final LauncherButton login;
    private final LauncherPasswordField password;
    private final LauncherTextField userName;

    private final UsernameSaver usernameSaver;
    public UserConfig userConfig;

    private final GameUpdater updater = new GameUpdater();
    private final LauncherRectangle updateRectangle;
    private final LauncherLabel currentFileLabel;
    private final LauncherLabel updateLabel;
    private final LauncherLabel currentStep;
    public LauncherProgressBar bar;


    public Panel(LauncherPane root, GameEngine engine) {

        drawBackgroundImage(engine, root, "background.png");

        usernameSaver = new UsernameSaver(engine);
        userConfig = new UserConfig(engine);

        LauncherRectangle rectangle = new LauncherRectangle(root, 0, 0, engine.getWidth(), 30);
        rectangle.setFill(Color.rgb(30, 25, 25, 1));

        this.drawLogo(engine, getResourceLocation().loadImage(engine, "alternative_logo.png"), engine.getWidth() / 2 - 150, 50, 340, 220, root, Mover.MOVE);

        LauncherLabel title = new LauncherLabel(root);
        title.setText("Launcher Ostix");
        FontLoader.setSize(20);
        title.setFont(fontComfortaa);
        title.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        title.setPosition(engine.getWidth() / 2 - 50, -5);
        title.setOpacity(1);
        title.setSize(500, 40);

        LauncherImage titleImage = new LauncherImage(root);
        titleImage.setImage(getResourceLocation().loadImage(engine, "favicon.png"));
        titleImage.setPosition(engine.getWidth() / 2 - 85, 2);
        titleImage.setSize(25, 25);

        userName = new LauncherTextField(root);
        userName.setPosition(engine.getWidth() / 2 - 100, engine.getHeight() / 2);
        userName.setSize(200, 35);
        userName.setFont(fontComfortaa);
        userName.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-text-fill: white");
        userName.setVoidText("Username (crack)/ adresse mail (mojang only)");
        userName.setText(usernameSaver.getUsername());

        password = new LauncherPasswordField(root);
        password.setPosition(engine.getWidth() / 2 - 100, engine.getHeight() / 2 + 50);
        password.setSize(200, 35);
        password.setFont(fontComfortaa);
        password.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-text-fill: white");
        password.setVoidText("password/ rien(crack)");

        login = new LauncherButton(root);
        login.setText("Login");
        login.setPosition(engine.getWidth() / 2 - 30, engine.getHeight() / 2 + 100);
        login.setSize(130, 35);
        login.setFont(fontComfortaa);
        login.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-text-fill: white");
        login.setOnAction(event -> {
            if (userName.getText().length() <= 3) {
                new LauncherAlert("Conection impossible", " /!\\ Le pseudo doit contenire plus 3 caracteres", Alert.AlertType.ERROR);
            } else if (password.getText().isEmpty()) {
                usernameSaver.writeUsername(userName.getText());
                new LauncherAlert("Compte Crack!", "Attention vous utilier un compte crack ", Alert.AlertType.WARNING);
                GameAuth auth = new GameAuth(userName.getText(), password.getText(), AccountType.OFFLINE);
                if (auth.isAuthed) {
                    update(engine, auth);
                } else {
                    new LauncherAlert("Conection impossible", " /!\\ Veifier votre conection Internet", Alert.AlertType.ERROR);
                }
            } else {
                GameAuth auth = new GameAuth(userName.getText(), password.getText(), AccountType.MOJANG);
                if (auth.isAuthed) {
                    update(engine, auth);
                } else {
                    new LauncherAlert("Conection impossible", " /!\\ Veifier votre conection Internet et vos identifiants", Alert.AlertType.ERROR);
                }
            }
        });

        settings = new LauncherButton(root);
        settings.setStyle("-fx-background-color: rgba(1,1,1,1); -fx-text-fill: white");
        LauncherImage imageSettings = new LauncherImage(root, getResourceLocation().loadImage(engine, "settings.png"));
        imageSettings.setSize(28, 28);
        settings.setGraphic(imageSettings);
        settings.setPosition(engine.getWidth() / 2 - 100, engine.getHeight() / 2 + 100);
        settings.setSize(28, 28);
        settings.setOnAction(event -> {
            Scene scene = new Scene(createSettingsPanel(engine));
            Stage stage = new Stage();
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Parametres Launcher");
            stage.setWidth(500);
            stage.setHeight(315);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        });

        LauncherButton minimize = new LauncherButton(root);
        minimize.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-text-fill: white");
        LauncherImage imageMinimize = new LauncherImage(root, getResourceLocation().loadImage(engine, "minimize.png"));
        imageMinimize.setSize(50, 20);
        minimize.setGraphic(imageMinimize);
        minimize.setPosition(engine.getWidth() - 130, 0);
        minimize.setSize(50, 20);
        minimize.setOnAction(event -> {
            Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });

        LauncherButton close = new LauncherButton(root);
        close.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-text-fill: white");
        LauncherImage imageClose = new LauncherImage(root, getResourceLocation().loadImage(engine, "close.png"));
        imageClose.setSize(50, 20);
        close.setGraphic(imageClose);
        close.setPosition(engine.getWidth() - 70, 0);
        close.setSize(50, 20);
        close.setOnAction(event -> {
            System.out.println("Fermeture du Launcher");
            System.exit(0);
        });

        LauncherButton youtube = new LauncherButton(root);
        youtube.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-text-fill: white");
        LauncherImage imageYoutube = new LauncherImage(root, getResourceLocation().loadImage(engine, "yt_icon.png"));
        imageYoutube.setSize(150, 100);
        youtube.setGraphic(imageYoutube);
        youtube.setPosition(engine.getWidth() / 2 - 75, engine.getHeight() / 2 + 200);
        youtube.setSize(150, 100);
        youtube.setOnAction(event -> openLink("https://www.youtube.com/channel/UCFJdIiRDdjAPqYkX6N6gneA/featured?view_as=subscriber"));


        //*******************UPDATE*****************

        updateRectangle = new LauncherRectangle(root, engine.getWidth() / 2 - 200, engine.getHeight() / 2-55, 400, 190);
        updateRectangle.setFill(Color.rgb(0, 0, 0, 0.6D));
        updateRectangle.setArcWidth(10.0D);
        updateRectangle.setArcHeight(10.0D);
        updateRectangle.setVisible(false);

        updateLabel = new LauncherLabel(root);
        updateLabel.setText("** Mise A Jour **");
        updateLabel.setAlignment(Pos.CENTER);
        updateLabel.setFont(fontComfortaa);
        updateLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        updateLabel.setPosition(engine.getWidth() / 2 - 95, engine.getHeight() / 2 - 60);
        updateLabel.setSize(190, 40);
        updateLabel.setVisible(false);

        bar = new LauncherProgressBar(root);
        bar.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2 + 40);
        bar.setSize(320, 10);
        bar.setVisible(false);

        currentStep = new LauncherLabel(root);
        currentStep.setText("** Preparation de la mise a jour **");
        currentStep.setAlignment(Pos.CENTER);
        currentStep.setFont(fontComfortaa);
        currentStep.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        currentStep.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2 + 78);
        currentStep.setOpacity(0.4D);
        currentStep.setSize(320, 40);
        currentStep.setVisible(false);

        currentFileLabel = new LauncherLabel(root);
        currentFileLabel.setText("** ... **");
        currentFileLabel.setAlignment(Pos.CENTER);
        currentFileLabel.setFont(fontComfortaa);
        currentFileLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
        currentFileLabel.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2);
        currentFileLabel.setSize(320, 40);
        currentFileLabel.setVisible(false);

    }

    public void update(GameEngine engine, GameAuth auth) {
        disableForUpdate(true);

        GameLinks links;
        links = new GameLinks("http://25.86.120.161/launcher_MC/", engine.getGameVersion().getVersion()+".json",engine);
        engine.reg(links);
        updater.reg(engine);
        updater.reg(auth.getSession());
        engine.reg(this.updater);
        Thread updateThread = new Thread(() -> engine.getGameUpdater().start());
        updateThread.start();
        /* ===================== REFAICHIR LE NOM DU FICHIER, PROGRESSBAR, POURCENTAGE  ===================== **/
        Timeline timeline = new Timeline(
                new KeyFrame(javafx.util.Duration.seconds(0.0), e -> updateDownload(engine)
                ),
                new KeyFrame(javafx.util.Duration.seconds(0.1D)
                ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        if (engine.getGameUpdater().getCurrentInfo().equals("Telechargement accompli."))
        {
            updateThread.interrupt();
            try {
                Thread.sleep(1000);
                disableForUpdate(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void disableForUpdate(boolean disable) {
        userName.setDisable(disable);
        password.setDisable(disable);
        userName.setDisable(disable);
        login.setDisable(disable);

        if (disable) {
            userName.setVisible(false);
            settings.setVisible(false);
            password.setVisible(false);
            login.setVisible(false);
            currentFileLabel.setVisible(true);
            currentStep.setVisible(true);
            updateLabel.setVisible(true);
            updateRectangle.setVisible(true);
            bar.setVisible(true);
        } else {
            currentFileLabel.setVisible(false);
            currentStep.setVisible(false);
            updateLabel.setVisible(false);
            updateRectangle.setVisible(false);
            bar.setVisible(false);
            userName.setVisible(true);
            settings.setVisible(true);
            password.setVisible(true);
            login.setVisible(true);
        }
    }


    private void updateDownload(GameEngine engine) {
        currentFileLabel.setText(engine.getGameUpdater().getCurrentFile());
        currentStep.setText(engine.getGameUpdater().getCurrentInfo());
        double percent = (engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload / 100.0D);
        this.bar.setProgress(percent);
    }

    private Parent createSettingsPanel(GameEngine engine) {
        LauncherPane contentPane = new LauncherPane(engine);
        Rectangle rect = new Rectangle(500, 315);
        rect.setArcHeight(15.0);
        rect.setArcWidth(15.0);
        contentPane.setClip(rect);
        contentPane.setStyle("-fx-background-color: transparent;");
        new Settings(contentPane, engine, this);
        return contentPane;
    }
}
