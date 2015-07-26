package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.lemsml.model.Child;
import org.lemsml.model.Children;
import org.lemsml.model.NamedTyped;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author borismarin
 *
 */
public class FamilyVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;

	public FamilyVisitor(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(Component comp) throws LEMSCompilerException {
		comp.getScope().setParent(lems.getScope()); //TODO: ugly but effective given depth-first traversal...
		//comp.setLemsRoot(lems);
		processChildrens(comp);
		processChilds(comp);

		return true;
	}

	//TODO: ambiguity when representing child / children
	//TODO: children (how does it work for >1 children of same type?)
	private void processChildrens(Component comp) throws LEMSCompilerException{
		List<Component> subComps = new ArrayList<Component>();
		for(Children expected : comp.getComponentType().getChildrens()){
			List<Component> subComponentsOfType = comp.getSubComponentsOfType(expected.getType());
			for(Component sc : subComponentsOfType){
				sc.setBoundTo(expected.getName());
			}
			subComps.addAll(subComponentsOfType);
		}
		for(Component subComp : subComps){
			subComp.getScope().setParent(comp.getScope());
			subComp.setParent(comp);
		}
		comp.setChildren(subComps);
	}

	//TODO: ambiguity when representing child / children
	//TODO: is child/children only a structural constraint?
	private void processChilds(Component comp) throws LEMSCompilerException {
		List<Component> subComps = new ArrayList<Component>();
		for (Child expected : comp.getComponentType().getChildren()) {
			// TODO: see https://github.com/LEMS/jLEMS/issues/71
			// 		 this is a quick & dirty workaround
			List<Component> subComponentsOfType = comp
					.getSubComponentsOfType(expected.getType());
			switch(subComponentsOfType.size()){
			case 0:
				missingChilds(comp, expected);
				break;
			case 1:
				Component sc = subComponentsOfType.get(0);
				sc.setBoundTo(expected.getName());
				subComps.add(sc); // only one here
				break;
			default: // more than 1
				List<Component> rightName = filterName(subComponentsOfType, expected.getName());
				switch(rightName.size()){
				case 0:
					unboundChild(comp, expected);
					break;
				case 1:
					sc = rightName.get(0);
					sc.setBoundTo(expected.getName());
					subComps.add(sc);
					break;
				default: //more than 1
					tooManyChilds(comp, expected);
					break;
				}
				break;
			}

		}
		for (Component subComp : subComps) {
			subComp.getScope().setParent(comp.getScope());
			subComp.setParent(comp);
		}
		comp.setChildren(subComps);
	}

	private void unboundChild(Component comp, Child expected) throws LEMSCompilerException {
		// TODO Auto-generated method stub
		String err = MessageFormat
				.format("Component {0} of type [{1}] needs to define a Child of type {2} named {3}.",
						comp.getName(),
						comp.getType(),
						expected.getType(),
						expected.getName());
		throw new LEMSCompilerException(err, LEMSCompilerError.UnboundChild);

	}

	public List<Component> filterName(List<Component> comps, String name) {
		return Lists.newArrayList(Iterables.filter(comps, isNamed(name)));
	}

	public static Predicate<Component> isNamed(final String name) {
		return new Predicate<Component>() {
			@Override
			public boolean apply(Component input) {
				return input.getName() != null && input.getName().equals(name);
			}
		};
	}

	private void tooManyChilds(Component comp, Child expected) throws LEMSCompilerException {
		String err = MessageFormat
				.format("Component {0} of type [{1}] defines too many Childs of type {2}.",
						comp.getName(),
						comp.getType(),
						expected.getType());
		throw new LEMSCompilerException(err, LEMSCompilerError.TooManyChildren);

	}

	private void missingChilds(Component comp, NamedTyped expected)
			throws LEMSCompilerException {
		String err = MessageFormat
				.format("Component {0} of type [{1}] expects {2} of type {3}",
						comp.getName(),
						comp.getType(),
						expected.getClass().getSimpleName(),
						expected.getType());
		throw new LEMSCompilerException(err, LEMSCompilerError.MissingChildren);
	}

}
