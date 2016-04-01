import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

public class CurrencyCalc extends Application {

    /**
     * Data structure that cointains currency and its corresponding rate.
     */
    private Map<String, BigDecimal> currencyValues;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Currency Calculator");
        this.primaryStage = primaryStage;
        primaryStage.setScene(optionsScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Creates the necessary elements for the currency calculator Scene.
     * Reads currency rate data from given parameter to convert currencys.
     * @param currencyRates currency rates
     * @return currency calculator Scene object
     */
    private Scene currencyCalculatorScene(Map<String, BigDecimal> currencyRates) {
        BorderPane border = new BorderPane();

        border.setTop(getLogo());

        // INITIALIZE GRID
        GridPane grid = new GridPane();

        ColumnConstraints columnConstraints = new ColumnConstraints(80);
        grid.getColumnConstraints().add(columnConstraints);

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // LABEL FOR EUR
        Label eurLabel = new Label("EUR");
        eurLabel.setFont(new Font(20));
        GridPane.setConstraints(eurLabel, 0, 0);
        grid.getChildren().add(eurLabel);

        // TEXTFIELD FOR EUR VALUE
        TextField eurTextField = new TextField("0");
        GridPane.setConstraints(eurTextField, 1, 0);
        grid.getChildren().add(eurTextField);


        // COMBOBOX FOR OTHER CURRENCIES
        ObservableList<String> currencyOptions =
                FXCollections.observableArrayList(currencyRates.keySet());
        ComboBox comboBox = new ComboBox(currencyOptions);
        comboBox.getSelectionModel().select(0); // Set some currency initially
        grid.getChildren().add(comboBox);
        GridPane.setConstraints(comboBox, 0, 1);


        // TEXTFIELD FOR OTHER CURRENCY
        TextField otherCurrencyTextField = new TextField("0");
        GridPane.setConstraints(otherCurrencyTextField, 1, 1);
        grid.getChildren().add(otherCurrencyTextField);

        // BUTTON TO EMPTY FIELDS
        Button button = new Button("Empty");
        grid.getChildren().add(button);
        GridPane.setConstraints(button, 1, 2);

        // BACK BUTTON
        Button button2 = new Button("Back");
        grid.getChildren().add(button2);
        GridPane.setConstraints(button2, 1,3);

        border.setCenter(grid);

        // EVENT LISTENERS
        // HANDLE "EMPTY" BUTTON EVENT
        button.setOnAction((event) -> {
            eurTextField.setText("0");
            otherCurrencyTextField.setText("0");
        });

        // HANDLE "BACK" BUTTON EVENT
        button2.setOnAction(event1 -> {
            setScene(optionsScene());
        });

        // COMBO BOX EVENT
        comboBox.setOnAction((event) -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();
            String eurValue = eurTextField.getText();
            if (NumberUtils.isParsable(eurValue)) {
                BigDecimal rate = currencyRates.get(selectedItem);
                BigDecimal oldValue = new BigDecimal(eurValue);
                otherCurrencyTextField.setText(String.valueOf(oldValue.multiply(rate)));
            } else {
                otherCurrencyTextField.setText("Invalid input");
            }
        });

        // EUR INPUT FIELD EVENT LISTENER
        eurTextField.setOnKeyReleased((event) -> {
            BigDecimal rate = currencyRates.get(comboBox.getSelectionModel().getSelectedItem().toString());
            if (NumberUtils.isParsable(eurTextField.getText()) && rate != null) {
                BigDecimal eurValue = new BigDecimal(eurTextField.getText());
                otherCurrencyTextField.setText(String.valueOf(eurValue.multiply(rate)));
            } else {
                otherCurrencyTextField.setText("Invalid input");
            }
        });

        // OTHER CURRENCY TEXT FIELD LISTENER
        otherCurrencyTextField.setOnKeyReleased((event) -> {
            BigDecimal rate = currencyRates.get(comboBox.getSelectionModel().getSelectedItem().toString());
            if (NumberUtils.isParsable(otherCurrencyTextField.getText()) && rate != null) {
                BigDecimal otherCurrencyValue = new BigDecimal(otherCurrencyTextField.getText());
                eurTextField.setText(String.valueOf(otherCurrencyValue.divide(rate, MathContext.DECIMAL32)));
            } else {
                eurTextField.setText("Invalid input");
            }
        });

        return new Scene(border, 250, 200);
    }

    /**
     * Method that creates necessary elemets for the options scene.
     * User will be prompted where to read the currency data from.
     * @return options Scene object.
     */
    private Scene optionsScene() {
        BorderPane optionsBorderPane = new BorderPane();
        optionsBorderPane.setTop(getLogo());

        ColumnConstraints columnConstraints = new ColumnConstraints(250);
        GridPane optionsGrid = new GridPane();

        optionsGrid.getColumnConstraints().add(columnConstraints);

        optionsGrid.setPadding(new Insets(10, 10, 10, 10));
        optionsGrid.setVgap(5);
        optionsGrid.setHgap(5);

        Button webDataButton = new Button("Get data from web");
        optionsGrid.getChildren().add(webDataButton);
        GridPane.setConstraints(webDataButton, 0, 0);

        Button fileDataButton = new Button("Get data from file");
        optionsGrid.getChildren().add(fileDataButton);
        GridPane.setConstraints(fileDataButton, 0, 1);

        optionsBorderPane.setCenter(optionsGrid);

        CurrencyDataGetter currencyDataGetter = new CurrencyDataGetter();

        webDataButton.setOnAction(event -> {
            setCurrencyValues(currencyDataGetter.getWebCurrencyData());
            setScene(currencyCalculatorScene(currencyValues));
        });

        fileDataButton.setOnAction(event -> {
            setCurrencyValues(currencyDataGetter.getFileCurrencyData());
            setScene(currencyCalculatorScene(currencyValues));
        });

        return new Scene(optionsBorderPane, 250, 200);
    }


    /**
     * Separate method to create logo on a Canvas object.
     *
     * @return Canvas object with drawn logo
     */
    private Canvas getLogo() {
        Canvas canvas = new Canvas(250, 50);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // BACKGROUND
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 250, 50);

        gc.setStroke(Color.GOLD);

        // EURO
        gc.setLineWidth(5);
        gc.strokeArc(10, 10, 30, 30, 60, 240, ArcType.OPEN);

        gc.setLineWidth(3);
        gc.strokeLine(10, 22, 30, 22);
        gc.strokeLine(10, 28, 30, 28);

        // DOLLAR
        gc.setLineWidth(5);
        gc.strokeArc(50, 10, 15, 15, 45, 225, ArcType.OPEN);
        gc.strokeArc(50, 24, 15, 15, 90, -225, ArcType.OPEN);

        gc.setLineWidth(3);
        gc.strokeLine(57, 6, 57, 43);

        // RUBLE
        gc.setLineWidth(5);
        gc.strokeLine(90, 10, 90, 40);
        gc.strokeArc(87, 10, 15, 15, 90, -180, ArcType.OPEN);
        gc.strokeLine(85, 25, 95, 25);
        gc.strokeLine(85, 33, 95, 33);

        // CHINESE YUAN
        gc.strokeLine(120, 10, 128, 25);
        gc.strokeLine(128, 25, 136, 10);
        gc.strokeLine(128, 25, 128, 40);
        gc.strokeLine(123, 30, 132, 30);

        return canvas;
    }

    /**
     * Method to switch scenes.
     * Made this method so scenes can be switched outside the main method
     * @param scene Scene to switch to
     */
    private void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    public void setCurrencyValues(Map<String, BigDecimal> currencyValues) {
        this.currencyValues = currencyValues;
    }
}