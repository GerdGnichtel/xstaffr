package de.jmda.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.lang.model.element.Name;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public abstract class StringUtil
{
	/**
	 * Removes the characters in <code>charsToTrim</code> from both sides of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed input string
	 */
	public static String allTrimChars(String input, String charsToTrim)
	{
		return lrTrimChars(input, charsToTrim);
	}

	/**
	 * @see #allTrimChars(String, String)
	 * @param input
	 * @param charsToTrim
	 * @return trimmed input string
	 */
	public static StringBuffer allTrimChars(
			StringBuffer input, String charsToTrim)
	{
		return lrTrimChars(input, charsToTrim);
	}

	/**
	 * Removes the characters in <code>charsToTrim</code> from both sides of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed input string
	 */
	public static String lrTrimChars(String input, String charsToTrim)
	{
		return
				rTrimChars(
						lTrimChars(input, charsToTrim),
						charsToTrim);
	}

	/**
	 * @see #lrTrimChars(String, String)
	 * @param input
	 * @param charsToTrim
	 * @return trimmed input string
	 */
	public static StringBuffer lrTrimChars(
			StringBuffer input, String charsToTrim)
	{
		return
				rTrimChars(
						lTrimChars(input, charsToTrim),
						charsToTrim);
	}

	/**
	 * Removes the characters in <code>charsToTrim</code> from left side of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed <code>input</code> string, <code>null</code> if <code>input
	 *         </code> was <code>null</code>
	 */
	public static String lTrimChars(String input, String charsToTrim)
	{
		if (input == null)
		{
			return null;
		}

		StringBuffer result = new StringBuffer(input);

		if (!StringUtils.isEmpty(input) && !StringUtils.isEmpty(charsToTrim))
		{
			result = trimChars(new StringBuffer(input), charsToTrim, true);
		}

		return result.toString();
	}

	/**
	 * @see #lTrimChars(String, String)
	 * @param input
	 * @param charsToTrim
	 * @return trimmed input string
	 */
	public static StringBuffer lTrimChars(
			StringBuffer input, String charsToTrim)
	{
		if (!isEmpty(input) && !StringUtils.isEmpty(charsToTrim))
		{
			return trimChars(input, charsToTrim, true);
		}

		return input;
	}

	/**
	 * Removes the characters in <code>charsToTrim</code> from right side of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed <code>input</code> string, <code>null</code> if <code>input
	 *         </code> was <code>null</code>
	 */
	public static String rTrimChars(String input, String charsToTrim)
	{
		if (input == null)
		{
			return null;
		}

		StringBuffer result = new StringBuffer(input);

		if (!StringUtils.isEmpty(input) && !StringUtils.isEmpty(charsToTrim))
		{
			result = trimChars(new StringBuffer(input), charsToTrim, false);
		}

		return result.toString();
	}

	/**
	 * @param input string buffer to be trimmed
	 * @param charsToTrim characters to be removed
	 * @return trimmed input string buffer
	 */
	public static StringBuffer rTrimChars(
			StringBuffer input, String charsToTrim)
	{
		if (!isEmpty(input) && !StringUtils.isEmpty(charsToTrim))
		{
			return trimChars(input, charsToTrim, false);
		}

		return input;
	}

	/**
	 * Recursive implementation!
	 * <p>
	 * Removes the characters in <code>charsToTrim</code> from front or back of
	 * <code>input</code>.
	 *
	 * @param input
	 * @param charsToTrim
	 * @param fromFront
	 * @return trimmed input string buffer
	 */
	private static StringBuffer trimChars(
			StringBuffer input, String charsToTrim, boolean fromFront)
	{
		// recursive calls may lead to empty input
		if (input.length() == 0)
		{
			return new StringBuffer();
		}

		char[] charsToTrimAsArray = charsToTrim.toCharArray();

		// test each character if it has to be trimmed from front / back of input
		for (char c : charsToTrimAsArray)
		{
			if (fromFront)
			{
				if (input.charAt(0) == c)
				{
					// found character to be trimmed from front, delete that character
					// and start recursive call
					input.deleteCharAt(0);
					return trimChars(input, charsToTrim, fromFront);
				}
			}
			else
			{
				int lastCharPos = input.length() - 1;
				if (input.charAt(lastCharPos) == c)
				{
					// found character to be trimmed from back, delete that character
					// and start recursive call
					input.deleteCharAt(lastCharPos);
					return trimChars(input, charsToTrim, fromFront);
				}
			}
		}

		return input;
	}

	/**
	 * @see #isEmpty(String)
	 */
	public static boolean isEmpty(StringBuffer input)
	{
		if (input == null)
		{
			return true;
		}

		return isEmpty(input.toString());
	}

	/**
	 * @param input string to be checked
	 * @return <code>true</code> if <code>input == null</code> or <code>
	 *         input.length() == 0</code>, <code>false</code> otherwise
	 */
	public static boolean isEmpty(String input)
	{
		if (input == null)
		{
			return true;
		}

		if (input.length() == 0)
		{
			return true;
		}

		return false;
	}

	/**
	 * Finds and returns longest string in <code>strings</code>.
	 *
	 * @param strings
	 *
	 * @return longest string in <code>strings</code>
	 */
	public static String findLongestString(String[] strings)
	{
		if (strings == null)
		{
			return "";
		}

		if (strings.length == 0)
		{
			return "";
		}

		String result = null;
		int maxLength = -1;

		for (String element : strings)
		{
			if (element.length() > maxLength)
			{
				result = element;
				maxLength = element.length();
			}
		}

		return result;
	}

	/**
	 * @param strings strings to be checked
	 * @return length of longest string in <code>strings</code>
	 */
	public static int getMaxStringLength(String[] strings)
	{
		return findLongestString(strings).length();
	}

	/**
	 * @see StringUtils#leftPad(String, int, char)
	 *
	 * @param input
	 * @param fillChar
	 * @param targetLength
	 * @return filled string
	 */
	public static String lFillCharsTargetLength(
			String input, char fillChar, int targetLength)
	{
		return StringUtils.leftPad(input, targetLength, fillChar);
	}

	/**
	 * @see StringUtils#rightPad(String, int, char)

	 * @param input
	 * @param fillChar
	 * @param targetLength
	 * @return filled string
	 */
	public static String rFillCharsTargetLength(
			String input, char fillChar, int targetLength)
	{
		return StringUtils.rightPad(input, targetLength, fillChar);
	}

	public static String firstLetterToLowerCase(String string)
	{
		return WordUtils.uncapitalize(string);
	}

	public static String firstLetterToLowerCase(Name name)
	{
		return firstLetterToLowerCase(name.toString());
	}

	public static String firstLetterToUpperCase(String string)
	{
		return WordUtils.capitalize(string);
	}

	public static String firstLetterToUpperCase(Name name)
	{
		return firstLetterToUpperCase(name.toString());
	}

	public static StringBuffer sb()
	{
		return new StringBuffer();
	}

	public static StringBuffer sb(String string)
	{
		return new StringBuffer(string);
	}

	public static String toString(Object o)
	{
		// parameter checking
		if (o == null)
		{
			return null;
		}

		// initialization
		StringBuffer        result      = new StringBuffer(o.getClass().getName());
		Class<?>            clss        = o.getClass();
		Method[]            methods     = clss.getMethods();
		String              methodName  = null;
		String              methodValue = null;
		Map<String, String> map         = new HashMap<String, String>();
		Iterator<String>    iter        = null;
		String[]            keys        = null;
		int                 max         = -1;
		int                 counter     = 0;

		// invoke all get methods without parameters (except Object.getClass()) and
		// store returned value together with the method name in map
		for (Method method : methods)
		{
			methodName = method.getName();

			if ((!methodName.equals("getClass")) &&
					methodName.startsWith("get")     &&
					(method.getParameterTypes().length == 0))
			{
				try
				{
					// null is a valid return value of invoke and we want to treat null
					// as a normal method value. Since we can't invoke Object.toString()
					// on null we concat Strings here to build methodValue
					methodValue = "" + method.invoke(o, (Object[]) null);
				}
				catch (IllegalAccessException e)
				{
					System.out.println(e);
				}
				catch (IllegalArgumentException e)
				{
					System.out.println(e);
				}
				catch (InvocationTargetException e)
				{
					System.out.println(e);
				}

				map.put(methodName.substring(3,
						4).toLowerCase() +
						methodName.substring(4),
						methodValue);
			}
		}

		// copy all get method names into keys array
		keys = new String[map.keySet().size()];

		iter = map.keySet().iterator();

		while (iter.hasNext())
		{
			keys[counter] = iter.next();
			counter++;
		}

		// sort keys array
		Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);

		max = StringUtil.findLongestString(keys).length();

		for (String key : keys)
		{
			result.append("\n\t")
			.append(key)
			.append(StringUtils.repeat(" ", max - key.length()))
			.append(" [")
			.append(map.get(key))
			.append("]");
		}

		return result.toString();
	}

	public static boolean isBlank(StringBuffer stringBuffer)
	{
		if (stringBuffer == null)
		{
			return true;
		}

		return StringUtils.isBlank(stringBuffer.toString());
	}

	public static boolean startsWith(StringBuffer stringBuffer, String prefix)
	{
		if (isBlank(stringBuffer))
		{
			return false;
		}

		return stringBuffer.toString().startsWith(prefix);
	}

	public static boolean startsWithUpper(StringBuffer stringBuffer)
	{
		if (isBlank(stringBuffer))
		{
			return false;
		}

		return Character.isUpperCase(stringBuffer.charAt(0));
	}

	public static boolean startsWithLower(StringBuffer stringBuffer)
	{
		if (isBlank(stringBuffer))
		{
			return false;
		}

		return Character.isLowerCase(stringBuffer.charAt(0));
	}

	public static boolean endsWith(StringBuffer stringBuffer, String suffix)
	{
		if (isBlank(stringBuffer))
		{
			return false;
		}

		return stringBuffer.toString().endsWith(suffix);
	}

	public static StringBuffer replace(
			StringBuffer stringBuffer, String oldString, String newString)
	{
		return
				new StringBuffer(stringBuffer.toString().replace(oldString, newString));
	}

	public static String indent(String string, String indent, int indentLevel)
	{
		if (StringUtils.isBlank(string))
		{
			return string;
		}

		if (StringUtils.isEmpty(indent))
		{
			return string;
		}

		if (indentLevel < 0)
		{
			return string;
		}

		String indentation = StringUtils.repeat(indent, indentLevel);

		return
				indentation +      // indent first line
				string.replaceAll( // indent following lines
						"\\n",
						"\n" + indentation);
	}

	public static StringBuffer indent(
			StringBuffer stringBuffer, String indent, int indentLevel)
	{
		if (stringBuffer == null)
		{
			return null;
		}

		return sb(indent(stringBuffer.toString(), indent, indentLevel));
	}
}