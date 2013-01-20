import com.rits.cloning.Cloner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Procedural {

    static Cloner CLONER = new Cloner();

    static Set<String> TERMINALS = new HashSet<String>();
    static Set<String> NON_TERMINALS = new HashSet<String>();
    static Map<String, List<String>> RULES = new HashMap<String, List<String>>();
    static String AXIOM;

    static Set<String> USABLE_NON_TERMINALS = new HashSet<String>();
    static Set<String> REACHABLE_NON_TERMINALS = new HashSet<String>();
    
    static Set<String> UNIFIED_ALPHABET = new HashSet<String>();

    public static void main(String ... args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("input.txt"));

        AXIOM = in.nextLine();

        while(in.hasNextLine()) {
            String rule = in.nextLine();
            String[] tokens = assortRules(rule);
            assortSymbols(tokens[0], tokens[1]);
        }

        equivalenceModifications();

        first();

        a();
    }

    static String[] assortRules(String rule) {
        String[] tokens = rule.split("->");
        List<String> rules = RULES.get(tokens[0]);
        if (null != rules) rules.add(tokens[1]);
        else {rules = new ArrayList<String>(); rules.add(tokens[1]); RULES.put(tokens[0], rules);}
        return tokens;
    }

    static void assortSymbols(String lhs, String rhs) {
        NON_TERMINALS.add(lhs);

        for(String symbol : rhs.split("")) {
            if("$".equals(symbol)) {
                TERMINALS.add("");
            } else if(symbol.toLowerCase().equals(symbol)) {
                TERMINALS.add(symbol);
            } else if(symbol.toUpperCase().equals(symbol)) {
                NON_TERMINALS.add(symbol);
            }
        }
        
    }

    static void a() {
        
    }

    static void first() {
        
    }

    static void equivalenceModifications() {

        // Removing non-usable symbols may cause some symbols to become unreachable
        allSymbols();   //+

        nonUsableSymbols();  //+

        removeNonUsableSymbols();

        unreachableSymbols(); //+

        removeUnreachableSymbols();

        removeLambdaRules();

        removeChainRules();

        factorize();
    }

    static void removeUnreachableSymbols() {
        
    }

    static void allSymbols() {
        for(Map.Entry<String, List<String>> entry : RULES.entrySet()) {
            if(!UNIFIED_ALPHABET.contains(entry.getKey())) UNIFIED_ALPHABET.add(entry.getKey());
            for(String rhs : entry.getValue()) {
                UNIFIED_ALPHABET.addAll(Arrays.asList(rhs.split("")));
            }
        }
    }

    static void factorize() {

    }

    static void removeChainRules() {

    }

    static void removeLambdaRules() {

    }

    static void removeNonUsableSymbols() {
    }

    static void nonUsableSymbols() {
        Map<String, List<String>> terminalOnlyRHSRules = findTerminalOnlyRHSRules();

        Set<String> newNonTerminals = new HashSet<String>();

        newNonTerminals.addAll(terminalOnlyRHSRules.keySet());

        Set<String> nonTerminals = new HashSet<String>();

        while(!newNonTerminals.equals(nonTerminals)) {
            nonTerminals.addAll(newNonTerminals);

            for(String rKey : RULES.keySet()) {
                for(String rhs : RULES.get(rKey)) {
                    if(containsOnlyNonTerminals(rhs, nonTerminals)) {
                        newNonTerminals.add(rKey);
                        break;
                    }
                }
            }
        }
        USABLE_NON_TERMINALS.addAll(nonTerminals);
    }

    static boolean containsOnlyNonTerminals(String rhs, Set<String> nonTerminals) {
        List<String> filteredSplit = filterSplitNonTerminals(rhs);
        filteredSplit.removeAll(nonTerminals);

        return filteredSplit.isEmpty();
    }

    static List<String> filterSplitNonTerminals(String rhs) {
        List<String> split = Arrays.asList(rhs.split(""));
        List<String> filteredSplit = new ArrayList<String>();
        for(String symbol : split) {
            if(NON_TERMINALS.contains(symbol)) filteredSplit.add(symbol);
        }
        return filteredSplit;
    }


    static void unreachableSymbols() {
        Set<String> newNonTerminals = new HashSet<String>();

        newNonTerminals.add(AXIOM);
        Set<String> nonTerminals = new HashSet<String>();

        while(!newNonTerminals.equals(nonTerminals)) {
            nonTerminals.addAll(newNonTerminals);
            for(String rKey : RULES.keySet()) {
                if(newNonTerminals.contains(rKey)) {
                    for(String rhs : RULES.get(rKey)) {
                        newNonTerminals.addAll(filterSplitNonTerminals(rhs));
                    }
                }
            }
        }
        REACHABLE_NON_TERMINALS.addAll(nonTerminals);
    }

    static Map<String, List<String>> findTerminalOnlyRHSRules() {
        Map<String, List<String>> terminalOnlyRHSRules = new HashMap<String, List<String>>();

        for(Map.Entry<String, List<String>> entry : RULES.entrySet()) {
            for(String rhs : entry.getValue()) {
                if(!containsNonTerminal(rhs)) {
                    if(terminalOnlyRHSRules.get(entry.getKey()) == null) {
                        terminalOnlyRHSRules.put(entry.getKey(), new ArrayList<String>());
                    }
                    terminalOnlyRHSRules.get(entry.getKey()).add(rhs);
                }
            }
        }
        return terminalOnlyRHSRules;
    }

    static boolean containsNonTerminal(String rhs) {
        String[] split = rhs.split("");
        boolean containsNonTerminal = false;
        for(String symbol : split) {
            if(NON_TERMINALS.contains(symbol)) {
                containsNonTerminal = true;
                break;
            }
        }
        return containsNonTerminal;
    }
}
