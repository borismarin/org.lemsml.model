package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.compiler.semantic.visitors.DimensionalAnalysisVisitor;
import org.lemsml.model.extended.Lems;

public class DimensionalAnalysis extends ASemanticPass {

	DimensionalAnalysis(Lems lems) throws Throwable {
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new DimensionalAnalysisVisitor(lems));
	}

}
