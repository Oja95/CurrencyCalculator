import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class CurrencyCalc extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Currency Calculator");
        BorderPane border = new BorderPane();


        border.setCenter(getLogo());
        border.setBottom(new Button("asd"));

/*
        FlowPane flow = new FlowPane();

        Label eurText = new Label("EUR ");
        TextField inputText = new TextField("0");
        flow.getChildren().add(eurText);
        flow.getChildren().add(inputText);



        flow.getChildren().add(canvas);

        ObservableList<String> items = FXCollections.observableArrayList (
                "USD", "bongs", "Doubloons");
        ComboBox<String> comboBox = new ComboBox<String>(items);
        comboBox.setMaxHeight(100);

        flow.getChildren().add(comboBox);

        TextField currency = new TextField("0");
        flow.getChildren().add(currency);

        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // change coenficent
            }
        });

        inputText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                float eurs = Float.parseFloat(inputText.getCharacters().toString());
                switch (comboBox.getValue()){
                    case "USD":
                        float usd = eurs * 1.13f;
                        currency.setText(String.valueOf(usd));
                        break;
                    case "Doubloons":
                        float doubloons = 20 * eurs;
                        currency.setText(String.valueOf(doubloons));
                        break;
                    case "bongs":
                        float bongs = 420 * eurs;
                        currency.setText(String .valueOf(bongs));
                        break;
                }
            }
        });
*/
        CurrencyDataGetter currencyDataGetter = new CurrencyDataGetter();
        System.out.println(currencyDataGetter.getConversionRates());

        Scene scene = new Scene(border, 250, 250);
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
        Canvas canvas = new Canvas(250,250);
        GraphicsContext gc = canvas.getGraphicsContext2D();

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
        return canvas;
    }
}
