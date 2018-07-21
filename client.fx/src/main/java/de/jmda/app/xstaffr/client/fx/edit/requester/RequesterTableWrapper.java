package de.jmda.app.xstaffr.client.fx.edit.requester;

import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.Requester_;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public final class RequesterTableWrapper
{
	private Requester requester;
	private StringProperty name;

	public RequesterTableWrapper(Requester requester)
	{
		this.requester = requester;
		try
		{
			name =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(requester)
							.name(Requester_.name.getName())
							.build();
		}
		catch (NoSuchMethodException e)
		{
			throw new ExceptionInInitializerError("failure creating " + getClass().getName() + " instance\n" + e);
		}
	}

	public Requester getRequester() { return requester; }
	public void setRequester(Requester requester) { this.requester = requester; }
	public String getName() { return name.get(); }
	public void setName(String name) { this.name.set(name); }
}