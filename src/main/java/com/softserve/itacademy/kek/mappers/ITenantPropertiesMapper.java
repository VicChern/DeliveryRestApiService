package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.models.impl.TenantProperties;

/**
 * Interface for {@link TenantProperties} mapping
 */
@Mapper(uses = IPropertyTypeMapper.class)
public interface ITenantPropertiesMapper {

    ITenantPropertiesMapper INSTANCE = Mappers.getMapper(ITenantPropertiesMapper.class);

    /**
     * Transform {@link ITenantProperties} to {@link TenantPropertiesDto}
     *
     * @param tenantProperties
     * @return tenantPropertiesDto
     */
    @Mapping(target = "propertyType", qualifiedByName = "toDto")
    TenantPropertiesDto toTenantPropertiesDto(ITenantProperties tenantProperties);

    /**
     * Transform {@link ITenantProperties} to {@link TenantProperties}
     *
     * @param tenantProperties
     * @return tenantProperties
     */
    @Mapping(target = "propertyType", ignore = true)
    TenantProperties toTenantProperties(ITenantProperties tenantProperties);
}
