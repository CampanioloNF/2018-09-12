/**
 * Sample Skeleton for 'PowerOutages.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.NercWeight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxNerc"
    private ComboBox<Nerc> cmbBoxNerc; // Value injected by FXMLLoader

    @FXML // fx:id="btnVisualizzaVicini"
    private Button btnVisualizzaVicini; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	model.creaGrafo();
    	cmbBoxNerc.getItems().addAll(model.getNercs());
    	
    }

    @FXML
    void doSimula(ActionEvent event) {

    	String input = txtK.getText();
    	
    	if(input!=null) {
    		
    		try {
    			
    			model.simula(Integer.parseInt(input));
    			txtResult.appendText(model.getStats().toString());
    			
    			
    		}catch(NumberFormatException nfe) {
    			txtResult.appendText("Si prega di inserire un numero intero K");
    			return;
    		}
    		
    		
    	}else
    		txtResult.appendText("Si prega di inserire un numero intero K");
    	
    }

    @FXML
    void doVisualizzaVicini(ActionEvent event) {

    	txtResult.clear();
    	
    	Nerc nerc = cmbBoxNerc.getValue();
    	
    	if(nerc!=null) {
    		
    		List<NercWeight> vicini = model.getVicini(nerc);
    		
    		for(NercWeight n : vicini) 
    			txtResult.appendText(n.getNerc()+"  ("+n.getPeso()+")\n");
    		
    		
    	}else 
    	  txtResult.appendText("Errore selezionare un Nerc dal menù a tendina");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert cmbBoxNerc != null : "fx:id=\"cmbBoxNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnVisualizzaVicini != null : "fx:id=\"btnVisualizzaVicini\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'PowerOutages.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;

	}
}
