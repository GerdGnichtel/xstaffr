package de.jmda.fx;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.gentyref.GenericTypeReflector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Base class for JavaFX scenes that provides mechanisms for convenient use of
 * <ul>
 *  <li>a type safe controller ({@code CNTRLLR}),</li>
 *  <li>a {@code .fxml} file and</li>
 *  <li>a {@code .css} file.</li>
 * </ul>
 * The support for the above mentioned features is in each case either based on conventions or configuration. If there
 * is no explicit configuration for a mentioned feature the conventions apply as follows:
 * <ul>
 *  <li>It will be attempted to create an instance of type {@code CNTRLLR}. If successful it will be assigned to {@link
 *      #controller}.
 *  <li>For a fx scene class named {@code Scene} the file {@code Scene.fxml} will be loaded if it can be found in the
 *      same package as {@code Scene}.</li>
 *  <li>For a fx scene class named {@code Scene} the file {@code Scene.css} will be loaded if it can be found in the
 *      same package as {@code Scene}.</li>
 * </ul>
 *
 * @param <CNTRLLR> controller class
 *
 * @author ruu@jmda.de
 */
public abstract class FXScene<CNTRLLR>
{
	private final static Logger LOGGER = LogManager.getLogger(FXScene.class);

	/**
	 * injected explicitly into {@link #FXScene(Object, String, String)} or initialised lazily in {@link #getController()}
	 *
	 * @see FXScene
	 */
	private CNTRLLR controller;

	/**
	 * injected explicitly into {@link #FXScene(Object, String, String)} or initialised lazily in {@link
	 * #getFXMLResourcename()}
	 *
	 * @see FXScene
	 */
	private String fxmlResourcename;

	/**
	 * injected explicitly into {@link #FXScene(Object, String, String)} or initialised lazily in {@link
	 * #getCSSResourcename()}
	 *
	 * @see FXScene
	 */
	private String cssResourcename;

	/**
	 * lazily loaded in {@link #getFXMLLoader()}
	 */
	private FXMLLoader fxmlLoader;

	/**
	 * lazily loaded in {@link #getScene()}
	 */
	private Scene scene;

	/**
	 * constructor for configuring features of this class explicitly
	 *
	 * @param controller
	 * @param fxmlResourcename
	 * @param cssResourcename
	 */
	public FXScene(CNTRLLR controller, String fxmlResourcename, String cssResourcename)
	{
		this.controller = controller;
		this.fxmlResourcename = fxmlResourcename;
		this.cssResourcename = cssResourcename;
	}

	/**
	 * constructor for default configuration of features based on naming conventions
	 *
	 * @see FXScene
	 */
	public FXScene() { this(null, null, null); }

	public Scene getScene() throws Exception
	{
		if (null == scene)
		{
			scene = new Scene(getRoot());
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
	 * override this method to perform cleanup operation
	 */
	public void dispose() { }

	protected Parent getRoot() throws Exception
	{
		Parent result;

		FXUtil.makeSureFXPlatformIsRunning();

		try
		{
			result = getFXMLLoader().load();
		}
		catch (IOException e)
		{
			LOGGER.error("failure loading fxml file for " + getFXMLResourcename(), e);
			throw e;
		}

		return result;
	}

	/**
	 * @return loader based on explicitly configured fxml resource name or naming convention
	 * @throws Exception
	 */
	protected FXMLLoader getFXMLLoader() throws Exception
	{
		if (fxmlLoader == null)
		{
			String fxmlResourcename = getFXMLResourcename();
			URL resource = getClass().getResource(fxmlResourcename);

			if (null == resource)
			{
				String msg = "failure creating fxml loader for null resource at " + fxmlResourcename;
				LOGGER.error(msg);
				throw new IOException(msg);
			}
			else
			{
				fxmlLoader = new FXMLLoader(resource);
				LOGGER.debug("initialised fxml loader with resource " + resource.toExternalForm());
				CNTRLLR cntrllr = getController();
				fxmlLoader.setController(cntrllr);
				LOGGER.debug("assigned controller instance of type " + cntrllr.getClass().getName() + " to loader");
			}
		}

		return fxmlLoader;
	}

	/**
	 * @return fxml resource name based on explicit configuration or naming convention
	 * @see {@link FXScene}
	 */
	protected String getFXMLResourcename()
	{
		if (null == fxmlResourcename)
		{
			fxmlResourcename = getClass().getSimpleName() + ".fxml";
		}

		return fxmlResourcename;
	}

	/**
	 * @return explicitly configured controller instance or lazily initialised controller based on type parameter CNTRLLR
	 * @throws Exception
	 */
	protected CNTRLLR getController() throws Exception
	{
		if (controller == null)
		{
			String typenameForFirstGenericParameter =
					GenericTypeReflector
							.getTypeParameter(getClass(), FXScene.class.getTypeParameters()[0])
							.getTypeName();
//							.replace('$', '.');

			try
			{
				// no npe here because forName either returns a value or fails with exception
				@SuppressWarnings("unchecked")
				CNTRLLR cntrllr = (CNTRLLR) Class.forName(typenameForFirstGenericParameter).newInstance();
				controller = cntrllr;
			}
			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
			{
				String msg = "failure creating controller " + typenameForFirstGenericParameter;
				LOGGER.error(msg, e);
				throw new Exception(msg, e);
			}
		}

		return controller;
	}

	/**
	 * @return css resource name based on explicit configuration or naming convention
	 * @see {@link FXScene}
	 */
	protected String getCSSResourcename()
	{
		if (null == cssResourcename)
		{
			cssResourcename = getClass().getSimpleName() + ".css";
		}

		return cssResourcename;
	}

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
}