package backend.academy.seminar14_example.di;

import backend.academy.seminar14_example.annotations.Autowired;
import backend.academy.seminar14_example.annotations.Component;
import backend.academy.seminar14_example.services.commands.Command;
import backend.academy.seminar14_example.services.commands.CommandDefinition;
import backend.academy.seminar14_example.services.commands.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class InjectionFactory {
    private final String basePackage;
    private final Logger logger = LoggerFactory.getLogger(InjectionFactory.class);
    private final Map<String, Object> singletonBeans = new ConcurrentHashMap<>();

    public InjectionFactory(String basePackage) {
        this.basePackage = basePackage;
    }

    public void instantiateComponents() throws IOException, URISyntaxException {
        logger.trace("============ Instantiation started ============");
        // В Java нет механизма скана пакетов из коробки, поэтому пишем свой;
        scanPackageAndApplyAction(this::loadBean);
        logger.trace("Instantiated BEANS={}", singletonBeans);
        logger.trace("============ Instantiation finished ============");
    }

    public void initializeComponents() throws IllegalAccessException {
        logger.trace("============ Initialization started ============");
        for (Map.Entry<String, Object> e : singletonBeans.entrySet()) {
            initialize(e.getValue());
        }
        logger.trace("============ Initialization finished ============");
    }

    // Публичный для упрощения начального запуска
    public void initialize(Object component) throws IllegalAccessException {
        Class<?> clazz = component.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                logger.trace(
                    "{} - Found autowired field: {}. Looking for suitable beans...",
                    clazz.getSimpleName(),
                    field.getName()
                );
                Class<?> fieldClass = field.getType();
                for (Map.Entry<String, Object> beanEntry : singletonBeans.entrySet()) {
                    Object bean = beanEntry.getValue();
                    if (fieldClass.isAssignableFrom(bean.getClass())) {
                        logger.trace("Bean '{}' is suitable to inject.", beanEntry.getKey());
                        if (field.trySetAccessible()) {
                            field.set(component, bean);
                        } else {
                            logger.error("Couldn't inject the bean={} to field={} in the class={}!",
                                beanEntry.getKey(), field.getName(), clazz.getSimpleName());
                        }
                    }
                }
            }
        }
    }

    public void registerCommands() {
        try {
            scanPackageAndApplyAction(this::registerCommand);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void registerCommand(File classFile) {
        String fileName = classFile.getName();
        if (fileName.endsWith(".class")) {
            try {
                String className = getClassName(classFile);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(CommandDefinition.class)
                    && Command.class.isAssignableFrom(clazz)) {
                    Constructor<?> ctor = clazz.getConstructors()[0];
                    Command command = (Command) ctor.newInstance();
                    initialize(command);
                    // Тут некоторое упрощение, чтобы не закапываться совсем вглубь и не делать прям всё всё динамически
                    CommandHandler handler = (CommandHandler) singletonBeans.get(CommandHandler.class.getName());
                    handler.registerCommand(command);
                }
            } catch (Exception e) {
                logger.error("Unable to register the command!");
            }
        }
    }

    private String getClassName(File classFile) {
        String absolutePath = classFile.getPath();
        String packageStyledPath = absolutePath.replaceAll(FileSystems.getDefault().getSeparator(), ".");
        String withoutExtension = packageStyledPath.substring(0, packageStyledPath.lastIndexOf(".class"));
        int packageNameStartIndex = withoutExtension.indexOf(basePackage);
        String className = withoutExtension.substring(packageNameStartIndex); // Cut off absolute path part
        return className;
    }

    private void scanPackageAndApplyAction(Consumer<File> consumer)
        throws IOException, URISyntaxException {
        logger.debug("Scanning package '{}'", basePackage);
        String path = packageToPath(basePackage);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        Iterator<URL> resourcesIterator = resources.asIterator();
        while (resourcesIterator.hasNext()) {
            var next = resourcesIterator.next();
            File resource = new File(next.toURI());
            if (resource.isDirectory()) {
                scanDirectoryAndApplyAction(resource, consumer);
            } else {
                consumer.accept(resource);
            }
        }
    }

    private void scanDirectoryAndApplyAction(File parentDirectory, Consumer<File> action) {
        File[] files = parentDirectory.listFiles();
        if (files == null) {
            return;
        }
        for (File classFile : files) {
            if (classFile.isDirectory()) {
                scanDirectoryAndApplyAction(classFile, action);
            } else {
                action.accept(classFile);
            }
        }
    }

    private void loadBean(File classFile) {
        try {
            String fileName = classFile.getName();
            if (fileName.endsWith(".class")) {
                String className = getClassName(classFile);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)) {
                    createBean(className, clazz);
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            logger.error("Error occurred during loading been!");
            logger.error(e.getMessage(), e);
        }
    }

    private void createBean(String beanName, Class<?> clazz)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        logger.debug("Creating bean: " + beanName);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        singletonBeans.put(beanName, instance);
    }

    private static String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", FileSystems.getDefault().getSeparator());
    }

    public <T> T getBean(Class<T> clazz) {
        for (Map.Entry<String, Object> e : singletonBeans.entrySet()) {
            if (clazz.isAssignableFrom(e.getValue().getClass())) {
                return (T) e.getValue();
            }
        }
        return null;
    }

}
