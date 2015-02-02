package extended;

import javax.measure.Unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.NONE)
public class Dimension extends org.lemsml.model.Dimension
{
	// TODO: notice that there is a discrepancy between what LEMS calls dimensions
	// and what UOM calls dimensions. We'll thus confusingly use Unit<?> here
	// to store a dimension...
	private javax.measure.Unit<?> dimension;

	public javax.measure.Unit<?> getDimension()
	{
		return dimension;
	}

	public void setDimension(Unit<?> unit)
	{
		this.dimension = unit;
	}

}
