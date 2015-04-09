package org.lemsml.model.compiler.utils;

import java.util.HashMap;

import org.lemsml.model.extended.LemsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashMapWarnOnOverwrite<K, V extends LemsNode> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(HashMapWarnOnOverwrite.class);

	@Override
	public V put(K key, V value) {
		if (this.containsKey(key)) {
			V oldval = this.get(key);
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Overwriting symbol '%s'!\n", key));
			sb.append(String.format("\t -> old: [defined in %s]: %s\n", oldval.getDefinedIn().getName(), oldval));
			sb.append(String.format("\t -> new: [defined in %s]: %s\n", value.getDefinedIn().getName(), value));
			logger.warn(sb.toString());
		}
		super.put(key, value);
		return value;
	}

}
