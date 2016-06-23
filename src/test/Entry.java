package test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.w3c.dom.Document;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class Entry extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Web View");

		WebView webview = new WebView();
		WebEngine engine = webview.getEngine();

		String uri = getClass().getResource("/javascript/index.html").toExternalForm();
		engine.load(uri);

		engine.documentProperty().addListener(new ChangeListener<Document>() {
			@Override
			public void changed(ObservableValue<? extends Document> observable, Document oldValue, Document newValue) {
				JSObject window = (JSObject) engine.executeScript("window");
				window.setMember("java", new Bridge());
			}
		});

		Scene scene = new Scene(webview);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public class Bridge {
		public String loadJSON() {
			InputStream in = getClass().getResourceAsStream("/javascript/test.json");

			BufferedInputStream bin = new BufferedInputStream(in);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			String json = null;

			try {
				int res = bin.read();
				while (res != -1) {
					byte b = (byte) res;
					buf.write(b);
					res = bin.read();
				}

				json = buf.toString("UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return json;
		}

		public void log(String message) {
			System.out.println(message);
		}
	}
}
