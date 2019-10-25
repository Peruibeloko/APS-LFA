public class StateMachine {

    private int activeState = 0;
    private TransitionTable states;
    // prevState, input, newState

    private String letMin = "[a-z]";
    private String id = "[a-z_0-9]";
    private String num = "[0-9]";
    private String op_arit = "[+\\-/*]";
    private String op_atrib = "=";
    private String pv = ";";
    private String dot = "\\.";

    public StateMachine() {

        states = new TransitionTable();

        states.createTransition(0, letMin, 1);
        states.createTransition(1, id, 1);
        states.createTransition(1, op_atrib, 2);
        states.createTransition(2, num, 3);
        states.createTransition(2, letMin, 4);
        states.createTransition(3, num, 3);
        states.createTransition(3, pv, 5);
        states.createTransition(3, dot, 6);
        states.createTransition(3, op_arit, 7);
        states.createTransition(4, id, 4);
        states.createTransition(4, op_arit, 7);
        states.createTransition(4, pv, 10);
        states.createTransition(6, num, 6);
        states.createTransition(6, op_arit, 7);
        states.createTransition(7, num, 9);
        states.createTransition(7, letMin, 4);
        states.createTransition(8, num, 8);
        states.createTransition(8, op_arit, 7);
        states.createTransition(8, pv, 10);
        states.createTransition(9, num, 9);
        states.createTransition(9, dot, 8);
        states.createTransition(9, op_arit, 7);
        states.createTransition(9, pv, 10);
    }

    public int transition(String input) {

        System.out.printf("δ(%d, %s)\n", activeState, input);
        activeState = states.change(activeState, input);

        try {

            activeState += 0;
            return activeState;

        } catch (NullPointerException e){

            activeState = -1;
            return activeState;
        }

    }
}
