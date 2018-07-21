package de.jmda.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CollectionsUtil
{
	@SafeVarargs
	public static <T> Set<T> asSet(T... elems)
	{
		if (elems == null) { return new HashSet<>(); }

		Set<T> result = new HashSet<>(elems.length);

		for (T elem : elems)
		{
			result.add(elem);
		}

		return result;
	}

	@SafeVarargs
	public static <T> List<T> asList(T... elems)
	{
		if (elems == null) { return new ArrayList<>(); }

		List<T> result = new ArrayList<>(elems.length);

		for (T elem : elems)
		{
			result.add(elem);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Class<?> clazz, Collection<T> collection)
	{
		return collection.toArray((T[]) Array.newInstance(clazz, collection.size()));
	}
}