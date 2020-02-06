package com.softserve.itacademy.kek.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "obj_global_properties")
public class GlobalProperties implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_property")
    private Long idProperty;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_Property_Type")
    private PropertyType propertyType;

    @Size(min = 1, max = 256)
    @Column(name = "key", unique = true, nullable = false, length = 256)
    private String key;

    @Size(min = 1, max = 4096)
    @Column(name = "value", nullable = false, length = 4096)
    private String value;

    public Long getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(Long idProperty) {
        this.idProperty = idProperty;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalProperties that = (GlobalProperties) o;
        return Objects.equals(idProperty, that.idProperty) &&
                Objects.equals(propertyType, that.propertyType) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProperty, propertyType, key, value);
    }

    @Override
    public String toString() {
        return "GlobalProperties{" +
                "idProperty=" + idProperty +
                ", propertyType=" + propertyType +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
