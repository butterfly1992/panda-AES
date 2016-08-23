package com.mbl.base64.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

public class JEditorPaneExample extends JFrame implements HyperlinkListener {
	public static void main(String[] argv) {
		JEditorPaneExample mainApp = new JEditorPaneExample();
	}

	JEditorPane htmlViewer;
	JScrollPane scrollpane;

	public JEditorPaneExample() {
		super("JEditorPaneExample Example");
		setBounds(0, 0, 600, 400);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Attempt to load an 'index' html page...
		try {
			URL url = null;

			try {
				url = getClass().getResource("index.html");
			} catch (Exception e) {
				System.out.println("Could not open file!");
				url = null;
			}

			if (url != null) {
				htmlViewer = new JEditorPane(url);
				htmlViewer.setEditable(false);
				htmlViewer.addHyperlinkListener(this);

				scrollpane = new JScrollPane();
				scrollpane.setBounds(10, 10, 570, 350);
				scrollpane.getViewport().add(htmlViewer);
			}
		} catch (MalformedURLException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		getContentPane().add(scrollpane);

		setVisible(true);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		// TODO Auto-generated method stub
		
	}


}