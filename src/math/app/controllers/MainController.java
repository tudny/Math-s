package math.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import math.app.Main;

public class MainController {

	private static final Integer minValue = 4;
	private static final Integer maxValue = 32;
	static final Integer buttonSize = 50;
	private Stage primary;
	private Boolean textFieldsReady = false;

	@FXML
	private TextField widthField;

	@FXML
	private Button goButton;

	@FXML
	private TextField heightField;

	@FXML
	void goButtonOnAction(ActionEvent event){
		try {
			getReady();
		} catch (Exception e){
			System.out.println("Screen error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void getReady() throws Exception{
		if(textFieldsReady){
			start();
		} else {
			throw new Exception("TextField ERROR!");
		}
	}

	private void start() throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("fxml/GameWindow.fxml"));
		AnchorPane root = loader.load();

		Integer buttonWidth = Integer.parseInt(widthField.getText());
		Integer buttonHeight = Integer .parseInt(heightField.getText()) + 5;

		primary.setScene(new Scene(root, buttonWidth * buttonSize, buttonHeight * buttonSize));

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		primary.setX((screenBounds.getWidth() - primary.getWidth()) / 2);
		primary.setY((screenBounds.getHeight() - primary.getHeight()) / 2);

		GameController cont = loader.getController();
		cont.setAll(primary, root, buttonWidth, buttonHeight);
	}

	public void setAll(Stage primary){

		this.primary = primary;

		widthField.textProperty().addListener((observable, oldValue, newValue) -> {
			manageTextField(widthField, newValue);
		});

		heightField.textProperty().addListener((observable, oldValue, newValue) -> {
			manageTextField(heightField, newValue);
		});
	}

	private void manageTextField(TextField textField, String newValue){
		if(isNumberProper(newValue)) {
			textField.setStyle("");
			textFieldsReady = true;
		} else {
			textField.setStyle("-fx-text-box-border: red;" +
					"-fx-focus-color: red;" +
					"-fx-text-fill: red");
			textField.setPromptText("Invalid...");
			textFieldsReady = false;
		}
	}

	private boolean isNumberProper(String string){
		try {
			Integer number = Integer.parseInt(string);
			return (minValue <= number && number <= maxValue);
		} catch (Exception e) {
			return false;
		}
	}

}
