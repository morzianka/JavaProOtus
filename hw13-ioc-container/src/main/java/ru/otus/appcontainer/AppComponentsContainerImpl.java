package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(String pkg) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> configs = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        configs.stream()
                .sorted(comparingInt(cfg -> cfg.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        try {
            checkConfigClass(configClass);
            Object cfg = configClass.getDeclaredConstructor().newInstance();
            stream(configClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted(comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                    .forEach(method -> {
                        Object component = invoke(cfg, method);
                        appComponents.add(component);
                        appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), component);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Configuration processing exception", e);
        }
    }

    private Object invoke(Object cfg, Method method) {
        return wrapException(() -> method.invoke(cfg, stream(method.getParameterTypes())
                .map(this::getAppComponent).toArray()));
    }

    private Object wrapException(Callable<Object> action) {
        try {
            return action.call();
        } catch (Exception e) {
            throw new RuntimeException("Configuration processing exception", e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(componentClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("No instance of %s found", componentClass)));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException(String.format("No instance with name %s found", componentName)));
    }
}
