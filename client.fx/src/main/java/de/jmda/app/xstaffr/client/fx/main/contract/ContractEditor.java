package de.jmda.app.xstaffr.client.fx.main.contract;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ContractEditor extends FXView
{
	@Override public ContractEditorController getController() { return (ContractEditorController) super.getController(); }

	public ContractEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		ContractEditor editor = CDI.current().select(ContractEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(root));
		stage.show();
	}
}