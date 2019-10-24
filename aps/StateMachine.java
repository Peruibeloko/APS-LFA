public class StateMachine {

    private String activeState = "q0";
    private String letMin = "[a-z]";
    private String id = "[a-z_0-9]";
    private String num = "[0-9]";
    private String op_arit = "[+\\-/*]";

    public void setActiveState(String state) {

        this.activeState = state;
    }

    public int transition(String input) {

        System.out.printf("δ(%s, %s)\n", activeState, input);

        if (activeState.equals("q0")) {

            if (input.matches(letMin)) {
                setActiveState("q1");
                return 0;
            }

        } else if (activeState.equals("q1")) {

            if (input.matches(id)) {
                setActiveState("q1");
                return 0;
            } else if (input.matches("=")) {
                setActiveState("q2");
                return 1;
            }

        } else if (activeState.equals("q2")) {

            if (input.matches(letMin)) {
                setActiveState("q3");
                return 0;
            } else if (input.matches(num)) {
                setActiveState("q4");
                return 0;
            }

        } else if (activeState.equals("q3")) {

            if (input.matches(id)) {
                setActiveState("q3");
                return 0;
            } else if (input.matches(";")) {
                setActiveState("q0");
                return 3;
            } else if (input.matches(op_arit)) {
                setActiveState("q2");
                return 0;
            }

        } else if (activeState.equals("q4")) {

            if (input.matches(num)) {
                setActiveState("q4");
                return 0;
            } else if (input.matches(";")) {
                setActiveState("q0");
                return 2;
            } else if (input.matches("\\.")) {
                setActiveState("q5");
                return 0;
            } else if (input.matches(op_arit)) {
                setActiveState("q2");
                return 0;
            }

        } else if (activeState.equals("q5")) {

            if (input.matches(num)) {
                setActiveState("q5");
                return 0;
            } else if (input.matches(";")) {
                setActiveState("q0");
                return 2;
            } else if (input.matches(op_arit)) {
                setActiveState("q2");
                return 0;
            }

        }

        return -1;
    }
}
