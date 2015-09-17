package projects.defaultProject.models.messageTransmissionModels;

import java.util.*;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.statistics.Distribution;

/**
 * transmission model whose delivery time is defined through a distribution.
 * Fifo transmission channels is preserved.
 * <p>
 * This class expects an entry in the configuration file that describes the
 * distribution of the delivery time.
 * 
 * <pre>
 * 		&lt;FifoRandom distribution="Uniform" min="1" max="10"/&gt;
 * </pre>
 * 
 * a random time that follows the above distribution is added to the last time a
 * message was sent through this channel.
 */
public class FifoRandom2 extends sinalgo.models.MessageTransmissionModel {

	/**
	 * internal class, tuple of (sender,receiver) nodes
	 * 
	 * @author rjamet
	 */
	class NodeTuple implements java.lang.Comparable<NodeTuple> {
		Node from, to;

		NodeTuple(Node from, Node to) {
			this.from = from;
			this.to = to;
		}

		public boolean equals(Object o) {
			return (o instanceof NodeTuple)
					&& ((NodeTuple) o).from.equals(from)
					&& ((NodeTuple) o).to.equals(to);
		}

		//faster than using object hashcodes() 
		public final int hashCode() {
			return (from.ID << 10) ^ to.ID;
		}

		
		//used for tests of faster Maps
		public int compareTo(NodeTuple e2) {
			if ((e2.from.ID > from.ID)
					|| (e2.from.ID == from.ID && e2.to.ID > e2.to.ID)) {
				return 1;
			} else if (e2.from.ID == from.ID && e2.to.ID == to.ID) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	private static Map<NodeTuple, Double> lastSent; 
	Distribution dist;

	/**
	 * Creates a new FifoRandom transmission model instance and reads the config
	 * for this object from the config file.
	 * 
	 * @throws CorruptConfigurationEntryException
	 */
	public FifoRandom2() throws CorruptConfigurationEntryException {
		dist = Distribution.getDistributionFromConfigFile("FifoRandom");
		lastSent = new TreeMap<NodeTuple, Double>();// plus rapide
	}

	@Override
	public double timeToReach(Node startNode, Node endNode, Message msg) {
		NodeTuple e = new NodeTuple(startNode, endNode);
		Double t = lastSent.get(e);

		if (t == null || t <= Global.currentTime) {
			t = Global.currentTime;
		}

		t = t + dist.nextSample();
		lastSent.put(e, t);
		return (t - Global.currentTime);
	}
}