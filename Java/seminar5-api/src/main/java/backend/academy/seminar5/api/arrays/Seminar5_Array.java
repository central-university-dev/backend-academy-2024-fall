package backend.academy.seminar5.api.arrays;

import java.util.Arrays;

public class Seminar5_Array {
    public static void main(String[] args) {
        int[] array = new int[16];
        System.out.println("array = " + Arrays.toString(array));

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        Object[] array2 = new Object[10];
        int lastIndex = 0;
        array2[lastIndex++] = new Object();
        array2[lastIndex++] = new Object();
        array2[lastIndex--] = null;

        //array2[10] = new Object();

        System.out.println("lastIndex = " + lastIndex);

        char[][] ddArray = new char[3][3];
        ddArray[0][0] = 'X';
        ddArray[0][1] = 'O';

        Object[] array3 = new Object[0];

        int newArrayLength = array3.length + 1;
        Object[] newArray = Arrays.copyOf(array3, newArrayLength);
        newArray[array3.length] = new Object();

        array3 = newArray;
        
        System.out.println("array3 = " + Arrays.toString(array3));
    }
}
