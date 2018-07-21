package de.jmda.app.xstaffr.client.fx.edit.customer;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerEditor extends FXView
{
	@Override public CustomerEditorController getController() { return (CustomerEditorController) super.getController(); }

	public CustomerEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		CustomerEditor editor = CDI.current().select(CustomerEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(root));
		stage.show();
	}
}