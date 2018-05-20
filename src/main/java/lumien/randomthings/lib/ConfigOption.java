package lumien.randomthings.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
public @interface ConfigOption
{
	String name();

	String category();

	String comment() default "";
}
