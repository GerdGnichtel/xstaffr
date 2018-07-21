package de.jmda.app.xstaffr.common.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-26T15:38:05.679+0200")
@StaticMetamodel(Supplier.class)
public class Supplier_ {
	public static volatile SingularAttribute<Supplier, Long> id;
	public static volatile SetAttribute<Supplier, SearchResult> results;
	public static volatile SingularAttribute<Supplier, String> name;
	public static volatile SingularAttribute<Supplier, String> text;
}
