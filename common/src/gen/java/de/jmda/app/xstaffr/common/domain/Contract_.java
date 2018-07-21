package de.jmda.app.xstaffr.common.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-15T10:57:07.379+0100")
@StaticMetamodel(Contract.class)
public class Contract_ {
	public static volatile SingularAttribute<Contract, Long> id;
	public static volatile SingularAttribute<Contract, SearchResult> searchResult;
	public static volatile SingularAttribute<Contract, LocalDate> inception;
	public static volatile SingularAttribute<Contract, LocalDate> expiration;
	public static volatile SingularAttribute<Contract, BigDecimal> hourlyRate;
	public static volatile SingularAttribute<Contract, String> text;
}
