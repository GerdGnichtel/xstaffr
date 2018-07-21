package de.jmda.app.xstaffr.client.fx;

import java.io.IOException;
import java.util.Optional;

import javax.enterprise.inject.spi.CDI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaff.common.domain.test.DBUtils;
import de.jmda.app.xstaffr.common.service.jpa.util.Populator;
import de.jmda.app.xstaffr.common.service.jpa.util.PopulatorCommon;
import de.jmda.core.util.io.IOUtil;
import de.jmda.fx.cdi.FXApp;
import de.jmda.fx.cdi.FXView;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class XStaffrApp extends FXApp
{
	private XStaffr fxView;

	public XStaffrApp()
	{
		// TODO find a way to move this to another place (controller or utility)
		stageProperty().addListener
		(
				(ChangeListener<Stage>) (observable, oldValue, newValue) ->
				{
					if (oldValue == null && newValue != null)
					{
//						newValue.setFullScreen(true);
						Screen screen = Screen.getPrimary();
						Rectangle2D bounds = screen.getVisualBounds();

						newValue.setX(bounds.getMinX());
						newValue.setY(bounds.getMinY());
						newValue.setWidth(bounds.getWidth());
						newValue.setHeight(bounds.getHeight());
					}
				}
		);
	}

	@Override protected FXView getFXView()
	{
		if (fxView == null)
		{
			fxView = CDI.current().select(XStaffr.class).get();
//			setPostFXMLLoadedAction(postFXMLLoadedAction());
		}

		return fxView;
	}

	private final static Logger LOGGER = LogManager.getLogger(XStaffrApp.class);

	private Optional<Runnable> postFXMLLoadedAction()
	{
		Runnable result =
				() ->
				{
					Populator populator = new PopulatorCommon(XStaffrServiceProviderJPASE.instance().provide());
					LOGGER.debug("population size: " + populator.populate());
					fxView.getService().reloadSearchRequests();
				};
		return Optional.of(result);
	}
}