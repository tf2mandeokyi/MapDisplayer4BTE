package com.mndk.bteterrarenderer.gui.components;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Number option class for {@link GuiNumberInput}
 * @param <T> Number type, such as Integer, Float or Double
 */
public class NumberOption<T extends Number> {

	public Supplier<T> getter;
	public Consumer<T> setter;
	protected T min, max;
	public String name;

	public NumberOption(Supplier<T> getter, Consumer<T> setter, T from, T to, String name) {
		this.getter = getter; this.setter = setter;
		this.name = name;
		this.min = from; this.max = to;
	}

	public T getMin() { return this.min; }
	public T getMax() { return this.max; }

	public T get() { return getter.get(); }
	public void set(T value) { setter.accept(value); }
}
