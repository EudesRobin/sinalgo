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

	// ATTENTION lorsque init est appel� les liens de communications n'existent pas
	// il faut attend une unit� de temps, avant que les connections soient r�alis�es
	// nous utilisons donc un timer

	public void init() {
		(new initTimer()).startRelative(1, this);
	}

	// Lorsque le timer pr�c�dent expire, la fonction start est appel�e
	// elle correspond ainsi � l'initialisation r�elle du processus

	public void start(){
		if(this.ID==1) {
			this.passage=true;
			this.send(new tokenMessage(this), this.getNode(1));
		}
	}

	// Cette fonction g�re la r�ception de message
	// Elle est appel�e r�guli�rement m�me si aucun message n'a �t� re�u

	public void handleMessages(Inbox inbox) {
		// Test si il y a des messages

		while(inbox.hasNext())
		{
			Message m=inbox.next();
			if(m instanceof tokenMessage) // Si le processus a re�u le jeton 
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
