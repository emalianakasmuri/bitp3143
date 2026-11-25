package workedexample;

/**
 * Applies KitaHub's routing rules with selection and iteration.
 */
public class ParcelRules {

    /**
     * Chooses the hub for a destination state (Table 1.2).
     *
     * @param state destination state written on the parcel
     * @return the hub name, or "Out of area" when KitaHub does not serve the state
     */
    static String chooseHub(String state) {
        return switch (state) {
            case "Kuala Lumpur", "Selangor" -> "Kuala Lumpur hub";
            case "Melaka", "Negeri Sembilan" -> "Melaka hub";
            case "Johor" -> "Johor Bahru hub";
            default -> "Out of area";
        };
    }

    /**
     * Chooses the vehicle from the parcel weight.
     *
     * @param weightKg parcel weight in kilograms
     * @return "Van", "Lorry" or "Rejected"
     */
    static String chooseVehicle(double weightKg) {
        if (weightKg > 0 && weightKg <= 5) {
            return "Van";
        } else if (weightKg > 5 && weightKg <= 30) {
            return "Lorry";
        } else {
            return "Rejected";
        }
    }

    public static void main(String[] args) {
        String[] states = {"Melaka", "Johor", "Selangor", "Kedah"};
        double[] weights = {2.5, 18.0, 31.0, 4.0};

        // Iteration: apply the same two decisions to every parcel
        for (int i = 0; i < states.length; i++) {
            System.out.printf("Parcel %d: %-8s %5.1f kg -> %-16s %s%n",
                    i + 1, states[i], weights[i],
                    chooseHub(states[i]), chooseVehicle(weights[i]));
        }
    }
}
