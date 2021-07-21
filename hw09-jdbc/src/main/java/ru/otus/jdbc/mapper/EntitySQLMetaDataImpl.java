package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * @author Shabunina Anita
 */
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        String className = entityClassMetaData.getName().toLowerCase();
        return String.format("select * from %s;", className);
    }

    @Override
    public String getSelectByIdSql() {
        Optional<Field> field = entityClassMetaData.getIdField();
        if (field.isEmpty()) {
            throw new RuntimeException("No id field present");
        }
        String className = entityClassMetaData.getName().toLowerCase();
        return String.format("select * from %s where %s = ?;",
                className, field.get().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        String className = entityClassMetaData.getName().toLowerCase();
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        StringBuilder names = new StringBuilder();
        fields.forEach(field -> names.append(field.getName()).append(","));
        names.deleteCharAt(names.length() - 1);
        StringBuilder params = new StringBuilder();
        params.append("?,".repeat(fields.size()))
                .deleteCharAt(params.length() - 1);
        return String.format("insert into %s (%s) values (%s);", className, names.toString(), params.toString());
    }

    @Override
    public String getUpdateSql() {
        String className = entityClassMetaData.getName().toLowerCase();
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        StringBuilder sb = new StringBuilder();
        fields.forEach(field -> sb.append(field.getName()).append("=").append("?,"));
        sb.deleteCharAt(sb.length() - 1);
        return String.format("update %s set %s;", className, sb.toString());
    }
}
