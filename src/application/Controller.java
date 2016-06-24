package application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PetriNet;
import netscape.javascript.JSObject;

public class Controller {

	@FXML
	private TabPane tpEditor;
	@FXML
	private TabPane tpOutput;
	@FXML
	private TextField tfFind;
	@FXML
	private WebView browser = new WebView();
	@FXML
	private TextArea textOutput;

	private int initial = 0;
	private HashMap<Tab, File> openFiles = new HashMap<>();

	@FXML
	private void newFile() {
		tpEditor.getTabs().add(newTab());
		setTextAreaFocus(getSelectedTab());
	}

	@FXML
	private void openFile() throws IOException {
		File file = openChooseFile();
		if (file == null)
			return;

		Tab tab = newTab(file.getName());
		try {
			String textFull = new String("");
			List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
			openFiles.put(tab, file);

			for (String line : lines)
				textFull += line + "\n";

			setSourceCode(getSelectedTab(), textFull);
		} catch (Exception e) {
			e.printStackTrace();
			setOuputText(e.getMessage().replaceAll("\\w+(\\.|:)", ""));
		}

		tpEditor.getTabs().add(tab);
	}

	@FXML
	private void saveFile() throws IOException {
		Tab tabCurrent = getSelectedTab();
		File file = persistedFile(tabCurrent);
		if (file == null) {
			file = openChooseFileToSave();
			if (file == null)
				return;

			tabCurrent.setText(file.getName());
			openFiles.put(tabCurrent, file);
		}

		Files.write(file.toPath(), getSourceCode(tabCurrent).getBytes());
	}

	@FXML
	private void visualizeNetPetri() {
		PetriNet net = getPetriNet();
		Bridge bridge = new Bridge();
		if (net != null)
			bridge.value = new Gson().toJson(net);

		loadPage(bridge);
	}

	@FXML
	private void focusFind() {
		tfFind.requestFocus();
	}

	@FXML
	private void showHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("Gramatica");
		alert.setContentText("By Jonhnny Werlley, Taisa Alves.");
		alert.setResizable(true);
		TextArea textArea = new TextArea(readme());
		textArea.setPrefSize(600, 600);
		textArea.setEditable(false);
		alert.setGraphic(textArea);
		alert.show();
	}

	private String readme() {
		return "Desenvolvido por: \nArinaldo Lopes da Silva, \nVitor Augusto Correa Cortez Almeda, \nTaisa Alves Ferreira.";
	}

	@FXML
	private void findText(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE)
			setTextAreaFocus(getSelectedTab());
		if (event.getCode() != KeyCode.ENTER)
			return;

		String term = tfFind.getText();
		String text = getSourceCode(getSelectedTab());

		int init = text.indexOf(term, this.initial);
		int end = init + term.length() + 1;
		((TextArea) getSelectedTab().getContent()).selectRange(init, end);
		this.initial = end;
	}

	@FXML
	private void onKeyTyped(KeyEvent event) throws IOException {
		if (event.isControlDown()) {
			switch (event.getCode()) {
			case N:
				newFile();
				break;
			case O:
				openFile();
				break;
			case S:
				saveFile();
				break;
			case W:
				closeTab(getSelectedTab());
				break;
			case F:
				tfFind.requestFocus();
				break;
			default:
				break;
			}
		} else if (event.getCode() == KeyCode.F8) {
			visualizeNetPetri();
		}
		// TODO adicionar outros metodos
		// runFile();
	}

	private Tab newTab(String name) {
		Tab tab = new Tab(name);
		Image image = new Image(getClass().getResourceAsStream("icon/documentIcon26.png"));
		ImageView iv = new ImageView(image);
		iv.setFitHeight(15);
		iv.setFitWidth(15);
		tab.setGraphic(iv);

		TextArea textArea = new TextArea();
		textArea.setFont(Font.font("Bitstream Vera Sans Mono Bold", 14));
		tab.setContent(textArea);

		setSelectedTab(tab);
		return tab;
	}

	private Tab newTab() {
		return newTab("United");
	}

	private Tab getSelectedTab() {
		return tpEditor.getSelectionModel().getSelectedItem();
	}

	private void setSelectedTab(Tab tab) {
		tpEditor.getSelectionModel().select(tab);
	}

	private String getSourceCode(Tab tab) {
		return ((TextArea) tab.getContent()).getText();
	}

	private void setSourceCode(Tab tab, String text) {
		((TextArea) tab.getContent()).setText(text);
	}

	private File openChooseFileToSave() {
		FileChooser fileChooser = dialogChooseFile("Save File");
		return fileChooser.showSaveDialog(new Stage());
	}

	private File openChooseFile() {
		// TODO return fileChooser.showOpenMultipleDialog(new Stage());
		FileChooser fileChooser = dialogChooseFile("Open File");
		return fileChooser.showOpenDialog(new Stage());
	}

	private FileChooser dialogChooseFile(String title) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Comets files (*.comet)", "*.comet");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setSelectedExtensionFilter(extFilter);
		fileChooser.setInitialFileName(getSelectedTab().getText() + ".comet");
		fileChooser.setTitle(title);
		return fileChooser;
	}

	private File persistedFile(Tab tab) {
		// TODO HasMap.get()
		// return openFiles.get(tab);
		for (Map.Entry<Tab, File> entry : openFiles.entrySet()) {
			if (entry.getKey().equals(tab)) {
				return entry.getValue();
			}
		}
		return null;
	}

	private void setTextAreaFocus(Tab tab) {
		((TextArea) tab.getContent()).requestFocus();
	}

	private void closeTab(Tab tab) {
		// TODO remove openFiles
		// openFiles.remove(tab);
		tpEditor.getTabs().remove(tab);
	}

	private void setOuputText(String text) {
		// Tab tabOuput = tpOutput.getSelectionModel().getSelectedItem();
		// setSourceCode(tabOuput, text);
		textOutput.setText(text);
	}

	private void loadPage(Bridge bridge) {
		WebEngine engine = browser.getEngine();
		String uri = getClass().getResource("/javascript/index.html").toExternalForm();
		engine.load(uri);

		engine.documentProperty().addListener(new ChangeListener<Document>() {
			@Override
			public void changed(ObservableValue<? extends Document> observable, Document oldValue, Document newValue) {
				JSObject window = (JSObject) engine.executeScript("window");
				window.setMember("java", bridge);
			}
		});
	}

	private PetriNet getPetriNet() {
		setOuputText(null);
		String sourceCode = getSourceCode(getSelectedTab());
		if (sourceCode == null || sourceCode.isEmpty()) {
			setOuputText("É necessario informar o json da Rede de Petri.");
			return null;
		}
		PetriNet petriNet = null;
		try {
			petriNet = new Gson().fromJson(sourceCode, PetriNet.class);
			if (petriNet != null && petriNet.getPlaces() == null)
				setOuputText("Não foi possivel converter o json em uma Rede de Petri.");
		} catch (JsonSyntaxException e) {
			setOuputText(e.getMessage().replaceAll("(\\w+(\\.|:)|\\$\\.null)", ""));
			e.printStackTrace();
		}

		return petriNet;
	}

	public class Bridge {
		private String value;

		public String loadJSON() {
			if (value == null) {
				Gson gson = new GsonBuilder().serializeNulls().create();
				value = gson.toJson(new PetriNet());
			}
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			try {
				buf.write(value.getBytes());
				return buf.toString("UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return value;
		}

		public void log(String message) {
			System.out.println(message);
		}
	}
}
