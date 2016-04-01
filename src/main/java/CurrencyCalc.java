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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Currency Calculator");
        BorderPane border = new BorderPane();

        border.setTop(getLogo());

        // GET CURRENCY DATA
        CurrencyDataGetter currencyDataGetter = new CurrencyDataGetter();
        Map<String, BigDecimal> currencyRates = currencyDataGetter.getConversionRates();

        // INITIALIZE GRID
        GridPane grid = new GridPane();

        ColumnConstraints columnConstraints = new ColumnConstraints(80);
        grid.getColumnConstraints().add(columnConstraints);

        grid.setPadding(new Insets(10,10,10,10));
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
        GridPane.setConstraints(comboBox, 0, 1 );


        // TEXTFIELD FOR OTHER CURRENCY
        TextField otherCurrencyTextField = new TextField("0");
        GridPane.setConstraints(otherCurrencyTextField, 1, 1);
        grid.getChildren().add(otherCurrencyTextField);

        // BUTTON TO EMPTY FIELDS
        Button button = new Button("Empty");
        grid.getChildren().add(button);
        GridPane.setConstraints(button, 1, 2);

        border.setCenter(grid);

        // EVENT LISTENERS
        // HANDLE BUTTON EVENT
        button.setOnAction((event) -> {
            eurTextField.setText("0");
            otherCurrencyTextField.setText("0");
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

        Scene scene = new Scene(border, 250, 180);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    /**
     * Separate method to create logo on a Canvas object.
     * Moved into separate method to make {@link #start} method more readable.
     * @return Canvas object with drawn logo
     */
    private Canvas getLogo() {
        Canvas canvas = new Canvas(250,50);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // BACKGROUND
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,250,50);

        gc.setStroke(Color.GOLD);

        // EURO
        gc.setLineWidth(5);
        gc.strokeArc(10, 10, 30, 30, 60, 240, ArcType.OPEN);

        gc.setLineWidth(3);
        gc.strokeLine(10,22,30,22);
        gc.strokeLine(10,28,30,28);

        // DOLLAR
        gc.setLineWidth(5);
        gc.strokeArc(50, 10, 15, 15, 45, 225, ArcType.OPEN);
        gc.strokeArc(50, 24, 15, 15, 90, -225, ArcType.OPEN);

        gc.setLineWidth(3);
        gc.strokeLine(57,6,57,43);

        // RUBLE
        gc.setLineWidth(5);
        gc.strokeLine(90,10,90,40);
        gc.strokeArc(87,10,15,15,90,-180, ArcType.OPEN);
        gc.strokeLine(85,25,95,25);
        gc.strokeLine(85,33,95,33);

        // CHINESE YUAN
        gc.strokeLine(120,10,128,25);
        gc.strokeLine(128,25,136,10);
        gc.strokeLine(128,25,128,40);
        gc.strokeLine(123,30,132,30);


        return canvas;
    }
}
