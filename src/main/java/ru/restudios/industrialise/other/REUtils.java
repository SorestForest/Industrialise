package ru.restudios.industrialise.other;

public class REUtils {


    public static <T> T castOrNull(Class<T> clazz, Object cast){
        if (clazz.isAssignableFrom(cast.getClass())){
            return clazz.cast(cast);
        }
        return null;
    }

    public static int keepInRange(int value, int minimal, int maximal){
        if (value <= minimal) { return minimal; }
        else if (value >= maximal) { return maximal;}
        return value;
    }
}
