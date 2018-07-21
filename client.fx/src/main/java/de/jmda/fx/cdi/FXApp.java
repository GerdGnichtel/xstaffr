package de.jmda.fx.cdi;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.stage.Stage;

public abstract class FXApp extends Application
{
	private final static Logger LOGGER = LogManager.getLogger(FXApp.class);

	/**
	 * {@code postFXMLLoadedAction} will be run immediately after {@code .fxml} file was loaded during {@link
	 * FXView#getScene()}.
	 */
	private static Optional<Runnable> postFXMLLoadedAction = Optional.ofNullable(null);

	/**
	 * {@code mainView} will be assigned immediately before {@link FXView#getScene()} gets passed to
	 * {@link Stage} during {@link #start(Stage)}. If {@code mainView} is not present this indicates,
	 * that {@link #start(Stage)} was not called yet.
	 */
	private static Optional<? extends FXView> mainView = Optional.ofNullable(null);

	private ReadOnlyObjectWrapper<Stage> stageProperty = new ReadOnlyObjectWrapper<>();

	public static void setPostFXMLLoadedAction(Optional<Runnable> postFXMLLoadedAction)
	{
		if (postFXMLLoadedAction == null) throw new IllegalArgumentException("parameter must not be null");
		FXApp.postFXMLLoadedAction = postFXMLLoadedAction;
	}

	public static Optional<Runnable> getPostFXMLLoadedAction() { return postFXMLLoadedAction; }

	public static Optional<? extends FXView> getMainView() { return mainView; }

	@Override public void start(Stage stage) throws Exception
	{
		mainView = Optional.of(getFXView());
		mainView.get().setPostFXMLLoadedAction(postFXMLLoadedAction);
		stage.setScene(mainView.get().getScene());
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();

		// now that the stage is initialised and shown on the screen stageProperty can be updated and the registered event
		// handlers will be called
		stageProperty.set(stage);
	}

	public Stage getStage()
	{
		Stage result = stageProperty.get();

		if (null == result) { LOGGER.warn("stage not (yet) initialised"); }

		return result;
	}

	/**
	 * Implement this method to provide a view for the app. The view's scene will be set to the stage
	 * of the app. Implementing classes have access to the stage via {@link #getStage()}.
	 * @return view for the app
	 */
	protected abstract FXView getFXView();

	protected ReadOnlyObjectProperty<Stage> stageProperty() { return stageProperty.getReadOnlyProperty(); }
}