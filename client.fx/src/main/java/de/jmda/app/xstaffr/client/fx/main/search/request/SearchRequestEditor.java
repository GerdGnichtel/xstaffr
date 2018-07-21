package de.jmda.app.xstaffr.client.fx.main.search.request;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SearchRequestEditor extends FXView
{
	@Override public SearchRequestEditorController getController() { return (SearchRequestEditorController) super.getController(); }

	public SearchRequestEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		SearchRequestEditor editor = CDI.current().select(SearchRequestEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
}