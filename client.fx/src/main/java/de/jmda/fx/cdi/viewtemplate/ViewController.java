package de.jmda.fx.cdi.viewtemplate;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ViewController implements ViewService
{
	private final static Logger LOGGER = LogManager.getLogger(ViewController.class);

	@FXML private VBox vbxRoot;

	@FXML private HBox hbxTop;
	@FXML private HBox hbxCenter;
	@FXML private HBox hbxBottom;

	@FXML private Pane pnLeft;
	@FXML private Pane pnMain;
	@FXML private Pane pnRight;

	@Inject private Event<ViewServiceAvailable> eventServiceAvailable;

	private Button btnTop = new Button("top");
	private Button btnBottom = new Button("bottom");

	private Button btnLeft = new Button("left");
	private Button btnMain = new Button("main");
	private Button btnRight = new Button("right");

	@FXML private void initialize()
	{
		LOGGER.debug("initialising");

		btnTop.setOnAction(e -> System.out.println(btnTop.getText()));
		btnBottom.setOnAction(e -> System.out.println(btnBottom.getText()));

		btnLeft.setOnAction(e -> System.out.println(btnLeft.getText()));
		btnMain.setOnAction(e -> System.out.println(btnMain.getText()));
		btnRight.setOnAction(e -> System.out.println(btnRight.getText()));

		hbxTop.getChildren().add(btnTop);
		hbxBottom.getChildren().add(btnBottom);

		pnLeft.getChildren().add(btnLeft);
		pnMain.getChildren().add(btnMain);
		pnRight.getChildren().add(btnRight);

		eventServiceAvailable.fire(new ViewServiceAvailable(this, this));
	}
}