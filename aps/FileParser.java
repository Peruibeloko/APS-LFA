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

    Stack<String> values = new Stack<>();
    Stack<String> ops = new Stack<>();
    String currVar;

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
            currVar = currentWord.toString().trim().substring(0, temp.length() - 1);
            currentWord = new StringBuilder();

        } else if (opCode == 5) {

            String temp = currentWord.toString();
            values.push(temp.substring(0, temp.length() - 1));
            currentWord = new StringBuilder();

            processLine();

        } else if (opCode == 7) {

            String temp = currentWord.toString();
            ops.push("" + temp.charAt(temp.length() - 1));
            values.push(temp.substring(0, temp.length() - 1));
            currentWord = new StringBuilder();

        } else if (opCode == -1) {

            noError = false;
            gui.addText("\nCurrent Capture: " + currentWord.toString() + '\n');
            gui.addText("INVALID ATTRIBUTION\n\n");
            currentWord = new StringBuilder();
        }
    }

    void processLine() {

        // Algoritmo das duas pilhas de Djikstra
    }
}
