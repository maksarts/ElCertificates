package artsishevskiy.cryptmethods.lab3;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class CertificatesHandler {
    //private String algorithm = "SHA1withRSA";
    private String algorithm = "MD5withRSA";

    private String alphabetUpper        = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞß";
    private String alphabetLower        = "àáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    private String rightAlphabetUpper   = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private String rightAlphabetLower   = "абвгдежзийклмнопрстуфхцчшщъыьэюя";
    // не смог найти нормальныу кодировки,
    // пришлось изобретать велосипед,
    // буква ё все ломает...

    private KeyStore keyStore;
    public Enumeration<String> data;

    public CertificatesHandler() {
        init();
    }

    public void init() {
        try {
            // подключение к хранилищу сертификатов
            keyStore = KeyStore.getInstance("Windows-MY");
            keyStore.load(null, null);
            data = keyStore.aliases();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasIt(String alias) {
        try {
            String enc = getEncodedString(alias);
            return keyStore.containsAlias(enc) && keyStore.isKeyEntry(enc) && keyStore.getCertificate(enc) != null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getDecodedString(String encoded) {
        StringBuilder decoded = new StringBuilder();

        for (var c : encoded.split("")) {
            if (alphabetLower.contains(c)) {
                decoded.append(rightAlphabetLower.charAt(alphabetLower.indexOf(c)));
            } else if (alphabetUpper.contains(c)) {
                decoded.append(rightAlphabetUpper.charAt(alphabetUpper.indexOf(c)));
            } else {
                decoded.append(c);
            }
        }

        return decoded.toString();
    }

    public String convert(String inString) throws UnsupportedEncodingException {
        byte[] bytes=inString.getBytes("unicode");
        String temp = new String(bytes, "windows-1251");
        return temp.substring(2, temp.length());
    }

    private String getEncodedString(String encoded) {
        StringBuilder decoded = new StringBuilder();

        for (var c : encoded.split("")) {
            if (rightAlphabetLower.contains(c)) {
                decoded.append(alphabetLower.charAt(rightAlphabetLower.indexOf(c)));
            } else if (rightAlphabetUpper.contains(c)) {
                decoded.append(alphabetUpper.charAt(rightAlphabetUpper.indexOf(c)));
            } else {
                decoded.append(c);
            }
        }

        return decoded.toString();
    }

    public boolean signDoc(String alias, String text, String path) {
        try {
            String enc = getEncodedString(alias);

            Certificate certificate = keyStore.getCertificate(enc);
            PublicKey publicKey = certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(enc, null);

            byte[] btxt = text.getBytes(StandardCharsets.UTF_8);

            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(btxt);

            byte[] sign = signature.sign();
            byte[] cert = certificate.getEncoded();

            int lenCert1 = cert.length / 256;
            int lenCert2 = cert.length % 256;
            int lenSign1 = sign.length / 256;
            int lenSign2 = sign.length % 256;

            byte[] res = new byte[4+cert.length+sign.length+btxt.length];
            res[0] = (byte)lenCert1;
            res[1] = (byte)lenCert2;
            res[2] = (byte)lenSign1;
            res[3] = (byte)lenSign2;

            System.arraycopy(cert, 0, res, 4, cert.length);
            System.arraycopy(sign, 0, res, 4 + cert.length, sign.length);
            System.arraycopy(btxt, 0, res, 4 + cert.length + sign.length, btxt.length);

            if (!path.contains(".sd")) {
                path = path.concat(".sd");
            }
            Files.write(Path.of(path), res);

            signature.initVerify(publicKey);
            signature.update(btxt);
            return signature.verify(sign);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String checkSignedDoc(String path) {
        try {
            Signature signature = Signature.getInstance(algorithm);

            byte[] data = Files.readAllBytes(Path.of(path));

            int lenCert1 = convertByte(data[0]);
            int lenCert2 = convertByte(data[1]);
            int lenSign1 = convertByte(data[2]);
            int lenSign2 = convertByte(data[3]);

            int lenCert = lenCert1 * 256 + lenCert2;
            int lenSign = lenSign1 * 256 + lenSign2;
            int lenBtxt = data.length - lenCert - lenSign - 4;

            byte[] sign = new byte[lenSign];
            byte[] cert = new byte[lenCert];
            byte[] btxt = new byte[lenBtxt];

            System.arraycopy(data, 4, cert, 0, lenCert);
            System.arraycopy(data, 4+lenCert, sign, 0, lenSign);
            System.arraycopy(data, 4+lenCert+lenSign, btxt, 0, lenBtxt);

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)(certificateFactory.generateCertificate(new ByteArrayInputStream(cert)));
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyStore.getCertificateAlias(certificate), null);
            if (privateKey == null) {
                return "";
            }

            var chain = keyStore.getCertificateChain(keyStore.getCertificateAlias(certificate));
            if (chain == null || !checkCertificates(chain)) {
                return "";
            }

            PublicKey publicKey = certificate.getPublicKey();
            signature.initVerify(publicKey);
            signature.update(btxt);
            //return getDecodedString(keyStore.getCertificateAlias(certificate));
            return convert(keyStore.getCertificateAlias(certificate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private boolean checkCertificates(Certificate[] certificates) {
        try {
            PublicKey publicKey = certificates[certificates.length - 1].getPublicKey();
            for (int i = certificates.length - 1; i >= 0; i--) {
                Certificate certificate = certificates[i];
                certificate.verify(publicKey);
                publicKey = certificate.getPublicKey();
            }
            return true;
        } catch (InvalidKeyException e) {
            return false;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String getTextFromSignedDoc(String path) {
        try {
            byte[] data = Files.readAllBytes(Path.of(path));

            int lenCert1 = convertByte(data[0]);
            int lenCert2 = convertByte(data[1]);
            int lenSign1 = convertByte(data[2]);
            int lenSign2 = convertByte(data[3]);

            int lenCert = lenCert1 * 256 + lenCert2;
            int lenSign = lenSign1 * 256 + lenSign2;
            int lenBtxt = data.length - lenCert - lenSign - 4;

            byte[] btxt = new byte[lenBtxt];

            System.arraycopy(data, 4 + lenCert + lenSign, btxt, 0, lenBtxt);
            return new String(btxt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void deleteCertificate(String alias) {
        try {
            String enc = getEncodedString(alias);
            keyStore.deleteEntry(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int convertByte(byte num) {
        if (num >= 0) {
            return num;
        }

        int buf1 = num * (-1);

        buf1 = 256 - buf1;
        return buf1;
    }
}
