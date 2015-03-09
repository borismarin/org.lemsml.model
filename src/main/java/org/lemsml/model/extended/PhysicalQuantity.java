package org.lemsml.model.extended;

/**
 * @author borismarin
 *
 */
public class PhysicalQuantity
{

	public Float value;
	public String unit;

	public Float getValue()
	{
		return value;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public void setValue(Float value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "PhysicalQuantity [value=" + value + ", unit=" + unit + "]";
	}

}
