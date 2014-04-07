package org.mcvly.tracker.core;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mcvly
 * @since 21.03.14
 */
@Entity
@Table(name = "training")
public class Training implements Serializable {

    private static final long serialVersionUID = -4720839718136077131L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TrainingType type;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person trainee;

    @Column(name = "training_start")
//    @Type(type="org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime trainingStart;

    @Column(name = "training_stop")
//    @Type(type="org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime trainingStop;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private List<Exercise> exercises = new ArrayList<>();

    public Training() {}

    public Training(TrainingType type, LocalDateTime trainingStart, LocalDateTime trainingStop) {
        this.type = type;
        this.trainingStart = trainingStart;
        this.trainingStop = trainingStop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public Person getTrainee() {
        return trainee;
    }

    public void setTrainee(Person trainee) {
        this.trainee = trainee;
    }

    public LocalDateTime getTrainingStart() {
        return trainingStart;
    }

    public void setTrainingStart(LocalDateTime trainingStart) {
        this.trainingStart = trainingStart;
    }

    public LocalDateTime getTrainingStop() {
        return trainingStop;
    }

    public void setTrainingStop(LocalDateTime trainingStop) {
        this.trainingStop = trainingStop;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
//        for (Exercise exercise : exercises) {
//            exercise.setTraining(this);
//        }
        this.exercises = exercises;
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
                ", trainee=" + (trainee != null ? trainee.getName() : null) +
                ", trainingStart=" + trainingStart +
                ", trainingStop=" + trainingStop +
                ", exercises=" + exercises +
                '}';
    }
}
