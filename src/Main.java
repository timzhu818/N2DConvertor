import objects.DFA;
import objects.NFA;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("input a regular expression");
        String re = in.nextLine();
        System.out.println("re:" + re);
        NFA nfa = new NFA(re);
        nfa.addJoinSymbol();
        nfa.postfix();
        nfa.re2nfa();
        nfa.print();

        DFA dfa = new DFA(nfa.getMate(),nfa.getLetter());
        dfa.createDFA();
        dfa.printDFA();
        
        System.out.println();
        System.out.println("re:" + re);
        System.out.println("To quit enter Q");
        while(in.hasNextLine()) {
            String string = in.nextLine();
            if(string.equals("Q"))
                break;
            System.out.println();
            System.out.println("To quit enter Q");
        }
        in.close();
    }
}
