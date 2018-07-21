package de.jmda.app.xstaffr.client.fx.settings.filter;

import de.jmda.fx.cdi.FXView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FilterEditor extends FXView
{
	@Override public FilterEditorController getController() { return (FilterEditorController) super.getController(); }

	public FilterEditorService getService() { return getController(); }

	private Stage localStage;

	private Stage getLocalStage()
	{
		if (localStage == null)
		{
			localStage = new Stage();
			localStage.initModality(Modality.APPLICATION_MODAL);
			localStage.setTitle("search request filter definition");
		}
		return localStage;
	}

	private Scene localScene;

	private Scene getLocalScene()
	{
		if (localScene == null)
		{
			localScene = new Scene(getLocalRoot());
		}
		return localScene;
	}

	public void show()
	{
		Stage localStage = getLocalStage();
		localStage.setScene(getLocalScene());
		localStage.showAndWait();
	}
}