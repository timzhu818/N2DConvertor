package objects;

import java.util.Stack;

public class NfaCinfig {
    private final int NFA_MAX = 256; 
    private Space[] nfaStatesArr = null;
    private Stack<Space> nfaStack = null;
    private int nextAlloc = 0; 
    private int nfaStates = 0; 
    
    public NfaCinfig()  {
    	nfaStatesArr = new Space[NFA_MAX];
    	for (int i = 0; i < NFA_MAX; i++) {
    		nfaStatesArr[i] = new Space();
    	}
    	
    	nfaStack = new Stack<Space>();
    	
    }
    
    public Space newNfa()  {
    	Space nfa = null;
    	if (nfaStack.size() > 0) {
    		nfa = nfaStack.pop();
    	}
    	else {
    		nfa = nfaStatesArr[nextAlloc];
    		nextAlloc++;
    	}
    	
    	nfa.clearState();
    	nfa.setState(nfaStates++);
    	nfa.setEdge(Space.EPSILON);
    	
    	return nfa;
    }
    
    public void discardNfa(Space nfaDiscarded) {
    	--nfaStates;
    	nfaDiscarded.clearState();
    	nfaStack.push(nfaDiscarded);
    }
    
   
}
