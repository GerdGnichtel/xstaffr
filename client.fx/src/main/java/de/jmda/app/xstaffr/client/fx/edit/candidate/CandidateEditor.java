package de.jmda.app.xstaffr.client.fx.edit.candidate;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CandidateEditor extends FXView
{
	@Override public CandidateEditorController getController() { return (CandidateEditorController) super.getController(); }

	public CandidateEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		CandidateEditor editor = CDI.current().select(CandidateEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(root));
		stage.show();
	}
}