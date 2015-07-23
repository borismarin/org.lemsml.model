package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemsml.model.exceptions.LEMSCompilerException;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public class PathQDParser {

	private static final ImmutableMap<String, String> REDUCERS = ImmutableMap
			.of("add", "+", "multiply", "*");
	static Pattern pattern = Pattern.compile("([^\\[\\]]*)\\[(.*)\\]([^\\[\\]]*)");


	static public String reduceToExpr(List<String> paths, Optional<String> reduce) {
		String expr;
		if (reduce.isPresent()) {
			expr = Joiner.on(REDUCERS.get(reduce.get())).join(paths);
		} else {
			expr = paths.get(0);
		}
		return expr;
	}

	public static List<String> expand(String path, Component comp) {
		ArrayList<String> deps = new ArrayList<String>();
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
			int n = comp.getSubComponentsOfType(matcher.group(1)).size();
			for (int i = 0; i < n; i++) {
				String depName = MessageFormat.format(
						"{0}[{1}]{2}",
						matcher.group(1),
						i,
						matcher.group(3));
				deps.add(depName);
			}
		} else {
			deps.add(path);
		}
		return deps;
	}

	public static Symbol resolvePath(String path, Component comp) throws LEMSCompilerException {
		return followPath(path, comp).getScope().resolve(path.split("\\.")[1]);
	}

	private static Component followPath(String path, Component comp){
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
			return comp.getSubComponentsOfType(matcher.group(1)).get(Integer.valueOf(matcher.group(2)));
		}
		else{
			return comp.getSubComponentsWithName(path.split("\\.")[0]).get(0);
		}
	}


}
