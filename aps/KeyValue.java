public class KeyValue {

    /*
    * A classe vai ser usada pra armazenar os valores das variáveis
    * Haverá um ArrayList de KeyValues para guardar as informações
    */

    private String key;
    private double value;

    public KeyValue(String key, double value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }
}