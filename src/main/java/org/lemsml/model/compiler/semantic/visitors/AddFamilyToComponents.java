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
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author borismarin
 *
 */
public class AddFamilyToComponents extends TraversingVisitor<Boolean, Throwable> {

	public AddFamilyToComponents(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
	}

	@Override
	public Boolean visit(Component comp) throws LEMSCompilerException, Throwable {
		processChildrens(comp);
		processChilds(comp);
	
		return true;
	}

	//TODO: ambiguity when representing child / children
	//TODO: inheritance
	private void processChildrens(Component comp) throws LEMSCompilerException{
		List<Component> subComps = new ArrayList<Component>();
		for(Children expected : comp.getComponentType().getChildrens()){
			subComps.addAll(getSubComponentsOfType(comp, expected.getType()));
		}
		for(Component subComp : subComps){
			subComp.setParent(comp);
		}
		comp.setChildren(subComps);
	}

	//TODO: ambiguity when representing child / children
	private void processChilds(Component comp) throws LEMSCompilerException{
		List<Component> subComps = new ArrayList<Component>();
		for(Child expected : comp.getComponentType().getChildren()){
			//TODO: child (only one..., how does it work for multiple?) logic
			//TODO: is child/children only a syntactic constraint?
			subComps.addAll(getSubComponentsOfType(comp, expected.getType()));
			if (subComps.size() == 0) {
				missingChilds(comp, expected);
			}else if (subComps.size() > 1) {
				tooManyChilds(comp, expected);
			}
		}
		for(Component subComp : subComps){
			subComp.setParent(comp);
		}
		comp.setChildren(subComps);
		
	}

	private void tooManyChilds(Component comp, Child expected) throws LEMSCompilerException {
		String err = MessageFormat
				.format("Component {0} of type [{1}] defines too many Childs of type {2}.",
						comp.getName(),
						comp.getType(),
						expected.getType());
		throw new LEMSCompilerException(err, LEMSCompilerError.TooManyChildren);
		
	}

	public void missingChilds(Component comp, NamedTyped expected)
			throws LEMSCompilerException {
		String err = MessageFormat
				.format("Component {0} of type [{1}] expects {2} of type {3}",
						comp.getName(),
						comp.getType(),
						expected.getClass().getSimpleName(),
						expected.getType());
		throw new LEMSCompilerException(err, LEMSCompilerError.MissingChildren);
	}

	public List<Component> getSubComponentsOfType(Component comp, String type) {
		return Lists.newArrayList(Iterables.filter(comp.getComponent(), hasType(type)));
	}

	private static Predicate<Component> hasType(final String type) {
	    return new Predicate<Component>() {
	        @Override
	        public boolean apply(Component input) {
	            return input.getType() != null && input.getType().equals(type);
	        }
	    };
	}
	
}
