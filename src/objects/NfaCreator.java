package objects;

public class NfaCreator {
	private NfaCinfig nfaCinfig = null;

	public NfaCreator() {
		nfaCinfig = new NfaCinfig();
	}

	public Mate constructStarClosure(Mate mateIn) {
		Mate mateOut = new Mate();
		mateOut.startNode = nfaCinfig.newNfa();
		mateOut.endNode = nfaCinfig.newNfa();

		mateOut.startNode.next = mateIn.startNode;
		mateIn.endNode.next = mateOut.endNode;

		mateOut.startNode.next2 = mateOut.endNode;
		mateIn.endNode.next2 = mateIn.startNode;

		mateIn.startNode = mateOut.startNode;
		mateIn.endNode = mateOut.endNode;

		return mateOut;
	}

	public Mate constructPlusClosure(Mate mateIn) {
		Mate mateOut = new Mate();

		mateOut.startNode = nfaCinfig.newNfa();
		mateOut.endNode = nfaCinfig.newNfa();

		mateOut.startNode.next = mateIn.startNode;
		mateIn.endNode.next = mateOut.endNode;

		mateIn.endNode.next2 = mateOut.startNode;

		mateIn.startNode = mateOut.startNode;
		mateIn.endNode = mateOut.endNode;

		return mateOut;
	}

	public Mate constructNfaForSingleCharacter(char c) {

		Mate mateOut = new Mate();
		mateOut.startNode = nfaCinfig.newNfa();
		mateOut.endNode = nfaCinfig.newNfa();
		mateOut.startNode.next = mateOut.endNode;
		mateOut.startNode.setEdge(c);

		return mateOut;
	}

	public Mate constructNfaForOR(Mate left, Mate right) {
		Mate mate = new Mate();
		mate.startNode = nfaCinfig.newNfa();
		mate.endNode = nfaCinfig.newNfa();

		mate.startNode.next = left.startNode;
		mate.startNode.next2 = right.startNode;

		left.endNode.next = mate.endNode;
		right.endNode.next = mate.endNode;

		return mate;
	}

	public Mate constructNfaForConnector(Mate left, Mate right) {
		Mate mateOut = new Mate();
		mateOut.startNode = left.startNode;
		mateOut.endNode = right.endNode;

		left.endNode.next = right.startNode;

		return mateOut;
	}
}
