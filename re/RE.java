package re;

import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

/**
 * Constructs an NFA from a regular expression
 * (RegEx) by parsing.
 */
public class RE implements REInterface {

	private String re; 				//regular expression string - access assumed by other methods. As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	private int stateCount = 0;

	private final char STAR = '*';
	private final char E = 'e';
	private final char OR = '|';
	private final char START_PRE = '(';
	private final char CLOSE_PRE = ')';


	/**
	 * Constructor
	 * @param re regular expression as string type
	 */
	public RE(String re) {
		this.re = re;
	}

	/**
	 * Recursive method. Returns the equivalent NFA for the RegEx used to instantiate RE object
	 * @see REInterface#getNFA()
	 */
	@Override
	public NFA getNFA() {
		NFA term = term();

		if (more() && peek() == OR) { 			//if there are still chars to consume in the regex string && the next char is an OR then need to create ONE NFA from both NFAs
			eat(OR);
			NFA nfa2 = getNFA();
			NFAState nfastate = (NFAState) term.getStartState();   			// getStartState returns a State, so must be casted to NFAState type to call NFAState methods later
			NFAState nfastate2 = (NFAState) nfa2.getStartState();   	// recursion returns the next

			term.addNFAStates(nfa2.getStates());
			term.addAbc(nfa2.getABC());

			String startState = String.valueOf(stateCount++);
			term.addStartState(startState);
			String finalState = String.valueOf(stateCount++);
			term.addFinalState(finalState);

			term.addTransition(startState, E, nfastate.getName());
			term.addTransition(startState, E, nfastate2.getName());

			for(State s:nfa2.getFinalStates()){
				NFAState state = (NFAState)s;
				state.setNonFinal();
				term.addTransition(state.getName(),E, finalState);
			}
		}
		return term;

	}

	/**
	 * Inspired by https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * EX: base()
	 */
	private NFA base() {
		NFA nfa;

		if(peek() == START_PRE){ // If a ( is detected next, showing precedence then
			eat(START_PRE);      // consume the open parenthesis
			nfa = getNFA();		 // get the NFA for the reGex inside the () recursively
			eat(CLOSE_PRE);		 // consume the closing parentheses
		} else{
			char c = next();	 // get the next char in the regex

			nfa = new NFA();	 // since no precedence was detected, instantiate a new NFA to return

			String startState = String.valueOf(stateCount++);  //get the startState name from the number of states - will be used to set startState then a finalState of stateCount + 1
			nfa.addStartState(startState);  //add the start state to the NFA

			String finalState = String.valueOf(stateCount++);
			nfa.addFinalState(finalState); //add this finalState to the NFA

			nfa.addTransition(startState, c, finalState);  //add a transition over char c read from the regEx from the Start state to the finalState
		}
		return nfa;
	}

	/**
	 * Inspired by https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * EX: factor()
	 */
	private NFA factor() {
		NFA nfa = base(); //get the NFA over the next char OR if () then get that NFA
		while (more() && peek() == STAR) { //until there are no additional *
			eat(STAR); //consume the *

			NFAState nfaState = (NFAState) nfa.getStartState();
			for(State s : nfa.getFinalStates()){ 					//for each final state of the nfa, add a transition over e to the StartState
				nfa.addTransition(s.getName(), E, nfaState.getName());
			}

			String state = String.valueOf(stateCount);
			nfa.addStartState(state); // state is now the start state, previous start state is just a regular state.
			nfa.addFinalState(state); // state is a final state
			nfa.addTransition(state, E, nfaState.getName()); //add transition over e, from the new state of name stateCount to the previously start state
			stateCount++;
		}
		return nfa ;
	}

	/**
	 * Inspired by https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * EX: term()
	 */
	private NFA term() {
		NFA factor = new NFA(); //generate a new NFA to add states to
		factor.addStartState(String.valueOf(stateCount++));
		factor.addFinalState(String.valueOf(stateCount));
		factor.addTransition(factor.getStartState().getName(), E, String.valueOf(stateCount)); //establish transition over e from start to final state
		stateCount++;

		while (more() && peek() != CLOSE_PRE && peek() != OR) { //as suggested in https://matt.might.net/articles/parsing-regex-with-recursive-descent/
			NFA nextFactor;

			if(peek() == STAR){
				nextFactor = base();
			} else{
				nextFactor = factor();
			}
			factor.addAbc(nextFactor.getABC());

			for(State s : factor.getFinalStates()){ //for each final state
				NFAState state = (NFAState)s;
				state.setNonFinal();
				state.addTransition( E, (NFAState)nextFactor.getStartState());
			}

			factor.addNFAStates(nextFactor.getStates());
		}

		return factor;
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 *  Returns the next item of input without consuming it
	 */
	private char peek() {
		return re.charAt(0);
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * Consumes the next item of input, failing if not equal to item
	 */
	private char eat(char c) {
		re = re.substring(1);
		return c;
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * Returns the next item of input and consumes it
	 */
	private char next() {
		return eat(peek());
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * The nonstandard primitve more() checks if there is more input available.
	 */
	private boolean more() {
		return re.length() > 0;
	}
}