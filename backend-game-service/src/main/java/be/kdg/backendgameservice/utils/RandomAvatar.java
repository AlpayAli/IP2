package be.kdg.backendgameservice.utils;

import java.util.Random;

public class RandomAvatar {
    public RandomAvatar() {
    }

    public static String getRandomAvatar() {
        Random random = new Random();

        return "https://storage.googleapis.com/image_bucket_ip2/general/avatars/" + (random.nextInt(6) + 1) + ".jpg";
    }
}
