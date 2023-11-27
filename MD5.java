public class MD5 {

    // Constants for initializing variables in the MD5 algorithm
    private static final int INIT_A = 0x67452301;
    private static final int INIT_B = (int) 0xEFCDAB89L;
    private static final int INIT_C = (int) 0x98BADCFEL;
    private static final int INIT_D = 0x10325476;

    // Shift amounts for each round of the algorithm
    private static final int[] SHIFT_AMTS = {7, 12, 17, 22, 5, 9, 14, 20, 4, 11, 16, 23, 6, 10, 15, 21};

    // Lookup table with precomputed values
    private static final int[] TABLE_T = new int[64];

    // Static block to initialize the lookup table
    static {
        for (int i = 0; i < 64; i++)
            // Initialize the lookup table with values based on the sine function
            TABLE_T[i] = (int) (long) ((1L << 32) * Math.abs(Math.sin(i + 1)));
    }

    // Method to compute MD5 hash of a given message
    public static byte[] computeMD5(byte[] message) {

        // Calculate the length of the message in bytes
        int messageLenBytes = message.length;

        // Calculate the number of 64-byte blocks required, including padding
        int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;

        // Calculate the total length of the padded message
        int totalLen = numBlocks << 6;

        // Create an array for padding bytes
        byte[] paddingBytes = new byte[totalLen - messageLenBytes];
        paddingBytes[0] = (byte) 0x80; // Set the first padding byte to 0x80

        // Calculate the length of the message in bits and store it in the padding
        long messageLenBits = (long) messageLenBytes << 3;
        for (int i = 0; i < 8; i++) {
            paddingBytes[paddingBytes.length - 8 + i] = (byte) messageLenBits;
            messageLenBits >>>= 8;
        }

        // Initialize variables for the MD5 algorithm
        int a = INIT_A;
        int b = INIT_B;
        int c = INIT_C;
        int d = INIT_D;
        int[] buffer = new int[16];

        // Process each 64-byte block
        for (int i = 0; i < numBlocks; i++) {

            // Calculate the index for the current block
            int index = i << 6;

            // Fill the buffer with data from the message or padding
            for (int j = 0; j < 64; j++, index++)
                
                buffer[j >>> 2] = ((int) ((index < messageLenBytes) ? message[index]
                        : paddingBytes[index - messageLenBytes]) << 24)
                        | (buffer[j >>> 2] >>> 8);

            // Save the original values of variables
            int originalA = a;
            int originalB = b;
            int originalC = c;
            int originalD = d;

            // Process each of the 64 rounds
            for (int j = 0; j < 64; j++) {
                int div16 = j >>> 4;
                int f = 0;
                int bufferIndex = j;

                // Determine the function for the current round
                switch (div16) {
                    case 0:
                        f = (b & c) | (~b & d);
                        break;
                    case 1:
                        f = (b & d) | (c & ~d);
                        bufferIndex = (bufferIndex * 5 + 1) & 0x0F;
                        break;
                    case 2:
                        f = b ^ c ^ d;
                        bufferIndex = (bufferIndex * 3 + 5) & 0x0F;
                        break;
                    case 3:
                        f = c ^ (b | ~d);
                        bufferIndex = (bufferIndex * 7) & 0x0F;
                        break;
                }

                // Update temporary variables using bitwise operations
                int temp = b + Integer.rotateLeft(a + f + buffer[bufferIndex] + TABLE_T[j],
                        SHIFT_AMTS[(div16 << 2) | (j & 3)]);
                a = d;
                d = c;
                c = b;
                b = temp;
            }

            // Update main variables
            a += originalA;
            b += originalB;
            c += originalC;
            d += originalD;
        }

        // Concatenate the four 32-bit variables to produce the MD5 hash
        byte[] md5 = new byte[16];
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int n = (i == 0) ? a : ((i == 1) ? b : ((i == 2) ? c : d));
            for (int j = 0; j < 4; j++) {
                md5[count++] = (byte) n;
                n >>>= 8;
            }
        }

        return md5;
    }

    // Method to convert a byte array to a hexadecimal string
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }

    // Main method for testing the MD5 implementation
    public static void main(String[] args) {
        String[] testStrings = {"siddharth"};
        for (String s : testStrings)
            System.out.println(toHexString(computeMD5(s.getBytes())) + " <== \"" + s + "\"");
    }
}
