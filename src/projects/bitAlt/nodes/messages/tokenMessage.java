package projects.bitAlt.nodes.messages;


import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class tokenMessage extends Message {
	public Node sender;
	public boolean estampille;

	public tokenMessage(Node s, boolean estampille){
		sender=s;
		this.estampille = estampille;
	}

	public Message clone() {
		return new tokenMessage(sender, estampille);
	}


}
