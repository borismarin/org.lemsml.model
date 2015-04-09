package org.lemsml.model.compiler.utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashMapWarnOnOverwrite<K, V> extends HashMap<K, V> {
	private static final Logger logger = LoggerFactory
			.getLogger(HashMapWarnOnOverwrite.class);
	private static final long serialVersionUID = 1L;

	@Override
	public V put(K key, V value) {
		if (this.containsKey(key)) {
			logger.warn(String.format(
					"Overwriting symbol '%s'.\n\t -> old: %s\n\t -> new: %s",
					key, this.get(key), value));
		}
		super.put(key, value);
		return value;
	}

}
