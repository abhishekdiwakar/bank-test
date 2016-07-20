package com.unibet.worktest.bank.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>AbstractMapper</code> is an abstract mapper that provides methods to map the one object or its list to its another object or its list.
 *
 * @param <F> MAP FROM : which needs to be mapped.
 * @param <T> MAP TO : to which it should be mapped
 * 
 * @author abhishek
 * @version 1.0
 */
public abstract class AbstractMapper<F, T> {
	public abstract T map(F from);

	public List<T> map(List<F> fromList) {
		if (fromList == null) {
			throw new RuntimeException();
		}
		List<T> result = new ArrayList<T>();
		for (F from : fromList) {
			result.add(map(from));
		}
		return result;
	}
}
