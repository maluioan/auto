package com.home.automation.usersserver.converters.impl;

import com.home.automation.users.dto.UserData;
import com.home.automation.usersserver.converters.UserConverter;
import com.home.automation.usersserver.domain.UserModel;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DefaultUserConverter extends ConfigurableMapper implements UserConverter {

    private static List<String> excludedFields = Collections.synchronizedList(Arrays.asList("password", "contactData.user", "dateCreated", "dateModified"));
    private static Map<String, String> mappedFields = Collections.synchronizedMap(new HashMap<>());
    static {
        mappedFields.put("roleData", "roleModels");
        mappedFields.put("contactData", "contactModel");
    }

    @Override
    protected void configure(MapperFactory factory) {
        final ClassMapBuilder<UserData, UserModel> classMap = factory.classMap(UserData.class, UserModel.class);
        excludedFields.stream().forEach(classMap::exclude);
        mappedFields.entrySet().forEach(entry -> classMap.field(entry.getKey(), entry.getValue()));
        classMap.byDefault().register();
    }

    @Override
    public UserData convertToData(UserModel model) {
        return super.dedicatedMapperFor(UserModel.class, UserData.class).map(model);
    }

    @Override
    public UserModel convertToModel(UserData data) {
        return super.dedicatedMapperFor(UserData.class, UserModel.class).map(data);
    }
}
