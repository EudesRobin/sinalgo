package projects.bitAlt.nodes.messages;


import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class ackMessage extends Message {
	public Node sender;
	public boolean estampille;

	public ackMessage(Node s, boolean estampille){
		sender=s;
		this.estampille = estampille;
	}

	public Message clone() {
		return new ackMessage(sender, estampille);
	}


}
