package projects.ringDij.nodes.messages;


import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class tokenMessage extends Message {
public Node sender;
public int state;

public tokenMessage(Node s,int state){
	sender=s;
	this.state=state;
}

public Message clone() {
	return new tokenMessage(sender,state);
}

	
}
