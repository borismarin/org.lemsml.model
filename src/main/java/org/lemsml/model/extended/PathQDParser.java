package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemsml.model.DerivedVariable;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public class PathQDParser {

	private static final ImmutableMap<String, String> REDUCERS = ImmutableMap
			.of("add", "+", "multiply", "*");
	private Component comp;
	private String path;
	private Optional<String> reduce;

	public PathQDParser(Symbol sym, Component comp) {
		// TODO Auto-generated constructor stub
		this.comp = comp;
		path = ((DerivedVariable) sym.getType()).getSelect().replace('/', '.');
		reduce = Optional.fromNullable(((DerivedVariable) sym.getType())
				.getReduce());
	}

	public String reduceToExpr() {
		String expr;
		if (reduce.isPresent()) {
			expr = Joiner.on(REDUCERS.get(reduce.get())).join(expand());
		} else {
			expr = path;
		}
		return expr;
	}

	public List<String> expand() {
		ArrayList<String> deps = new ArrayList<String>();
		Pattern pat = Pattern.compile("([^\\[\\]]*)\\[(.*)\\]([^\\[\\]]*)");
		Matcher m = pat.matcher(path);
		if (m.find()) {
			int n = comp.getSubComponentsOfType(m.group(1)).size();
			for (int i = 0; i < n; i++) {
				String depName = MessageFormat.format("{0}[{1}]{2}",
						m.group(1), i, m.group(3));
				deps.add(depName);
			}
		} else {
			deps.add(path);
		}
		return deps;
	}

}
