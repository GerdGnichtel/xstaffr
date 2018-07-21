package de.jmda.fx.cdi;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import javafx.fxml.FXMLLoader;

public class FXMLLoaderProducer
{
	@Inject private Instance<Object> cdiInstance;

	@Produces public FXMLLoader produce(InjectionPoint injectionPoint)
	{
		// TODO use injection or some other cdi mechanism?
		FXMLLoader result = new FXMLLoader();

		Class<?> beanType = injectionPoint.getBean().getBeanClass();
		result.setLocation(beanType.getResource(beanType.getSimpleName() + ".fxml"));

		String controllerTypeName = beanType.getName() + "Controller";
		Class<?> controllerType;

		try
		{
			controllerType = Class.forName(controllerTypeName);
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalStateException("failure producing controller for " + controllerTypeName, e);
		}

		// the factory will not be called unless fx:controller does not have a value in .fxml file, "java.lang.Object" as
		// value works
//		result.setControllerFactory(c -> cdiInstance.select(controllerType).get());
		// if fx:controller does not have a value in .fxml file the controller can be set directly
		result.setController(cdiInstance.select(controllerType).get());

		return result;
	}

	/** package visibility, just for testing */
	Instance<Object> getInstanceController() { return cdiInstance; }
}