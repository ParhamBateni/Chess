package View;

import javafx.scene.media.AudioClip;

public class Sound {
    private static AudioClip audioClip;
    public static void play(SoundType soundType) {
        audioClip=new AudioClip(soundType.url);
        audioClip.play();
    }

    public enum SoundType {
        START("start.mp3"),
        MOVE("move.mp3"),
        CAPTURE("capture.mp3"),
        CASTLE("castle.mp3"),
        CHECK("check.mp3"),
        END("end.mp3");

        private String url;

        SoundType(String name) {
            this.url=String.valueOf(getClass().getResource("/Visuals/mp3/"))+name;
        }
    }

}
