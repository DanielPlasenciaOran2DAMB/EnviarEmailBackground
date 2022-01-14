package dad.emailbackground;

import dad.emailbackground.ui.EmailBackgroundController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

	private static Stage primaryStage;

	private EmailBackgroundController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		App.primaryStage = primaryStage;
		controller = new EmailBackgroundController();

		Scene scene = new Scene(controller.getView());

		primaryStage.setTitle("Enviar email");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("/img/email-send-icon-32x32.png"));
		primaryStage.show();
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
