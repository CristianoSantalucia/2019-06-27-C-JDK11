/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Parziale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxCategoria"
	private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

	@FXML // fx:id="boxGiorno"
	private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="boxArco"
	private ComboBox<DefaultWeightedEdge> boxArco; // Value injected by FXMLLoader

	@FXML // fx:id="btnPercorso"
	private Button btnPercorso; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML void doCreaGrafo(ActionEvent event)
	{
		txtResult.clear();
		txtResult.appendText("Crea grafo...\n");

		String category; 
		LocalDate data; 
		try
		{
			category = this.boxCategoria.getValue(); 
			data = this.boxGiorno.getValue(); 

			if (category != null && category != null)
			{
				//grafo
				this.model.creaGrafo(category, data);

				this.btnPercorso.setDisable(false);
				this.boxArco.setDisable(false);

				this.txtResult.appendText("\nGRAFO CREATO CON:\n" 
						+ "\n#Vertici: " 
						+ this.model.getNumVertici()
						+ "\n#Archi: " 
						+ this.model.getNumArchi());


				//percroso
				this.txtResult.appendText("\nARCHI INFERIORI: " + this.model.stampaInferiori());

				this.boxArco.getItems().clear();
				this.boxArco.getItems().addAll(this.model.getInferiori()); 
			}
		}
		catch (Exception e)
		{
			this.txtResult.appendText("\n\nERRORE CREAZIONE GRAFO!");
			e.printStackTrace();
		}
	}

	@FXML void doCalcolaPercorso(ActionEvent event)
	{ 
		txtResult.appendText("\n\nCalcola percorso...\n");

		DefaultWeightedEdge arco = null; 
		try
		{
			arco = this.boxArco.getValue();
			if (arco != null)
			{
				this.txtResult.appendText("\n\nPERCORSO:" ); 
				for (Parziale p : this.model.calcolaPercorso(arco))
					this.txtResult.appendText("\n" + p); 
			}
		}
		catch (Exception e)
		{
			this.txtResult.appendText("\n\nERRORE PERCORSO!");
			e.printStackTrace();
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model; 

		try
		{
			this.boxCategoria.getItems().addAll(this.model.getCategories()); 
			this.boxGiorno.getItems().addAll(this.model.getDates()); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
