package projects.tokenTree.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.tokenTree.nodes.messages.tokenMessage;
import projects.tokenTree.nodes.timers.initTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;


public class tokenNode extends Node {

	public boolean passage =false;

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
		if(this.ID==1) {
			this.passage=true;
			this.send(new tokenMessage(this), this.getNode(1));
		}
	}

	// Cette fonction gère la réception de message
	// Elle est appelée régulièrement même si aucun message n'a été reçu

	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext())
		{
			Message m=inbox.next();
			if(m instanceof tokenMessage)
			{	
				if ((this.ID == 1) && (getLabel(((tokenMessage) m).sender) == this.outgoingConnections.size())){

					Tools.stopSimulation();				
				}
				else{
					this.passage = true;
					send(new tokenMessage(this), getNode(getLabel(((tokenMessage) m).sender) %  this.outgoingConnections.size() + 1 ));
				}
			}	
		}
	}

	private int getLabel(Node node){

		Iterator<Edge> liste = this.outgoingConnections.iterator();
		int i = 1;
		while(liste.hasNext()){
			if (node == liste.next().endNode){
				return i;
			}
			i++;
		}
		return -1;
	}

	private Node getNode(int label){
		Iterator<Edge> liste = this.outgoingConnections.iterator();
		for(int i = 1; i < label; i++){
			liste.next();
		}
		return liste.next().endNode;
	}

	public void neighborhoodChange() {}
	public void postStep() {}
	public void checkRequirements() throws WrongConfigurationException {}

	public Color Couleur(){
		if (passage){
			return Color.GREEN;
		}
		return Color.RED;
	}

	// affichage du noeud
	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
		this.setColor(this.Couleur());
		String text = ""+this.ID;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}
}