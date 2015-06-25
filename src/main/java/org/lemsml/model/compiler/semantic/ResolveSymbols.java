package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.SymbolVisitor;
import org.lemsml.model.compiler.semantic.visitors.ScopeTraverser;
import org.lemsml.model.extended.Lems;

public class ResolveSymbols extends ASemanticPass {

	ResolveSymbols(Lems lems) throws Throwable {
		super.setLems(lems);
		super.setTraverser(new ScopeTraverser<Throwable>());
		super.setVisitor(new SymbolVisitor(lems));
	}

	@Override
	protected void apply() throws Throwable {
		super.apply();
		((SymbolVisitor) super.getVisitor()).evalInterdependentExprs();
	}

}
