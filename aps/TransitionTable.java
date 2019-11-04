import java.util.HashMap;

public class TransitionTable {

    HashMap<String, Integer> matrix;

    public TransitionTable() {

        this.matrix = new HashMap<>();
    }

    public int change(int state, String input) {

        String key = state + "|" + input;

        try {

            int out = matrix.get(key);
            out += 0;
            System.out.printf("δ(%d, %s) → %d\n", state, input, out);

            return out;

        } catch (Exception e) {

            return -1;
        }
    }

    public void createTransition(int prevState, String input, int newState) {

        String key = prevState + "|" + input;
        matrix.put(key, newState);
    }
}