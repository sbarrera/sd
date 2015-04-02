package edu.ub.sd.sevenhalf.utils;

import java.net.*;
import java.io.*;
import java.text.DecimalFormat;


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

    protected DataInputStream dis;
    protected DataOutputStream dos;

    protected Socket socket;

    public ComUtils() {}

    public ComUtils(Socket socket) throws Exception {
        setSocket(socket);
    }

    public ComUtils(File file) throws Exception {
        dis = new DataInputStream(new FileInputStream(file));
        dos = new DataOutputStream(new FileOutputStream(file));
    }

    public void setSocket(Socket socket) throws Exception {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public byte[] readBytes(int numBytes) throws Exception {
        int len = 0;
        int cl = 0;
        byte bStr[] = new byte[numBytes];
        do {
            cl = dis.read(bStr, len, numBytes - len);
            if ( cl == -1 ) throw new IOException("readBytes broken");
            len += cl;
        } while (len < numBytes);
        return bStr;
    }

    /* Llegir un enter de 32 bits */
    public int readInteger() throws Exception {
        byte bytes[] = new byte[4];
        bytes = readBytes(4);

        return bytesToInt32(bytes, Endianness.BIG);
    }

    public String readString(int size) throws Exception {
        String str;
        byte bStr[] = new byte[size];
        char cStr[] = new char[size];

        bStr = readBytes(size);

        for (int i = 0; i < size; i++)
            cStr[i] = (char) bStr[i];

        str = String.valueOf(cStr);

        return str.trim();
    }

    public int readLengthInteger() throws Exception {
        byte length_bytes[] = readBytes(2);

        int tenth = Character.getNumericValue(length_bytes[0]);
        int unit = Character.getNumericValue(length_bytes[1]);

        return ( tenth * 10 ) + unit;
    }

    public double readDouble() throws Exception {
        byte bytes[] = readBytes(4);

        double tenth = Character.getNumericValue(bytes[0]) * 10.0;
        double unit = Character.getNumericValue(bytes[1]);
        double decimal = Character.getNumericValue(bytes[3]) / 10.0;

        return tenth + unit + decimal;
    }

    public void send(byte bytes[]) throws Exception {
        dos.write(bytes, 0, bytes.length);
    }

    protected int bytesToInt32(byte bytes[], Endianness endianness) {
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

    public static byte[] toByteArray(int number) {
        return toByteArray(number, Endianness.BIG);
    }

    public static byte[] toByteArray(int number, Endianness endianness) {
        byte bytes[] = new byte[4];
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
        return bytes;
    }

    public static byte[] toByteArray(String str) {
        byte data[] = new byte[str.length()];
        for ( int i = 0 ; i < str.length() ; i++ ) {
            data[i] = (byte) str.charAt(i);
        }
        return data;
    }

    public static byte[] toByteArray(double value) {
        String strValue = new DecimalFormat("00.0").format(value);
        strValue = strValue.replace(",", ".");
        return toByteArray(strValue);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        char l, r;
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            l = hexArray[v >>> 4];
            r = hexArray[v & 0x0F];
            if ( j != bytes.length - 1 ) {
                buffer.append(String.format("%c%c ", l, r));
            } else {
                buffer.append(String.format("%c%c", l, r));
            }
        }
        return buffer.toString();
    }

}
