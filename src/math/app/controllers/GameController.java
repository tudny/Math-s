package math.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import math.app.classes.OwnArrayList;
import math.app.classes.Pair;
import java.util.ArrayList;

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
	private ArrayList<Pair<Button, Button>> history;

	@FXML
	public Button undoButton;

	void setAll(Stage primary, AnchorPane root, Integer buttonWidth, Integer buttonHeight) {
		this.primary = primary;

		primary.getScene().setOnKeyReleased(event -> {
			System.out.println("Key pressed: " + event.getCode());
			if(event.getCode() == KeyCode.ESCAPE){
				resetGreen();
			}
		});

		buttonArray = new OwnArrayList<>(buttonWidth, buttonHeight);
		isPawn = new OwnArrayList<>(buttonWidth, buttonHeight);

		history = new ArrayList<>();

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
								playButton(button, X, Y);
							}
						} else if(isPawn.myGet(X, Y).equals(Boolean.TRUE)){
							isAlreadyClicked = true;

							alreadyX = X;
							alreadyY = Y;

							int cnt = 0;
							int rememberX = -1;
							int rememberY = -1;
							Button rememberButton = null;

							for (int i = 0; i < 4; i++) {
								Integer nX = X + dx[1][i];
								Integer nY = Y + dy[1][i];
								Integer jX = X + dx[0][i];
								Integer jY = Y + dy[0][i];

								if (0 <= nX && nX < buttonWidth && 0 <= nY && nY < buttonHeight) {
									if (isPawn.myGet(nX, nY).equals(Boolean.FALSE) && isPawn.myGet(jX, jY).equals(Boolean.TRUE)) {
										buttonArray.myGet(nX, nY).setStyle(greenButton);
										cnt++;
										rememberX = nX;
										rememberY = nY;
										rememberButton = buttonArray.myGet(nX, nY);
									}
								}
							}

							if(cnt == 0){
								isAlreadyClicked = false;
								alreadyX = null;
								alreadyY = null;
							} else if(cnt == 1){
								playButton(rememberButton, rememberX, rememberY);
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

		Line line = new Line(0, MainController.buttonSize * MainController.rowsUp, MainController.buttonSize * buttonWidth, MainController.buttonSize * MainController.rowsUp);
		line.setFill(Color.RED);
		root.getChildren().add(line);

		for(int y = 0; y < buttonHeight; y++){
			for(int x = 0; x < buttonWidth; x++){
				Button button = buttonArray.myGet(x, y);
				if(y > MainController.rowsUp - 1) {
					button.setStyle(takenCSS);
					isPawn.mySet(x, y, Boolean.TRUE);
				} else {
					button.setStyle(notTakenCss);
				}
			}
		}

		undoButton.toFront();
	}

	private void playButton(Button button, Integer X, Integer Y) {

		history.add(new Pair<>(button, buttonArray.myGet(alreadyX, alreadyY)));

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

	@FXML
	public void undo(ActionEvent event){

		try {

			Pair<Button, Button> last;

			try {
				last = history.get(history.size() - 1);
			} catch (Exception e){
				throw new Exception("No more history left", e.getCause());
			}

			Button button = last.getFirst();
			Integer X = buttonArray.xIndexOf(button);
			Integer Y = buttonArray.yIndexOf(button);

			Button old = last.getSecond();
			Integer oX = buttonArray.xIndexOf(old);
			Integer oY = buttonArray.yIndexOf(old);

			Integer jX = (X + oX) / 2;
			Integer jY = (Y + oY) / 2;
			Button jumpButton = buttonArray.myGet(jX, jY);

			button.setStyle(notTakenCss);
			isPawn.mySet(X, Y, Boolean.FALSE);

			old.setStyle(takenCSS);
			isPawn.mySet(oX, oY, Boolean.TRUE);

			jumpButton.setStyle(takenCSS);
			isPawn.mySet(jX, jY, Boolean.TRUE);

			resetGreen();

			removeLastElementFromArrayList(history);
		} catch (Exception e){
			System.out.println("Error undoing: " + e.getMessage());
		}
	}

	private void removeLastElementFromArrayList(ArrayList arrayList) throws Exception {
		try {
			arrayList.remove(arrayList.size() - 1);
		} catch (Exception e){
			Exception error = new Exception("Array is empty");
			error.setStackTrace(error.getStackTrace());
			throw error;
		}
	}
}
