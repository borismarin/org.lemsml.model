package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DimensionalAnalysisVisitor;
import org.lemsml.model.compiler.semantic.visitors.ScopeTraverser;
import org.lemsml.model.extended.Lems;

public class CheckExpressionDimensions extends ASemanticPass {

	CheckExpressionDimensions(Lems lems) throws Throwable {
		super.setLems(lems);
		super.setTraverser(new ScopeTraverser<Throwable>());
		super.setTraveseFirst(true);
		super.setVisitor(new DimensionalAnalysisVisitor(lems));
	}

}
