package org.lemsml.model.extended;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author borismarin
 *
 */
public class PhysicalQuantityAdapter extends
		XmlAdapter<String, PhysicalQuantity> {

	public PhysicalQuantity unmarshal(String value) {
		if (value == null) {
			return null;
		} else {
			String regExp = "\\s*([0-9-]*\\.?[0-9]*[eE]?[-+]?[0-9]+)?\\s*(\\w*)";
			PhysicalQuantity pq = new PhysicalQuantity();

			Pattern pattern = Pattern.compile(regExp);
			Matcher matcher = pattern.matcher(value);

			if (matcher.find()) {
				pq.setValue(Double.parseDouble(matcher.group(1)));
				pq.setUnitSymbol(matcher.group(2));
			}

			return pq;
		}
	}

	public String marshal(PhysicalQuantity pq) {
		return (pq.evaluate().toString() + pq.getUnitSymbol());
	}

}
