package projects.defaultProject.models.messageTransmissionModels;

import java.util.*;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.statistics.Distribution;


class Edge { // internal class edge, tuple of sender / receiver
	Node from, to;
	Edge(Node from, Node to) { this.from = from; this.to = to; }
	public boolean equals(Object o) {
		return (o instanceof Edge) &&
			((Edge)o).from.equals(from) && ((Edge)o).to.equals(to);
	}
	public int hashCode() { return from.hashCode() + to.hashCode(); }
}

class Temps{
	double tps_trans;
	double tps_rel;
	Temps(double t1, double t2){
		this.tps_trans=t1;
		this.tps_rel=t2;
	}
}

/**
 * transmission model whose delivery time is defined
 * through a distribution. Fifo transmission channels is preserved.
 * <p>
 * This class expects an entry in the configuration file that describes
 * the distribution of the delivery time.
 * <pre>
		&lt;FifoRandom distribution="Uniform" min="1" max="10"/&gt;
 * </pre>
 * a random time that follows the above distribution is added to the last
 * time a message was sent through this channel.
 */
public class FifoRandom extends sinalgo.models.MessageTransmissionModel {
	
	private HashMap<Edge,Temps> lastSent; // maps each edge to the last time it sent a message
	
	Distribution dist;
	
	/**
	 * Creates a new FifoRandom transmission model instance and reads the 
	 * config for this object from the config file. 
	 * @throws CorruptConfigurationEntryException
	 */
	public FifoRandom() throws CorruptConfigurationEntryException {
		dist = Distribution.getDistributionFromConfigFile("FifoRandom");
		lastSent = new HashMap<Edge, Temps>();
	}
	
	@Override
	public double timeToReach(Node startNode, Node endNode, Message msg) {
		
		Edge edge = new Edge(startNode, endNode);
		Temps isLast = lastSent.get(edge);
		double last;
				
		if(isLast == null){
			isLast = new Temps(0,Global.currentTime);
			last = 0;
		}else{
			if(isLast.tps_trans < (Global.currentTime-isLast.tps_rel)){
				last = 0;
			}else{
				last = isLast.tps_trans - (Global.currentTime-isLast.tps_rel);
			}
		}
		
		double time;
		time = last + dist.nextSample() + 1;
		/*if(startNode.ID == 2){
		System.out.println("temps : "+time+"  ;  idreceive : "+endNode.ID+"  ;  "+(Global.currentTime-isLast.tps_rel));
		}*/
		//System.out.println("temps : "+time);
		isLast.tps_rel = Global.currentTime;
		isLast.tps_trans = time;
		lastSent.put(edge,isLast);
		return time;
	}
}