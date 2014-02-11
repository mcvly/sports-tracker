package org.mcvly.tracker.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

/**
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 11.02.14
 */
@Embeddable
public class PersonStats implements Serializable {

    private static final long serialVersionUID = -3600120141713936957L;

    private Date measureDate;

    private Double weight;

    public Date getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(Date measureDate) {
        this.measureDate = new Date(measureDate.getTime());
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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
