package projects.tokenTree.nodes.messages;


import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class tokenMessage extends Message {
public Node sender;

public tokenMessage(Node s){
	sender=s;
}

public Message clone() {
	return new tokenMessage(sender);
}

	
}
