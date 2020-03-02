package des_simplificado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author richie
 */
public class algoritmo {

    private String clave;
    private String subclave1;
    private String subclave2;
    private ArrayList<String> mensaje;
    private ArrayList<String> mensajeCifrado;

    public algoritmo(String clave) {
        if (clave.isEmpty()) {
            this.clave = claveAleatoria();
        } else {
            this.clave = clave;
        }
        this.mensajeCifrado = new ArrayList<>();
        this.mensaje = new ArrayList<>();
        this.generacionDeClaves();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = new ArrayList<>();
        //this.mensaje.add("10111101");
        //Para generar los bloques de 8 bits
        String aux = "";
        for (int i = 0; i < mensaje.length(); i++) {
            aux += Integer.toBinaryString(mensaje.charAt(i));
        }
        for (int i = 0; i < aux.length(); i += 8) {
            String sub = aux.substring(i, i + 8 < aux.length() ? i + 8 : aux.length());
            while (sub.length() < 8) {
                sub = "0" + sub;
            }
            this.mensaje.add(sub);
        }
    }

    public void setMensajeCifrado(String mensajeCifrado) {
        this.mensajeCifrado = new ArrayList<>();
        this.mensajeCifrado.add("01110101");
        //Para generar los bloques de 8 bits
        /*String aux = "";
        for (int i = 0; i < mensajeCifrado.length(); i++) {
            aux += Integer.toBinaryString(mensajeCifrado.charAt(i));
        }
        for (int i = 0; i < aux.length(); i += 8) {
            String sub = aux.substring(i, i + 8 < aux.length() ? i + 8 : aux.length());
            while (sub.length() < 8) {
                sub = "0" + sub;
            }
            this.mensajeCifrado.add(sub);
        }*/
    }

    public final String claveAleatoria() {
        String key = "";
        for (int i = 0; i < 10; i++) {
            key += (int) Math.floor(Math.random() * 100) % 2;
        }
        return key;
    }

    public String permutacion(String cadena, ArrayList<Integer> orden) {
        String aux = "";
        for (Integer posicion : orden) {
            aux += cadena.charAt(posicion);
        }
        return aux;
    }

    public String corrimientoCircular(String cadena) {
        String aux = "";
        for (int i = 0; i < cadena.length(); i++) {
            if (1 + i < cadena.length()) {
                aux += cadena.charAt(1 + i);
            } else {
                aux += cadena.charAt(cadena.length() - (1 + i));
            }
        }
        return aux;
    }

    public void generacionDeClaves() {
        ArrayList<Integer> permutacionP10 = new ArrayList<>();
        permutacionP10.add(2);
        permutacionP10.add(4);
        permutacionP10.add(1);
        permutacionP10.add(6);
        permutacionP10.add(3);
        permutacionP10.add(9);
        permutacionP10.add(0);
        permutacionP10.add(8);
        permutacionP10.add(7);
        permutacionP10.add(5);

        ArrayList<Integer> permutacionP8 = new ArrayList<>();
        permutacionP8.add(5);
        permutacionP8.add(2);
        permutacionP8.add(6);
        permutacionP8.add(3);
        permutacionP8.add(7);
        permutacionP8.add(4);
        permutacionP8.add(9);
        permutacionP8.add(8);

        String p10 = this.permutacion(clave, permutacionP10);
        //Separación de la clave en dos bloques, después de P10
        String p10L = p10.substring(0, 5);
        String p10R = p10.substring(5, 10);
        //Se corren ambos bloques de 5 bits de forma circular
        p10L = this.corrimientoCircular(p10L);
        p10R = this.corrimientoCircular(p10R);
        this.subclave1 = this.permutacion(p10L.concat(p10R), permutacionP8);
        //Corrimiento circular de 2 posiciones
        for (int i = 0; i < 2; i++) {
            p10L = this.corrimientoCircular(p10L);
            p10R = this.corrimientoCircular(p10R);
        }
        this.subclave2 = this.permutacion(p10L.concat(p10R), permutacionP8);
    }

    public void cifradoDescifrado(boolean opcion) {
        //True - Cifrar / False - Descifrar
        ArrayList<Integer> permutacionIP = new ArrayList<>();
        permutacionIP.add(1);
        permutacionIP.add(5);
        permutacionIP.add(2);
        permutacionIP.add(0);
        permutacionIP.add(3);
        permutacionIP.add(7);
        permutacionIP.add(4);
        permutacionIP.add(6);

        ArrayList<Integer> permutacionIPInv = new ArrayList<>();
        permutacionIPInv.add(3);
        permutacionIPInv.add(0);
        permutacionIPInv.add(2);
        permutacionIPInv.add(4);
        permutacionIPInv.add(6);
        permutacionIPInv.add(1);
        permutacionIPInv.add(7);
        permutacionIPInv.add(5);
        if (opcion) {
            for (int i = 0; i < this.mensaje.size(); i++) {
                this.mensaje.set(i, this.permutacion(this.mensaje.get(i), permutacionIP));
            }

            for (int i = 0; i < this.mensaje.size(); i++) {
                String ronda1 = this.rondas(1, mensaje.get(i));
                String ronda2 = this.rondas(2, ronda1.substring(4, 8).concat(ronda1.substring(0, 4)));

                this.mensajeCifrado.add(this.permutacion(ronda2, permutacionIPInv));
            }
        } else {
            for (int i = 0; i < this.mensajeCifrado.size(); i++) {
                this.mensajeCifrado.set(i, this.permutacion(this.mensajeCifrado.get(i), permutacionIP));
            }

            for (int i = 0; i < this.mensajeCifrado.size(); i++) {
                String ronda1 = this.rondas(2, mensajeCifrado.get(i));
                String ronda2 = this.rondas(1, ronda1.substring(4, 8).concat(ronda1.substring(0, 4)));
                this.mensaje.add(this.permutacion(ronda2, permutacionIPInv));
            }
        }
    }

    public String rondas(int num, String cadena) {
        ArrayList<Integer> permutacionExpansion = new ArrayList<>();
        ArrayList<Integer> permutacionSBoxes = new ArrayList<>();
        ArrayList<Integer> permutacionP4 = new ArrayList<>();
        //Set valores de la permutacion E/P
        permutacionExpansion.add(3);
        permutacionExpansion.add(0);
        permutacionExpansion.add(1);
        permutacionExpansion.add(2);
        permutacionExpansion.add(1);
        permutacionExpansion.add(2);
        permutacionExpansion.add(3);
        permutacionExpansion.add(0);
        //Primeros 2 bits indican la fila y últimos 2 la columna
        permutacionSBoxes.add(0);
        permutacionSBoxes.add(3);
        permutacionSBoxes.add(1);
        permutacionSBoxes.add(2);
        //Permutacion P4
        permutacionP4.add(1);
        permutacionP4.add(3);
        permutacionP4.add(2);
        permutacionP4.add(0);

        String izquierda = cadena.substring(0, 4);
        String derecha = cadena.substring(4, 8);

        String derechaExpandida = this.permutacion(derecha, permutacionExpansion);

        String auxiliar = "";
        for (int i = 0; i < derechaExpandida.length(); i++) {
            if (num == 1) {
                auxiliar += this.subclave1.charAt(i) ^ derechaExpandida.charAt(i);
            } else {
                auxiliar += this.subclave2.charAt(i) ^ derechaExpandida.charAt(i);
            }
        }

        String auxIzq = auxiliar.substring(0, 4);
        String auxDer = auxiliar.substring(4, 8);

        //El valor obtenido de las S-Boxes, que se permuta con P4
        auxiliar = this.buscarEnSBoxes(this.permutacion(auxIzq, permutacionSBoxes), false)
                .concat(this.buscarEnSBoxes(this.permutacion(auxDer, permutacionSBoxes), true));
        auxiliar = this.permutacion(auxiliar, permutacionP4);

        String aux2 = "";
        for (int i = 0; i < 4; i++) {
            aux2 += auxiliar.charAt(i) ^ izquierda.charAt(i);
        }
        return aux2.concat(derecha);
    }

    public String buscarEnSBoxes(String cadena, boolean opc) {
        Map<String, String> s0 = new HashMap<>(), s1 = new HashMap<>();
        //Declaración de la s-box 0
        s0.put("0000", "01");
        s0.put("0001", "00");
        s0.put("0010", "11");
        s0.put("0011", "10");

        s0.put("0100", "11");
        s0.put("0101", "10");
        s0.put("0110", "01");
        s0.put("0111", "00");

        s0.put("1000", "00");
        s0.put("1001", "10");
        s0.put("1010", "01");
        s0.put("1011", "11");

        s0.put("1100", "11");
        s0.put("1101", "01");
        s0.put("1110", "11");
        s0.put("1111", "10");

        //S-box 1
        s1.put("0000", "00");
        s1.put("0001", "01");
        s1.put("0010", "10");
        s1.put("0011", "11");

        s1.put("0100", "10");
        s1.put("0101", "00");
        s1.put("0110", "01");
        s1.put("0111", "11");

        s1.put("1000", "11");
        s1.put("1001", "00");
        s1.put("1010", "01");
        s1.put("1011", "00");

        s1.put("1100", "10");
        s1.put("1101", "01");
        s1.put("1110", "00");
        s1.put("1111", "11");

        return opc ? s1.get(cadena) : s0.get(cadena);
    }

    public String getClave() {
        return clave;
    }

    public String getSubclave1() {
        return subclave1;
    }

    public String getSubclave2() {
        return subclave2;
    }

    public ArrayList<String> getMensaje() {
        return mensaje;
    }

    public ArrayList<String> getMensajeCifrado() {
        return mensajeCifrado;
    }

}
