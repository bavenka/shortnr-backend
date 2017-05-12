package com.ks.projects.shortnrbackend.services.converter;

/**
 * Created by kamaz on 02.05.2017.
 */
public class LinkTokenConverter {

    private static final String BASE58_ALPHABET = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int ALPHABET_LENGTH = BASE58_ALPHABET.length();

    public static String encodeLinkId(long id) {

        StringBuilder encoded = new StringBuilder();
        Double castedId = (double) id;

        while (castedId > 0) {
            double index = castedId % ALPHABET_LENGTH;
            castedId = Math.floor(castedId / ALPHABET_LENGTH);
            encoded.insert(0, Character.toString(BASE58_ALPHABET.charAt((int) index)));
        }
        return encoded.toString();

    }

    public static long decodeToken(String token) {
        StringBuilder decode = new StringBuilder(token);
        Double decodedId = 0.0;

        while (decode.length() > 0) {
            int index = BASE58_ALPHABET.indexOf(decode.charAt(0));
            int power = decode.length() - 1;
            decodedId += index * (Math.pow((double) ALPHABET_LENGTH, (double) power));
            decode.deleteCharAt(0);
        }

        return decodedId.longValue();
    }

}
