package Input;

import data.CommandError;
import data.GameInputReturn;
import data.PrintTemplate;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameInput {
    static int NOT_MINE = -1; //유신님 코드 불러야함
    static int ERROR = GameInputReturn.ERROR.getCode();
    static int HELP = GameInputReturn.HELP.getCode();

    static int EXIT = GameInputReturn.EXIT.getCode();
    static int START = GameInputReturn.START.getCode();
    static int QUIT = GameInputReturn.QUIT.getCode();
    static int SAVE = GameInputReturn.SAVE.getCode();
    static int LOAD = GameInputReturn.LOAD.getCode();
    static int DEL_SAVE = GameInputReturn.DEL_SAVE.getCode();
    static int SAVE_FILE = GameInputReturn.SAVE_FILE.getCode();
    static int REGISTER = GameInputReturn.REGISTER.getCode();
    static int LOGIN = GameInputReturn.LOGIN.getCode();
    static int LOGOUT = GameInputReturn.LOGOUT.getCode();
    static int TOGGLE = GameInputReturn.TOGGLE.getCode();
    static int OPTION = GameInputReturn.OPTION.getCode();


    public static int number = 0;
    public static String input;

    public static void main(String[] args) {
        gameInput();
    }

    public static int gameInput() {
        // 이거 input을 받아서 튀어 나오게 해야할듯
        //return은 int로 하고
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        return checkInput();
    }

    private static int checkInput() {
        if(input.isEmpty()){
            return UserInput.handleInput(input);
        }
        if(input.charAt(0)=='/'){
            try{
                return checkOrderInput();
            } catch (Exception e) {
                if(e instanceof NumberFormatException){
                    System.out.println( PrintTemplate.BOLDLINE + "\n"
                            + CommandError.WRONG_NUMBER + "\n"
                            + PrintTemplate.BOLDLINE);

                }
                else{
                    System.out.println(PrintTemplate.BOLDLINE + "\n"
                            + CommandError.WRONG_COMMAND + "\n"
                            + PrintTemplate.BOLDLINE);

                }
                return ERROR;
            }

        }
        // 유신님 인풋으로 보내기
        return UserInput.handleInput(input);

    }
    private static int checkOrderInput(){
        for(int i = 1; i<input.length(); i++){
            if(input.charAt(i)=='/'){
                throw new InputMismatchException("/가 너무 많음");
            }
        }
        String[] parts = input.split("/");
        if(parts[1].startsWith("help")){
            String now = parts[1].substring("help".length());
            if(checking(now, HELP) != 0){
                number = checking(now, HELP);
                return HELP;
            }
            else{
                throw new InputMismatchException("help 실패");
            }
        }
        else if(parts[1].startsWith("savefile")){//인자없음
            parts[1] =  blank(parts[1]);
            if(parts[1].equals("savefile")){
                return SAVE_FILE;
            }
            else{
                throw new InputMismatchException("savefile 실패");
            }

        }
        else if(parts[1].startsWith("save")){
            String now = parts[1].substring("save".length());
            if(checking(now,SAVE)!=0){
                number= checking(now,SAVE);
                return SAVE;// 파일매니저로 보내기
            }
            else{
                throw new InputMismatchException("save 실패");
            }

        } //인자있음

        else if(parts[1].startsWith("delsave")){//아님
            String now = parts[1].substring("delsave".length());
            if(checking(now,DEL_SAVE)!=0){
                number= checking(now,DEL_SAVE);
                return DEL_SAVE;
            }
            else{
                throw new InputMismatchException("delsave 실패");
            }


        }//인자있음
        else if(parts[1].startsWith("load")){
            String now = parts[1].substring("load".length());
            if(checking(now,LOAD)!=0){
                number= checking(now,LOAD);
                return LOAD; // 파일매니저로보내기
            }
            else{
                throw new InputMismatchException("load 실패");
            }
        }//인자있음
        else if(parts[1].startsWith("start")){
            String now = parts[1].substring("start".length());
            if(checking(now,START)!=0){
                number= checking(now,START);
                return START;
            }
            else{
                throw new InputMismatchException("start 실패");
            }

        }
        else if(parts[1].startsWith("quit")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("quit")){
                return QUIT;
            }
            else{
                throw new InputMismatchException("quit 실패");
            }
        }
        else if(parts[1].startsWith("exit")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("exit")){
                return EXIT;
            }
            else{
                throw new InputMismatchException("exit 실패");
            }
        }
        else if (parts[1].startsWith("resgister")){
            parts[1]=  blank(parts[1]);
            if(parts[1].equals("resgister")){
                return REGISTER;
            }
            else{
                throw new InputMismatchException("resgister 실패");
            }
        }
        else if (parts[1].startsWith("login")){
            parts[1]=  blank(parts[1]);
            if(parts[1].equals("login")){
                return LOGIN;
            }
            else{
                throw new InputMismatchException("login 실패");
            }
        }
        else if (parts[1].startsWith("logout")){
            parts[1]=  blank(parts[1]);
            if(parts[1].equals("logout")){
                return LOGOUT;
            }
            else{
                throw new InputMismatchException("logout 실패");
            }
        }
        else if(parts[1].startsWith("toggle")){
            String now = parts[1].substring("toggle".length());
            if(checkStr(now)){
                return TOGGLE;
            }
            else{
                throw new InputMismatchException("toggle 실패");
            }
        }
        else if (parts[1].startsWith("option")){
            parts[1]=  blank(parts[1]);
            if(parts[1].equals("option")){
                return OPTION;
            }
            else{
                throw new InputMismatchException("option 실패");
            }
        }


        else{
            throw new InputMismatchException("없는 구문");
        }

    }

    private static String blank(String parts){
        return parts.chars()
                .filter(c -> !Character.isWhitespace(c))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    private static int checking(String now, int cmdCode) {

        int max;
        if(cmdCode==SAVE|| cmdCode==DEL_SAVE|| cmdCode==LOAD){
            max=3;
        }
        else{
            max=4;
        }
        if (Character.isWhitespace(now.charAt(0)) && now.charAt(1) >= '0' && now.charAt(1) <= '9') {
            boolean number = true;
            for(int i=2; i<now.length(); i++){
                boolean b = now.charAt(i) >= '0' && now.charAt(i) <= '9';
//                    if(!b&&!Character.isWhitespace(now.charAt(i))){ throw new Exception(); }
                if(number){
                    if(!(b)){
                        number =false;
                    }
                }
                else{
                    if(b){
                        throw new InputMismatchException();
                    }
                }
            }

            now = blank(now);
            int num;
            try{
                num= Integer.parseInt(now);
            } catch (NumberFormatException e) {
                throw new InputMismatchException();
            }

            if (num >= 1 && num <= max) {
                return num;
            }
            if(num>=0 && num <=9){
                throw new NumberFormatException(" 1부터 5사이가 아님");
            }
            else {
                throw new InputMismatchException();
            }
        } else {
            throw new InputMismatchException();
        }
    }
    private static boolean checkStr(String now){
        if(Character.isWhitespace(now.charAt((0)))){
            String[] parts = now.split(" ");

            if(parts.length != 2) {
                throw new InputMismatchException();
            }
            parts[0] = blank(parts[0]);
            parts[1] = blank(parts[1]);
            boolean isRuleValid = true;
            boolean isOnValid = true;

            // check special rule str
            if(parts[0].startsWith("enpassant")){}
            else if(parts[0].startsWith("castling")){}
            else if(parts[0].startsWith("promotion")){}
            else isRuleValid = false;

            // check on/off str
            if(parts[1].startsWith("on")){}
            else if (parts[1].startsWith("off")){}
            else isOnValid = false;

            if(isRuleValid && isOnValid){
                return true;
            }else{
                throw new InputMismatchException();
            }
        }

        return false;
    }

}
