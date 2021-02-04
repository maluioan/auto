package com.home.automation.homeboard.converters.base;

import com.home.automation.homeboard.data.BaseData;
import com.home.automation.homeboard.domain.BaseModel;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

// TODO: move this in a common place
public abstract class AbstractBaseConverter<M extends BaseModel, D extends BaseData> implements BaseConverter<M, D> {

    private ConfigurableMapper configurableMapper;

    private List<String> excludedFields;
    private Map<String, String> mappedFields;
    private BidirectionalConverter fieldConverters;

    private final Class<M> modelClass;
    private final Class<D> dataClass;

    protected AbstractBaseConverter(Class<M> modelClass, Class<D> dataClass) {
        this.modelClass = modelClass;
        this.dataClass = dataClass;

        init();
    }

    protected void init() {
        configurableMapper = new ConfigurableMapper() {
            @Override
            protected void configure(MapperFactory factory) {
                configureInternal(factory);
            }
        };
    }

    private void configureInternal(MapperFactory factory) {
        final ClassMapBuilder<D, M> classMap = factory.classMap(dataClass, modelClass);
        CollectionUtils.emptyIfNull(excludedFields).stream().forEach(classMap::exclude);
        MapUtils.emptyIfNull(mappedFields).entrySet().forEach(entry ->
                classMap.field(entry.getKey(), entry.getValue()));

        // TODO: try to add bidirectional converter for field
        if (fieldConverters != null) {
            factory.getConverterFactory().registerConverter(fieldConverters);
        }

        classMap.byDefault().register();
        // TODO: create an configureInternal() to hook in confg process
    }

    @Override
    public D convertToData(M model) {
        return configurableMapper.dedicatedMapperFor(modelClass, dataClass).map(model);
    }

    @Override
    public M convertToModel(D data) {
        return configurableMapper.dedicatedMapperFor(dataClass, modelClass).map(data);
    }

    public void setExcludedFields(List<String> excludedFields) {
        this.excludedFields = excludedFields;
    }

    public void setMappedFields(Map<String, String> mappedFields) {
        this.mappedFields = mappedFields;
    }

    public void setConverter(BidirectionalConverter fieldBidirectionalConverters) {
        this.fieldConverters = fieldBidirectionalConverters;
    }
}
