package ru.java.pro;

import org.apache.commons.lang3.ClassUtils;
import ru.java.pro.annotation.After;
import ru.java.pro.annotation.Before;
import ru.java.pro.annotation.Test;
import ru.java.pro.exception.TestFailedException;
import ru.java.pro.util.ReflectionHelper;
import ru.java.pro.util.Triple;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

/**
 * @author Shabunina Anita
 */
public class TestRunner {

    private static final Logger LOGGER = Logger.getLogger(TestRunner.class.getName());
    private static final String JAVA_TEST = "Test.java";

    public static void main(String[] args) {
        findTestClasses().forEach(name -> runTest(getClass(name)));
    }

    private static Set<String> findTestClasses() {
        String path = new File("hw03-annotations/src/main/java/ru/java/pro/test").getAbsolutePath();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(JAVA_TEST))
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOGGER.log(SEVERE, "Couldn't find test directory with path " + path, e);
        }

        return Collections.emptySet();
    }

    private static Class<?> getClass(String name) {
        try {
            String packageName = ClassUtils.getPackageName(name);
            String className = packageName
                    .replaceFirst(".+/src/main/java/", "")
                    .replace('/', '.');
            return ClassUtils.getClass(className);
        } catch (ClassNotFoundException e) {
            LOGGER.log(SEVERE, "Couldn't get class for name " + name, e);
        }

        return null;
    }

    private static void runTest(Class<?> clazz) {
        Method before = null;
        Set<Method> testMethods = new HashSet<>();
        Method after = null;
        //refactor
        try {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(method);
                } else if (method.isAnnotationPresent(Before.class)) {
                    before = method;
                } else if (method.isAnnotationPresent(After.class)) {
                    after = method;
                }
            }
            int counter = 0;
            for (Method test : testMethods) {
                try {
                    run(clazz, new Triple<>(before, test, after));
                } catch (TestFailedException e) {
                    counter++;
                }
            }
            printResult(testMethods.size(), counter);
        } catch (Exception e) {
            LOGGER.log(SEVERE, "Couldn't run tests", e);
        }
    }

    private static void run(Class<?> clazz, Triple<Method> methods) {
        Object instant = ReflectionHelper.instantiate(clazz);
        methods.getList().forEach(it -> run(instant, it));
    }

    private static void run(Object instant, Method method) {
        if (method != null) {
            try {
                ReflectionHelper.callMethod(instant, method.getName());
            } catch (Exception e) {
                throw new TestFailedException();
            }
        }
    }

    private static void printResult(int all, int fail) {
        System.out.println("Tests count: " + all);
        System.out.println("Tests passed: " + (all - fail));
        System.out.println("Tests failed: " + fail);
    }
}