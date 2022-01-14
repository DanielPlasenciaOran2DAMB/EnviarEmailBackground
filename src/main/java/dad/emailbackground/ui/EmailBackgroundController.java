package dad.emailbackground.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import dad.emailbackground.App;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class EmailBackgroundController implements Initializable {

	@FXML
	private TextField asuntoText;

	@FXML
	private PasswordField contrasenaText;

	@FXML
	private TextField destinatarioText;

	@FXML
	private TextArea mensajeText;

	@FXML
	private TextField puertoText;

	@FXML
	private TextField remitenteText;

	@FXML
	private TextField servidorText;

	@FXML
	private CheckBox sslCheck;

	@FXML
	private BorderPane view;

	EmailBackgroundModel model = new EmailBackgroundModel();

	public EmailBackgroundController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmailBackgroundView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		servidorText.textProperty().bindBidirectional(model.servidorProperty());
		puertoText.textProperty().bindBidirectional(model.puertoProperty(), new NumberStringConverter());
		sslCheck.selectedProperty().bindBidirectional(model.conexionSSLProperty());
		remitenteText.textProperty().bindBidirectional(model.remitenteProperty());
		contrasenaText.textProperty().bindBidirectional(model.contrasenaProperty());
		destinatarioText.textProperty().bindBidirectional(model.destinatarioProperty());
		asuntoText.textProperty().bindBidirectional(model.asuntoProperty());
		mensajeText.textProperty().bindBidirectional(model.mensajeProperty());
	}

	@FXML
	void onCerrarAction(ActionEvent event) {
		App.getPrimaryStage().close();
	}

	@FXML
	void onEnviarAction(ActionEvent event) throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName(model.getServidor());
		email.setSmtpPort(model.getPuerto());
		email.setAuthenticator(new DefaultAuthenticator(model.getRemitente(), model.getContrasena()));
		email.setSSLOnConnect(model.isConexionSSL());
		email.setFrom(model.getRemitente());
		email.setSubject(model.getAsunto());
		email.setMsg(model.getMensaje());
		email.addTo(model.getDestinatario());

		Task<Email> tarea = new Task<Email>() {
			@Override
			protected Email call() throws Exception {
				email.send();
				return email;
			}
		};

		tarea.setOnSucceeded(e -> {
			Alert enviado = new Alert(AlertType.INFORMATION);
			enviado.setTitle("Mensaje enviado");
			enviado.setHeaderText("Mensaje enviado con éxito a '" + model.getDestinatario() + "'.");

			Stage stage = (Stage) enviado.getDialogPane().getScene().getWindow();
			stage.getIcons().setAll(App.getPrimaryStage().getIcons());

			enviado.showAndWait();
		});

		tarea.setOnFailed(e -> {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No se pudo enviar el email.");
			error.setContentText("Invalid message supplied");

			Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
			stage.getIcons().setAll(App.getPrimaryStage().getIcons());

			error.showAndWait();
		});
	}

	@FXML
	void onVaciarAction(ActionEvent event) {
		model.setServidor("");
		model.setPuerto(0);
		model.setConexionSSL(false);
		model.setRemitente("");
		model.setContrasena("");
		model.setDestinatario("");
		model.setAsunto("");
		model.setMensaje("");
	}

	public BorderPane getView() {
		return view;
	}
}
