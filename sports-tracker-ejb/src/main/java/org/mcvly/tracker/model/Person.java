package org.mcvly.tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 11.02.14
 */
@Entity
@Table(name = "person")
@XmlRootElement
public class Person implements Serializable {

    private static final long serialVersionUID = -5008361276815324241L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date birth;

    private Integer height;

    @ElementCollection // one-to-many with embeddable
    @CollectionTable(name = "person_stats", joinColumns = @JoinColumn(name = "person_id"))
    private List<PersonStats> stats = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = new Date(birth.getTime());
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<PersonStats> getStats() {
        return stats;
    }

    public void setStats(List<PersonStats> stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name='" + name + '\'' + ", birth=" + birth + ", height=" + height + ", stats=" + stats + '}';
    }
}
