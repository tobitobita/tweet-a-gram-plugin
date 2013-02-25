package dsk.tweet_a_gram.plugin.facebook.gui;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dsk.common.exception.DskRuntimeException;
import dsk.common.util.IoTools;

public class AuthApplication extends Application {
	private ClassLoader classLoader;

	private AuthController controller;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(this.createScene());
		stage.show();
	}

	public Scene createScene() {
		ClassLoader theClassLoader = getClass().getClassLoader();
		if (null != this.classLoader) {
			theClassLoader = this.classLoader;
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setClassLoader(theClassLoader);
		InputStream is = null;
		try {
			is = theClassLoader.getResource("dsk/tweet_a_gram/plugin/facebook/gui/auth.fxml").openConnection()
					.getInputStream();
			loader.load(is);
			this.controller = loader.getController();
			Parent root = loader.getRoot();
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			throw new DskRuntimeException("fxmlの指定が不正です", e);
		} finally {
			IoTools.close(is);
		}
	}

	/* getter, setter */

	public AuthController getController() {
		return this.controller;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}
