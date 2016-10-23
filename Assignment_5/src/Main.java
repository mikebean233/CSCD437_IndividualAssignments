import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main implements Runnable {
    private enum Operator{
        LessThen,
        LessThenOrEqualTo,
        EqualTo,
        NotEqualTo,
        GreaterThenOrEqualTo,
        GreaterThen,
        Even,
        Odd
    }

    private class Condition{
        private Operator operator;
        private int value;
        public Condition(int inValue, Operator inOperator){
            if(inOperator == null)
                throw new NullPointerException();

            operator = inOperator;
            value = inValue;
        }

        public boolean verify (int testValue){
            switch(operator){
                case LessThen:             return testValue <  value;
                case LessThenOrEqualTo:    return testValue <= value;
                case EqualTo:              return testValue == value;
                case NotEqualTo:           return testValue != value;
                case GreaterThenOrEqualTo: return testValue >= value;
                case GreaterThen:          return testValue >  value;
                case Even:                 return testValue % 2 == 0;
                case Odd:                  return testValue % 2 != 0;
                default: throw new UnsupportedOperationException("An invalid enum was detected");
            }
        }
    }

    private class StringVerifier{
        String regex;
        ArrayList<Condition> conditions = new ArrayList<>(); // All the conditions that must be tru in order for

        public StringVerifier(String inRegex, Condition[] inConditions)
        {
            if(inRegex == null || inConditions == null || inConditions.length == 0)
                throw new NullPointerException();

            for(Condition thisCondition : inConditions)
                conditions.add(thisCondition);

            this.regex = inRegex;
        }

        public boolean verify(String inputString){
            if(inputString == null)
                throw new NullPointerException();

            Matcher matcher = Pattern.compile(regex).matcher(inputString);
            int matchCount = 0;
            while(matcher.find())
                ++matchCount;

            if(matchCount == 0 && matcher.matches())
                matchCount = 1;

            for(Condition thisCondition : conditions)
                if(!thisCondition.verify(matchCount))
                    return false;
            return true;
        }
    }

    private class RegexMenuItem{
        private StringVerifier[] verifiers;
        private String description;

        public RegexMenuItem(StringVerifier[] inVerifiers, String inDescription){
            verifiers = inVerifiers;
            description = inDescription;
        }

        public RegexMenuItem(String inRegex, String inDescription){
            if(inRegex == null || inDescription == null)
                throw new NullPointerException();

            verifiers = new StringVerifier[]{new StringVerifier(inRegex, new Condition[]{new Condition(0, Operator.GreaterThen)})};
            description = inDescription;
        }

        public String getDescription(){return this.description;}

        public boolean checkForMatch(String inputString){
            for(StringVerifier thisVerifier : verifiers)
                if(!thisVerifier.verify(inputString))
                    return false;
            return true;
        }
    }

    Hashtable<String, RegexMenuItem> menuMap = new Hashtable<> ();

    private void buildMenu(){
        menuMap.put("a", new RegexMenuItem("^[0-9]{3}(\\s|-|())[0-9]{2}\\1[0-9]{4}$"                                        ,"Social Security Number"));
        menuMap.put("b", new RegexMenuItem("^(\\([0-9]{3}\\)|[0-9]{3})(\\s|())[0-9]{3}(\\s|()|-)[0-9]{4}$"                  ,"US Phone number"));
        menuMap.put("c", new RegexMenuItem("^([a-zA-Z0-9]|_)*@([a-zA-Z0-9]|_)*\\.[a-zA-Z]{2,3}$"                            ,"E-mail address"));
        menuMap.put("d", new RegexMenuItem("^[A-Z][a-z]+,\\s[A-Z][a-z]+,\\s[A-Z]+$"                                         ,"Name on a class roster, assuming one or more middle initials - Last name, First name, MI"));
        menuMap.put("e", new RegexMenuItem("^((((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(01|1[0-9]|2[0-9])))-[0-9]{2}([13579][26]|[02468][048]))|((((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(01|1[0-9]|2[0-8])))-[0-9]{2}([02468][135679]|[13579][01345789]))$","Date in MM-DD-YYYY format"));
        menuMap.put("f", new RegexMenuItem("^[1-9][0-9]+\\s[A-Z][a-z]*\\s(?i)(street|road|boulevard|avenue|st|rd|blvd|ave)$","House address - Street number, street name, abbreviation for road, street, boulevard or avenue"));
        menuMap.put("g", new RegexMenuItem("^([A-Z][a-z]{2,}\\s)*([A-Z][a-z]{2,}|A[LKZRS]|C[AOT]|D[CE]|F[LM]|G[AU]|I[DLNA]|K[SY]|LA|M[EDAINHPSOT]|N[EVHJMYCD]|O[HKR]|P[AWR]|RI|S[CD]|T[NX]|UT|V[ITA]|W[AVIY])\\s\\d{5}(-\\d{4})?$","City followed by state followed by zip as it should appear on a letter"));
        menuMap.put("h", new RegexMenuItem("^([01]\\d|2[0-3])[0-5]\\d:[0-5]\\d$"                                            ,"Military time, including seconds"));
        menuMap.put("i", new RegexMenuItem("^\\$[^,0]([1-9]|[1-9]\\d|[1-9]\\d\\d|())((?<comma>(,|()))\\d{3})?(\\k<comma>\\d{3})*\\.\\d{2}$","US Currency down to the penny (ex: $123,456,789.23)"));
        menuMap.put("j", new RegexMenuItem("^http:\\/\\/([^\\/\\s]*\\/[^\\/\\s]*)*"                                         ,"URL, including http:// (upper and lower case should be accepted)"));
        menuMap.put("k", new RegexMenuItem(
                new StringVerifier[]{
                        new StringVerifier("[^\\s]" , new Condition[]{new Condition(10, Operator.GreaterThenOrEqualTo)}),
                        new StringVerifier("[A-Z]"  , new Condition[]{new Condition( 1, Operator.GreaterThenOrEqualTo)}),
                        new StringVerifier("[a-z]"  , new Condition[]{new Condition( 1, Operator.GreaterThenOrEqualTo)}),
                        new StringVerifier("\\d"    , new Condition[]{new Condition( 1, Operator.GreaterThenOrEqualTo)}),
                        new StringVerifier("[\\!\\\"\\#\\$\\%\\&\\'\\(\\)\\*\\+\\,\\.\\/\\:\\;\\<\\=\\>\\?\\@\\\\\\^\\_\\`\\{\\|\\}\\~\\-]", new Condition[]{new Condition(1, Operator.GreaterThenOrEqualTo)}),
                        new StringVerifier("[a-z]{4,}", new Condition[]{new Condition(0, Operator.EqualTo)}),
                }, "A password that contains at least 10 characters and includes at least one upper case character, lower case character, digit, punctuation mark, and does not have more than 3 consecutive lower case characters")
        );
        menuMap.put("l", new RegexMenuItem(
                new StringVerifier[]{
                        new StringVerifier("ion$"    , new Condition[]{new Condition( 1, Operator.EqualTo)}),
                        new StringVerifier("\\s"     , new Condition[]{new Condition( 0, Operator.EqualTo)}),
                        new StringVerifier("[a-zA-Z]", new Condition[]{new Condition(-1, Operator.Odd)})
                },  "All words containing an odd number of alphabetic characters, ending in \"ion\"")
        );
    }

    public static void main(String[] args){
        new Main().run();
    }

    public void run(){
        buildMenu();
        boolean goAgain = true;
        Scanner stdinScanner = new Scanner(System.in);
        do{
            printMenu();
            String menuChoice = stdinScanner.nextLine();
            if(menuMap.containsKey(menuChoice)){
                RegexMenuItem thisRegexMenuItem = menuMap.get(menuChoice);
                System.out.print("Enter input string: ");
                String inputString = stdinScanner.nextLine();
                processRegexItem(inputString, thisRegexMenuItem);
            }
            else if(menuChoice.equals("q")){
                goAgain = false;
            } else {
                System.err.println(menuChoice + " is not a valid option, try again");
            }
        }while(goAgain );
    }


    public void processRegexItem(String inputString, RegexMenuItem regexMenuItem){
        if(regexMenuItem == null || inputString == null)
            throw new NullPointerException();

        System.out.println();
        System.out.println("---------- Processing new Regex -------------");
        System.out.println("expression description:    " + regexMenuItem.getDescription());
        System.out.println("input string to be tested: " + inputString);
        System.out.println("Do we have a match? :      " + regexMenuItem.checkForMatch(inputString));
        System.out.println("---------------------------------------------");
    }

    public void printMenu(){
        System.out.println("         ---- Choose an expression type ----          ");

        // Get our keyset and sort it
        String[] keys = this.menuMap.keySet().toArray(new String[]{});
        Arrays.sort(keys);

        for(String thisKey : keys){
            System.out.println(thisKey + ". " + this.menuMap.get(thisKey).getDescription());
        }
        System.out.println("q. quit");
        System.out.println("> ");
    }
}
