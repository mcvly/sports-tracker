package org.mcvly.tracker.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author mcvly
 * @since 21.03.14
 */
@Entity
@Table(name = "activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 6732488835292095579L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TrainingType type;

    @ManyToOne
    @JoinColumn(name = "sub_type_id")
    private TrainingSubType subType;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 2048)
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public TrainingSubType getSubType() {
        return subType;
    }

    public void setSubType(TrainingSubType subType) {
        this.subType = subType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (description != null ? !description.equals(activity.description) : activity.description != null)
            return false;
        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;
        if (name != null ? !name.equals(activity.name) : activity.name != null) return false;
        if (subType != null ? !subType.equals(activity.subType) : activity.subType != null) return false;
        if (type != activity.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (subType != null ? subType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", type=" + type +
                ", subType=" + subType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
