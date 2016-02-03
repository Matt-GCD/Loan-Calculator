// imports
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import java.text.DecimalFormat;

// class definition
public class LoanCalculator extends Application{
	@Override
	public void init() {
	//Populate the ComboBoxes
		//initialize all of our widgets and layouts
		gp = new GridPane();
	    gp.setVgap(6);
	    gp.setHgap(10);
	    gp.setPadding(new Insets(10, 10, 10, 10));
		cb_loan_type = new ComboBox<String>();
		tf_loan_amount = new TextField();
		cb_interest_rate_type = new ComboBox<String>();
		tf_interest_rate = new TextField();
	    //slider default values
		slider_loan_duration = new Slider();
		tf_duration = new TextField();
	    tf_total = new TextField();
	    tf_credit_cost = new TextField();
	    cb_payments = new ComboBox<String>();
	    tf_payments = new TextField();
	    bt_calculate = new Button();
	    bt_clear = new Button();
	    root = new Group();
		
	  //Layout controls
	    //set combobox data.
		//loan type combo box
		cb_loan_type.getItems().addAll("Select type","Mortgage","Car");
		cb_loan_type.setValue("Select type");

	    //Interest Rate box
		cb_interest_rate_type.getItems().addAll("Select type", "Variable", "Fixed");
		cb_interest_rate_type.setValue("Select type");
		
	    //payment per week/month/year calculation box
	    cb_payments.getItems().addAll("Select type", "Monthly", "bi-Monthly");
	    cb_payments.setValue("Select type");
	    
		//set default amounts
		tf_loan_amount.setText("0.00");
	    tf_interest_rate.setText("€"+"0.00");
	    tf_total.setText("€"+"0.00");
	    tf_credit_cost.setText("€"+"0.00");
	    tf_payments.setText("€"+"0.00");
	    tf_duration.setPromptText("1.0");
	    
	    //slider defaults
	    slider_loan_duration.setMin(0.0);
	    slider_loan_duration.setMax(2.0);
	    slider_loan_duration.setValue(1.0);
		slider_loan_duration.setShowTickMarks(true);
	    slider_loan_duration.setShowTickLabels(true);
	    slider_loan_duration.setMajorTickUnit(1);
	    slider_loan_duration.setMinorTickCount(1);
	    slider_loan_duration.setBlockIncrement(1);

	    //name buttons
	    //calculate button
	    bt_calculate.setText("Calculate");
	    //clear button
	    bt_clear.setText("Clear");
	    
	    //name labels
	    lb_loan_type = new Label("Loan type: ");
	    lb_loan_amount = new Label("Loan Amount: ");
	    lb_interest_rate = new Label("Interest Rate: ");
	    lb_loan_duration = new Label("Loan Duration (Years): ");
	    lb_total = new Label("Total (Including interest): ");
	    lb_credit_cost = new Label("Credit cost: ");
	    lb_payments = new Label("Payments: ");
	    
		//grid data
	    //column 1
	    gp.add(lb_loan_type, 0, 0);
	    gp.add(lb_loan_amount, 0, 1);
	    gp.add(lb_interest_rate, 0, 2);
	    gp.add(lb_loan_duration, 0, 3);
	    gp.add(lb_total, 0, 4);
	    gp.add(lb_credit_cost, 0, 5);
	    gp.add(lb_payments, 0, 6);
	    
	    //column 2
	    gp.add(cb_loan_type, 1, 0);
	    gp.add(tf_loan_amount, 1, 1);
	    gp.add(tf_interest_rate, 1, 2);
	    gp.add(tf_duration, 1, 3);
	    gp.add(tf_total, 1, 4);
	    gp.add(tf_credit_cost, 1, 5);
	    gp.add(tf_payments, 1, 6);
	    gp.add(bt_calculate, 0, 7);
	    gp.add(bt_clear, 1, 7);
	    
	    //column 3
	    gp.add(cb_interest_rate_type, 2, 2);
	    gp.add(slider_loan_duration, 2, 3);
	    gp.add(cb_payments, 2, 6);
	    
		//Set certain fields as non editable to avoid the user interfering with results	
	    tf_interest_rate.setDisable(true);
	    tf_duration.setDisable(true);
	    tf_total.setDisable(true);
	    tf_credit_cost.setDisable(true);
	    tf_payments.setDisable(true);
	    
	    //set style opacity
	    tf_interest_rate.setStyle("-fx-opacity: 1.0;");
	    tf_duration.setStyle("-fx-opacity: 1.0;");
	    tf_total.setStyle("-fx-opacity: 1.0;");
	    tf_credit_cost.setStyle("-fx-opacity: 1.0;");
	    tf_payments.setStyle("-fx-opacity: 1.0;");
		
		//Method call to initialize Interest Rates TextField based on the Loan Type and Loan Interest Type
	    cb_interest_rate_type.setOnAction(new EventHandler<ActionEvent>() {
	    	//overridden handle method
			 @Override
			 public void handle(ActionEvent event) {
				 //change the text depending on what choice was made
				 setRate();
			}
		 });
	    
		//Method call to initialize the Slider and the Loan Duration TextField
	    cb_loan_type.setOnAction(new EventHandler<ActionEvent>() {
	    	//overridden handle method
			 @Override
			 public void handle(ActionEvent event) {
				 //change the text depending on what choice was made
				 setSlider();
			}
		 });
		    
		//Action Listener for slider to set a variable and TextField
	    slider_loan_duration.valueProperty().addListener(new ChangeListener<Number>(){public void changed(final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue){
	    		//converts newValue to decimal format and then to string.
	    		df = new DecimalFormat("0.0");
	    		val = df.format(newValue);
	    		tf_duration.setText(val);
	    		d_loan_duration = Double.parseDouble(val);
		    }
	   	});
	    
		//5 Event Handlers, one for each button. Each will call a method. 
	    //Some will call the same methods.
	    tf_loan_amount.textProperty().addListener(new ChangeListener<String>() {
	    	// method that must be overridden to listen for changes in the text area
	    	@Override
	    	public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
	    		//sets load amount, converts, displays in text area
	    		tf_loan_amount.setText(newValue);
	    		d_loan_amount = Double.parseDouble(newValue);
	    	}
	    });
	    
	    //calculator button
	    bt_calculate.setOnAction(new EventHandler<ActionEvent>() {
	    	// must override this method for an action event event handler
	    	@Override
	    	public void handle(ActionEvent event) {
	    		//calls methods for calculations
	    		calculateTotal();
	    		calculatePayments();
		    }
	    });
	    //clear button
	    bt_clear.setOnAction(new EventHandler<ActionEvent>() {
	    	// must override this method for an action event event handler
	    	@Override
	    	public void handle(ActionEvent event) {
	    		//resets all data
	    		clear();
		    }
	    });
	}

	//Overridden start method
	@Override
	public void start(Stage primaryStage) {
		// set a title on the window, set a scene, size, and show the window
		primaryStage.setTitle("Loan Calculator");
		primaryStage.setScene(new Scene(root, 480, 300, Color.web("#d3d3d3")));
		root.getChildren().add(gp);
		primaryStage.show();
	}

	//Overridden stop method
	@Override
	public void stop() {}

	//Entry point to our program
	public static void main(String[]args) {
		launch(args);
	}

	//Method to calculate - the total (including interest) & the credit cost and set these TextFields.
	//It should set some variables with values read from TextFields and catch errors in this process.
	//Formula to calculate compound interest can be found here  http://bit.ly/1MtJqSw. The interest should be
	//compounded once per year. This should also calculate the payments by calling another method.
	private void calculateTotal(){
		//calculations
		if(!tf_loan_amount.getText().matches("\\d+.\\d+")){
			tf_loan_amount.setText("Error");
		}
		else{
			Double cal = (1+(d_interest_rate/d_loan_duration));		
			Double pow = (Math.pow(cal, d_loan_duration));
			
			total = (d_loan_amount*pow);
			credit_cost = total - d_loan_amount;
			
			//format to two decimal places
			df = new DecimalFormat("0.0#");
			st_total = df.format(total);
			st_credit = df.format(credit_cost);
			
			//set text
			tf_total.setText("€"+st_total);
			tf_credit_cost.setText("€"+st_credit);
		}
	}

	//Method to calculate the payments and display them in the relevant TextField
	//If the total has not been calculated then an error message will be displayed 
	//in the TextField.
	private void calculatePayments(){
		//calculate monthly repay
		if(cb_payments.getValue() == "Monthly"){
			df = new DecimalFormat("0.0#");
			pay_cal = total/(d_loan_duration*12);
			payments = df.format(pay_cal);
			tf_payments.setText("€"+payments);
		}
		//calculate bi-monthly repay
		else if(cb_payments.getValue() == "bi-Monthly"){
			df = new DecimalFormat("0.0#");
			pay_cal = total/(d_loan_duration*6);
			payments = df.format(pay_cal);
			tf_payments.setText("€"+payments);
		}
		//throws error
		else{
			tf_payments.setText("Error: Bad value");
			}
	}

	//Method to set the Slider based on the Loan Type
	private void setSlider(){
		//sets mortgage bar
		if(cb_loan_type.getValue() == "Mortgage"){
			tf_duration.setPromptText("15.0");
		    slider_loan_duration.setMin(0.0);
		    slider_loan_duration.setMax(30.0);
		    slider_loan_duration.setValue(15.0);
		    slider_loan_duration.setMajorTickUnit(10);
		    slider_loan_duration.setMinorTickCount(5);
		    slider_loan_duration.setBlockIncrement(1);
		}
		//sets car bar
		else if(cb_loan_type.getValue() == "Car"){
			tf_duration.setPromptText("5.0");
		    slider_loan_duration.setMin(0.0);
		    slider_loan_duration.setMax(10.0);
		    slider_loan_duration.setValue(5.0);
		    slider_loan_duration.setMajorTickUnit(2);
		    slider_loan_duration.setMinorTickCount(1);
		    slider_loan_duration.setBlockIncrement(1);
		}
		else{}
	}

	//Method to set the rate based on the Loan Type and the Interest Rate Type
	private void setRate(){
		//interest for variable mortgage
	    if(cb_interest_rate_type.getValue() == "Variable" && cb_loan_type.getValue() == "Mortgage"){
	    	rate_list = INTEREST_RATES[0];
	    	d_interest_rate = INTEREST_RATES[0];
	    	tf_interest_rate.setText("€"+rate_list.toString());
	    }
	    //interest for fixed mortgage
	    else if(cb_interest_rate_type.getValue() == "Fixed" && cb_loan_type.getValue() == "Mortgage"){
	    	rate_list = INTEREST_RATES[1];
	    	d_interest_rate = INTEREST_RATES[1];
	    	tf_interest_rate.setText("€"+rate_list.toString());
	    }
	    //interest for variable car
	    else if(cb_interest_rate_type.getValue() == "Variable" && cb_loan_type.getValue() == "Car"){
	    	rate_list = INTEREST_RATES[2];
	    	d_interest_rate = INTEREST_RATES[2];
	    	tf_interest_rate.setText("€"+rate_list.toString());
	    }
	    //interest for fixed car
	    else if(cb_interest_rate_type.getValue() == "Fixed" && cb_loan_type.getValue() == "Car"){
	    	rate_list = INTEREST_RATES[3];
	    	d_interest_rate = INTEREST_RATES[3];
	    	tf_interest_rate.setText("€"+rate_list.toString());
	    }
	    else{
	    	//false rate
	    	rate_list = 0.00;
	    	tf_interest_rate.setText("€"+"0.00");
	    }
	}

	//Method to clear all the TextFields and update the Interest Rate and Loan Duration TextFields.
	private void clear(){
		tf_loan_amount.setText("0.00");
	    tf_interest_rate.setText("0.00");
	    tf_total.setText("0.00");
	    tf_credit_cost.setText("0.00");
	    tf_payments.setText("0.00");
		cb_loan_type.setValue("Select type");
		cb_interest_rate_type.setValue("Select type");
	    cb_payments.setValue("Select type");
	    slider_loan_duration.setValue(0.0);
	}

	//The following variables should be initialized in appropriate location. 
	//For the purposes of this practical you may to choose to initialize most of them here.
	
	//Layout
	private GridPane gp;
	private Group root;

	//Loan Type
	private Label lb_loan_type;
	private ComboBox<String> cb_loan_type;

	//Loan Amount
	private Label lb_loan_amount;
	private TextField tf_loan_amount;
	private Double d_loan_amount;

	//Interest Rate
	private Label lb_interest_rate;
	private TextField tf_interest_rate;
	private ComboBox<String> cb_interest_rate_type;
	private Double d_interest_rate;
	//Values 0 and 1 refer to Variable Rate and Fixed Rate Mortgages respectively 
	//Values 2 and 3 refer to Variable Rate and Fixed Rate Car Loans respectively 
	private final Double[] INTEREST_RATES = {0.034,.05,.09,.11};
	private Double rate_list;
	//slider values
	private Double sl_min;
	private Double sl_max;

	//Loan Duration
	private Label lb_loan_duration;
	private TextField tf_duration;
	private Double d_loan_duration;
	private Slider slider_loan_duration;
	//Formating the values in the duration box
	DecimalFormat df;
	private String val;

	//Total (including interest)
	private Label lb_total;
	private TextField tf_total;
	private Double total;

	//Credit Cost
	private Label lb_credit_cost;
	private TextField tf_credit_cost;
	private Double credit_cost;

	//Payments
	private Label lb_payments;
	private TextField tf_payments;
	private ComboBox<String> cb_payments;
	private Double pay_cal;
	private String payments;
	
	//calculation variables
	private String st_total;
	private String st_credit;

	//Calculate Button
	private Button bt_calculate;
	
	//Clear Button
	private Button bt_clear;
}
