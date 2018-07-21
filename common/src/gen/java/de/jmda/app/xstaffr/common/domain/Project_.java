package de.jmda.app.xstaffr.common.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-15T10:57:07.392+0100")
@StaticMetamodel(Project.class)
public class Project_ {
	public static volatile SingularAttribute<Project, Long> id;
	public static volatile SingularAttribute<Project, Customer> customer;
	public static volatile SetAttribute<Project, SearchRequest> searchRequests;
	public static volatile SingularAttribute<Project, String> name;
}
