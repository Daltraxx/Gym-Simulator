public class Member {
  private final int id;
  private Routine routine;
  public Gym gym;

  public Member(int id, Gym gym) {
      this.id = id;
      this.gym = gym;
      this.routine = new Routine();
  }

  public int getId() {
      return this.id;
  }

  public void performRoutine() throws InterruptedException {

    routine.getRoutine().forEach(exercise -> {
      System.out.println("Gym member " + this.getId() 
        + " has the following left in their routine:\n" 
        + this.routine 
        + "----------------------------------------------------\n");
      
      MachineType currentMachine = exercise.getMachine();

      try {
        synchronized (gym) {
          while (gym.checkIfMachineAvailable(currentMachine) == false || gym.checkIfWeightsAvailable(exercise) == false) {
            
            if (gym.checkIfMachineAvailable(currentMachine) == false) {
              System.out.println("Gym Member " + this.getId() + " is waiting for " + currentMachine.machineName + "\n");
            }
            
            if (gym.checkIfWeightsAvailable(exercise) == false) {
              System.out.println("Gym Member " + this.getId() + " is waiting for weights to become available\n");
            }

            gym.wait();
          }
          gym.useMachine(currentMachine);
          gym.useWeights(exercise);
          System.out.println("Gym Member " + this.getId() + " performing exercise: " + exercise + " on " + currentMachine.machineName + "\n");
        }
        try {
            Thread.sleep(exercise.getDuration());
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        synchronized (gym) {
          System.out.println(currentMachine.machineName + " is now available to use.\n");
          gym.vacateMachine(currentMachine);
          gym.finishWeights(exercise);
          gym.notifyAll(); 
        }
      } catch (InterruptedException e) {
        System.out.println(e);
        e.printStackTrace();
      }

      this.routine.finishWorkout();
      if (this.routine.finished == true) {
        System.out.println("Gym member " + this.getId() + " has finished their routine!\n");
      }
    });
}
}