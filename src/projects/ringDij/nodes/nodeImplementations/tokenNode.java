package projects.ringDij.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.ringDij.nodes.messages.tokenMessage;
import projects.ringDij.nodes.timers.initTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;


public class tokenNode extends Node {

	private int state;
	public void preStep() {}

	// ATTENTION lorsque init est appelé les liens de communications n'existent pas
	// il faut attend une unité de temps, avant que les connections soient réalisées
	// nous utilisons donc un timer

	public void init() {
		(new initTimer()).startRelative(1, this);
	}

	// Lorsque le timer précédent expire, la fonction start est appelée
	// elle correspond ainsi à l'initialisation réelle du processus

	public void start(){
		state = (int) (Math.random() * 100000.0) % Tools.getNodeList().size();
		Node n = this.outgoingConnections.iterator().next().endNode;
		this.send(new tokenMessage(this,state), getDroite());
	}

	// Cette fonction gère la réception de message
	// Elle est appelée régulièrement même si aucun message n'a été reçu

	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext())
		{
			Message m=inbox.next();
			Node n = this.outgoingConnections.iterator().next().endNode;
			System.out.println(this.ID+":"+n.ID);
			if(this.ID==1 && ((tokenMessage) m).state==state){
				state= (state +1  )% Tools.getNodeList().size();
				this.send(new tokenMessage(this,state), getDroite());
			}
			
			if(this.ID!=1 && ((tokenMessage) m).state!=state){
				state = ((tokenMessage) m).state;
				this.send(new tokenMessage(this,state), getDroite());
			}
		}
	}

	private Node getDroite(){
		
		Iterator<Edge> liste = this.outgoingConnections.iterator();
		Node n;
		while(liste.hasNext()){
			n = liste.next().endNode;
			if (n.ID == (this.ID) % Tools.getNodeList().size() + 1){
				return n;
			}
		}
		return null;
	}
	
	

	public void neighborhoodChange() {}
	public void postStep() {}
	public void checkRequirements() throws WrongConfigurationException {}

	public Color Couleur(){
		if (state % 2 == 0){
			return Color.GREEN;
		}
		return Color.RED;
	}

	// affichage du noeud
	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
		this.setColor(this.Couleur());
		String text = ""+this.state;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}
}