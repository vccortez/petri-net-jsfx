package application;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Controller {

	@FXML
	private TabPane tpEditor;
	@FXML
	private TabPane tpOutput;
	@FXML
	private TextField tfFind;
	@FXML
	private WebView browser = new WebView();
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
			// TODO
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
	private void runFile() {
		WebEngine engine = browser.getEngine();
		engine.load(getSourceCode(getSelectedTab()));

		// Parse parse = new Parse(getSourceCode(getSelectedTab()));
		// setOuputText(parse.parseExpression());
	}

	@FXML
	private void runLexFile() {
		// Lexer lex = new Lexer(new
		// StringReader(getSourceCode(getSelectedTab())));

		StringBuffer buffer = new StringBuffer();
		// EnumToken token;
		// do {
		// token = lex.scan().token;
		// buffer.append(token).append("\n");
		// } while (!token.equals(EnumToken.EOF));

		setOuputText(buffer.toString());
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
		return "<programa> ::= { <comando> [`;´] } [<ultimocomando> [`;´]]\n" + "<bloco> ::= <programa>\n"
				+ "<comando> ::=    <listavar> `=´ <listaexp> |\n" + "		 <chamadadefuncao> | \n"
				+ "	 	 do <bloco> end | \n" + "	 	 while <exp> do <bloco> end | \n"
				+ "	 	 repeat <bloco> until <exp> | \n"
				+ "	 	 if <exp> then <bloco> [{elseif <exp> then <bloco>}] [else <bloco>] end |\n"
				+ "	 	 for <Nome> `=´ <exp> `,´ <exp> [`,´ <exp>] do <bloco> end |\n"
				+ "	 	 for <listadenomes> in <listaexp> do <bloco> end |\n"
				+ "	 	 function <nome> <corpodafuncao> \n" + "<ultimocomando> ::= return [<listaexp>] | break\n"
				+ "<listadenomes> ::= <Nome> {`,´ <Nome>}\n" + "<listaexp> ::=  <exp> {`,´<exp> } \n"
				+ "<exp> ::=  null<exp2> | false<exp2> | true<exp2>| <string><exp2> | <numero><exp2> |\n"
				+ "		 <funcao><exp2> | <expprefixo><exp2> | <construtortabela><exp2> |<opunaria> <exp><exp2>\n"
				+ "<exp2> = <opbin> <exp> <exp2> \n"
				+ "<expprefixo> ::= <var> |   <chamadadefuncao> | `(´ <exp> `)´\n"
				+ "<listavar> ::= <var> {`,´ <var>}\n"
				+ "<var> ::=  <nome> <var2>| <chamadadefuncao><var2> | <chamadadefuncao><var2> |`(´ exp `) <var2> | `(´ exp `)´ <var2>\n"
				+ "<var2> ::= `[´ exp `]´ <var2> |  `.´ <Nome>  <var2> | `.´ <chamadadefuncao>\n"
				+ "<chamadadefuncao> ::= <Nome>  `(´ <args> `)´\n"
				+ "<args> ::=   <listaexp>  | <construtortabela> \n" + "<funcao> ::= function <corpodafuncao>\n"
				+ "<corpodafuncao> ::= `(´ [<listadenomes>] `)´ <bloco> end\n"
				+ "<construtortabela> ::= `{´ [listadecampos] `}´\n"
				+ "<listadecampos> ::= <campo> {<separadordecampos> <campo>} [<separadordecampos>]\n"
				+ "<campo> ::= `[´ <exp> `]´ `=´ <exp> | <nome> `=´ <exp> | <exp>\n"
				+ "<separadordecampos> ::= `,´ | `;´\n"
				+ "<opbin> ::= `+´ | `-´ | `*´ | `/´ | `^´ | `%´ | `..´ | `<´ | `<=´ | `>´ | `>=´ | `==´ | `~=´ | and | or\n"
				+ "<opunaria> ::= `-´ | not | `#´\n" + "<string> ::= `\"´| `'´ {<digito> <letra>} `\"´| `'´ \n";
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
			runLexFile();
		} else if (event.getCode() == KeyCode.F9) {
			runFile();
		}
	}

	private Tab newTab(String name) {
		Tab tab = new Tab(name);
		Image image = new Image(getClass().getResourceAsStream("icon/documentIcon26.png"));
		ImageView iv = new ImageView(image);
		iv.setFitHeight(15);
		iv.setFitWidth(15);
		tab.setGraphic(iv);

		TextArea textArea = new TextArea();
		// TODO style
		textArea.setFont(Font.font("Bitstream Vera Sans Mono Bold", 12));
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
		tpEditor.getTabs().remove(tab);
	}

	private void setOuputText(String text) {
		Tab tabOuput = tpOutput.getSelectionModel().getSelectedItem();
		setSourceCode(tabOuput, text);
	}
}
