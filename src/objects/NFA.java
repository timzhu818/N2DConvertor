package objects;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class NFA {

	private int restate = 0;
	
	private String re;
	private String reJoined;
	private String rePostfix;

	private String[] letter;
	private Mate mate;
	
	private ConsoleOutput table;

	public NFA(String re) {
		this.re = re;
		reJoined = null;
		rePostfix = null;
		Set<Character> temp = new HashSet<>();
		for(int i=0;i<this.re.length();i++){
			if(isLetter(this.re.charAt(i))){
				temp.add(this.re.charAt(i));
			}
		}
		letter = new String[temp.size()+2];
		Object []tempObj = temp.toArray();
		int i=0;
		letter[i] = "";
		for (;i<tempObj.length;i++) {
			letter[i+1] = (char)tempObj[i]+"";
		}
		letter[i+1] = "EPSILON";
		table = new ConsoleOutput(letter.length, true);
		table.appendRow();
		for (String string : letter) {
			table.appendColum(string);
		}
	}

	public Mate getMate() {
		return mate;
	}
	
	public String[] getLetter() {
		return letter;
	}

	public void setMate(Mate mate) {
		this.mate = mate;
	}

	public String addJoinSymbol() {
		int length = re.length();
		if(length==1) {
			System.out.println("add join symbol:" + re);
			reJoined = re;
			return re;
		}
		int return_string_length = 0;
		char return_string[] = new char[2 * length + 2];
		char first, second = '0';
		for (int i = 0; i < length - 1; i++) {
			first = re.charAt(i);
			second = re.charAt(i + 1);
			return_string[return_string_length++] = first;
			if (first != '(' && first != '|' && isLetter(second)) {
				return_string[return_string_length++] = '.';
			}
			else if (second == '(' && first != '|' && first != '(') {
				return_string[return_string_length++] = '.';
			}
		}
		return_string[return_string_length++] = second;
		String rString = new String(return_string, 0, return_string_length);
		System.out.println("add join symbol:" + rString);
		System.out.println();
		reJoined = rString;
		return rString;
	}

	private boolean isLetter(char check) {
		{
			if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z')
				return true;
			return false;
		}
	}

	public String postfix() {
		reJoined = reJoined + "#";

		Stack<Character> s = new Stack<>();
		char ch = '#', ch1, op;
		s.push(ch);
		String out_string = "";
		int read_location = 0;
		ch = reJoined.charAt(read_location++);
		while (!s.empty()) {
			if (isLetter(ch)) {
				out_string = out_string + ch;
				ch = reJoined.charAt(read_location++);
			} else {
				ch1 = s.peek();
				if (isp(ch1) < icp(ch)) {
					s.push(ch);
					ch = reJoined.charAt(read_location++);
				} else if (isp(ch1) > icp(ch)) {
					op = s.pop();
					out_string = out_string + op;
				} else {
					op = s.pop();
					if (op == '(')
						ch = reJoined.charAt(read_location++);
				}
			}
		}
		System.out.println("postfix:" + out_string);
		System.out.println();
		rePostfix = out_string;
		return out_string;
	}

	private int isp(char c) {
		switch (c) {
		case '#':
			return 0;
		case '(':
			return 1;
		case '*':
			return 7;
		case '+':
			return 7;
		case '.':
			return 5;
		case '|':
			return 3;
		case ')':
			return 8;
		}
		return -1;
	}

	private int icp(char c) {
		switch (c) {
		case '#':
			return 0;
		case '(':
			return 8;
		case '*':
			return 6;
		case '+':
			return 6;
		case '.':
			return 4;
		case '|':
			return 2;
		case ')':
			return 1;
		}
		return -1;
	}

	public void re2nfa() {
		mate = new Mate();
		Mate tmp = new Mate();
		Mate right, left;
		NfaCreator creator = new NfaCreator();
		char ch[] = rePostfix.toCharArray();
		Stack<Mate> stack = new Stack<>();
		for (char c : ch) {
			switch (c) {
			case '|':
				right = stack.pop();
				left = stack.pop();
				mate = creator.constructNfaForOR(left, right);
				stack.push(mate);
				break;
			case '*':
				tmp = stack.pop();
				mate = creator.constructStarClosure(tmp);
				stack.push(mate);
				break;
			case '+':
				tmp = stack.pop();
				mate = creator.constructPlusClosure(tmp);
				stack.push(mate);
				break;
			case '.':
				right = stack.pop();
				left = stack.pop();
				mate = creator.constructNfaForConnector(left, right);
				stack.push(mate);
				break;
			default:
				mate = creator.constructNfaForSingleCharacter(c);
				stack.push(mate);
				break;
			}
		}
	}

	public void print() {
		restate(this.mate.startNode);
		revisit(this.mate.startNode);
		System.out.println("--------NFA--------");
		table.appendRow();
		printNfa(this.mate.startNode);
		System.out.print(table);
		revisit(this.mate.startNode);
		System.out.println("--------NFA--------");
		System.out.println("start state: " + (this.mate.startNode.getState()));
		System.out.println("end state: " + (this.mate.endNode.getState()));
	}

	private void restate(Space startNfa) {
		if (startNfa == null || startNfa.isVisited()) {
			return;
		}
		startNfa.setVisited();
		startNfa.setState(restate++);
		restate(startNfa.next);
		restate(startNfa.next2);
	}
	private void revisit(Space startNfa) {
		if (startNfa == null || !startNfa.isVisited()) {
			return;
		}
		startNfa.setUnVisited();
		revisit(startNfa.next);
		revisit(startNfa.next2);
	}

	private void printNfa(Space startNfa) {
		if (startNfa == null || startNfa.isVisited()) {
			return;
		}

		startNfa.setVisited();

		printNfaNode(startNfa);
		if (startNfa.next != null) {
			table.appendRow();
		}
		printNfa(startNfa.next);
		printNfa(startNfa.next2);
	}

	private void printNfaNode(Space node) {
		if (node.next != null) {
			table.appendColum(node.getState());
			if(node.getEdge()==-1) {
				for(int i=0;i<letter.length-2;i++) {
					table.appendColum(" ");
				}
				if (node.next2 != null)
					table.appendColum("{"+node.next.getState()+","+node.next2.getState()+"}");
				else
					table.appendColum("{"+node.next.getState()+"}");
				}
			else {
				int index = getindex(""+(char)node.getEdge());
				for(int i=0;i<letter.length-1;i++) {
					if(i!=index)
						table.appendColum(" ");
					else {
						if (node.next2 != null)
							table.appendColum("{"+node.next.getState()+","+node.next2.getState()+"}");
						else
							table.appendColum("{"+node.next.getState()+"}");
					}
				}
			}
		}
		else {
			table.appendColum(node.getState());
			table.appendColum(" ");
			table.appendColum(" ");
			table.appendRow();
		}
	}
	
	//“”,a,b,EPS
	private int getindex(String ch) {
		for (int i=0;i<letter.length;i++) {
			if(letter[i].equals(ch))
				return i-1;
		}
		return -1;
	}
	
}
