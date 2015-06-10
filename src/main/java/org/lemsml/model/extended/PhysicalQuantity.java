package org.lemsml.model.extended;

import static tec.units.ri.AbstractUnit.ONE;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.Unit;

import org.lemsml.model.compiler.IDimensionalEvaluable;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;

import tec.units.ri.quantity.NumberQuantity;

/**
 * @author borismarin
 *
 */
public class PhysicalQuantity implements IDimensionalEvaluable
{

	private Double value;
	private Unit<?> unit;
	private String unitSymbol;

	public PhysicalQuantity(Double value, String unitSymbol)
	{
		this.value = value;
		this.unitSymbol = unitSymbol;
	}

	public PhysicalQuantity(String value) throws LEMSCompilerException
	{
		String regExp = "\\s*([0-9-]*\\.?[0-9]*[eE]?[-+]?[0-9]+)?\\s*(\\w*)";

		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(value);

		if(matcher.find())
		{
			this.unitSymbol = matcher.group(2);
			this.value = Double.parseDouble(matcher.group(1));
		}
		else {
			throw new LEMSCompilerException("Could not parse ", LEMSCompilerError.CantParseValueUnit);
		}
	}

	@Override
	public Unit<?> getUnit()
	{
		return unit;
	}

	public void setUnit(Unit<?> unit)
	{
		if(null != unit)
		{
			this.unit = unit;
		}
		else
		{
			this.unit = ONE;
		}
	}

	@Override
	public Double evaluate(Map<String, Double> context)
	{
		return this.value;
	}

	public String getUnitSymbol()
	{
		return this.unitSymbol;
	}

	public void setUnitSymbol(String symb)
	{
		this.unitSymbol = symb;
	}

	@Override
	public String toString()
	{
		return "PhysicalQuantity [value=" + value + ", unit=" + unitSymbol + "]";
	}

	@Override
	public Double evaluateSI()
	{
		return new Double(NumberQuantity.of(value, unit).toSI().getValue().doubleValue());
	}

	public void setValue(double val)
	{
		this.value = val;
	}

}
