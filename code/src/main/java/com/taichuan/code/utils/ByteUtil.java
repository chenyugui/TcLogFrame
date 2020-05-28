package com.taichuan.code.utils;

/**
 * Created by gui on 2017/9/28.
 */

public class ByteUtil {
    private static final char HEX_DIGITS[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

            'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * 字节数组格式化，把字节数组每4个就加一段空格，每20个换行
     */
    public static String cmdFormat(byte[] buf) {
        String dump = "";
        try {
            int dataLen = buf.length;
            for (int i = 0; i < dataLen; i++) {
                if (i % 20 == 0) {
                    dump += '\n';
                } else if (i % 4 == 0) {
                    dump += "  ";
                }
                dump += Character.forDigit((buf[i] >> 4) & 0x0f, 16);
                dump += Character.forDigit(buf[i] & 0x0f, 16);
                dump += ' ';
            }
            dump = dump.toUpperCase();
        } catch (Throwable t) {
            // catch everything as this is for debug
            dump = "Throwable caught when dumping = " + t;
        }
        return dump;
    }

    /**
     * 字节数组格式化，把字节数组每4个就加一段空格，每20个换行
     */
    public static String cmdFormat(byte[] buf, String splitChar) {
        String dump = "";
        try {
            int dataLen = buf.length;
            for (int i = 0; i < dataLen; i++) {
                dump += Character.forDigit((buf[i] >> 4) & 0x0f, 16);
                dump += Character.forDigit(buf[i] & 0x0f, 16);
                if (i != dataLen - 1) {
                    dump += splitChar;
                }
            }
            dump = dump.toUpperCase();
        } catch (Throwable t) {
            // catch everything as this is for debug
            dump = "Throwable caught when dumping = " + t;
        }
        return dump;
    }

    public static byte[] hexStrToByteArray(String str) {
        if (str.length() % 2 > 0) {
            str = "0" + str;
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, (i + 1) * 2);
            byteArray[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return byteArray;
    }


    public static final int getInt(byte[] b) {
        return getInt(b, 0);
    }

    //byte[2]-->short (little-endian)
    public static final int getInt(byte a, byte b) {
        byte[] c = new byte[2];
        c[0] = b;
        c[1] = a;
        return (int) (((c[1] & 0xFF) << 8) | (c[0] & 0xFF));
    }


    /**
     * @param b      高字节在前
     * @param offset
     * @return
     */
    public static final short getShortByHigh(byte[] b, int offset) {
        short n = 0;
        int len = b.length;
        if (len >= offset + 2) {
            for (int i = 0; i < 2; i++) {
                n |= (b[offset + i] & 0xff) << ((2 - 1 - i) * 8);
            }
        }
        return n;
    }

    public static final int getInt(byte[] b, int offset) {
        int n = 0;
        int len = b.length;
        if (len >= offset + 4) {
            for (int i = 0; i < 4; i++) {
                n |= (b[offset + i] & 0xff) << i * 8;
            }
        }
        return n;
    }

    public static final int getIntByHigh(byte[] b, int offset) {
        int n = 0;
        int len = b.length;
        if (len >= offset + 4) {
            for (int i = 0; i < 4; i++) {
                n |= (b[offset + i] & 0xff) << (4 - 1 - i) * 8;
            }
        }
        return n;
    }

    /**
     * @param b      低字节在前
     * @param offset
     * @return
     */
    public static final short getShortByLow(byte[] b, int offset) {
        short n = 0;
        int len = b.length;
        if (len >= offset + 2) {
            for (int i = 0; i < 2; i++) {
                n |= (b[offset + i] & 0xff) << i * 8;
            }
        }
        return n;
    }

    public static double getDouble(byte[] b, int offset) {
        long accum = 0;
        accum = b[offset] & 0xFF;
        accum |= (long) (b[offset + 1] & 0xFF) << 8;
        accum |= (long) (b[offset + 2] & 0xFF) << 16;
        accum |= (long) (b[offset + 3] & 0xFF) << 24;
        accum |= (long) (b[offset + 4] & 0xFF) << 32;
        accum |= (long) (b[offset + 5] & 0xFF) << 40;
        accum |= (long) (b[offset + 6] & 0xFF) << 48;
        accum |= (long) (b[offset + 7] & 0xFF) << 56;
        return Double.longBitsToDouble(accum);
    }


    public static byte[] intToArray(int value) {
        byte[] byteRet = new byte[4];
        byteRet[0] = (byte) (value & 0xFF);
        byteRet[1] = (byte) ((value >> 8) & 0xFF);
        byteRet[2] = (byte) ((value >> 16) & 0xFF);
        byteRet[3] = (byte) ((value >> 24) & 0xFF);
        return byteRet;
    }

    /**
     * short转byte[2]，高字节在前
     *
     * @param s
     * @return
     */
    public static byte[] shortToByteArrayByHigh(short s) {
        byte[] shortBuf = new byte[2];
        byte[] temp = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (temp.length - 1 - i) * 8;
            temp[i] = (byte) ((s >>> offset) & 0xff);
        }
        shortBuf[0] = temp[0];
        shortBuf[1] = temp[1];
        return shortBuf;
    }

    /**
     * short转byte[2]，低字节在前
     *
     * @param s
     * @return
     */
    public static byte[] shortToByteArrayByLow(short s) {
        byte[] shortBuf = new byte[2];
        byte[] temp = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = i * 8;
            temp[i] = (byte) ((s >>> offset) & 0xff);
        }
        shortBuf[0] = temp[0];
        shortBuf[1] = temp[1];
        return shortBuf;
    }

    /**
     * long转byte[8]，低字节在前
     */
    public static byte[] longToByteArrayByLow(long l) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            int offset = i * 8;
            bytes[i] = (byte) ((l >>> offset) & 0xff);
        }
        return bytes;
    }

    /**
     * long转byte[8]，高字节在前
     */
    public static byte[] longToByteArrayByHigh(long l) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            int offset = (bytes.length - 1 - i) * 8;
            bytes[i] = (byte) ((l >>> offset) & 0xff);
        }
        return bytes;
    }


    /**
     * 将byte转为Long
     *
     * @param b      低位在前
     * @param offset
     * @return
     */
    public static long getLong(byte[] b, int offset) {
        long n = 0;
        int len = b.length;
        if (len >= offset + 8) {
            for (int i = 0; i < 8; i++) {
                n |= ((long) b[offset + i] & 0xff) << i * 8;
            }
        }
        return n;
    }

    /**
     * 将byte转为Long
     *
     * @param b 低位在前
     * @return
     */
    public static long getLong(byte[] b) {
        return getLong(b, 0);
    }

    /**
     * 将byte转为Long
     *
     * @param b      高位在前
     * @param offset
     * @return
     */
    public static long getLongByHigh(byte[] b, int offset) {
        long n = 0;
        long len = b.length;
        if (len >= offset + 8) {
            for (int i = 0; i < 8; i++) {
                n = n | (((long) b[offset + i] & 0xff) << (8 - 1 - i) * 8);
            }
        }
        return n;
    }

    /**
     * 将byte转为Long
     *
     * @param b 高位在前
     * @return
     */
    public static long getLongByHigh(byte[] b) {
        return getLongByHigh(b, 0);
    }

    public static byte[] floatToArray(float Value) {
        int accum = Float.floatToRawIntBits(Value);
        byte[] byteRet = new byte[4];
        byteRet[0] = (byte) (accum & 0xFF);
        byteRet[1] = (byte) ((accum >> 8) & 0xFF);
        byteRet[2] = (byte) ((accum >> 16) & 0xFF);
        byteRet[3] = (byte) ((accum >> 24) & 0xFF);
        return byteRet;
    }

    public static byte[] doubleToArray(double Value) {
        long accum = Double.doubleToRawLongBits(Value);
        byte[] byteRet = new byte[8];
        byteRet[0] = (byte) (accum & 0xFF);
        byteRet[1] = (byte) ((accum >> 8) & 0xFF);
        byteRet[2] = (byte) ((accum >> 16) & 0xFF);
        byteRet[3] = (byte) ((accum >> 24) & 0xFF);
        byteRet[4] = (byte) ((accum >> 32) & 0xFF);
        byteRet[5] = (byte) ((accum >> 40) & 0xFF);
        byteRet[6] = (byte) ((accum >> 48) & 0xFF);
        byteRet[7] = (byte) ((accum >> 56) & 0xFF);
        return byteRet;
    }

    public static float arryToFloat(byte[] Array, int Pos) {
        int accum = 0;
        accum = Array[Pos + 0] & 0xFF;
        accum |= (long) (Array[Pos + 1] & 0xFF) << 8;
        accum |= (long) (Array[Pos + 2] & 0xFF) << 16;
        accum |= (long) (Array[Pos + 3] & 0xFF) << 24;
        return Float.intBitsToFloat(accum);
    }

    public static double arryToDouble(byte[] Array, int Pos) {
        long accum = 0;
        accum = Array[Pos + 0] & 0xFF;
        accum |= (long) (Array[Pos + 1] & 0xFF) << 8;
        accum |= (long) (Array[Pos + 2] & 0xFF) << 16;
        accum |= (long) (Array[Pos + 3] & 0xFF) << 24;
        accum |= (long) (Array[Pos + 4] & 0xFF) << 32;
        accum |= (long) (Array[Pos + 5] & 0xFF) << 40;
        accum |= (long) (Array[Pos + 6] & 0xFF) << 48;
        accum |= (long) (Array[Pos + 7] & 0xFF) << 56;
        return Double.longBitsToDouble(accum);
    }
}
