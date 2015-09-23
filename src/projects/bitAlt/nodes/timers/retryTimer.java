package projects.bitAlt.nodes.timers;

import projects.bitAlt.nodes.nodeImplementations.tokenNode;
import sinalgo.nodes.timers.Timer;

public class retryTimer extends Timer {

	@Override
	public void fire() {
		tokenNode n= (tokenNode) this.node;
		n.resend();
	}

}
