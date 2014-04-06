package org.mcvly.tracker.core;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * TODO: get rid of java.util.Date when hibernate natively supports Java 8 Date API
 * @author mcvly
 * @since 11.02.14
 */
@Embeddable
public class PersonStats implements Serializable {

    private static final long serialVersionUID = -3600120141713936957L;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "measure_date")
//    @Type(type="org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime measureDate;

    @Column(nullable = false, name = "weight")
    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDateTime getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(LocalDateTime measureDate) {
        this.measureDate = measureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonStats)) {
            return false;
        }

        PersonStats that = (PersonStats) o;

        if (measureDate != null ? !measureDate.equals(that.measureDate) : that.measureDate != null) {
            return false;
        }
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = measureDate != null ? measureDate.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonStats{" + "measureDate=" + measureDate + ", weight=" + weight + '}';
    }
}
