import java.util.ArrayList;

public class Calculator{

    double currentCount;
    ArrayList<Character> characters;
    int furthestLeft;
    int furthestRight;
    int leftParenthesis;
    int rightParenthesis;
    int leftPosSwitch; // keeps track of operator position switch while remove parenthesis
    int rightPosSwitch;
 
    public Calculator(){
        currentCount = 0;
        characters = new ArrayList<Character>();
        furthestLeft = 0;
        leftParenthesis = -1;
        rightParenthesis = -1;
        leftPosSwitch = -1;
        rightPosSwitch = -1;
    }

    public double addition(double left, double right){
        return left + right;
    }

    public double subtraction(double left, double right){
        return left - right;
    }

    public double multiplication(double left, double right){
        return left * right;
    }

    public double division(double left, double right){
        return left / right;
    }

    public String buildFinalAnswer(){
        String full = String.valueOf(currentCount);
        String answer = stripTrailingZeros(full);
        return answer;
    }

    public void checkNumberOfOperators(String input){ // Strange edge cases to explore here
        int operatorCount = 0;
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == '+' || input.charAt(i) == '-' || input.charAt(i) == '*' ||input.charAt(i) == '/'){
                operatorCount++;
            }
        }
        if(operatorCount == 0 || (operatorCount == 1 && input.charAt(0) == '-')){ // check for - in front of a number as only operator
            System.out.println("Invalid Operator structure");
            System.exit(1);
        }
    }

    public int findCorrespondingParenthesis(int location){ // finds the corresponding parenthesis to a )
        int index = -1;
        for(int i=location; i > -1; i--){
            if(characters.get(i) == '('){
                index = i;
                break;
            }
        }
        return index;
    }

    public int findHighestOperatorParenthesis(int leftBracket, int rightBracket){ // recurssive function that finds the deepest nested ()
        int highestOpPos = -1;
        char highestOpChar = 'c';
        
        for(int i = rightBracket -1; i > leftBracket; i--){
            if( (characters.get(i) == '+' || characters.get(i) == '-') && (highestOpChar == 'c' || highestOpChar == '+' || highestOpChar == '-') && (i != 0)){
                highestOpPos = i;
                highestOpChar = characters.get(i);
            }
            if((characters.get(i) == '*' ||  characters.get(i) == '/' && (highestOpChar != '(' && highestOpChar != ')')) ){
                highestOpPos = i;
                highestOpChar = characters.get(i);
            }
            if(characters.get(i) == ')'){
                int leftBracket2 = findCorrespondingParenthesis(i);
                int rightBracket2 = i;
                leftParenthesis = leftBracket2;
                rightParenthesis = rightBracket2;
                int operator = findHighestOperatorParenthesis(leftBracket2,rightBracket2);
                highestOpPos = operator;
            }
        }
        
        if (highestOpChar == 'c'){
            return highestOpPos;
        }
        return highestOpPos;
    }

    public int findHighestOperator(){ // finds the location of the most significant operator and returns its index
        int highestOpPos = -1;
        char highestOpChar = 'c';
        for(int i = characters.size() -1 ; i > -1 ; i--){
            if( (characters.get(i) == '+' || characters.get(i) == '-') && (highestOpChar == 'c' || highestOpChar == '+' || highestOpChar == '-') && (i != 0)){
                highestOpPos = i;
                highestOpChar = characters.get(i);
            }
            if((characters.get(i) == '*' ||  characters.get(i) == '/' && (highestOpChar != '(' && highestOpChar != ')')) ){
                highestOpPos = i;
                highestOpChar = characters.get(i);
            }

            if(characters.get(i) == ')'){
                int leftBracket = findCorrespondingParenthesis(i);
                int rightBracket = i;
                leftParenthesis = leftBracket;
                rightParenthesis = rightBracket;
                int operator = findHighestOperatorParenthesis(leftBracket,rightBracket);
                highestOpPos = operator;
                highestOpChar = characters.get(highestOpPos);
                break;
            }
        }
        if (highestOpChar == 'c'){
            return highestOpPos;
        }
        return highestOpPos;
    }

    public void makeCalculation(int operatorPos){
        double left = readLeft(operatorPos);
        double right = readRight(operatorPos);
        double answer = 0;
        if(characters.get(operatorPos) == '+'){
            answer = addition(left, right);
        }
        if(characters.get(operatorPos) == '-'){
            answer = subtraction(left, right);
        }
        if(characters.get(operatorPos) == '*'){
            answer = multiplication(left, right);
        }
        if(characters.get(operatorPos) == '/'){
            answer = division(left, right);
        }
        currentCount = answer;
    }

    public void removeParenthesis(){ // removes parenthesis after calculating
        if(leftParenthesis != -1 && rightParenthesis != -1){
            characters.remove(leftParenthesis);
            characters.remove(rightParenthesis - 1); // since we just removed a char subract 1
            leftPosSwitch = leftParenthesis;
            rightPosSwitch = rightParenthesis;
            leftParenthesis = -1;
            rightParenthesis = -1;
        }
    }

    public void readInput(String input){ // reads string into ArrayList
        for(int i = 0; i < input.length(); i++){
            characters.add(input.charAt(i));
        }
        furthestRight = input.length() -1;
    }

    public double readLeft(int operatorPos){ // reads all characters left of an operator
        String myNumLeft = "";
        double left;
        int negativeCountLeft = 0;
        boolean signFlag = false;
        for(int i = operatorPos-1; i > -1 ; i--){ // negative numbers may increment the operator counter. This accounts for that possibility. 
            if(characters.get(i) == '-'){
                negativeCountLeft += 1;
            }
            if(characters.get(i) == '+' || characters.get(i) == '*' || characters.get(i) == '/' || characters.get(i) == '(' || negativeCountLeft > 1){
                furthestLeft = i+1;
                signFlag = true;
                break;
            }
            myNumLeft += characters.get(i);
        }
        myNumLeft = reverseString(myNumLeft);
        left = Double.parseDouble(myNumLeft); 
        if(!signFlag){
            furthestLeft = 0;
        }
        return left;
    }

    public double readRight(int operatorPos){ // reads all characters right of an operator
        String myNumRight = "";
        double right;
        boolean signFlag = false;
        for(int i = operatorPos+1; i < characters.size(); i++){
            if(characters.get(i) == '+' ||  characters.get(i) == '*' || characters.get(i) == '/' || characters.get(i) == ')'  ||(i > operatorPos + 1 && characters.get(i) == '-')){
                furthestRight = i;
                signFlag = true;
                break;
            }
            myNumRight += characters.get(i);
        }
        right = Double.parseDouble(myNumRight); 
        if(!signFlag ){
            furthestRight += (characters.size() - furthestRight); // there is no last sign to catch the furthest right so to get the last number take the size of the array - the last sign
        }
        return right;
    }

    public void removeEquationChunk(){ // removes a calcualted chunk of characters form the array. 
        ArrayList<Character> temp = new ArrayList<Character>();
        String currentCountStr = String.valueOf(currentCount); 
        for (int i =0 ; i < furthestLeft; i ++){
            temp.add(characters.get(i));
        }
        for (int i = 0; i < currentCountStr.length(); i++){
            temp.add(currentCountStr.charAt(i));
        }
        for (int i = furthestRight; i <  characters.size(); i++){
            temp.add(characters.get(i));
        }
        characters = temp;
    }

    public String reverseString(String input){ // reverses a string
        String reverse = "";
        for(int i = input.length()-1; i > -1; i--){
            reverse += input.charAt(i);
        }
        return reverse;
    }

    public String stripTrailingZeros(String answer){ // removes trailing zeros. 
        int dotIndex = -1;
        boolean dotFound = false;
        boolean okToStrip = true;
        for(int i = 0; i < answer.length(); i++){
            if(dotFound){
                if(answer.charAt(i) != '0'){
                    okToStrip = false;
                }
            }
            if(answer.charAt(i) == '.'){
                dotIndex = i;
                dotFound = true;
            }
        }
        if(okToStrip){
            answer = answer.substring(0,dotIndex);
        }
        return answer;
    }

    public void validateParenthesis(String input){ // simple error checking to validate parenthesis pairing
        int leftCount = 0; // (
        int rightCount = 0; //)
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == '('){
                leftCount++;
            }
            if(input.charAt(i) == ')'){
                rightCount++;
            }
            if (rightCount > leftCount){
                System.out.println("Invalid parenthesis starting at: " + Integer.toString(i));
                System.exit(1);
            }
        }
        if (leftCount != rightCount){
            System.out.println("Parenthesis count does not match");
            System.exit(1);
        }

    }

    public void checkSyntax(String input){ // looks for invalid syntax in an expression
        char prevChar = 'Q';
        char currChar = 'R';
        int numDigits = 0;
        if(input.length() < 3){ // makes sure there are enough characters to make a calculation
            System.out.println("Not enough characrters to make a calculation");
            System.exit(1);
        }
        for(int i = 0; i < input.length(); i++){ // check for number of digits and invalid operators at start of expression.
            if(Character.isDigit(input.charAt(i))){
                numDigits++;
            }
        }
        if(numDigits < 2){
            System.out.println("Please enter at least 2 digits");
            System.exit(1);
        }
        checkNumberOfOperators(input);
        if(input.charAt(0) == '+' || input.charAt(0) == '*' || input.charAt(0) == '/'){ // checks for invalid starting operator
            System.out.println("Invalid operator at start of expression");
            System.exit(1);
        }
        validateParenthesis(input);
        for(int i = 1; i < input.length(); i++){
            prevChar = input.charAt(i-1);
            currChar = input.charAt(i);
            if(Character.isLetter(prevChar) || Character.isLetter(currChar)){ // check for letters
                System.out.println("A letter was found in the calculation please remove it");
                System.exit(1);
            }
            if(!Character.isDigit(prevChar) && !Character.isDigit(currChar)){ // white list approach of usable syntax combinations
                if((prevChar == '-' && currChar == '-' && i!= 1) || (prevChar == '+' && currChar == '-') || 
                    (prevChar == '*' && currChar == '-') || (prevChar == '/' && currChar == '-') || (prevChar =='-' && currChar == '.') ||
                     (prevChar == '+' && currChar == '.' && i!= 1) || (prevChar == '*' && currChar == '.' && i!= 1) || (prevChar == '/' && currChar == '.' && i!= 1) ||
                        prevChar == '(' || prevChar == ')' || currChar == '(' || currChar == ')'){ 
                }  // this is ok
           
                else {
                    System.out.println("An invalid combination of operators was found at index: " + Integer.toString(i-1));
                    System.exit(1);
                }
            }
        }
    }

    public void solveEquation(String input){ // main function of Calculator
        String noSpace = input.replaceAll("\\s+",""); // remove all whitespace
        checkSyntax(noSpace);
        readInput(noSpace);
        int operatorPos = 0;
        while (true){
            operatorPos = findHighestOperator();
            if(operatorPos == -1){ // no more operators found print final answer
                System.out.println(buildFinalAnswer());
                break;
            }
            removeParenthesis(); // removing parenthesis can affect the position of the opererator.
            if(operatorPos > leftPosSwitch && leftPosSwitch != -1){
                operatorPos--;
                leftPosSwitch = -1;
            }
            if(operatorPos > rightPosSwitch && rightPosSwitch != -1){
                operatorPos--;
                rightPosSwitch = -1;
            }
            makeCalculation(operatorPos); // there is a perenthesis on the left side of the operator;
            removeEquationChunk();
        }
    }
public static void main(String[] args){
    if (args.length != 1){
        System.out.println("Please provide one input to the calculator. Ex: \"1 + 1\"");
        System.exit(1);
    };
    Calculator myCalc = new Calculator();
    myCalc.solveEquation(args[0]);
    }   

}