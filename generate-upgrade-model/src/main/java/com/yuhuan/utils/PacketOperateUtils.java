package com.yuhuan.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class PacketOperateUtils {

    private static final byte[] key = "elite_oap_packet".getBytes();

    public static void encryptStringToFile(String value, File file) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value.getBytes());
        deleteIfExists(file);
        Cipher cipher = getCipher();
        writeFile(byteArrayInputStream, file, cipher, 16383);
    }

    private static void deleteIfExists(File file) throws IOException {
        Files.deleteIfExists(file.toPath());
        String parent = file.getParent();
        if (parent != null) {
            File parentDir = new File(parent);
            if (!parentDir.isDirectory()) {
                parentDir.mkdirs();
            }
        }
    }

    public static Cipher getCipher() throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, secretKey);
        return cipher;
    }

    private static void writeFile(InputStream encryptedInputStream, File decryptedFile, Cipher cipher, int bufferSize) throws IOException, IllegalBlockSizeException, BadPaddingException {
        try {
            InputStream inputStream = encryptedInputStream;
            Throwable var5 = null;

            try {
                OutputStream outputStream = Files.newOutputStream(decryptedFile.toPath());
                Throwable var7 = null;

                try {
                    writeFile(cipher, bufferSize, inputStream, outputStream);
                } catch (Throwable var32) {
                    var7 = var32;
                    throw var32;
                } finally {
                    if (outputStream != null) {
                        if (var7 != null) {
                            try {
                                outputStream.close();
                            } catch (Throwable var31) {
                                var7.addSuppressed(var31);
                            }
                        } else {
                            outputStream.close();
                        }
                    }
                }
            } catch (Throwable var34) {
                var5 = var34;
                throw var34;
            } finally {
                if (encryptedInputStream != null) {
                    if (var5 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var30) {
                            var5.addSuppressed(var30);
                        }
                    } else {
                        encryptedInputStream.close();
                    }
                }

            }
        } catch (Exception var36) {
            throw var36;
        }
    }

    private static void writeFile(Cipher cipher, int bufferSize, InputStream inputStream, OutputStream outputStream) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = new byte[bufferSize];
        int readLength = 0;

        int oneByte;
        while ((oneByte = inputStream.read()) > -1) {
            bytes[readLength] = (byte) oneByte;
            ++readLength;
            if (readLength == bufferSize) {
                writeBytes(cipher, outputStream, bytes);
                bytes = new byte[bufferSize];
                readLength = 0;
            }
        }

        if (readLength > 0) {
            byte[] newBytes = new byte[readLength];
            System.arraycopy(bytes, 0, newBytes, 0, readLength);
            writeBytes(cipher, outputStream, newBytes);
        }
    }

    private static void writeBytes(Cipher cipher, OutputStream outputStream, byte[] bytes) throws IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] doFinal = cipher.doFinal(bytes);
        outputStream.write(doFinal);
        outputStream.flush();
    }
}

