package projects.ensMax.nodes.messages;


import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class tokenMessage extends Message {
public Node sender;
public Boolean state;

public tokenMessage(Node s, Boolean state){
	sender=s;
	this.state = state;
}

public Message clone() {
	return new tokenMessage(sender, state);
}

	
}
