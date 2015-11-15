package projects.ensMax.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import java.util.Random;



import projects.ensMax.nodes.messages.tokenMessage;
import projects.ensMax.nodes.timers.initTimer;
import projects.ensMax.nodes.timers.sendTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;


public class tokenNode extends Node {

	
	private HashMap<Integer, Boolean> listState = new HashMap<Integer, Boolean>();
	
	private boolean state =false;
	
	private sendTimer timer;

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
		Random rand = new Random(42);
		Boolean bool;
		state = rand.nextBoolean();
		
		for (Edge edge : this.outgoingConnections) {
			bool = rand.nextBoolean();
	
			this.listState.put(edge.endNode.ID, bool);
		}
		
		this.timer = new sendTimer();
		this.timer.startRelative(10, this);
	
	}

	// Cette fonction gère la réception de message
	// Elle est appelée régulièrement même si aucun message n'a été reçu

	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext())
		{
			tokenMessage mess = (tokenMessage) inbox.next();
			listState.put(mess.sender.ID, mess.state);
			if (state){
				
				for (Edge edge : outgoingConnections) {
					if (listState.get(edge.endNode.ID) && edge.endNode.ID < this.ID){
						this.state = false;
					
					}
				}
				
			}
			else{
				Boolean test = true;
				for (Edge edge : outgoingConnections) {
					//if(! (edge.endNode.ID > this.ID)){
					test = test && (!(listState.get(edge.endNode.ID)) || edge.endNode.ID> this.ID); 
					
				}
				
					this.state = test;
				}
			
		}
	}

	public void sendState(){
		this.broadcast(new tokenMessage(this, state));
		
		this.timer = new sendTimer();
		this.timer.startRelative(10, this);
	}
	
	public void neighborhoodChange() {}
	public void postStep() {}
	public void checkRequirements() throws WrongConfigurationException {}

	public Color Couleur(){
		if (state){
			return Color.red;
		}
		return Color.yellow;
	}

	// affichage du noeud
	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
		this.setColor(this.Couleur());
		String text = ""+this.ID;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}
}