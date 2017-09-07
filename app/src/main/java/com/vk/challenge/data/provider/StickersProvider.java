package com.vk.challenge.data.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 07.09.17.
 */

public class StickersProvider {

    public static List<String> getStickers() {
        List<String> stickers = new ArrayList<>();
        stickers.add(stickerPath("1.png"));
        stickers.add(stickerPath("2.png"));
        stickers.add(stickerPath("3.png"));
        stickers.add(stickerPath("4.png"));
        stickers.add(stickerPath("5.png"));
        stickers.add(stickerPath("6.png"));
        stickers.add(stickerPath("7.png"));
        stickers.add(stickerPath("8.png"));
        stickers.add(stickerPath("9.png"));
        stickers.add(stickerPath("10.png"));
        stickers.add(stickerPath("11.png"));
        stickers.add(stickerPath("12.png"));
        stickers.add(stickerPath("13.png"));
        stickers.add(stickerPath("14.png"));
        stickers.add(stickerPath("15.png"));
        stickers.add(stickerPath("16.png"));
        stickers.add(stickerPath("17.png"));
        stickers.add(stickerPath("18.png"));
        stickers.add(stickerPath("19.png"));
        stickers.add(stickerPath("20.png"));
        stickers.add(stickerPath("21.png"));
        stickers.add(stickerPath("22.png"));
        stickers.add(stickerPath("23.png"));
        stickers.add(stickerPath("24.png"));
        return stickers;
    }

    private static String stickerPath(String stickerName) {
        return "file:///android_asset/stickers/" + stickerName;
    }

}
