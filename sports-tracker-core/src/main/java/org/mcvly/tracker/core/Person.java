package org.mcvly.tracker.core;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: get rid of type converter when hibernate natively supports Java 8 Date API
 * @author mcvly
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

    @Column(nullable = false, length = 64)
    private String name;

    @Column(name="birth")
//    @Temporal(TemporalType.DATE)
//    @Type(type="org.jadira.usertype.dateandtime.threeten.PersistentLocalDate")
    private LocalDate birth;

    @Basic
    private Integer height;

    @ElementCollection// one-to-many with embeddable
    @CollectionTable(name = "person_stats", joinColumns = @JoinColumn(name = "person_id"))
    private List<PersonStats> stats = new ArrayList<>();

    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings = new ArrayList<>();

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

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
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

    public void addStats(PersonStats stat) {
        this.stats.add(stat);
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void addTraining(Training training) {
        this.trainings.add(training);
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", height=" + height +
                '}';
    }
}
