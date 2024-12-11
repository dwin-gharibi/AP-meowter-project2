package ir.ac.kntu.Meowter.util;

public class CliFormatter {
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String UNDERLINE = "\033[4m";

    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";

    public static String bold(String text) {
        return BOLD + text + RESET;
    }

    public static String underline(String text) {
        return UNDERLINE + text + RESET;
    }

    public static String red(String text) {
        return RED + text + RESET;
    }

    public static String green(String text) {
        return GREEN + text + RESET;
    }

    public static String yellow(String text) {
        return YELLOW + text + RESET;
    }

    public static String blue(String text) {
        return BLUE + text + RESET;
    }

    public static String magenta(String text) {
        return MAGENTA + text + RESET;
    }

    public static String cyan(String text) {
        return CYAN + text + RESET;
    }

    public static String boldRed(String text) {
        return bold(red(text));
    }

    public static String boldGreen(String text) {
        return bold(green(text));
    }

    public static String boldYellow(String text) {
        return bold(yellow(text));
    }

    public static String blueUnderlined(String text) {
        return underline(blue(text));
    }

    public static void printMeow(){
        System.out.println("        /\\     /\\");
        System.out.println("       {  `---'  }");
        System.out.println("       {  O   O  }");
        System.out.println("       ~~>  V  <~~");
        System.out.println("        \\ \\|/ /");
        System.out.println("         `-----'____");
        System.out.println("         /     \\    \\_");
        System.out.println("        {       }\\  )_\\_   _");
        System.out.println("        |  \\_/  |/ / /   \\_/ \\");
        System.out.println("         \\__/  /(_/ /     \\__/");
        System.out.println("           (__/");
        System.out.println();
        System.out.println("   ~~~~~~~~~ Meowter ~~~~~~~~~");
    }
}

