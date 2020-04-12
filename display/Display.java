package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.Constants;
import converter.modules.Upload;
import display.utils.RoundedPanel;
import extractor.Extractor;

public class Display {

	private JFrame frame = new JFrame();

	private final int WIDTH = 100, HEIGHT = 100;

	private JLabel label = new JLabel();

	@SuppressWarnings("serial")
	public Display() {
		JPanel panel = new RoundedPanel(new Dimension(WIDTH, HEIGHT), new RoundRectangle2D.Float(0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT));

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.setContentPane(panel);
		frame.pack();
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setContentPane(panel);
		frame.setAlwaysOnTop(true);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PDF", "pdf");
				
				chooser.setFileFilter(filter);
				
				int returnVal = chooser.showOpenDialog(null);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					setProcessingState();
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					File file = updateInput(chooser.getSelectedFile());
					
					new Upload(file.getAbsolutePath());
					
					System.out.println("Selected file: " + file.getName());
				}
			}
		});
		label.setDropTarget(new DropTarget() {
			@Override
			public synchronized void dragEnter(DropTargetDragEvent dtde) {
				((RoundedPanel) panel).color = Color.decode("#FF0202");
				((RoundedPanel) panel).repaint();
			}

			@Override
			public synchronized void dragExit(DropTargetEvent dte) {
				((RoundedPanel) panel).color = Color.decode("#FF8402");
				((RoundedPanel) panel).repaint();
			}

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					setProcessingState();
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					evt.acceptDrop(DnDConstants.ACTION_COPY);

					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						if (file.getName().endsWith(".pdf")) {
							File f = updateInput(file);
							new Upload(f.getAbsolutePath());
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		label.setOpaque(false);
		label.setBorder(null);
		label.setBounds(14, 0, WIDTH / 2, HEIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon(Display.class.getClassLoader().getResource(Constants.RESOURCES_DIRECTORY + "/drop-icon.png")));
		panel.add(label);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frame.setLocation((int) (screenSize.getWidth() - WIDTH / 2), (int) (screenSize.getHeight() / 3) - HEIGHT);
		frame.setVisible(true);
	}

	public void setProcessingState() {
		new Thread(() -> {
			label.setIcon(new ImageIcon(Display.class.getClassLoader().getResource(Constants.RESOURCES_DIRECTORY + "/processing.png")));
		}).start();
	}

	private File updateInput(File file) {
		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));

		String path = Constants.MAIN_DIRECTORY + "input/" + Extractor.removeAccents(Extractor.deAccent(name)) + ".pdf";

		if (!new File(Constants.MAIN_DIRECTORY + "input/").exists()) {
			new File(Constants.MAIN_DIRECTORY + "input/").mkdirs();
		}
		
		try {
			Files.copy(file.toPath(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new File(path);
	}

}
