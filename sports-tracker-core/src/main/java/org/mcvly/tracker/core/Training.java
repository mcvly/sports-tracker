package org.mcvly.tracker.core;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mcvly
 * @since 21.03.14
 */
@Entity
@Table(name = "training")
@Access(AccessType.PROPERTY)
public class Training implements Serializable {

    private static final long serialVersionUID = -4720839718136077131L;

    private Long id;

    private TrainingType type;

    private Person trainee;

    private LocalDateTime trainingStart;

    private LocalDateTime trainingStop;

    private List<Exercise> exercises = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "type_id")
    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "person_id")
    public Person getTrainee() {
        return trainee;
    }

    public void setTrainee(Person trainee) {
        this.trainee = trainee;
    }

    @Column(name = "training_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date getTrainingStartDB() {
        return trainingStart != null ? Date.from(trainingStart.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    private void setTrainingStartDB(Date trainingStart) {
        this.trainingStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(trainingStart.getTime()), ZoneId.systemDefault());
    }

    @Column(name = "training_stop")
    @Temporal(TemporalType.TIMESTAMP)
    private Date getTrainingStopDB() {
        return trainingStop != null ? Date.from(trainingStop.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    private void setTrainingStopDB(Date trainingStop) {
        this.trainingStop = LocalDateTime.ofInstant(Instant.ofEpochMilli(trainingStop.getTime()), ZoneId.systemDefault());
    }

    @Transient
    public LocalDateTime getTrainingStart() {
        return trainingStart;
    }

    public void setTrainingStart(LocalDateTime trainingStart) {
        this.trainingStart = trainingStart;
    }

    @Transient
    public LocalDateTime getTrainingStop() {
        return trainingStop;
    }

    public void setTrainingStop(LocalDateTime trainingStop) {
        this.trainingStop = trainingStop;
    }

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        for (Exercise exercise : exercises) {
            exercise.setTraining(this);
        }
    }

    public void addExercise(Exercise exercise) {
        if (!exercises.contains(exercise)) {
            this.exercises.add(exercise);
        }
        if (exercise.getTraining() != this) {
            exercise.setTraining(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Training training = (Training) o;

        if (exercises != null ? !exercises.equals(training.exercises) : training.exercises != null) return false;
        if (id != null ? !id.equals(training.id) : training.id != null) return false;
        if (trainee != null ? !trainee.equals(training.trainee) : training.trainee != null) return false;
        if (trainingStart != null ? !trainingStart.equals(training.trainingStart) : training.trainingStart != null)
            return false;
        if (trainingStop != null ? !trainingStop.equals(training.trainingStop) : training.trainingStop != null)
            return false;
        if (type != training.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (trainee != null ? trainee.hashCode() : 0);
        result = 31 * result + (trainingStart != null ? trainingStart.hashCode() : 0);
        result = 31 * result + (trainingStop != null ? trainingStop.hashCode() : 0);
        result = 31 * result + (exercises != null ? exercises.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", type=" + type +
                ", trainee=" + trainee.getName() +
                ", trainingStart=" + trainingStart +
                ", trainingStop=" + trainingStop +
                ", exercises=" + exercises +
                '}';
    }
}
