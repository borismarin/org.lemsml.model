package extended;

import javax.measure.Unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType (XmlAccessType.NONE)
public class Dimension extends org.lemsml.model.Dimension{
	
	private Unit<?> dimension;

	public Unit<?> getDimension() {
		return dimension;
	}

	public void setDimension(Unit<?> dimension) {
		this.dimension = dimension;
	}
	

}
