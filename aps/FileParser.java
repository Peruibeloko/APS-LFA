import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Iterator;

public class FileParser {

    private StringBuilder currentWord = new StringBuilder();
    private StateMachine st = new StateMachine();

    private String filename;
    private BufferedReader input = null;
    private Output gui;

    LinkedList<String> exp = new LinkedList<>();
    Stack<String> ops = new Stack<>();
    String currVar = "";

    private Map<String, Double> varList = new HashMap<>();
    private Entry<String, Double> en;
    private Iterator it;

    private boolean noError = true;
    private boolean creatingVars = true;

    public FileParser(Output gui, String filename) {

        this.gui = gui; /* Reference to the output screen */
        this.filename = filename; /* File to be processed */
    }

    void readFile() {

        int i = 0;

        try {

            input = new BufferedReader(new FileReader(filename));
            gui.addText("\n\n----- BEGIN FILE READ -----\n\n");

            while (input.ready() && noError) {

                if (creatingVars) {

                    currentWord.append(input.readLine());

                    if (currentWord.toString().equals("")) {

                        creatingVars = false;
                        gui.addText("----- END VARIABLE DECLARATION, BEGIN PROCESSING -----\n");

                    } else {

                        gui.addText(currentWord.toString() + "\n");
                        String[] split = currentWord.toString().replace(";", "").split("=");

                        if (split[0].trim().charAt(0) < 97 || split[0].trim().charAt(0) > 122) {

                            gui.addText("Invalid Identifier: " + split[0] + "\n");
                            noError = false;

                        } else {

                            varList.put(split[0].trim(), Double.parseDouble(split[1].trim()));
                            currentWord = new StringBuilder();
                        }
                    }

                } else {

                    int ch = input.read();

                    if (ch == 13) { // Carriage Return

                        gui.addText("[CR]");

                    } else if (ch == 10) { // Line Feed

                        gui.addText("[LF]\n");

                    } else if (ch == 32) { // Space

                        gui.addText("[ ]");

                    } else {

                        gui.addText((char) ch);
                        parseCharacter(ch);
                    }

                    i++;
                }
            }

            input.close();
            gui.addText("\n\n----- END FILE READ -----\n\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename);
        }

        gui.addText("Characters read: " + i + "\n");
        gui.addText("Variables: " + varList.size() + "\n\n");

        it = varList.entrySet().iterator();

        while (it.hasNext()) {

            en = (Entry) it.next();
            gui.addText(en.getKey() + ": " + en.getValue() + "\n");
        }
    }

    void parseCharacter(int ch) {

        currentWord.append((char) ch);
        int opCode = st.transition("" + (char) ch);

        if (opCode == 2) {

            String temp = currentWord.toString();

            if (currVar.equals("")) {

                currVar = temp.trim().substring(0, temp.length() - 1);

                try { varList.get(currVar).toString(); } catch (Exception f) {

                    noError = false;
                    gui.addText("\nNon existent variable: " + currVar + '\n');
                    gui.addText("INVALID ATTRIBUTION\n\n");
                }

            } else {

                exp.add(temp.substring(0, temp.length() - 1));

                while (!ops.empty() && precedence((char) ch) <= precedence(ops.peek().charAt(0)))
                    exp.add(ops.pop());

                ops.push("" + temp.charAt(temp.length() - 1));
            }

            currentWord = new StringBuilder();

        } else if (opCode == 6) {

            String temp = currentWord.toString();
            exp.add(temp.substring(0, temp.length() - 1));

            while (!ops.isEmpty())
                exp.add(ops.pop());

            currentWord = new StringBuilder();
            processLine();
            currVar = "";

        } else if (opCode == -1) {

            noError = false;
            gui.addText("\nCurrent Capture: " + currentWord.toString() + '\n');
            gui.addText("INVALID ATTRIBUTION\n\n");
            currentWord = new StringBuilder();
        }
    }

    void processLine() {

        int i = 0;
        while (exp.size() > 1) {

            if (exp.get(i).matches("[+\\-/*]")) {

                double a, b;
                String tok1 = exp.get(i - 2);
                String tok2 = exp.get(i - 1);

                if (tok1.charAt(0) >= 97 && tok1.charAt(0) <= 122) {
                    a = varList.get(tok1);
                    try {
                        a += 0;
                    } catch (Exception e){
                        noError = false;
                        gui.addText("\nNon existent variable: " + tok1 + '\n');
                        gui.addText("INVALID ATTRIBUTION\n\n");
                    }
                } else
                    a = Double.parseDouble(tok1);

                if (tok2.charAt(0) >= 97 && tok2.charAt(0) <= 122) {
                    b = varList.get(tok2);
                    try {
                        b += 0;
                    } catch (Exception e){
                        noError = false;
                        gui.addText("\nNon existent variable: " + tok2 + '\n');
                        gui.addText("INVALID ATTRIBUTION\n\n");
                    }
                } else
                    b = Double.parseDouble(tok2);

                char op = exp.get(i).charAt(0);

                try {

                    exp.set(i, String.valueOf(applyOp(a, b, op)));
                    exp.remove(i - 2);
                    exp.remove(i - 2);
                    i = 0;

                } catch (Exception e) {

                    noError = false;
                    gui.addText("\nCurrent Capture: " + currentWord.toString() + '\n');
                    gui.addText("INVALID ATTRIBUTION\n\n");
                }

            } else {
                i++;
            }
        }

        varList.replace(currVar, Double.parseDouble(exp.pop()));
    }

    int precedence(char op) {
        if (op == '+' || op == '-')
            return 1;
        if (op == '*' || op == '/')
            return 2;
        return 0;
    }

    double applyOp(double a, double b, char op) throws Exception {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new Exception();
                else
                    return a / b;
            default:
                throw new Exception();
        }
    }
}
