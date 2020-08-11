package fr.trxyy.alternative.alternative_api_ui.base;

import java.awt.Point;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.maintenance.Maintenance;
import fr.trxyy.alternative.alternative_api.utils.Logger;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class LauncherBase {

	final Point dragDelta = new Point();

	public LauncherBase(final Stage stage, Scene scene, StageStyle style, GameEngine engine) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
			new LauncherAlert(e.toString(), AlertType.ERROR);
			Logger.err(e.toString());
		}

		stage.initStyle(style);
		if (style.equals(StageStyle.TRANSPARENT)) {
			scene.setFill(Color.TRANSPARENT);
		}
		stage.setResizable(false);
		stage.setTitle(engine.getLauncherPreferences().getName());
		stage.setWidth(engine.getLauncherPreferences().getWidth());
		stage.setHeight(engine.getLauncherPreferences().getHeight());
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent windowEvent) {
				Platform.exit();
				System.exit(0);
			}
		});
		if (engine.getLauncherPreferences().isMoveable()) {
			scene.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					dragDelta.x = (int) (stage.getX() - mouseEvent.getScreenX());
					dragDelta.y = (int) (stage.getY() - mouseEvent.getScreenY());
				}
			});
			scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					stage.setX(mouseEvent.getScreenX() + dragDelta.x);
					stage.setY(mouseEvent.getScreenY() + dragDelta.y);
				}
			});
		}
		stage.setScene(scene);
		this.displayCopyrights();
		
		if (engine.getGameMaintenance() != null) {
			if (engine.getGameMaintenance().getMaintenance().equals(Maintenance.USE)) {
				String result = "";
				try {
					result = engine.getGameMaintenance().readMaintenance();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!result.equals("Ok")) {
					new LauncherAlert("" + result, AlertType.WARNING);
				} else {
					stage.show();
				}
			} else {
				stage.show();
			}
		} else {
			stage.show();
		}

	}

	public void setIconImage(Stage primaryStage, Image img) {
		primaryStage.getIcons().add(img);
	}

	private void displayCopyrights() {
		Logger.log("========================================");
		Logger.log("|    Thanks for using AlternativeAPI   |");
		Logger.log("|         AlternativeAPI 1.2.62        |");
		Logger.log("|           Version: RELEASE           |");
		Logger.log("|           Author(s): Trxyy           |");
		Logger.log("========================================");
	}	

}