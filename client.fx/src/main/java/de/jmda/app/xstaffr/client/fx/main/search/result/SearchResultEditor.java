package de.jmda.app.xstaffr.client.fx.main.search.result;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SearchResultEditor extends FXView
{
	@Override public SearchResultEditorController getController() { return (SearchResultEditorController) super.getController(); }

	public SearchResultEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		SearchResultEditor editor = CDI.current().select(SearchResultEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}
}