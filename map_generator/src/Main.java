import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {

	final static int XLIMIT = 100;

	final static int YLIMIT = 100;

	public static void main(String[] args) throws IOException {
//		first();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainWindow();
			}
		});	
	}
}