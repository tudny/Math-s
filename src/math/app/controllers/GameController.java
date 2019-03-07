package math.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import math.app.classes.OwnArrayList;

public class GameController {

	private String notTakenCss = "-fx-border-color: transparent";
	private String takenCSS = "-fx-background-color: \n" +
			"        #c3c4c4,\n" +
			"        linear-gradient(#d6d6d6 50%, white 100%),\n" +
			"        radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);\n" +
			"    -fx-background-radius: 30;\n" +
			"    -fx-background-insets: 0,1,1;\n" +
			"    -fx-text-fill: black;\n" +
			"    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );";
	private String greenButton = "-fx-background-color:\n" +
			"        linear-gradient(#f0ff35, #a9ff00),\n" +
			"        radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);\n" +
			"    -fx-background-radius: 6, 5;\n" +
			"    -fx-background-insets: 0, 1;\n" +
			"    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n" +
			"    -fx-text-fill: #395306;";

	private Stage primary;

	private Boolean isAlreadyClicked = false;
	private Integer alreadyX = null;
	private Integer alreadyY = null;

	private int[][] dx = {{0, 1, 0, -1}, {0, 2, 0, -2}};
	private int[][] dy = {{1, 0, -1, 0}, {2, 0, -2, 0}};

	private OwnArrayList<Button> buttonArray;
	private OwnArrayList<Boolean> isPawn;

	public void setAll(Stage primary, AnchorPane root, Integer buttonWidth, Integer buttonHeight) {
		this.primary = primary;

		primary.getScene().setOnKeyReleased(event -> {
			System.out.println("Key pressed: " + event.getCode());
			if(event.getCode() == KeyCode.ESCAPE){
				resetGreen();
			}
		});

		buttonArray = new OwnArrayList<>(buttonWidth, buttonHeight);
		isPawn = new OwnArrayList<>(buttonWidth, buttonHeight);

		for(int i = 0; i < buttonWidth * buttonHeight; i++) buttonArray.add(new Button());
		for(int i = 0; i < buttonWidth * buttonHeight; i++) isPawn.add(Boolean.FALSE);

		try {
			for (int y = 0; y < buttonHeight; y++) {
				for (int x = 0; x < buttonWidth; x++) {
					Button button = new Button();
					button.setPrefWidth(MainController.buttonSize);
					button.setPrefHeight(MainController.buttonSize);

					buttonArray.mySet(x, y, button);
					root.getChildren().add(button);

					button.setOnAction(event -> {
						Integer X = buttonArray.xIndexOf(button);
						Integer Y = buttonArray.yIndexOf(button);
						System.out.println("Button: " + X + " " + Y + ": " + isPawn.myGet(X, Y));

						if(isAlreadyClicked){
							if(button.getStyle().equals(greenButton)){
								button.setStyle(takenCSS);
								isPawn.mySet(X, Y, Boolean.TRUE);

								buttonArray.myGet(alreadyX, alreadyY).setStyle(notTakenCss);
								isPawn.mySet(alreadyX, alreadyY, Boolean.FALSE);

								Integer jX = (alreadyX + X) / 2;
								Integer jY = (alreadyY + Y) / 2;
								buttonArray.myGet(jX, jY).setStyle(notTakenCss);
								isPawn.mySet(jX, jY, Boolean.FALSE);

								resetGreen();
							}
						} else if(isPawn.myGet(X, Y).equals(Boolean.TRUE)){
							isAlreadyClicked = true;

							alreadyX = X;
							alreadyY = Y;

							int cnt = 0;

							for (int i = 0; i < 4; i++) {
								Integer nX = X + dx[1][i];
								Integer nY = Y + dy[1][i];
								Integer jX = X + dx[0][i];
								Integer jY = Y + dy[0][i];

								if (0 <= nX && nX < buttonWidth && 0 <= nY && nY < buttonHeight) {
									if (isPawn.myGet(nX, nY).equals(Boolean.FALSE) && isPawn.myGet(jX, jY).equals(Boolean.TRUE)) {
										buttonArray.myGet(nX, nY).setStyle(greenButton);
										cnt++;
									}
								}
							}

							if(cnt == 0){
								isAlreadyClicked = false;
								alreadyX = null;
								alreadyY = null;
							}
						}

					});

					AnchorPane.setTopAnchor(button, (double) y * MainController.buttonSize);
					AnchorPane.setLeftAnchor(button, (double) x * MainController.buttonSize);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		Line line = new Line(0, MainController.buttonSize * 5, MainController.buttonSize * buttonWidth, MainController.buttonSize * 5);
		line.setFill(Color.RED);
		root.getChildren().add(line);

		for(int y = 0; y < buttonHeight; y++){
			for(int x = 0; x < buttonWidth; x++){
				Button button = buttonArray.myGet(x, y);
				if(y > 4) {
					button.setStyle(takenCSS);
					isPawn.mySet(x, y, Boolean.TRUE);
				} else {
					button.setStyle(notTakenCss);
				}
			}
		}
	}

	private void resetGreen() {

		if(!isAlreadyClicked) return;

		for (int i = 0; i < 4; i++) {
			Integer nX = alreadyX + dx[1][i];
			Integer nY = alreadyY + dy[1][i];
			if(buttonArray.myGet(nX, nY).getStyle().equals(greenButton)){
				buttonArray.myGet(nX, nY).setStyle(notTakenCss);
			}
		}

		isAlreadyClicked = false;
		alreadyX = null;
		alreadyY = null;
	}

}
