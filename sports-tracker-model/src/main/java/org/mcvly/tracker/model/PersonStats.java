package org.mcvly.tracker.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * TODO: get rid of java.util.Date when hibernate natively supports Java 8 Date API
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 11.02.14
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class PersonStats implements Serializable {

    private static final long serialVersionUID = -3600120141713936957L;

    private LocalDateTime measureDate;

    private Double weight;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "measureDate")
    private Date getMeasureDateForDB() {
        return measureDate != null ? Date.from(measureDate.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    private void setMeasureDateForDB(Date measureDate) {
        this.measureDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(measureDate.getTime()), ZoneId.systemDefault());
    }

    @Column(nullable = false, name = "weight")
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Transient
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
