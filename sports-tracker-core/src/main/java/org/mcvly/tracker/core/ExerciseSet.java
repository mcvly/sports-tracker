package org.mcvly.tracker.core;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.Duration;

/**
 * @author mcvly
 * @since 21.03.14
 */
@Embeddable
public class ExerciseSet implements Serializable {

    private static final long serialVersionUID = -7657444066197510292L;

    @Column(name = "duration")
    private String duration;

    private Double result;

    private Integer reps;

    private String note;

    public Duration getDuration() {
        return Duration.parse(duration);
    }

    public void setDuration(String durationString) {
        this.duration = durationString;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
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

        ExerciseSet exerciseSet = (ExerciseSet) o;

        if (duration != null ? !duration.equals(exerciseSet.duration) : exerciseSet.duration != null) return false;
        if (duration != null ? !duration.equals(exerciseSet.duration) : exerciseSet.duration != null)
            return false;
        if (note != null ? !note.equals(exerciseSet.note) : exerciseSet.note != null) return false;
        if (reps != null ? !reps.equals(exerciseSet.reps) : exerciseSet.reps != null) return false;
        if (result != null ? !result.equals(exerciseSet.result) : exerciseSet.result != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = duration != null ? duration.hashCode() : 0;
        result1 = 31 * result1 + (duration != null ? duration.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (reps != null ? reps.hashCode() : 0);
        result1 = 31 * result1 + (note != null ? note.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "ExerciseSet{" +
                "duration=" + duration +
                ", result=" + result +
                ", reps=" + reps +
                ", note='" + note + '\'' +
                '}';
    }
}
