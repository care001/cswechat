package org.easywechat.util;

public class Validator {

	public static boolean hasNull(Object... params) {
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				return true;
			}
		}
		return false;
	}
}
