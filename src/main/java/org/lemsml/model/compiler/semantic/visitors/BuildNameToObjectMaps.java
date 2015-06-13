package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.visitors.BaseVisitor;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author borismarin
 *
 */ public class BuildNameToObjectMaps extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(BuildNameToObjectMaps.class);

	/**
	 * @param lems
	 * @throws Throwable 
	 */
	public BuildNameToObjectMaps(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(Component c) throws Throwable {
		logRegistration(c.getId(), c);
		Component old = this.lems.registerComponentId(c.getId(), c);
		if(null != old){
			warnMapOverwrite(c.getId(), old, c);
		};
		return true;
	}

	@Override
	public Boolean visit(ComponentType ct) throws Throwable {
		logRegistration(ct.getName(), ct);
		ComponentType old = this.lems.registerComponentTypeName(ct.getName(), ct);
		if(null != old){
			warnMapOverwrite(ct.getName(), old, ct);
		};
		return true;
	}

//	@Override
//	public Boolean visit(Constant ctt) throws Throwable {
//		logRegistration(ctt.getName(), ctt);
//		Constant old = this.lems.registerConstantName(ctt.getName(), ctt);
//		if(null != old){
//			warnMapOverwrite(ctt.getName(), old, ctt);
//		};
//		return true;
//	}

	private void logRegistration(String identifier, LemsNode n) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Registering [%s '%s'] ", n.getClass()
				.getSimpleName(), identifier));
		sb.append(String.format("defined in " + n.getDefinedIn().getName()));
		logger.debug(sb.toString());
	}

	private <K, V extends LemsNode> void warnMapOverwrite(K key, V oldval, V newval) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Overwriting symbol '%s'!\n", key));
		sb.append(String.format("\t -> old: [defined in %s]: %s\n", oldval.getDefinedIn().getName(), oldval));
		sb.append(String.format("\t -> new: [defined in %s]: %s\n", newval.getDefinedIn().getName(), newval));
		logger.warn(sb.toString());
	}

}
