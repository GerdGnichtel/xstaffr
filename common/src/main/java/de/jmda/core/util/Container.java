package de.jmda.core.util;

/**
 * Container for generic values. Useful to solve the "must be final or effectively final" problem.
 *
 * @param <T>
 *
 * @author roger@jmda.de
 */
public class Container<T>
{
	public T value;
	public Container() {};
	public Container(T value) { this.value = value; };
}