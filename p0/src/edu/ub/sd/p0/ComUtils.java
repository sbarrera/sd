package edu.ub.sd.p0;

import java.net.*;
import java.io.*;
import java.util.Locale;


public class ComUtils {
    public static enum Endianness {

        BIG, LITTLE;

        public static boolean isBigEndian(Endianness endianness) {
            return endianness == Endianness.BIG;
        }

        public static boolean isLittleEndian(Endianness endianness) {
            return endianness == Endianness.LITTLE;
        }

    }

    /* Mida d'una cadena de caracters */
    private final int STRSIZE = 40;
    /* Objectes per escriure i llegir dades */
    private DataInputStream dis;
    private DataOutputStream dos;

    public ComUtils(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public ComUtils(File file) throws IOException {
        dis = new DataInputStream(new FileInputStream(file));
        dos = new DataOutputStream(new FileOutputStream(file));
    }

    /* Llegir un enter de 32 bits */
    public int read_int32() throws IOException {
        byte bytes[] = new byte[4];
        bytes = read_bytes(4);

        return bytesToInt32(bytes, Endianness.BIG);
    }

    /* Escriure un enter de 32 bits */
    public void write_int32(int number) throws IOException {
        byte bytes[] = new byte[4];

        int32ToBytes(number, bytes, Endianness.BIG);
        dos.write(bytes, 0, 4);
    }

    /* Llegir un string de mida STRSIZE */
    public String read_string() throws IOException {
        String str;
        byte bStr[] = new byte[STRSIZE];
        char cStr[] = new char[STRSIZE];

        bStr = read_bytes(STRSIZE);

        for (int i = 0; i < STRSIZE; i++)
            cStr[i] = (char) bStr[i];

        str = String.valueOf(cStr);

        return str.trim();
    }

    /* Escriure un string */
    public void write_string(String str) throws IOException {
        //definir variabls numbytes y lenStr
        int numBytes, lenStr;
        // inicializamos un array de tamaño STRSIZE = 40
        byte bStr[] = new byte[STRSIZE];
        //miramos el tamaño de STR que viene pasado por paramtro y se lo assignamos a lenStr
        lenStr = str.length();
        // si LenSTR es mayor k STRSIZE (40)
        if (lenStr > STRSIZE) {
            //assginamos STRSIZE A numBytes
            numBytes = STRSIZE;
        }
        else{
            //Sino se le asigna lenStr
            numBytes = lenStr;
        }
        //Para cada caracter que tiene str lo asginamos a la posicion i del array
        for (int i = 0; i < numBytes; i++)
            bStr[i] = (byte) str.charAt(i);
        // rellenamos desde numBytes y hasta STRSIZE (40) la array con espacios en blanco
        for (int i = numBytes; i < STRSIZE; i++)
            bStr[i] = (byte) ' ';
        //escribe STRSIZE bytes del array bstr al outputstream
        dos.write(bStr, 0, STRSIZE);
    }

    /* Passar d'enters a bytes */
    private int int32ToBytes(int number, byte bytes[], Endianness endianness) {
        if ( Endianness.isBigEndian(endianness) ) {
            bytes[0] = (byte) ((number >> 24) & 0xFF);
            bytes[1] = (byte) ((number >> 16) & 0xFF);
            bytes[2] = (byte) ((number >> 8) & 0xFF);
            bytes[3] = (byte) (number & 0xFF);
        } else {
            bytes[0] = (byte) (number & 0xFF);
            bytes[1] = (byte) ((number >> 8) & 0xFF);
            bytes[2] = (byte) ((number >> 16) & 0xFF);
            bytes[3] = (byte) ((number >> 24) & 0xFF);
        }
        return 4;
    }

    /* Passar de bytes a enters */
    private int bytesToInt32(byte bytes[], Endianness endianness) {
        int number;

        if ( Endianness.isBigEndian(endianness) ) {
            number = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) |
                    ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        } else {
            number = (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) |
                    ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24);
        }
        return number;
    }

    //llegir bytes.
    private byte[] read_bytes(int numBytes) throws IOException {
        int len = 0;
        byte bStr[] = new byte[numBytes];
        do {
            len += dis.read(bStr, len, numBytes - len);
        } while (len < numBytes);
        return bStr;
    }

    /* Llegir un string  mida variable size = nombre de bytes especifica la longitud*/
    public String read_string_variable(int size) throws IOException {
        byte bHeader[] = new byte[size];
        char cHeader[] = new char[size];
        int numBytes = 0;

        // Llegim els bytes que indiquen la mida de l'string
        bHeader = read_bytes(size);
        // La mida de l'string ve en format text, per tant creem un string i el parsejem
        for (int i = 0; i < size; i++) {
            cHeader[i] = (char) bHeader[i];
        }
        numBytes = Integer.parseInt(new String(cHeader));

        // Llegim l'string
        byte bStr[] = new byte[numBytes];
        char cStr[] = new char[numBytes];
        bStr = read_bytes(numBytes);
        for (int i = 0; i < numBytes; i++)
            cStr[i] = (char) bStr[i];
        return String.valueOf(cStr);
    }

    /* Escriure un string mida variable, size = nombre de bytes especifica la longitud  */
	/* String str = string a escriure.*/
    public void write_string_variable(int size, String str) throws IOException {

        // Creem una seqüència amb la mida
        byte bHeader[] = new byte[size];
        String strHeader;
        int numBytes = 0;

        // Creem la capçalera amb el nombre de bytes que codifiquen la mida
        numBytes = str.length();

        strHeader = String.valueOf(numBytes);
        int len;
        if ((len = strHeader.length()) < size)
            for (int i = len; i < size; i++) {
                strHeader = "0" + strHeader;
            }
        //System.out.println(strHeader);
        for (int i = 0; i < size; i++)
            bHeader[i] = (byte) strHeader.charAt(i);
        // Enviem la capçalera
        dos.write(bHeader, 0, size);
        // Enviem l'string writeBytes de DataOutputStrem no envia el byte més alt dels chars.
        dos.writeBytes(str);
    }

    public void writeTest(String str) {
        try {
            write_string_variable(str.length(), str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String readTest(String str) {
        String retorno = "";
        try {
            retorno = read_string_variable(str.length());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return retorno;
    }

}
