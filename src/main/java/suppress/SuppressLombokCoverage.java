package suppress;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: David Fernández Esteban
 * @c
 * Attach this to any class that you want to get covered, countering the lombok boilerplate on tests
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SuppressLombokCoverage {
}
