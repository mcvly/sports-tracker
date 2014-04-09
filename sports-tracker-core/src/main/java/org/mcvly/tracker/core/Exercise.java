package org.mcvly.tracker.core;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mcvly
 * @since 21.03.14
 */
@Entity
@Table(name = "exercise")
public class Exercise implements Serializable {

    private static final long serialVersionUID = 563481250048474007L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ElementCollection(fetch = FetchType.EAGER) // one-to-many with embeddable
    @CollectionTable(name = "exercise_set", joinColumns = @JoinColumn(name = "exercise_id"))
    private List<ExerciseSet> exerciseSets = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<ExerciseSet> getExerciseSets() {
        return exerciseSets;
    }

    public void setExerciseSets(List<ExerciseSet> exerciseSets) {
        this.exerciseSets = exerciseSets;
    }

    public void addSet(ExerciseSet exerciseSet) {
        this.exerciseSets.add(exerciseSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercise exercise = (Exercise) o;

        if (activity != null ? !activity.equals(exercise.activity) : exercise.activity != null) return false;
        if (exerciseSets != null ? !exerciseSets.equals(exercise.exerciseSets) : exercise.exerciseSets != null) return false;
        if (training != null && training.getExercises() != null ? !training.getId().equals(exercise.training.getId()) : exercise.training != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = training != null ? training.hashCode() : 0;
        result = 31 * result + (activity != null ? activity.hashCode() : 0);
        result = 31 * result + (exerciseSets != null ? exerciseSets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", activity=" + activity +
                ", exerciseSets=" + exerciseSets +
                ", trainingId=" + training.getId() +
                '}';
    }
}
