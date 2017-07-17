/**
 * Created by paul on 05.07.17.
 */
public class TestStuff {

    public static void main(String... args){
        String str = "DFA";
        str = decrement(str);
        System.out.println(str);
    }

    public static String decrement(String str){
        if((str.charAt(str.length()-1)) == 'A'){
            return decrement(str.substring(0, str.length()-1)) + 'Z';
        }
        return str.substring(0, str.length()-1)+((char)(str.charAt(str.length()-1)-1));
    }
}
