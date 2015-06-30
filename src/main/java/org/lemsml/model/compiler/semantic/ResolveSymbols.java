//package org.lemsml.model.compiler.semantic;
//
//import org.lemsml.model.compiler.semantic.visitors.SymbolVisitor;
//import org.lemsml.model.compiler.semantic.visitors.ScopeTraverser;
//import org.lemsml.model.extended.Lems;
//import org.lemsml.model.extended.Scope;
//
//public class ResolveSymbols extends ASemanticPass {
//
//	ResolveSymbols(Lems lems) throws Throwable {
//		super.setLems(lems);
//		super.setTraverser(new ScopeTraverser<Throwable>());
//		super.setVisitor(new SymbolVisitor(lems));
//	}
//
//	@Override
//	protected void apply() throws Throwable {
//		super.apply();
//		//finalise construction...
//		SymbolVisitor visitor = (SymbolVisitor) super.getVisitor();
//		Scope scope = (Scope) visitor.getContext().getScope();
//		scope.evalInterdependentExprs();
//	}
//
//}
