package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemsml.model.DerivedVariable;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public class PathQDParser {

	private static final ImmutableMap<String, String> REDUCERS = ImmutableMap
			.of("add", "+", "multiply", "*");
	static Pattern predicatePattern = Pattern
			.compile("([^\\[\\]]*)\\[(.*)\\]([^\\[\\]]*)");
	static Pattern equalityPattern = Pattern.compile("(.*)='(.*)'");

	static public String reduceToExpr(List<String> paths,
			Optional<String> reduce) throws LEMSCompilerException {
		String expr;
		if (reduce.isPresent()) {
			String op = REDUCERS.get(reduce.get());
			if (null == op) {
				throw new LEMSCompilerException("Invalid reduce argument: "
						+ reduce.get(), LEMSCompilerError.UndefinedReducer);
			}
			expr = Joiner.on(op).join(paths);
		} else {
			expr = paths.get(0);
		}
		return expr;
	}

	public static List<String> expand(String path, Component comp)
			throws LEMSCompilerException {
		ArrayList<String> deps = new ArrayList<String>();
		Matcher predMatcher = predicatePattern.matcher(path);
		// TODO: argh!! we need a proper parser...
		// in particular, predicates after the first node (e.g. a/b[*]) are not
		// supported.
		if (predMatcher.find()) { // this is a predicate-like expression
			if (predMatcher.group(2).equals("*")) { // select '*'
				List<Component> allWithName = comp
						.getSubComponentsBoundToName(predMatcher.group(1));
				for (Component c : allWithName) {
					String depName = MessageFormat.format("{0}[{1}]{2}",
							predMatcher.group(1), allWithName.indexOf(c),
							predMatcher.group(3));
					deps.add(depName);
				}
			} else {
				Matcher eqMatcher = equalityPattern.matcher(predMatcher
						.group(2));
				if (eqMatcher.find()) { // x='y' predicate
					List<Component> allWithText = comp
							.getSubComponentsWithTextValue(eqMatcher.group(1),
									eqMatcher.group(2));
					for (Component c : allWithText) {
						String depName = MessageFormat.format("{0}[{1}]{2}",
								predMatcher.group(1), comp.getComponent()
										.indexOf(c), predMatcher.group(3));
						deps.add(depName);
					}
				} else {
					String err = MessageFormat.format(
							"Invalid path predicate {0} in path '{1}'.",
							predMatcher.group(2), path);
					throw new LEMSCompilerException(err,
							LEMSCompilerError.InvalidPath);
				}

			}
		} else {
			deps.add(path);
		}
		return deps;
	}

	public static Symbol resolvePath(String path, Component comp)
			throws LEMSCompilerException {
		// TODO: this is stupid. Symbols should be children along with subcomps
		// so that path walking will be uniform
		String[] steps = path.split("\\.");
		return followPath(steps, comp).getScope().resolve(
				steps[steps.length - 1]);
	}

	private static Component followPath(String[] steps, Component comp) {
		String first = steps[0];
		String[] rest = Arrays.copyOfRange(steps, 1, steps.length);
		if (steps.length == 1) {
			return comp;
		}
		Matcher matcher = predicatePattern.matcher(first);
		if (matcher.find()) { // [...] predicate
			return followPath(
					rest,
					comp.getSubComponentsBoundToName(matcher.group(1)).get(
							Integer.valueOf(matcher.group(2))));
		} else {
			return followPath(rest, comp.getSubComponentsBoundToName(first)
					.get(0));
		}
	}

	public static String getEmptySelection(DerivedVariable dv, Lems lemsRoot) {
		String firstUnitOfDim = lemsRoot
				.getAllUnitsForDimension(dv.getDimension()).get(0).getSymbol();
		String emptySel;
		if (dv.getReduce().equals("add")) {
			emptySel = "0" + firstUnitOfDim; // TODO: UGLYUGLY
		} else
			emptySel = "1" + firstUnitOfDim;
		return emptySel;
	}

}
