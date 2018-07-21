package de.jmda.app.xstaffr.client.fx.edit.supplier;

import javax.enterprise.inject.spi.CDI;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.fx.cdi.FXView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SupplierEditor extends FXView
{
	@Override public SupplierEditorController getController() { return (SupplierEditorController) super.getController(); }

	public SupplierEditorService getService() { return getController(); }

	public static void show(XStaffrService xStaffrService)
	{
		SupplierEditor editor = CDI.current().select(SupplierEditor.class).get();
		Parent root = editor.getLocalRoot();

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(root));
		stage.show();
	}
}