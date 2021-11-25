package re;

import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

/**
 * Constructs an NFA from a regular expression
 * (RegEx) by parsing.
 */
public class RE implements REInterface {

	private String re; //regular expression string - access assumed by other methods
	private int stateCount = 0;

	private final char STAR = '*';
	private final char E = 'e';
	private final char OR = '|';
	private final char START_PRE = '(';
	private final char CLOSE_PRE = ')';


	/**
	 * Construcor
	 * @param re regular expression as string type
	 */
	public RE(String re) {
		this.re = re;
	}

	/**
	 * Returns the equivalent NFA for the RegEx used to instantiate RE object
	 * @see re.REInterface#getNFA()
	 */
	@Override
	public NFA getNFA() { //TODO: refactor code
		NFA term = term();

		if (more() && peek() == OR) {
			eat(OR);

			NFAState nfaState = (NFAState) term.getStartState();
			NFAState nfaState2 = (NFAState) getNFA().getStartState();

			term.addNFAStates(getNFA().getStates());
			term.addAbc(getNFA().getABC());

			String startState = Integer.toString(stateCount++);
			term.addStartState(startState);
			String finalState = Integer.toString(stateCount++);
			term.addFinalState(finalState);

			term.addTransition(startState, E, nfaState.getName());
			term.addTransition(startState, E, nfaState2.getName());

			for(State s:getNFA().getFinalStates()){
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
	private NFA base() { //TODO: refactor code
		NFA nfa;

		if(peek() == START_PRE){ //check redEx for specified precedence
			eat(START_PRE);
			nfa = getNFA();
			eat(CLOSE_PRE);
		} else{
			char c = next();

			nfa = new NFA();

			String startState = Integer.toString(stateCount++);
			nfa.addStartState(startState);

			String finalState = Integer.toString(stateCount++);
			nfa.addFinalState(finalState);

			nfa.addTransition(startState, c, finalState);
		}

		return nfa;
	}

	/**
	 * Inspired by https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * EX: factor()
	 */
	private NFA factor() {  //TODO: refactor code
		NFA base = base() ;
		while (more() && peek() == STAR) {
			eat(STAR) ;

			NFAState nfaState = (NFAState) base.getStartState();
			for(State nfa : base.getFinalStates()){
				base.addTransition(nfa.getName(), E, nfaState.getName());
			}

			String state = Integer.toString(stateCount++);
			base.addStartState(state);
			base.addFinalState(state);
			base.addTransition(state, E, nfaState.getName());
		}
		return base ;
	}

	/**
	 * Inspired by https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * EX: term()
	 */
	private NFA term() { //TODO: refactor code
		NFA factor = new NFA();
		factor.addStartState(String.valueOf(stateCount++));
		factor.addFinalState(String.valueOf(stateCount));
		factor.addTransition(factor.getStartState().getName(), E, String.valueOf(stateCount));
		stateCount++;

		while (more() && peek() != CLOSE_PRE && peek() != OR) { //as suggested in https://matt.might.net/articles/parsing-regex-with-recursive-descent/
			NFA nextFactor = factor();
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