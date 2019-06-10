package red.github.meowsic;

public class MusicItem {
    private String musicName;
    private String musicArtist;
    private String albumName;
    private String albumPicture;
    private String mid;

    public MusicItem(String musicName, String musicArtist, String albumName, String albumPicture, String mid){
        this.musicName = musicName;
        this.musicArtist = musicArtist;
        this.albumName = albumName;
        this.albumPicture = albumPicture;
        this.mid = mid;
    }

    public String getMid(){
        return this.mid;
    }
}
