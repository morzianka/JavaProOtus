package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sql = entitySQLMetaData.getSelectByIdSql();
        Function<ResultSet, T> rsHandler = rs -> {
            try {
                if (rs.next()) {
                    T instance = createNewInstance();
                    return fillInstance(instance, rs);
                }
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
            return null;
        };
        return dbExecutor.executeSelect(connection, sql, List.of(id), rsHandler);
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();
        Function<ResultSet, List<T>> rsHandler = rs -> {
            try {
                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    T instance = createNewInstance();
                    list.add(fillInstance(instance, rs));
                }
                return list;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        };
        return dbExecutor.executeSelect(connection, sql, emptyList(), rsHandler).get();
    }

    @Override
    public long insert(Connection connection, T client) {
        String sql = entitySQLMetaData.getInsertSql();
        List<Object> params = getValues(entityClassMetaData.getFieldsWithoutId(), client);
        return dbExecutor.executeStatement(connection, sql, params);
    }

    @Override
    public void update(Connection connection, T client) {
        String sql = entitySQLMetaData.getUpdateSql();
        List<Object> params = getValues(entityClassMetaData.getFieldsWithoutId(), client);
        dbExecutor.executeStatement(connection, sql, params);
    }

    private T createNewInstance() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Optional<Constructor<T>> constructor = entityClassMetaData.getConstructor();
        if (constructor.isEmpty()) {
            throw new DataTemplateException("No constructor present");
        }
        return constructor.get().newInstance();
    }

    private T fillInstance(T instance, ResultSet rs) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Field field : entityClassMetaData.getAllFields()) {
            instance.getClass().getMethod("set" + capitalize(field.getName()), field.getType())
                    .invoke(instance, rs.getObject(field.getName()));
        }
        return instance;
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private List<Object> getValues(List<Field> fields, T client) {
        return fields.stream()
                .map(field -> {
                    try {
                        return client.getClass().getMethod("get" + capitalize(field.getName()))
                                .invoke(client);
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
