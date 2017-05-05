package SpatialQueryProcessing;

import java.awt.*;

/*
* AppletUI class. Provides most of the user interface for the applet.
*/
@SuppressWarnings("serial")
class AppletUI extends Panel {

	public AppletUI(TriangulationAlgorithm algorithm[]) {
		@SuppressWarnings("unused")
		Label l;
		Panel p;

		setLayout(new BorderLayout());

		// Per algorithm controls. 
		p = new Panel();
		p.setLayout(new GridLayout(0, 1));

		// Headings for algorithm controls. 
		//p.add(new AlgorithmUIHeading());

		// One set of controls per algorithm.
		//for (int i = 0; i < algorithm.length; i++)
		//	p.add(algorithm[i].algorithmUI());

		// Add panel to controls. 
//		add("Center", p);

//		// Applet controls. 
		p = new Panel();
		p.setLayout(new GridLayout(0, 1));
		p.add(new Button("Start"));
//		p.add(new Button("Stop"));
//		p.add(new Button("New"));
//		p.add(new Label("Step Mode", Label.CENTER));
//		Choice c = new Choice();
//		c.addItem("Auto");
//		c.addItem("Manual");
//		p.add(c);
//
//		// Add panel to controls. 
		add("East", p);
	}
}