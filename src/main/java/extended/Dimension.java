package extended;

import javax.measure.Unit;

public class Dimension extends org.lemsml.model.Dimension{
	private Unit<?> dimension;

	public Unit<?> getDimension() {
		return dimension;
	}

	public void setDimension(Unit<?> dimension) {
		this.dimension = dimension;
	}
	

}
