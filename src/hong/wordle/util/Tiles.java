package hong.wordle.util;

public enum Tiles {

    ABSENT('A'), PRESENT('P'), CORRECT('C');

    private char c;
    Tiles(char c){
        this.c = c;
    }
    private static Tiles[] tiles = new Tiles[]{
            ABSENT, null, CORRECT, null, null, null, null, null, null, null, null, null, null, null, null, PRESENT,
            null, null, null, null, null, null, null, null, null, null
    };

    public static Tiles valueOf(char c) throws NullPointerException {
        if(Character.isUpperCase(c)){
            return tiles[c-'A'];
        }else if(Character.isLowerCase(c)){
            return tiles[c-'a'];
        }
        throw new IllegalArgumentException();
    }
    public char toChar(){
        return c;
    }
}
