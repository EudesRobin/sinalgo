package projects.tokenTree.nodes.timers;

import projects.tokenTree.nodes.nodeImplementations.tokenNode;
import sinalgo.nodes.timers.Timer;

public class initTimer extends Timer {

	public void fire() {
		tokenNode n= (tokenNode) this.node;
		n.start();
	}
	

}
