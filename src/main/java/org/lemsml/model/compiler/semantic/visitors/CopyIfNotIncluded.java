package org.lemsml.model.compiler.semantic.visitors;

import java.io.File;

import org.lemsml.model.Constant;
import org.lemsml.model.Include;
import org.lemsml.model.Target;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

public class CopyIfNotIncluded extends BaseVisitor<Boolean, Throwable> implements ILemsProcessor {

	private Lems processedLems;
	private File baseFile;

	public CopyIfNotIncluded(File baseFile) throws Throwable {
		this.baseFile = baseFile;
		processedLems = new Lems();
	}

	@Override
	public Boolean visit(Include include) {
		processedLems.getIncludes().add(include);
		return true;
	}

	@Override
	public Boolean visit(Constant constant) {
		if(constant.getDefinedIn().equals(this.baseFile)){
			processedLems.getConstants().add(constant);
		}
		return true;

	}

	@Override
	public Boolean visit(ComponentType componentType) {
		if(componentType.getDefinedIn().equals(this.baseFile)){
			processedLems.getComponentTypes().add(componentType);
		}
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Component component) {
		if(component.getDefinedIn().equals(this.baseFile)){
			processedLems.getComponents().add(component);
		}
		return true;
	}

	@Override
	public Boolean visit(Target target) {
		if(target.getDefinedIn().equals(this.baseFile)){
			processedLems.getTargets().add(target);
		}
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Dimension dimension) {
		if(dimension.getDefinedIn().equals(this.baseFile)){
			processedLems.getDimensions().add(dimension);
		}
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Unit unit) {
		if(unit.getDefinedIn().equals(this.baseFile)){
			processedLems.getUnits().add((org.lemsml.model.extended.Unit) unit);
		}
		return true;
	}

	@Override
	public Lems getLems() {
		return processedLems;
	}
}
