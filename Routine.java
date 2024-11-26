import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Routine {
    private List<Exercise> routine;
    public int currentWorkout;
    public int totalWorkouts;
    public boolean finished;

    public Routine() {
        this.routine = generateRoutine();
        this.currentWorkout = 1;
        this.totalWorkouts = this.routine.size();
        this.finished = false;
    }

    public List<Exercise> getRoutine() {
        return this.routine;
    }
    public List<Exercise> generateRoutine() {
        return IntStream.range(0, 6).mapToObj(_i -> new Exercise()).collect(Collectors.toList());
    }

    public void finishWorkout() {
        this.currentWorkout++;
        if (this.currentWorkout > this.totalWorkouts) {
            this.finished = true;
        }
        
    }

    @Override
    public String toString() {
        StringBuilder workoutRoutine = new StringBuilder();
        int finishedWorkouts = 1;
        for (Exercise exercise : this.routine) {
            if (finishedWorkouts >= this.currentWorkout) {
                workoutRoutine.append("\n " + exercise);
            }
            finishedWorkouts++;
        }
        return workoutRoutine.toString();
    }

}
