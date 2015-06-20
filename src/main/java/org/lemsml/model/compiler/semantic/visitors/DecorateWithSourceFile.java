package org.lemsml.model.compiler.semantic.visitors;

import java.io.File;

import org.lemsml.model.Constant;
import org.lemsml.model.Target;
import org.lemsml.model.Lems;
import org.lemsml.visitors.BaseVisitor;

/**
 * @author borismarin
 *
 */
public class DecorateWithSourceFile extends BaseVisitor<Boolean, Throwable> {

	private File sourceFile;

	public DecorateWithSourceFile(File sourceDoc) {
		sourceFile = sourceDoc;
	}

	@Override
	public Boolean visit(Lems lems) {
		lems.setDefinedIn(sourceFile);
		return true;

	}

	@Override
	public Boolean visit(Constant constant) {
		constant.setDefinedIn(sourceFile);
		return true;

	}

	@Override
	public Boolean visit(org.lemsml.model.extended.ComponentType componentType) {
		componentType.setDefinedIn(sourceFile);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Component component) {
		component.setDefinedIn(sourceFile);
		return true;
	}

	@Override
	public Boolean visit(Target target) {
		target.setDefinedIn(sourceFile);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Dimension dimension) {
		dimension.setDefinedIn(sourceFile);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Unit unit) {
		unit.setDefinedIn(sourceFile);
		return true;
	}
}
