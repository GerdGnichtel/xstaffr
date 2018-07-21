package de.jmda.fx.cdi;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.fx.FXScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Base class for fx view classes that provides mechanisms for convenient use of
 * <ul>
 *  <li>a {@code .fxml} file,</li>
 *  <li>a type safe controller and</li>
 *  <li>a {@code .css} file.</li>
 * </ul>
 * The support for the above mentioned features is in each case based on conventions. The conventions apply as follows:
 * <ul>
 *  <li>For a fx view class named {@code FXView} the file {@code FXView.fxml} will be loaded if it can be found in the
 *      same package as {@code FXView}.</li>
 *  <li>For a fx view class named {@code FXView} it will be attempted to create a controller instance of type {@code
 *      FXViewController}. The controller will be assigned to the {@link FXMLLoader} instance that is used to load
 *      the file {@code FXView.fxml} (see above).
 *  <li>For a fx view class named {@code FXView} the file {@code FXView.css} will be loaded if it can be found in the
 *      same package as {@code FXView}.</li>
 * </ul>
 *
 * @author ruu@jmda.de
 */
//@Dependent
public abstract class FXView
{
	private final static Logger LOGGER = LogManager.getLogger(FXView.class);

	@Inject private FXMLLoader fxmlLoader;

	/**
	 * Local root node of the {@link FXView} instance. {@code FXView} instances may build a hierarchy of nodes that may
	 * contain multiple local root nodes.
	 */
	private Parent localRoot;
	private Scene scene;
	private Optional<Runnable> postFXMLLoadedAction = Optional.ofNullable(null);

	/**
	 * initialised lazily in {@link #getCSSResourcename()}
	 *
	 * @see FXScene for naming conventions
	 */
	private String cssResourcename;

	public Parent getLocalRoot()
	{
		if (localRoot == null)
		{
			try
			{
				// if there is a controller assigned to fxmlLoader and that controller has a @FXML annotated
				// initialise() method that method will be called during execution for fxmlLoader.load()
				Object object = fxmlLoader.load();

				if (object instanceof Parent)
				{
					localRoot = (Parent) object;
					postFXMLLoadedAction.ifPresent(a -> a.run());
				}
				else
				{
					throw new IllegalStateException(
							"unexpected type " + object.getClass() + " loaded from " + fxmlLoader.getLocation().toExternalForm());
				}
			}
			catch (IOException e)
			{
				throw new IllegalStateException("failure loading Parent from " + fxmlLoader.getLocation().toExternalForm(), e);
			}
		}

		return localRoot;
	}

	public Scene getScene()
	{

		if (scene == null)
		{
			scene = new Scene(getLocalRoot());

			try
			{
				addStylesheet(scene);
			}
			catch (IOException e)
			{
				LOGGER.warn("failure setting style sheet for " + getClass().getName(), e);
			}
		}

		return scene;
	}

	/**
	 * This method allows to keep track of associated {@code FXView} / controller pairs even if there are
	 * multiple pairs of the same type at the same time.
	 *
	 * @return the controller associated with this {@code FXView}
	 */
	public Object getController() { return getFXMLLoader().getController(); }

	protected void addStylesheet(Scene scene) throws IOException
	{
		String resourcename = getCSSResourcename();
		URL resource = getClass().getResource(resourcename);

		if (null == resource)
		{
			throw new IOException("failure creating style sheet for null resource at " + resourcename);
		}

		String externalForm = resource.toExternalForm();
		scene.getStylesheets().add(externalForm);
		LOGGER.debug("added stylesheet " + externalForm + " to scene " + getClass().getName());
	}

	/**
	 * @return css resource name based on naming convention
	 * @see {@link FXView} for naming conventions
	 */
	protected String getCSSResourcename()
	{
		if (null == cssResourcename)
		{
			cssResourcename = getClass().getSimpleName() + ".css";
		}

		return cssResourcename;
	}

	protected FXMLLoader getFXMLLoader() { return fxmlLoader; }

	protected void setPostFXMLLoadedAction(Optional<Runnable> postFXMLLoadedAction)
	{
		this.postFXMLLoadedAction = postFXMLLoadedAction;
	}
}