import java.util.HashMap;

class Main {
  public static void main(String[] args) {
    Gym gym = new Gym(10, new HashMap<>() {
      {
        put(MachineType.LEGPRESSMACHINE, 2);
        put(MachineType.BARBELL, 2);
        put(MachineType.SQUATMACHINE, 2);
        put(MachineType.LEGEXTENSIONMACHINE, 2);
        put(MachineType.LEGCURLMACHINE, 2);
        put(MachineType.LATPULLDOWNMACHINE, 2);
        put(MachineType.CABLECROSSOVERMACHINE, 2);
        put(MachineType.PECDECKMACHINE, 2);
      }
    });

    gym.openForTheDay();
  }
}