package extended;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PhysicalQuantityAdapter extends XmlAdapter<String, PhysicalQuantity>
{

	public PhysicalQuantity unmarshal(String value)
	{
		if(value == null)
		{
			return null;
		}
		else
		{
			String regExp = "\\s*([0-9-]*\\.?[0-9]*[eE]?[-+]?[0-9]+)?\\s*(\\w*)";
			PhysicalQuantity pq = new PhysicalQuantity();

			Pattern pattern = Pattern.compile(regExp);
			Matcher matcher = pattern.matcher(value);

			if(matcher.find())
			{
				pq.setValue(Float.parseFloat(matcher.group(1)));
				pq.setUnit(matcher.group(2));
			}

			return pq;
		}
	}

	public String marshal(PhysicalQuantity pq)
	{
		return (pq.getValue().toString() + pq.getUnit());
	}

}
