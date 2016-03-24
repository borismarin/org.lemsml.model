package org.lemsml.model.compiler.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.StringRenderer;

import com.google.common.base.Joiner;

public class SafeJavaStringRenderer extends StringRenderer implements
		AttributeRenderer {

	static final List<String> reserved = new ArrayList<String>(Arrays.asList("include", "component"));

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		String s = (String) o;
		String nextFormat = formatString;
		if(formatString != null){
			List<String> args = new LinkedList<String>(Arrays.asList(formatString.split(",")));
			int safe = args.indexOf("safe");
			if(safe >= 0){
				s = removePunctuation(s);
				s = renameReserved(s);
				args.remove(safe);
				nextFormat = args.size() > 0 ? Joiner.on("").join(args) : null;
			}
		}
		s = (String) super.toString(s, nextFormat, locale);
		return s;
	}

	public String renameReserved(String s) {
		if(reserved.contains(s.toLowerCase())){
			return s + "_";
		}
		return s;
	}

	public String removePunctuation(String s) {
		return s.replaceAll(":", "_");
	}

}
