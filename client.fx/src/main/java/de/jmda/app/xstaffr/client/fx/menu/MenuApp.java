package de.jmda.app.xstaffr.client.fx.menu;

import java.util.Optional;

import javax.enterprise.inject.spi.CDI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.client.fx.edit.candidate.CandidateEditor;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditCandidatesEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditCustomersEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditProjectsEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditRequestersEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditSuppliersEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.ExitEvent;
import de.jmda.core.cdi.event.ActionOnEvent;
import de.jmda.fx.cdi.FXApp;
import de.jmda.fx.cdi.FXView;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuApp extends FXApp
{
	private final static Logger LOGGER = LogManager.getLogger(MenuApp.class);

	public MenuApp() { setPostFXMLLoadedAction(Optional.of(() -> initMenuActions())); }

	private FXView fxView;

	@Override protected FXView getFXView()
	{
		if (fxView == null) { fxView = CDI.current().select(Menu.class).get(); }
		return fxView;
	}

	private void initMenuActions()
	{
		ActionOnEvent actionOnEvent = ActionOnEvent.instance();
		actionOnEvent.register(EditCandidatesEvent.class, (e) -> onEditCandidates());
		actionOnEvent.register(EditCustomersEvent.class, (e) -> LOGGER.debug("received event " + e.getClass()));
		actionOnEvent.register(EditProjectsEvent.class, (e) -> LOGGER.debug("received event " + e.getClass()));
		actionOnEvent.register(EditRequestersEvent.class, (e) -> LOGGER.debug("received event " + e.getClass()));
		actionOnEvent.register(EditSuppliersEvent.class, (e) -> LOGGER.debug("received event " + e.getClass()));
		actionOnEvent.register(ExitEvent.class, (e) -> onExit());
	}

	private void onEditCandidates() { CandidateEditor.show(XStaffrServiceProviderJPASE.instance().provide()); }

	private void onExit()
	{
		Window window = getStage().getScene().getWindow();
		if (window instanceof Stage)
		{
			Stage stage = (Stage) window;
			stage.close();
		}
		else
		{
			LOGGER.debug("unexpected: window is not instance of Stage, using Platform.exit() to exit application");
			Platform.exit();
		}
	}
}