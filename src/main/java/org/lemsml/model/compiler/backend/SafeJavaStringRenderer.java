package org.lemsml.model.compiler.backend;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.StringRenderer;

import com.google.common.base.Joiner;

public class SafeJavaStringRenderer extends StringRenderer implements
		AttributeRenderer {

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		String s = (String) o;
		String newFormat = formatString;
		if(formatString != null){
			List<String> args = new LinkedList<String>(Arrays.asList(formatString.split(",")));
			int safe = args.indexOf("safe");
			if(safe >= 0){
				s = s.replaceAll(":", "_");
				args.remove(safe);
				newFormat = args.size() > 0 ? Joiner.on("").join(args) : null;
			}
		}
		s = (String) super.toString(s, newFormat, locale);
		return s;
	}

}
