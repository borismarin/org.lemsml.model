package org.lemsml.model.compiler.semantic;

import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class LEMSSemanticAnalyser {

	private Lems lems;
	private BuildNameToObjMaps mapBuilder;
	private ResolveUnitsDimensions dimensionResolver;
	private DecorateWithDimensions dimensionDecorator;
	private DecorateComponentsWithType typeDecorator;
	private BuildStructure structureBuilder;
	private ExtendTypes typeExtender;
	private AddFamilyToComponents familyAdder;
	private BuildScope scopeBuilder;
	private CheckExpressionDimensions exprDimChecker;

	public LEMSSemanticAnalyser(Lems lems) throws Throwable {
		super();
		this.lems = lems;

		mapBuilder = new BuildNameToObjMaps(lems);
		dimensionResolver  = new ResolveUnitsDimensions(lems);
		dimensionDecorator  = new DecorateWithDimensions(lems);
		typeDecorator  = new DecorateComponentsWithType(lems);
		setTypeExtender(new ExtendTypes(lems));
		familyAdder = new AddFamilyToComponents(lems);
		structureBuilder = new BuildStructure(lems);
		scopeBuilder = new BuildScope(lems);
		exprDimChecker = new CheckExpressionDimensions(lems);
	}

	public Lems analyse() throws Throwable {

		mapBuilder.apply();
		dimensionResolver.apply();
		dimensionDecorator.apply();
		typeDecorator.apply();
		getTypeExtender().apply();
		familyAdder.apply();
		structureBuilder.apply();
		scopeBuilder.apply();
		exprDimChecker.apply();

		return lems;

	}

	public ExtendTypes getTypeExtender() {
		return typeExtender;
	}

	public void setTypeExtender(ExtendTypes typeExtender) {
		this.typeExtender = typeExtender;
	}


}
