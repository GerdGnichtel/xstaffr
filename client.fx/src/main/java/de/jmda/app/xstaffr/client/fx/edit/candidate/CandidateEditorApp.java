package de.jmda.app.xstaffr.client.fx.edit.candidate;

import java.util.Optional;

import javax.enterprise.inject.spi.CDI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.common.service.jpa.util.Populator;
import de.jmda.app.xstaffr.common.service.jpa.util.PopulatorCommon;
import de.jmda.fx.cdi.FXApp;
import de.jmda.fx.cdi.FXView;

public class CandidateEditorApp extends FXApp
{
	private final static Logger LOGGER = LogManager.getLogger(CandidateEditorApp.class);

	private CandidateEditor fxView;

	@Override protected FXView getFXView()
	{
		if (fxView == null)
		{
			fxView = CDI.current().select(CandidateEditor.class).get();
			setPostFXMLLoadedAction(postFXMLLoadedAction());
		}

		return fxView;
	}

	private Optional<Runnable> postFXMLLoadedAction()
	{
		Runnable result =
				() ->
				{
					Populator populator = new PopulatorCommon(XStaffrServiceProviderJPASE.instance().provide());
					LOGGER.debug("population size: " + populator.populate());
					fxView.getService().reload();
				};
		return Optional.of(result);
	}
}