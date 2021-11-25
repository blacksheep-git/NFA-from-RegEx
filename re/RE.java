package re;

import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

/**
 * TODO//
 */
public class RE implements REInterface {

	private String regEx; //regular expression string - access assumed by other methods
	private int stateCount = 0;

	private final char STAR = '*';
	private final char E = 'e';
	private final char OR = '|';
	private final char START_PRE = '(';
	private final char CLOSE_PRE = ')';


	/**
	 * Construcor
	 * @param regEx regular expression as string type
	 */
	public RE(String regEx) {
		this.regEx = regEx;
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
		switch (peek()) {
		case START_PRE:
			eat(START_PRE);
			nfa = getNFA();
			eat(CLOSE_PRE);
			return nfa;

		default:
			char c = next();

			nfa = new NFA();

			String startState = Integer.toString(stateCount++);
			nfa.addStartState(startState);

			String finalState = Integer.toString(stateCount++);
			nfa.addFinalState(finalState);

			nfa.addTransition(startState, c, finalState);

			return nfa;
		}
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

		factor.addStartState(Integer.toString(stateCount++));
		String finalstate = Integer.toString(stateCount);
		factor.addFinalState(Integer.toString(stateCount++));
		factor.addTransition(factor.getStartState().getName(), E, finalstate);

		while (more() && peek() != CLOSE_PRE && peek() != OR) {
			NFA nextFactor = factor();
			factor.addAbc(nextFactor.getABC());

			for(State s:factor.getFinalStates()){
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
		return regEx.charAt(0);
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * Consumes the next item of input, failing if not equal to item
	 */
	private void eat(char c) {
		if (peek() == c){
			regEx = regEx.substring(1);
		}else{
			throw new RuntimeException("Expected: " + c + "; got: " + peek());
		}
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * Returns the next item of input and consumes it
	 */
	private char next() {
		char c = peek();
		eat(c);
		return c;
	}

	/**
	 * As suggested on https://matt.might.net/articles/parsing-regex-with-recursive-descent/
	 * The nonstandard primitve more() checks if there is more input available.
	 */
	private boolean more() {
		return regEx.length() > 0;
	}
}