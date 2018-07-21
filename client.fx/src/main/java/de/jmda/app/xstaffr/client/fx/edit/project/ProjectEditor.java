package de.jmda.app.xstaffr.client.fx.edit.project;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectEditor extends FXView
{
	@Override public ProjectEditorController getController() { return (ProjectEditorController) super.getController(); }

	public ProjectEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		ProjectEditor editor = CDI.current().select(ProjectEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(root));
		stage.show();
	}
}