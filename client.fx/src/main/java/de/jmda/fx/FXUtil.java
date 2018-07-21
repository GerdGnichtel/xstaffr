package de.jmda.fx;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class FXUtil
{
	private final static Logger LOGGER = LogManager.getLogger(FXUtil.class);

	/** The lock that guarantees that only one JavaFX thread will be started. */
	private static final ReentrantLock LOCK = new ReentrantLock();

	private static AtomicBoolean started = new AtomicBoolean();

	public static void makeSureFXPlatformIsRunning()
	{
		try
		{
			// Lock or wait.  This gives another call to this method time to finish
			// and release the lock before another one has a go
			LOCK.lock();

			started.set(Platform.isFxApplicationThread());

			if (!started.get())
			{
				// not started, so start the JavaFX platform, prepare
				final ExecutorService executor = Executors.newSingleThreadExecutor();

				executor.execute(new Runnable()
				{
					@Override
					public void run()
					{
						LOGGER.debug("    starting fx platform ...");
						// the following starts fx platform, it is intended that JFXPanel is not used after construction
						new JFXPanel();
						LOGGER.debug("... started  fx platform");
						started.set(true);
					}
				});

				while (!started.get())
				{
					Thread.yield();
				}
			}
		}
		finally
		{
			LOCK.unlock();
		}
	}

	public static Optional<Stage> getStage(Node node)
	{
		Scene scene = node.getScene();

		if (scene == null) return Optional.ofNullable(null);

		Window window = scene.getWindow();

		if (window instanceof Stage)
		{
			return Optional.of((Stage) window);
		}

		return Optional.ofNullable(null);
	}

	public static <T> void bindSetToList(ObservableSet<T> set, ObservableList<T> list)
	{
		list.setAll(set);
		set.addListener
		(
				(SetChangeListener<T>) change ->
				{
					if      (change.wasAdded())   list.add(   change.getElementAdded());
					else if (change.wasRemoved()) list.remove(change.getElementRemoved());
				}
		);
	}

	public static <T> void bindListToList(ObservableList<T> listSource, ObservableList<T> listTarget)
	{
		listTarget.setAll(listSource);
		listSource.addListener
		(
				(ListChangeListener<T>) change ->
				{
					if      (change.wasAdded())   listTarget.addAll(   change.getAddedSubList());
					else if (change.wasRemoved()) listTarget.removeAll(change.getRemoved());
				}
		);
	}

	public static HostServices getHostServices()
	{
		Application application =
				new Application()
				{
					@Override public void start(Stage primaryStage) throws Exception { throw new RuntimeException("never start this application"); }
				};
		return application.getHostServices();
	}
}