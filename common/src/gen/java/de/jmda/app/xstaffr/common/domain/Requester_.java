package de.jmda.app.xstaffr.common.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-15T10:57:07.398+0100")
@StaticMetamodel(Requester.class)
public class Requester_ {
	public static volatile SingularAttribute<Requester, Long> id;
	public static volatile SetAttribute<Requester, SearchRequest> requests;
	public static volatile SingularAttribute<Requester, String> name;
}
