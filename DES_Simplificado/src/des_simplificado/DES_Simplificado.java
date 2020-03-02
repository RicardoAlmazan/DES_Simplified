package des_simplificado;

/**
 *
 * @author richie
 */
public class DES_Simplificado {

    public static void main(String[] args) {
        //String cadena = "Hola mundo.";
        String cadena = "½";
        String clave = "1010000010";

        algoritmo des = new algoritmo(clave);
        System.out.println("Key: " + des.getClave());
        //Prueba de cifrado
        System.out.println("\n.::Cifrado::.");
        des.setMensaje(cadena);
        des.cifradoDescifrado(true);
        String mensajeCifrado = "";
        for (int i = 0; i < des.getMensajeCifrado().size(); i++) {
            mensajeCifrado += (char) binarioADec(Integer.parseInt(des.getMensajeCifrado().get(i)), 0);
            System.out.println("Bloque cifrado: " + des.getMensajeCifrado().get(i));
        }

        System.out.println("Mensaje cifrado: " + mensajeCifrado);
        
        //Prueba de descifrado
        System.out.println("\n.::Descifrado::.");
        des = new algoritmo(clave);
        des.setMensajeCifrado(cadena);
        des.cifradoDescifrado(false);
        String mensajeClaro = "";
        for (int i = 0; i < des.getMensaje().size(); i++) {
            mensajeClaro += (char) binarioADec(Integer.parseInt(des.getMensaje().get(i)), 0);
            System.out.println("Bloque claro: " + des.getMensaje().get(i));
        }
        
        System.out.println("Mensaje claro: " + mensajeClaro);
    }

    public static int binarioADec(int x, int empiezaEnCero) {
        int y = empiezaEnCero;
        if (x == 0) {
            return 0;
        } else {
            return (((x % 10) * (int) Math.pow(2, y)) + binarioADec((x / 10), ++y));
        }
    }
}
