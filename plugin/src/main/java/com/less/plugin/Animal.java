package com.less.plugin;

public interface Animal {
	public interface Callback {
		void done(String message);
	}
	void say(Callback callback);
}
