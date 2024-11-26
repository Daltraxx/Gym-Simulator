import java.util.Map;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Gym {
  private final int totalGymMembers;
  private Map<MachineType, Integer> availableMachines;
  private Map<Weight, Integer> availableWeights;

  public Gym(int totalGymMembers, Map<MachineType, Integer> availableMachines) {
    this.totalGymMembers = totalGymMembers;
    this.availableMachines = availableMachines;
    this.availableWeights = Weight.numOfWeightsInGym;
  }

  public Map<MachineType, Integer> getAvailableMachines() {
    return this.availableMachines;
  }

  public synchronized boolean checkIfMachineAvailable(MachineType machine) {
    return this.availableMachines.get(machine) > 0;
  }

  public synchronized boolean checkIfWeightsAvailable(Exercise exercise) {
    for (Weight weight : exercise.getWeight().keySet()) {
      if (this.availableWeights.get(weight) < exercise.getWeight().get(weight)) {
        return false;
      }
    }
    return true;
  }

  public synchronized void useWeights(Exercise exercise) {
    for (Weight weight : exercise.getWeight().keySet()) {
      int weightsAvailable = this.availableWeights.get(weight);
      int weightsRequired = exercise.getWeight().get(weight);
      this.availableWeights.put(weight, weightsAvailable - weightsRequired);
    }
  }

  public synchronized void finishWeights(Exercise exercise) {
    for (Weight weight : exercise.getWeight().keySet()) {
      int weightsAvailable = this.availableWeights.get(weight);
      int weightsFinished = exercise.getWeight().get(weight);
      this.availableWeights.put(weight, weightsAvailable + weightsFinished);
    }
  }

  public synchronized void useMachine(MachineType machine) {
    int currentlyAvailable = availableMachines.get(machine) - 1;
    this.availableMachines.put(machine, currentlyAvailable);
  }

  public synchronized void vacateMachine(MachineType machine) {
    int currentlyAvailable = availableMachines.get(machine) + 1;
    this.availableMachines.put(machine, currentlyAvailable);
  }

  public void openForTheDay() {
    List<Thread> gymMembersRoutines;

    gymMembersRoutines = IntStream.rangeClosed(1, this.totalGymMembers)
    .mapToObj((id) -> {
      Member member = new Member(id, this);
      return new Thread(() -> {
        try {
          member.performRoutine();
        } catch (Exception e) {
          System.out.println(e);
        }
      });
    })
    .collect(Collectors.toList());

    Thread supervisor = createSupervisor(gymMembersRoutines);
    gymMembersRoutines.forEach(Thread::start);
    supervisor.start();
  }

  private Thread createSupervisor(List<Thread> threads) {
    Thread supervisor = new Thread(() -> {
      while (true) {
        List<String> runningThreads = threads.stream()
          .filter(Thread::isAlive)
          .map(Thread::getName)
          .collect(Collectors.toList());

        System.out.println(Thread.currentThread().getName() 
          + " - " + runningThreads.size() 
          + " members currently exercising: " 
          + runningThreads + "\n");
        
        if (runningThreads.isEmpty()) break;

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          System.out.println(e);
        }

        /* Alternate method for creating runningThreads
        * using lambda expressions rather than method referencing

        List<String> runningThreads = threads.stream()
          .filter((thread) -> thread.isAlive())
          .map((runningThread) -> runningThread.getName())
          .collect(Collectors.toList());
        */
      }

      System.out.println(Thread.currentThread().getName() + " - All members have finished exercising.");
    });

    supervisor.setName("Gym Staff");
    return supervisor;
  }

}