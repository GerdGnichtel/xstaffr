package de.jmda.app.xstaffr.common.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-04-14T16:47:12.176+0200")
@StaticMetamodel(Candidate.class)
public class Candidate_ {
	public static volatile SingularAttribute<Candidate, Long> id;
	public static volatile SingularAttribute<Candidate, String> lastName;
	public static volatile SingularAttribute<Candidate, String> firstName;
	public static volatile SingularAttribute<Candidate, String> uniqueName;
	public static volatile SingularAttribute<Candidate, String> text;
}
