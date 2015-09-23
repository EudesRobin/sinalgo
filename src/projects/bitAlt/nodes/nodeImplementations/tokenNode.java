package projects.bitAlt.nodes.nodeImplementations;


import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.bitAlt.nodes.messages.ackMessage;
import projects.bitAlt.nodes.messages.tokenMessage;
import projects.bitAlt.nodes.timers.initTimer;
import projects.bitAlt.nodes.timers.retryTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;


public class tokenNode extends Node {

	public boolean passage =false;

	private boolean estampille = false;

	private Timer timer;
	
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
			Node node = this.outgoingConnections.iterator().next().endNode;
			estampille = !estampille;
			this.send(new tokenMessage(this, estampille), node);
			System.out.println("nouveau Msg envoye");
		}
		(new retryTimer()).startRelative(10,this);
	}

	// Cette fonction g�re la r�ception de message
	// Elle est appel�e r�guli�rement m�me si aucun message n'a �t� re�u

	public void handleMessages(Inbox inbox) {
		// Test si il y a des messages

		while(inbox.hasNext())
		{
			Message m=inbox.next();
			if (m instanceof ackMessage){
				if (this.estampille == ((ackMessage) m).estampille){
					System.out.println("Ack reçut");
					this.start();
				}
			}
			else if (m instanceof tokenMessage){
				this.estampille = ((tokenMessage) m).estampille;
				this.send(new ackMessage(this, estampille), ((tokenMessage) m).sender);
			}

		}
	}
	
	public void resend(){
		Node node = this.outgoingConnections.iterator().next().endNode;
		this.send(new tokenMessage(this, estampille), node);
		System.out.println("Msg renvoye");
		(new retryTimer()).startRelative(10,this);
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
