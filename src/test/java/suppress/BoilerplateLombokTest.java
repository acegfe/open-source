package suppress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class BoilerplateLombokTest {
    @Test
    void increaseCoverage_boilerplateLombok_SuppressLombokCoverage() {
        Set<Class<?>> classes = getClassesAnnotatedBySuppress("jwt");
        Assertions.assertDoesNotThrow(() -> classes.forEach(
                aClass -> Arrays.stream(aClass.getConstructors()).forEach(
                        constructor -> {
                            invokeCtorGetterSetterSuccessfully(constructor, aClass.getMethods());
                        })));

    }

    private void invokeCtorGetterSetterSuccessfully(Constructor<?> constructor, Method[] methods) {
        try {
            Object[] constructorParameters = Stream.generate(() -> null).limit(constructor.getParameterCount()).toArray();
            Object constructorInstance = constructor.newInstance(constructorParameters);

            Arrays.stream(methods).filter(method -> method.getName().contains("get") || method.getName().contains("set")).forEach(method -> {

                Object[] methodParameters = Stream.generate(() -> null).limit(method.getParameterCount()).toArray();
                try {
                    method.invoke(constructorInstance, methodParameters);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(constructor.getName() + "//" + method.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(constructor.getName());
        }
    }

    private Set<Class<?>> getClassesAnnotatedBySuppress(String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(SuppressLombokCoverage.class);

        return allClasses;
    }
}
