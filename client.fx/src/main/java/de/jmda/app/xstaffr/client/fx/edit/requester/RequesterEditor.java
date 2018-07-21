package de.jmda.app.xstaffr.client.fx.edit.requester;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RequesterEditor extends FXView
{
	@Override public RequesterEditorController getController() { return (RequesterEditorController) super.getController(); }

	public RequesterEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		RequesterEditor editor = CDI.current().select(RequesterEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(root));
		stage.show();
	}
}