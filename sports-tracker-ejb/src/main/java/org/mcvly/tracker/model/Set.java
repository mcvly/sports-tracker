package org.mcvly.tracker.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.Duration;

/**
 * @author mcvly
 * @since 21.03.14
 */
@Embeddable
public class Set implements Serializable {

    private static final long serialVersionUID = -7657444066197510292L;

    private transient Duration duration; //TODO: migrate to jpa annotation when it appears

    @Column(name = "duration")
    private String durationString;

    private Float result;

    private Integer reps;

    private String note;

    public Duration getDuration() {
        return duration;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
        this.duration = Duration.parse(durationString);
    }

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Set set = (Set) o;

        if (duration != null ? !duration.equals(set.duration) : set.duration != null) return false;
        if (durationString != null ? !durationString.equals(set.durationString) : set.durationString != null)
            return false;
        if (note != null ? !note.equals(set.note) : set.note != null) return false;
        if (reps != null ? !reps.equals(set.reps) : set.reps != null) return false;
        if (result != null ? !result.equals(set.result) : set.result != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = duration != null ? duration.hashCode() : 0;
        result1 = 31 * result1 + (durationString != null ? durationString.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (reps != null ? reps.hashCode() : 0);
        result1 = 31 * result1 + (note != null ? note.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "Set{" +
                "duration=" + duration +
                ", result=" + result +
                ", reps=" + reps +
                ", note='" + note + '\'' +
                '}';
    }
}
